package com.abc.warehouse.config;

import com.abc.warehouse.utils.JwtUtils;
import com.abc.warehouse.utils.RsaUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

import static com.abc.warehouse.utils.RsaUtil.createKeys;

/**
 * 设置初始任务
 */
@Configuration
@EnableScheduling
public class InitialTask {

    // 每天凌晨2点更换私钥
    @Scheduled(cron = "0 0 2 * * ?")
    public void refreshSecretKey(){
        JwtUtils.updateSecretKey();
    }

    // 每天凌晨2点更换rsa密钥
    @Scheduled(cron = "0 0 2 * * ?")
    public void refreshRsaKey(){
        try {
            RsaUtil.createKeys();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 应用启动后，将所有权限uri加入redis缓存
     */
//    @PostConstruct
//    public void initPermissionTypes() {
//        permissionTypeService.createRedisCache();
//    }
}
