package com.abc.warehouse.controller;

import cn.hutool.json.JSONObject;
import com.abc.warehouse.annotation.Decrypt;
import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.annotation.JsonParam;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.constants.PageConstants;
import com.abc.warehouse.mapper.MaterialMapper;
import com.abc.warehouse.mapper.StoreMapper;
import com.abc.warehouse.pojo.Store;
import com.abc.warehouse.service.MaterialService;
import com.abc.warehouse.service.MaterialTypeService;
import com.abc.warehouse.service.StoreService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
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
    @Autowired
    private MaterialService materialService;

//
//    @PostMapping("/findNames")
//    @Encrypt
//    @Decrypt
//    public Result getMaterialNamesByStoreTime(
//            @JsonParam("startTime") String startTime, @JsonParam("endTime") String endTime) throws ParseException {
////        JSONObject json=new JSONObject(requestBody);
////        String startTime = json.getStr("startTime");
////        String endTime = json.getStr("endTime");
//        return storeService.findNameBetweenDates(startTime, endTime);
//    }
//
//    @PostMapping("/findCountByNames")
//    @Encrypt
//    @Decrypt
//    public Result findCountByNameBetweenDates(
//            @JsonParam("startTime") String startTime, @JsonParam("endTime") String endTime) throws ParseException {
////        JSONObject json=new JSONObject(requestBody);
////        String startTime = json.getStr("startTime");
////        String endTime = json.getStr("endTime");
//
//        return storeService.findCountByNameBetweenDates(startTime, endTime);
//    }


    @PostMapping("/findNames")
    public Result getMaterialNamesByStoreTime(
            @RequestBody String requestBody) throws ParseException {
        JSONObject json=new JSONObject(requestBody);
        String startTime = json.getStr("startTime");
        String endTime = json.getStr("endTime");
        return storeService.findNameBetweenDates(startTime, endTime);
    }

    @PostMapping("/findCountByNames")
    @Encrypt
    @Decrypt
    public Result findCountByNameBetweenDates(
            @RequestBody String requestBody) throws ParseException {
        JSONObject json=new JSONObject(requestBody);
        String startTime = json.getStr("startTime");
        String endTime = json.getStr("endTime");

        return storeService.findCountByNameBetweenDates(startTime, endTime);
    }
    @GetMapping
    public Result enter(){
        return Result.ok();
    }

    //@Encrypt 加密
    //@Decrypt 解密

    @GetMapping("/searchAll/{page}")
    @Encrypt
    public Result getAll(@PathVariable("page") Integer page){
        List<Store> result = storeMapper.searchAll(page, PageConstants.STORE_SEARCH_PAGE_SIZE);
        if(result != null){
            QueryWrapper<Store> wrapper = new QueryWrapper<>();
            Long totalPage = (storeMapper.selectCount(wrapper) + PageConstants.STORE_SEARCH_PAGE_SIZE - 1) / PageConstants.STORE_SEARCH_PAGE_SIZE ;
            return new Result(true, "0", result, totalPage);
        }else{
            return new Result(false, "1", null, 0L);
        }
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
            Long totalNum = storeMapper.totalNum(storeNo, houseName, start, end, materialId, userId, notes);
            Long totalPage = (totalNum + PageConstants.STORE_SEARCH_PAGE_SIZE - 1) / PageConstants.STORE_SEARCH_PAGE_SIZE ;
            return new Result(true, "0", result, totalPage);
        }else{
            return new Result(false, null, null, 0L);
        }
    }

    @PostMapping("/simpleStore")
    public Result simpleStore(@RequestBody Map<String, Object> params){
        storeMapper.callSimpleStore(params);
        return Result.ok();
    }

    @PostMapping("/selectStoreByDate")
    @Encrypt
    @Decrypt
    public Result selectStoreByDate(@JsonParam("year") String year, @JsonParam("month") String month){
        System.out.println("year:" + year + " month:" + month);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        Date endDate = calendar.getTime();

        List<Store> result = storeMapper.selectStoreByDate(startDate, endDate);
        if(result != null){
            return new Result(true, "0", result, Long.valueOf(result.size()));
        }else{
            return new Result(false, null, null, 0L);
        }
    }

    @PostMapping("/storeByYear")
    @Encrypt
    @Decrypt
    public Result storeByYear(@JsonParam("Year") String year, @JsonParam("id") Long materialId,
                              @JsonParam("HouseName") String houseName){
        Timestamp startYear = Timestamp.valueOf(year + "-01-01 00:00:00");
        Timestamp endYear = Timestamp.valueOf(year + "-12-31 23:59:59");
        List<Store> stores = storeMapper.selectStoreByYear(startYear, endYear, materialId, houseName);
        if(stores != null){
            return new Result(true, "0", stores, Long.valueOf(stores.size()));
        }
        return new Result(false, null, null, 0L);
    }

}
