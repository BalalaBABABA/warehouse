package com.abc.warehouse.controller;

import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONUtil;
import com.abc.warehouse.annotation.Decrypt;
import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.annotation.JsonParam;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.constants.PageConstants;
import com.abc.warehouse.mapper.StoreMapper;
import com.abc.warehouse.pojo.Store;
import com.abc.warehouse.service.StoreService;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.statements.SpringFailOnTimeout;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/store")
public class StoreController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreMapper storeMapper;

    @GetMapping
    public Result enter(){
        return Result.ok();
    }

    //@Encrypt 加密
    //@Decrypt 解密

    @GetMapping("/searchAll/{page}")
    @Encrypt
    public Result getAll(@PathVariable("page") Integer page){
        return storeService.storePage(page);
    }

    @GetMapping("/del/{id}")
    @Decrypt
    public Result deleteStore(@PathVariable("id") Long id){
        return storeService.del_store(id);
    }

    @PostMapping("/update")
    @Encrypt
    @Decrypt
    public Result updateStore(@JsonParam("storeId") Long storeId, @JsonParam("storeNo") Long storeNo,
                              @JsonParam("houseName") String houseName, @JsonParam("storeTime") Date storeTime,
                              @JsonParam("materialId") Long materialId, @JsonParam("storeCount") Integer storeCount,
                              @JsonParam("userId") Long userId, @JsonParam("notes") String notes){
        Store store = new Store(storeId, storeNo, houseName, storeTime, materialId, storeCount, userId, notes);
        return storeService.update_store(store);
    }

    @PostMapping("/conditionSearch")
    @Encrypt
    @Decrypt
    public Result conditionSearch(@JsonParam("storeNo") Long storeNo, @JsonParam("houseName") String houseName,
                                  @JsonParam("startTime") String startTime, @JsonParam("endTime") String endTime,
                                  @JsonParam("materialId") Long materialId, @JsonParam("userId") Long userId,
                                  @JsonParam("notes") String notes, @JsonParam("page") Integer page) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null, end = null;

        if(startTime != null && !startTime.isEmpty()){
            start = sdf.parse(startTime);
        }
        if(endTime != null && !endTime.isEmpty()){
            end = sdf.parse(endTime);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(end);
            calendar.add(Calendar.DATE, 1);
            end = calendar.getTime();
        }

        List<Store> result = storeMapper.selectByCondition(storeNo, houseName, start,
                end, materialId, userId, notes, page, PageConstants.STORE_SEARCH_PAGE_SIZE);

        if(result != null){
            return new Result(true, "0", result, Long.valueOf(result.size()));
        }else{
            return new Result(false, null, null, 0L);
        }
    }

    @PostMapping("/simpleStore")
    public Result simpleStore(@RequestBody Map<String, Object> params){
        storeMapper.callSimpleStore(params);
        return Result.ok();
    }
}
