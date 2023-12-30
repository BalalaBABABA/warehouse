package com.abc.backend_server.mapper;

import com.abc.backend_server.pojo.Resource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResourceMapper {

    List<Resource> listAll();

    Boolean addResource(@Param("name")String name,
                        @Param("uriName")String uriName,
                        @Param("icon")String icon,
                        @Param("page")String page);

    Boolean delResource(@Param("resourceId") Long resourceId);

    void updateResource(@Param("id")Long id,
                        @Param("name")String name,
                        @Param("uri")String uri,
                        @Param("icon")String icon,
                        @Param("page")String page);
}