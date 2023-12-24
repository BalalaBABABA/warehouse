package com.abc.warehouse.controller;

import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.mapper.StoreMapper;
import com.abc.warehouse.service.DeliverService;
import com.abc.warehouse.service.StoreService;
import jdk.internal.util.xml.impl.ReaderUTF8;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/deliver")
@RestController
public class DeliverController {
    @Autowired
    private DeliverService deliverService;

    @GetMapping
    public Result enter(){
        return Result.ok();
    }
    @GetMapping("/searchAll/{page}")
    public Result getAll(@PathVariable("page") Integer page){
        return deliverService.deliverPage(page);
    }


}
