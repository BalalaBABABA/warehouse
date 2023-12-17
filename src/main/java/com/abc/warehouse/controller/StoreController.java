package com.abc.warehouse.controller;

import com.abc.warehouse.annotation.Decrypt;
import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.annotation.JsonParam;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.mapper.StoreMapper;
import com.abc.warehouse.pojo.Store;
import com.abc.warehouse.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
                                  @JsonParam("storeTime") Date startTime, @JsonParam("storeTime") Date endTime,
                                  @JsonParam("materialId") Long materialId,
                                  @JsonParam("userId") Long userId, @JsonParam("notes") String notes){
        List<Store> result = storeMapper.selectByCondition(storeNo, materialId, houseName, startTime,
                endTime, userId, notes);
        if(result != null){
            return new Result(true, "0", result, Long.valueOf(result.size()));
        }else{
            return new Result(false, null, null, 0L);
        }
    }
}
