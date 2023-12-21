package com.abc.warehouse.controller;

import com.abc.warehouse.annotation.Decrypt;
import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.annotation.JsonParam;
import com.abc.warehouse.dto.EncryotResult;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.utils.RsaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rsa")
public class RSAController {
    @GetMapping
    public String getPublicKey(){
        return RsaUtil.keyStore.getPublicKey();
    }

    @PostMapping("/test")
    @Encrypt
    @Decrypt
    public EncryotResult test(){
        List<String> list = new ArrayList<>();
        list.add("add");
        list.add("del");
        list.add("modify");
        return EncryotResult.ok(list);
    }

    @GetMapping("/test1/{id}")
    @Encrypt
    public EncryotResult test1(@PathVariable("id") Long id){
        List<String> list = new ArrayList<>();
        list.add("add");
        list.add("del");
        list.add("modify");
        return EncryotResult.ok(list);
    }

    @PostMapping("/test2")
    @Encrypt
    @Decrypt
    public EncryotResult test2(@RequestBody User user){
        System.out.println(user.getId());
        List<String> list = new ArrayList<>();
        list.add("add");
        list.add("del");
        list.add("modify");
        return EncryotResult.ok(list);
    }
}
