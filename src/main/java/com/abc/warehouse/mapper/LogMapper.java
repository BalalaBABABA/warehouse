package com.abc.warehouse.mapper;

import com.abc.warehouse.dto.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


/**
* @author 吧啦
* @description 针对表【log_208201302】的数据库操作Mapper
* @createDate 2023-09-21 23:17:23
* @Entity com.abc.warehouse.dto.Log
*/
public interface LogMapper extends BaseMapper<Log> {
    int add(@Param("id")Long user_id, @Param("content") String content);
}




