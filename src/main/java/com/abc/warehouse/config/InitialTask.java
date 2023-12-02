package com.abc.warehouse.config;

import com.abc.warehouse.service.PermissionTypeService;
import com.abc.warehouse.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

/**
 * 设置初始任务
 */
@Configuration
@EnableScheduling
public class InitialTask {
    @Autowired
    private PermissionTypeService permissionTypeService;

    // 每天凌晨2点更换私钥
    @Scheduled(cron = "0 0 2 * * ?")
    public void refreshSecretKey(){
        JwtUtils.updateSecretKey();
    }

    /**
     * 应用启动后，将所有权限uri加入redis缓存
     */
    @PostConstruct
    public void initPermissionTypes() {
        permissionTypeService.createRedisCache();
    }
}
