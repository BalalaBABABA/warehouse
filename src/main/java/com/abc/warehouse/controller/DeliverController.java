package com.abc.warehouse.controller;



import cn.hutool.json.JSONObject;
import com.abc.warehouse.annotation.Decrypt;
import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.annotation.JsonParam;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.constants.PageConstants;
import com.abc.warehouse.mapper.StoreMapper;
import com.abc.warehouse.pojo.Deliver;
import com.abc.warehouse.pojo.Store;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.service.DeliverService;
import com.abc.warehouse.service.StoreService;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequestMapping("/deliver")
@RestController
public class DeliverController {
    @Autowired
    private DeliverService deliverService;

    @GetMapping
    public Result enter() {
        return Result.ok();
    }

//    @GetMapping("/searchAll/{page}")
//    public Result getAll(@PathVariable("page") Integer page) {
//        return deliverService.getAll(page);
//    }

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

    @PostMapping("/multiDelivery")
    public Result MultiDelivery(@RequestBody List<Deliver> deliverList) {
        return deliverService.MultiDelivery(deliverList);
    }

    //@Encrypt 加密
    //@Decrypt 解密

    @GetMapping("/searchAll/{page}")
    @Encrypt
    public Result getAll(@PathVariable("page") Integer page){
        return deliverService.getAll(page);
    }


    @PostMapping("/conditionSearch")
    @Encrypt
    @Decrypt
    public Result conditionSearch(@JsonParam("storeNo") Long storeNo, @JsonParam("houseName") String houseName,
                                  @JsonParam("startTime") String startTime, @JsonParam("endTime") String endTime,
                                  @JsonParam("materialId") Long materialId, @JsonParam("userId") Long userId,
                                  @JsonParam("notes") String notes, @JsonParam("page") Integer page) throws ParseException {

       return deliverService.conditionSearch(storeNo,houseName,startTime,endTime,materialId,userId,notes,page);
    }
}

