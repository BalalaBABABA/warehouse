package com.abc.warehouse.mapper;

import com.abc.warehouse.pojo.FreeUri;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 吧啦
* @description 针对表【free_uri_208201302】的数据库操作Mapper
* @createDate 2023-12-21 13:32:59
* @Entity com.abc.warehouse.pojo.FreeUri
*/
public interface FreeUriMapper extends BaseMapper<FreeUri> {
    List<String> getFreeUriList();

    boolean deleteFromFreeUri( @Param("permissionId")Long permissionId);

    boolean AddUriToFreeUri( @Param("permissionId")Long permissionId);
}




