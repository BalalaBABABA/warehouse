package com.abc.warehouse.controller;

import com.abc.warehouse.annotation.Decrypt;
import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.annotation.JsonParam;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.mapper.DeliverMapper;
import com.abc.warehouse.mapper.StoreMapper;
import com.abc.warehouse.pojo.Deliver;
import com.abc.warehouse.pojo.Store;
import com.abc.warehouse.service.DeliverService;
import com.abc.warehouse.service.MaterialService;
import com.abc.warehouse.service.StoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.TimeZone;

@RequestMapping("/deliver")
@RestController
public class DeliverController {
    @Autowired
    private DeliverService deliverService;
    @Autowired
    private DeliverMapper deliverMapper;
    @Autowired
    private MaterialService materialService;

    @GetMapping
    public Result enter(){
        return Result.ok();
    }
    @GetMapping("/searchAll/{page}")
    public Result getAll(@PathVariable("page") Integer page){
        return deliverService.deliverPage(page);
    }

    @PostMapping("/deliverByYear")
    @Encrypt
    @Decrypt
    public Result deliverByYear(@JsonParam("Year") String year, @JsonParam("id") Long materialId,
                              @JsonParam("HouseName") String houseName){
        Timestamp startYear = Timestamp.valueOf(year + "-01-01 00:00:00");
        Timestamp endYear = Timestamp.valueOf(year + "-12-31 23:59:59");
        List<Deliver> delivers = deliverMapper.selectDeliverByYear(startYear, endYear, materialId, houseName);
        for (Deliver deliver : delivers) {
            System.out.println(deliver);
        }
        if(delivers != null){
            return new Result(true, "0", delivers, Long.valueOf(delivers.size()));
        }
        return new Result(false, null, null, 0L);
    }
}
