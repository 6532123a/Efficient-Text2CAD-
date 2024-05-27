package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Mov;
import com.example.springboot.mapper.MovMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 收藏---add---
 * 按钮---方法
 */
@Service
public class MovService extends ServiceImpl<MovMapper, Mov> {

    @Resource
    MovMapper movMapper;


    public Mov selectByMovname(String movname) {
        QueryWrapper<Mov> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("movname", movname);  //  eq => ==   where movname = #{movname}
        // 根据电影名查询数据库的电影信息
        return getOne(queryWrapper); //  select * from mov where movname = #{movname}
    }



    
}
