package com.abc.backend_server.controller;

import com.abc.backend_server.dto.Result;
import com.abc.backend_server.pojo.FreeUri;
import com.abc.backend_server.pojo.PermissionTypeUri;
import com.abc.backend_server.service.FreeUriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/free_uri")
public class FreeUriController {
    @Autowired
    private FreeUriService freeUriService;

    @GetMapping("/byresource")
    public Result FreeByResourceId(@RequestParam("resourceId")Long resourceId){
        return freeUriService.delByResourceId(resourceId);
    }
    @GetMapping("/bytype")
    public Result FreeByPermssionTypeId(@RequestParam("typeId")Long typeId){
        return freeUriService.delByPermissionId(typeId);
    }
    @GetMapping("/byuri")
    public Result FreeByUriId(@RequestParam("uriId")Long uriId){
        return freeUriService.delByUriId(uriId);
    }

    @GetMapping("/list")
    public Result listAll(){
        return freeUriService.getFreeUriList();
    }
    @PostMapping("/del")
    public Result del(@RequestBody List<FreeUri> list){
        return freeUriService.delIdList(list);
    }

    @GetMapping("/add")
    public Result add(@RequestParam("uri")String uri){
        return freeUriService.AddFreeUri(uri);
    }

    @PostMapping("/update")
    public Result update(@RequestBody FreeUri freeUri){
        return freeUriService.updateFreeUri(freeUri);
    }
}
