package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.Deliver;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 吧啦
* @description 针对表【deliver_208201302】的数据库操作Service
* @createDate 2023-12-02 16:59:21
*/
public interface DeliverService extends IService<Deliver> {
    Result deliverPage(Integer page);

}
