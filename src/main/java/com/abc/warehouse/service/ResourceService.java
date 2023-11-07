package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.Resource;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 吧啦
* @description 针对表【resource_208201302(资源表)】的数据库操作Service
* @createDate 2023-09-23 16:58:22
*/
public interface ResourceService extends IService<Resource> {

    Result getAllResources();

    Result getAllResourcesInfo();
}
