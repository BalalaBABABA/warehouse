package com.abc.warehouse.controller;

import com.abc.warehouse.service.MaterialTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/materialType")
public class MaterialTypeController {
    @Autowired
    private MaterialTypeService materialTypeService;

}
