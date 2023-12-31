package com.abc.warehouse.mapper;

import cn.hutool.json.JSONObject;
import com.abc.warehouse.pojo.Deliver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 吧啦
* @description 针对表【deliver_208201302】的数据库操作Mapper
* @createDate 2023-12-02 16:59:21
* @Entity com.abc.warehouse.pojo.Deliver
*/
public interface DeliverMapper extends BaseMapper<Deliver> {
    List<Deliver> selectDeliverByYear(@Param("startYear") Date startYear, @Param("endYear") Date endYear,
                                      @Param("materialId") Long materialId, @Param("houseName") String houseName);

        List<Deliver> searchAll(@Param("pageNum") Integer page, @Param("pageSize") Integer pageSize);
        List<Deliver> selectByCondition(@Param("deliverNo") Long deliverNo,
                                        @Param("houseName") String houseName,
                                        @Param("startTime") Date startTime,
                                        @Param("endTime") Date endTime,
                                        @Param("materialId") Long materialId,
                                        @Param("userId") Long userId,
                                        @Param("notes") String notes,
                                        @Param("pageNum")Integer pageNum,
                                        @Param("pageSize")Integer pageSize);

        Long totalNum(@Param("deliverNo") Long deliverNo,
                      @Param("houseName") String houseName,
                      @Param("startTime") Date startTime,
                      @Param("endTime") Date endTime,
                      @Param("materialId") Long materialId,
                      @Param("userId") Long userId,
                      @Param("notes") String notes);

        @MapKey("materialName")
        List<Map<String, Object>> findCountByNameBetweenDates(
                @Param("startTime") Date startTime,
                @Param("endTime") Date endTime
        );

        void callMultiDelivery(@Param("deliverList") String deliverList, @Param("resultMessage") String resultMessage);


    List<Deliver> selectDeliverByYearAndMaterialName(@Param("startYear") Date startYear, @Param("endYear") Date endYear,
                                                     @Param("materialName") String materialName);
}




