package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Like;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.LikeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 收藏---add---
 * 按钮---方法
 */
@Service
public class LikeService extends ServiceImpl<LikeMapper, Like> {

    @Resource
    LikeMapper likeMapper;

    public Like selectByMovname(String movname) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("movname", movname);  //  eq => ==   where movname = #{movname}
        // 根据用户名查询数据库的用户信息
        return getOne(queryWrapper); //  select * from user where username = #{username}
    }
    public Like selectByUsername(String username) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);  //  eq => ==   where movname = #{movname}
        // 根据用户名查询数据库的用户信息
        return getOne(queryWrapper); //  select * from user where username = #{username}
    }

    public Like selectByLikename(String movname) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("movname", movname);  //  eq => ==   where movname = #{movname}
        // 根据电影名查询数据库的电影信息
        return getOne(queryWrapper); //  select * from mov where movname = #{movname}
    }
    public Like selectByMore(String movname,String username) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("movname",movname)
                .and(wrapper -> wrapper.eq("username", username));
        return getOne(queryWrapper); //  select * from mov where
    }



    /**判断是否收藏存在，不存在则添加
     */
    public Like resist(Like like) {

        Like dbLike = selectByMore(like.getMovname(),like.getUsername());
        if (dbLike != null ) {
            // 抛出一个自定义的异常
            throw new ServiceException("收藏已存在");
        }
        save(like);
        return like;
    }


}
