package com.abc.warehouse.service.impl;

import cn.hutool.json.JSONUtil;
import com.abc.warehouse.dto.EncryotResult;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.constants.PageConstants;
import com.abc.warehouse.utils.GenerateID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.Deliver;
import com.abc.warehouse.service.DeliverService;
import com.abc.warehouse.mapper.DeliverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* @author 吧啦
* @description 针对表【deliver_208201302】的数据库操作Service实现
* @createDate 2023-12-02 16:59:21
*/
@Service
public class DeliverServiceImpl extends ServiceImpl<DeliverMapper, Deliver>
    implements DeliverService{

    @Autowired
    private DeliverMapper deliverMapper;
    @Resource
    private GenerateID generateID;

    public Result getAll(Integer page){
        List<Deliver> result = deliverMapper.searchAll(page, PageConstants.DELIVER_SEARCH_PAGE_SIZE);
        if(result != null){
            QueryWrapper<Deliver> wrapper = new QueryWrapper<>();
            Long totalPage = (deliverMapper.selectCount(wrapper) + PageConstants.DELIVER_SEARCH_PAGE_SIZE - 1) / PageConstants.DELIVER_SEARCH_PAGE_SIZE ;
            return new Result(true, "0", result, totalPage);
        }else{
            return new Result(false, "1", null, 0L);
        }
    }

    public Result conditionSearch(Long deliverNo, String houseName, String startTime,String endTime,
                                  Long materialId, Long userId,
                                  String notes,Integer page) throws ParseException
    {
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

        List<Deliver> result = deliverMapper.selectByCondition(deliverNo, houseName, start,
                end, materialId, userId, notes, page, PageConstants.DELIVER_SEARCH_PAGE_SIZE);

        if(result != null){
            Long totalNum = deliverMapper.totalNum(deliverNo, houseName, start, end, materialId, userId, notes);
            Long totalPage = (totalNum + PageConstants.DELIVER_SEARCH_PAGE_SIZE - 1) / PageConstants.DELIVER_SEARCH_PAGE_SIZE ;
            return new Result(true, "0", result, totalPage);
        }else{
            return new Result(false, null, null, 0L);
        }
    }

    public Result deliverPage(Integer curPage) {
        LambdaQueryWrapper<Deliver> wrapper = new LambdaQueryWrapper<>();  //查询条件构造器
        IPage<Deliver> pageQuery = new Page<>(curPage, PageConstants.DELIVER_SEARCH_PAGE_SIZE);
        IPage<Deliver> page = baseMapper.selectPage(pageQuery, wrapper);
        System.out.println(page);
        return Result.ok(page.getRecords(), page.getPages());
    }


    public Result findNameBetweenDates(Date startTime, Date endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null, end = null;

//        if(startTime != null && !startTime.isEmpty()){
//            //start = sdf.parse(startTime);
//        }
//        if(endTime != null && !endTime.isEmpty()){
//            //end = sdf.parse(endTime);
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(end);
//            calendar.add(Calendar.DATE, 1);
//            end = calendar.getTime();
//        }
        List<String> nameList = new ArrayList<>();
        List<Map<String, Object>> resultMapList = deliverMapper.findCountByNameBetweenDates(startTime,endTime);
        // 遍历列表并获取每个Map的键和值
        for (Map<String, Object> resultMap : resultMapList) {
            nameList.add(resultMap.get("materialName").toString());
        }
        return EncryotResult.ok(nameList);
    }

    public Result findCountByNameBetweenDates(Date startTime, Date endTime) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date start = null, end = null;
//
//        if(startTime != null && !startTime.isEmpty()){
//            start = sdf.parse(startTime);
//        }
//        if(endTime != null && !endTime.isEmpty()){
//            end = sdf.parse(endTime);
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(end);
//            calendar.add(Calendar.DATE, 1);
//            end = calendar.getTime();
//        }
        List<Long>countList = new ArrayList<>();
        List<Map<String, Object>> resultMapList = deliverMapper.findCountByNameBetweenDates(startTime,endTime);
        // 遍历列表并获取每个Map的键和值
        for (Map<String, Object> resultMap : resultMapList) {
            Long totalDeliverCount = (Long) resultMap.get("totalDeliverCount");
            countList.add(totalDeliverCount);
        }
        return EncryotResult.ok(countList);

    }

    public Result MultiDelivery(String deliverList) {
        List<Deliver> list = JSONUtil.toList(deliverList, Deliver.class);
        Long deliverNo = generateID.getId("2", "deliver");
        for(Deliver deliver : list){
            deliver.setDeliverNo(deliverNo);
            deliver.setDeliverTime(null);
        }
        String jsonList = JSONUtil.toJsonStr(list);

        Map<String, Object> map = new HashMap<>();
        map.put("deliverList", jsonList);
        List<Map<String, Object>> result = deliverMapper.CallDeliverProcedure(map);
        String resultMessage = (String) map.get("resultMessage");
        System.out.println(resultMessage);
        if(resultMessage.equals("出库成功"))
        {
            return EncryotResult.ok(resultMessage);
        }
        else {
            return EncryotResult.fail(resultMessage);
        }
//        System.out.println(list.toString());
//
//
//
//        List<Store> list = JSONUtil.toList(deliverList, Store.class);
//        long id = generateID.getId("2", "Deliver");
//        for (Deliver deliver : list) {
//            deliver.setDeliverNo(id);
//            deliver.setDeliverTime(null);
//        }
//
//        String jsonList = JSONUtil.toJsonStr(list);
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("storeList", jsonList);
//        List<Map<String, Object>> result = storeMapper.CallStoreProcedure(map);
//        String resultMessage = (String) map.get("resultMessage");
//        System.out.println(resultMessage);
//        return Result.ok(resultMessage);
//
//
//
//        long id = generateID.getId("2", "Deliver");
//        for (Deliver deliver : list) {
//            deliver.setDeliverNo(id);
//        }
//        String deliverList=list.toString();
//        String resultMessage=" ";
//        // 调用存储过程
//        deliverMapper.callMultiDelivery(deliverList, resultMessage);
//        if(resultMessage.equals("出库成功"))
//        {
//            return Result.ok(resultMessage);
//        }
//        else {
//            return Result.fail(resultMessage);
//        }
    }
}




