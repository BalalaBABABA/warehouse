package com.abc.warehouse.config;

import com.abc.warehouse.utils.JwtUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableScheduling
public class InitialTask {
    //添加定时任务，每天24点更换私钥
    @Scheduled(cron = "0 0 0 * * ?")
    public void RefreshSecretKey(){
        JwtUtils.updateSecretKey();
    }

}
