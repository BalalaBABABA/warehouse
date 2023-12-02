package com.abc.warehouse.controller;

import com.abc.warehouse.annotation.Decrypt;
import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.dto.EncryotResult;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.utils.RsaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/test1")
    public EncryotResult test1(){
        List<String> list = new ArrayList<>();
        list.add("add");
        list.add("del");
        list.add("modify");
        return EncryotResult.ok(list);
    }

    @GetMapping("/test2")
    @Encrypt
    @Decrypt
    public EncryotResult test2(){
        List<String> list = new ArrayList<>();
        list.add("add");
        list.add("del");
        list.add("modify");
        return EncryotResult.ok(list);
    }
}
