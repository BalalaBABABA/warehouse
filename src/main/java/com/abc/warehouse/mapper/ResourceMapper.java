package com.abc.warehouse.mapper;

import com.abc.warehouse.pojo.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 吧啦
* @description 针对表【resource_208201302(资源表)】的数据库操作Mapper
* @createDate 2023-09-23 16:58:22
* @Entity com.abc.warehouse.pojo.Resource
*/
public interface ResourceMapper extends BaseMapper<Resource> {

    List<Resource> getUserResources(Long userId);

}




