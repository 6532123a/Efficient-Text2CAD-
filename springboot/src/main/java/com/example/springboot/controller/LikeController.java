package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.HoneyLogs;
import com.example.springboot.common.LogType;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Like;
import com.example.springboot.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    LikeService likeService;

    /**
     * 新增电影信息
     */
    @HoneyLogs(operation = "电影", type = LogType.ADD)
    @PostMapping("/add")
    public Result add(@RequestBody Like mov) {
        try {
            likeService.resist(mov);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                return Result.error("插入数据库错误或已存在");
            } else {
                return Result.error("系统错误");
            }
        }
        return Result.success();
    }

    /**
     * 修改电影信息
     */
    @HoneyLogs(operation = "电影", type = LogType.UPDATE)
    @PutMapping("/update")
    public Result update(@RequestBody Like mov) {
        likeService.updateById(mov);
        return Result.success();
    }

    /**
     * 删除电影信息
     */
    @HoneyLogs(operation = "电影", type = LogType.DELETE)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        likeService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除电影信息
     */
    @HoneyLogs(operation = "电影", type = LogType.BATCH_DELETE)
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {  //  [7, 8]
        likeService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 查询全部电影信息
     *      @GetMapping("/selectById/{id}")
     *     public Result selectById(@PathVariable Integer id) {
     *         User user = userService.getById(id);
     *         return Result.success(user);
     *     }
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Like> movList = likeService.list(new QueryWrapper<Like>().orderByDesc("id"));  // select * from mov order by id desc
        return Result.success(movList);
    }

    @GetMapping("/selectByusername")
    public Result selectByusername(@RequestParam String username) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<Like>().orderByDesc("id");
        queryWrapper.eq("username",username);
        List<Like> likeList=likeService.list(queryWrapper);
        return Result.success(likeList);
    }



    /**
     * 多条件查询电影信息
     * pageNum 当前的页码
     * pageSize 每页查询的个数
     * 判断收藏表中是否已存在
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String movname,
                               @RequestParam String username) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<Like>().orderByDesc("id");  // 默认倒序，让最新的数据在最上面
        queryWrapper.eq(StrUtil.isNotBlank(movname), "movname", movname);
        queryWrapper.eq(StrUtil.isNotBlank(username), "username", username);
        // select * from mov where movname = '%#{movname}%' and username = '%#{username}%'
        Page<Like> page = likeService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }

    /**
     * 批量导出数据
     */
    @GetMapping("/export")
    public void exportData(@RequestParam(required = false) String movname,
                           @RequestParam(required = false) String username,
                           @RequestParam(required = false) String ids,  //   1,2,3,4,5
                           HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);

        List<Like> list;
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(ids)) {     // ["1", "2", "3"]   => [1,2,3]
            List<Integer> idsArr1 = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in("id", idsArr1);
        } else {
            // 第一种全部导出或者条件导出
            queryWrapper.like(StrUtil.isNotBlank(movname), "movname", movname);
            queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
        }
        list = likeService.list(queryWrapper);   // 查询出当前Like表的所有数据
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("电影信息表", "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 批量导入
     * @param file 传入的excel文件对象
     * @return 导入结果
     */
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<Like> movList = reader.readAll(Like.class);
        // 写入数据到数据库
        try {
            likeService.saveBatch(movList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("数据批量导入错误");
        }
        return Result.success();
    }

}
