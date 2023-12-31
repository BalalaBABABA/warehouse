package com.abc.warehouse.service;

import com.abc.warehouse.pojo.FreeUri;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 吧啦
* @description 针对表【free_uri_208201302】的数据库操作Service
* @createDate 2023-12-21 13:32:59
*/
public interface FreeUriService extends IService<FreeUri> {
    List<String> getFreeUriList();

    boolean deleteFromFreeUri(Long permissionId);

    boolean AddUriToFreeUri(Long permissionId);

}
