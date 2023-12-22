package com.abc.warehouse.controller;

import com.abc.warehouse.dto.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/deliver")
@RestController
public class DeliverController {
    @GetMapping
    public Result enter(){
        return Result.ok();
    }
}
