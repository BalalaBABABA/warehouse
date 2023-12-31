package com.abc.warehouse.service.impl;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.constants.PageConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.Deliver;
import com.abc.warehouse.service.DeliverService;
import com.abc.warehouse.mapper.DeliverMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

/**
* @author 吧啦
* @description 针对表【deliver_208201302】的数据库操作Service实现
* @createDate 2023-12-02 16:59:21
*/
@Service
public class DeliverServiceImpl extends ServiceImpl<DeliverMapper, Deliver>
    implements DeliverService{

    public Result deliverPage(Integer curPage) {
        LambdaQueryWrapper<Deliver> wrapper = new LambdaQueryWrapper<>();  //查询条件构造器
        IPage<Deliver> pageQuery = new Page<>(curPage, PageConstants.DELIVER_SEARCH_PAGE_SIZE);
        IPage<Deliver> page = baseMapper.selectPage(pageQuery, wrapper);
        System.out.println(page);
        return Result.ok(page.getRecords(), page.getPages());
    }


}




