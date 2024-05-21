package com.chaos.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.mail.domain.dto.ApplyGroupDto;
import com.chaos.mail.domain.dto.HandleGroupApplicationDto;
import com.chaos.mail.domain.entity.Mail;
import com.chaos.response.ResponseResult;


/**
 * 邮件表(Mail)表服务接口
 *
 * @author wzq
 * @since 2024-05-18 23:02:12
 */
public interface MailService extends IService<Mail> {

    ResponseResult getMyMail(Integer pageNum, Integer pageSize);

    ResponseResult applyGroup(ApplyGroupDto dto);

    ResponseResult<?> handleGroupApplication(HandleGroupApplicationDto dto);
}

