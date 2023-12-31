package com.abc.warehouse.controller;

import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.mapper.StoreMapper;
import com.abc.warehouse.service.DeliverService;
import com.abc.warehouse.service.StoreService;

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
    @Autowired
    private DeliverMapper deliverMapper;
    @Autowired
    private MaterialService materialService;

    @GetMapping
    public Result enter(){
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
