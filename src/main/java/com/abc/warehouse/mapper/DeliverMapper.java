package com.abc.warehouse.mapper;

import com.abc.warehouse.pojo.Deliver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

        @MapKey("materialName")
        List<Map<String, Object>> findCountByNameBetweenDates(
                @Param("startTime") Date startTime,
                @Param("endTime") Date endTime
        );

        void callMultiDelivery(@Param("deliverList") List<Deliver> deliverList, @Param("resultMessage") String resultMessage);


}




