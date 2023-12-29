package com.abc.warehouse.mapper;

import com.abc.warehouse.pojo.Store;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
    List<Store> selectByCondition(@Param("storeNo") Long storeNo,
                                  @Param("houseName") String houseName,
                                  @Param("startTime") Date startTime,
                                  @Param("endTime") Date endTime,
                                  @Param("materialId") Long materialId,
                                  @Param("userId") Long userId,
                                  @Param("notes") String notes,
                                  @Param("pageNum")Integer pageNum,
                                  @Param("pageSize")Integer pageSize);

    @Select("{call simpleStore_208201302(#{no, mode=IN, jdbcType=BIGINT}, #{hname, mode=IN, jdbcType=VARCHAR}," +
            " #{time, mode=IN, jdbcType=DATE}, #{mid, mode=IN, jdbcType=BIGINT}, #{storenum, mode=IN, jdbcType=INTEGER}, " +
            "#{uid, mode=IN, jdbcType=BIGINT}, #{note, mode=IN, jdbcType=VARCHAR})}")
    @Options(statementType = StatementType.CALLABLE)
    void callSimpleStore(Map<String, Object> params);

    List<Store> selectStoreByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}