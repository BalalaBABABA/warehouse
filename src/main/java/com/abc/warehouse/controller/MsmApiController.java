package com.abc.warehouse.controller;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.service.MsmService;
import com.abc.warehouse.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;



@RestController
@RequestMapping("/msm")
public class MsmApiController {
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;    //注入redis


    @GetMapping(value = "/send/{phone}")
    public Result code(@PathVariable String phone) {
        //1、从redis中获取验证码，如果获取到就直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)) return Result.fail("验证码已发送，请勿重复操作");

        //2、如果获取不到，就进行阿里云发送
        code = RandomUtil.getFourBitRandom();//生成验证码的随机值
        Map<String,Object> param = new HashMap<>();
        param.put("code", code);

        //调用方法
        boolean isSend = msmService.send(param,phone);
        if(isSend) {
            //往redis中设置数据：key、value、过期值、过期时间单位  MINUTES代表分钟
            redisTemplate.opsForValue().set(phone, code,20, TimeUnit.MINUTES);
            return Result.ok();
        } else {
            return Result.fail("发送验证码失败");
        }
    }

    @GetMapping("/checkCode/{key}/{code}")
    public Result checkCode(@PathVariable("key") String key,
                            @PathVariable("code")String code){
        //获取到操作String的对象
        ValueOperations<String,String> value = redisTemplate.opsForValue();
        //根据key值查询
        String redisCode = value.get(key);
        if (code.equals(redisCode)){
            System.out.println("通过");
            return Result.ok("验证通过");
        }

        return redisCode == null ? Result.fail("请先获取验证码再进行校验！") : Result.fail("短信验证码错误，请重新输入");
    }

}

