package com.abc.warehouse.mapper;

import com.abc.warehouse.annotation.JsonParam;
import com.abc.warehouse.pojo.Store;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 吧啦
* @description 针对表【store_208201302】的数据库操作Mapper
* @createDate 2023-12-02 16:59:21
* @Entity com.abc.warehouse.pojo.Store
*/
public interface StoreMapper extends BaseMapper<Store> {
    List<Store> searchAll(@Param("pageNum") Integer page, @Param("pageSize") Integer pageSize);
    List<Store> selectByCondition(@Param("storeNo") Long storeNo,
                                  @Param("houseName") String houseName,
                                  @Param("startTime") Date startTime,
                                  @Param("endTime") Date endTime,
                                  @Param("materialId") Long materialId,
                                  @Param("userId") Long userId,
                                  @Param("notes") String notes,
                                  @Param("pageNum")Integer pageNum,
                                  @Param("pageSize")Integer pageSize);
    Long totalNum(@Param("storeNo") Long storeNo,
                  @Param("houseName") String houseName,
                  @Param("startTime") Date startTime,
                  @Param("endTime") Date endTime,
                  @Param("materialId") Long materialId,
                  @Param("userId") Long userId,
                  @Param("notes") String notes);

    List<Store> selectStoreByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @MapKey("materialName")
    List<Map<String, Object>> findCountByNameBetweenDates(
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime
    );


    List<Store> selectStoreByYear(@Param("startYear") Date startYear, @Param("endYear") Date endYear,
                                  @Param("materialId") Long materialId, @Param("houseName") String houseName);

    List<Store> selectStoreByYearAndMaterialName(@Param("startYear") Date startYear, @Param("endYear") Date endYear,
                                                 @Param("materialName") String materialName);

//    @Select("{call callStoreProcedure(#{storeList, mode=IN}, #{resultMessage, mode=OUT, jdbcType=VARCHAR})}")
//    @Options(statementType = StatementType.CALLABLE)
//    void callStoreProcedure(@Param("storeList") String storeList, @Param("resultMessage") String resultMessage);

    List<Map<String, Object>> CallStoreProcedure(Map<String, Object> map);


}