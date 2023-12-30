package com.abc.warehouse.controller;



import cn.hutool.json.JSONObject;
import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.annotation.JsonParam;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.mapper.StoreMapper;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.service.DeliverService;
import com.abc.warehouse.service.StoreService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@RequestMapping("/deliver")
@RestController
public class DeliverController {
    @Autowired
    private DeliverService deliverService;

    @GetMapping
    public Result enter() {
        return Result.ok();
    }

    @GetMapping("/searchAll/{page}")
    public Result getAll(@PathVariable("page") Integer page) {
        return deliverService.deliverPage(page);
    }

    @PostMapping("/getNames")
    public Result getMaterialNamesByDeliverTime(
            @RequestBody String requestBody) throws ParseException {
        JSONObject json=new JSONObject(requestBody);
        String startTime = json.getStr("startTime");
        String endTime = json.getStr("endTime");

        return deliverService.findMaterialNamesByDeliverTime(startTime, endTime);
    }

    @PostMapping("/findCountByNames")
    public Result findCountByNameBetweenDates(
            @RequestBody String requestBody) throws ParseException {
        JSONObject json=new JSONObject(requestBody);
        String startTime = json.getStr("startTime");
        String endTime = json.getStr("endTime");

        return deliverService.findCountByNameBetweenDates(startTime, endTime);
    }


}

