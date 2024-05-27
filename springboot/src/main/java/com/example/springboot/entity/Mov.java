package com.example.springboot.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("movie")
public class Mov {

    @TableId(type=IdType.AUTO)
    @Alias("序号")
    private Integer id;
    @Alias("电影名")
    private String movname;
    @Alias("导演演员")
    private String actors;
    @Alias("上映时间")
    private String time;
    @Alias("国家")
    private String country;
    @Alias("类型")
    private String type;
    @Alias("评分")
    private String star;
    @Alias("封面")
    private String photo;
    @Alias("概述")
    private String summary;

    @TableField(exist = false)
    private String token;

}
