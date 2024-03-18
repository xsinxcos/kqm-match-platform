package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.domain.dto.admin.AdminAddRotatingPicDto;
import com.chaos.domain.dto.admin.AdminDeleteRotatingPicDto;
import com.chaos.domain.entity.Resource;
import com.chaos.domain.vo.app.RotatingPicVo;
import com.chaos.mapper.ResourceMapper;
import com.chaos.response.ResponseResult;
import com.chaos.service.ResourceService;
import com.chaos.util.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 用户端静态资源(Resource)表服务实现类
 *
 * @author chaos
 * @since 2024-03-18 21:30:55
 */
@Service("resourceService")
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    private static final Integer ROTATING_PICTURE_TYPE = 0;

    private static final Integer RESOURCE_DELETE_FLAG = 1;

    @Override
    public ResponseResult getRotatingPic() {
        LambdaQueryWrapper<Resource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Resource::getType, ROTATING_PICTURE_TYPE);
        List<Resource> resources = list(wrapper);
        List<RotatingPicVo> picVos = BeanCopyUtils.copyBeanList(resources, RotatingPicVo.class);
        return ResponseResult.okResult(picVos);
    }

    @Override
    public ResponseResult adminAddRotatingPic(AdminAddRotatingPicDto dto) {
        Resource resource = Resource.builder()
                .name(dto.getName())
                .url(dto.getUrl())
                .type(ROTATING_PICTURE_TYPE)
                .build();
        save(resource);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult adminDeleteRotatingPic(AdminDeleteRotatingPicDto dto) {
        //防御性编程
        Optional.ofNullable(dto.getId()).orElseThrow(() -> new RuntimeException("ID不能为空"));

        //删除轮播图
        LambdaUpdateWrapper<Resource> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Objects.nonNull(dto.getId()), Resource::getId, dto.getId())
                .set(Objects.nonNull(dto.getId()), Resource::getDelFlag, RESOURCE_DELETE_FLAG);

        update(wrapper);
        return ResponseResult.okResult();
    }
}

