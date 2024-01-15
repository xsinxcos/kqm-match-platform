package com.chaos.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.entity.Hotel;
import com.chaos.mapper.HotelMapper;
import com.chaos.service.IHotelService;
import org.springframework.stereotype.Service;

/**
 * (Hotel)表服务实现类
 *
 * @author makejava
 * @since 2023-12-21 21:33:12
 */
@Service("hotelService")
public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {

}

