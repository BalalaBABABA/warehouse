package com.abc.warehouse.controller;
import com.abc.warehouse.utils.RsaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rsa")
public class RSAController {
    @GetMapping
    public String getPublicKey(){
        return RsaUtil.keyStore.getPublicKey();
    }
}
