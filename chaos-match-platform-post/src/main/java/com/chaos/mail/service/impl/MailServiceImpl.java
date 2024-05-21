package com.chaos.mail.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.config.vo.PageVo;
import com.chaos.entity.User;
import com.chaos.group.domain.entity.Group;
import com.chaos.group.domain.entity.GroupUser;
import com.chaos.group.service.GroupService;
import com.chaos.group.service.GroupUserService;
import com.chaos.mail.MailMessageTemplate;
import com.chaos.mail.domain.dto.ApplyGroupDto;
import com.chaos.mail.domain.dto.HandleGroupApplicationDto;
import com.chaos.mail.domain.entity.GroupApplyMail;
import com.chaos.mail.domain.entity.GroupApplyResponseMail;
import com.chaos.mail.domain.entity.IMail;
import com.chaos.mail.domain.entity.Mail;
import com.chaos.mail.domain.vo.ListMailVo;
import com.chaos.mail.enums.MessageType;
import com.chaos.mail.mapper.MailMapper;
import com.chaos.mail.service.MailService;
import com.chaos.response.ResponseResult;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.RedisCache;
import com.chaos.util.SecurityUtils;
import com.chaos.util.SnowFlakeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 邮件表(Mail)表服务实现类
 *
 * @author wzq
 * @since 2024-05-18 23:02:12
 */
@Service("mailService")
@RequiredArgsConstructor
public class MailServiceImpl extends ServiceImpl<MailMapper, Mail> implements MailService {

    private static final Integer SYS_MAIL_SENDER_ID = -1;

    private static final Integer GROUP_SUPER_ADMIN = 2;

    private final GroupUserService groupUserService;

    private final GroupService groupService;

    private final RedisCache redisCache;
    @Override
    public ResponseResult<?> getMyMail(Integer pageNum, Integer pageSize) {
        Long userId = SecurityUtils.getUserId();
        //分页器
        Page<Mail> mailPage = new Page<>(pageNum ,pageSize);
        lambdaQuery()
                .eq(Mail::getReceiverId ,userId)
                .orderByDesc(Mail::getCreateTime)
                .page(mailPage);

        //根据type转化成不同的类型
        List<ListMailVo> vos = new ArrayList<>();
        for (Mail record : mailPage.getRecords()) {
            ListMailVo vo = ListMailVo.builder()
                    .id(record.getId())
                    .type(record.getType())
                    .isRead(record.getIsRead())
                    .message(record.getMessage())
                    .build();
            vos.add(vo);
        }
        return ResponseResult.okResult(new PageVo(vos ,mailPage.getTotal()));
    }

    @Override
    public ResponseResult applyGroup(ApplyGroupDto dto) {
        User user = SecurityUtils.getLoginUser().getUser();
        Long userId = user.getId();
        //找到社群群主
        CompletableFuture<Long> getGroupManId = CompletableFuture.supplyAsync(() -> {
            return groupUserService.lambdaQuery().eq(GroupUser::getGroupId, dto.getGroupId())
                    .eq(GroupUser::getType, GROUP_SUPER_ADMIN)
                    .one().getUserId();
        });
        //找到社群信息
        CompletableFuture<Group> groupDetail = CompletableFuture.supplyAsync(() -> {
            return groupService.getById(dto.getGroupId());
        });

        Long groupMan = null;
        Group group = null;
        try {
            groupMan = getGroupManId.get();
            group = groupDetail.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        //构建入群申请消息
        GroupApplyMail applyMail = GroupApplyMail.builder()
                .groupId(dto.getGroupId())
                .groupName(group.getName())
                .userId(userId)
                .userName(user.getUserName())
                .userAvatar(user.getAvatar())
                .build();
        //加入缓存
        String key = MailMessageTemplate.groupApplyKey(dto.getGroupId(), userId);
        redisCache.setCacheObject(key ,applyMail ,7 , TimeUnit.DAYS);
        //消息发送给社群群主
        Mail mail = Mail.builder()
                .id(SnowFlakeUtil.getDefaultSnowFlakeId())
                .isRead(0)
                .message(JSON.toJSONString(applyMail))
                .senderId(userId)
                .receiverId(groupMan)
                .type(MessageType.GROUP_APPLY_MESSAGE_TYPE.getType())
                .build();
        save(mail);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> handleGroupApplication(HandleGroupApplicationDto dto) {
        String key = MailMessageTemplate.groupApplyKey(dto.getGroupId(), dto.getUserId());
        GroupApplyMail applyMail = redisCache.getCacheObject(key);
        //申请不存在则抛出
        Optional.ofNullable(applyMail).orElseThrow(()-> new RuntimeException("处理失败"));
        //修改原消息状态
        Mail mail = getById(dto.getId());
        mail.setIsRead(dto.getIsRead());
        updateById(mail);
        //构建消息回应
        GroupApplyResponseMail responseMail = GroupApplyResponseMail.builder()
                .groupId(applyMail.getGroupId())
                .groupName(applyMail.getGroupName())
                .userId(applyMail.getUserId())
                .userAvatar(applyMail.getUserAvatar())
                .userName(applyMail.getUserName())
                .result(dto.getIsRead() == 2)
                .build();
        //构建邮件
        Mail build = Mail.builder()
                .id(SnowFlakeUtil.getDefaultSnowFlakeId())
                .senderId(SecurityUtils.getUserId())
                .receiverId(dto.getUserId())
                .message(JSON.toJSONString(responseMail))
                .isRead(0)
                .type(MessageType.GROUP_RESPONSE_MESSAGE_TYPE.getType())
                .build();
        save(build);

        if(dto.getIsRead() == 2){
            groupUserService.save(GroupUser.builder()
                    .type(0)
                    .groupId(dto.getGroupId())
                    .userId(dto.getUserId())
                    .build());
        }

        return ResponseResult.okResult();
    }
}

