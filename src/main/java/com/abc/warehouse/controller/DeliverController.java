package com.abc.warehouse.controller;



import cn.hutool.json.JSONObject;
import com.abc.warehouse.annotation.Decrypt;
import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.annotation.JsonParam;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.constants.PageConstants;
import com.abc.warehouse.mapper.DeliverMapper;
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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RequestMapping("/deliver")
@RestController
public class DeliverController {
    @Autowired
    private DeliverService deliverService;
    @Autowired
    private DeliverMapper deliverMapper;

    @GetMapping
    public Result enter() {
        return Result.ok();
    }


    @PostMapping("/findNames")
    public Result getMaterialNamesByDeliverTime(
            @RequestBody String requestBody) throws ParseException {
        JSONObject json=new JSONObject(requestBody);
        String startTime = json.getStr("startTime");
        String endTime = json.getStr("endTime");
        return deliverService.findNameBetweenDates(startTime, endTime);
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


    @PostMapping({"/selectDeliverByDate"})
    @Encrypt
    @Decrypt
    public Result selectDeliverByDate(@JsonParam("year") String year, @JsonParam("month") String month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
        Date startDate = calendar.getTime();
        calendar.add(2, 1);
        calendar.add(5, -1);
        Date endDate = calendar.getTime();
        List<Deliver> result = this.deliverMapper.selectDeliverByDate(startDate, endDate);
        return result != null ? new Result(true, "0", result, (long)result.size()) : new Result(false, (String)null, (Object)null, 0L);
    }

    @PostMapping({"/deliverByYear"})
    @Encrypt
    @Decrypt
    public Result deliverByYear(@JsonParam("Year") String year, @JsonParam("id") Long materialId, @JsonParam("HouseName") String houseName) {
        Timestamp startYear = Timestamp.valueOf(year + "-01-01 00:00:00");
        Timestamp endYear = Timestamp.valueOf(year + "-12-31 23:59:59");
        List<Deliver> delivers = this.deliverMapper.selectDeliverByYear(startYear, endYear, materialId, houseName);
        Iterator var7 = delivers.iterator();

        while(var7.hasNext()) {
            Deliver deliver = (Deliver)var7.next();
            System.out.println(deliver);
        }

        return delivers != null ? new Result(true, "0", delivers, (long)delivers.size()) : new Result(false, (String)null, (Object)null, 0L);
    }
}
