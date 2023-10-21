package com.abc.warehouse.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/autoupgrade")
@Slf4j
public class AutoUpgradeController {
    @Value("${server.version}")
    private String version;

    @Value("${server.upgrade-zip}")
    private String zipFileName;

    @Autowired
    private ResourceLoader resourceLoader;
    /**
     * 返回更新的zip包
     * @return
     * @throws IOException
     */
    @GetMapping( produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<FileSystemResource> downloadZip() throws IOException {
        /**
         * 1.获取zip包路径，得到zipfile文件
         * 2.设置响应头
         * 3.返回
         */
        String staticPath = resourceLoader.getResource("classpath:static").getFile().getAbsolutePath();
        String zipFilePath = staticPath + "\\autoupgrade\\"+zipFileName; // 替换为实际的文件名和扩展名
        log.info("用户访问安装包:"+zipFilePath);
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        FileSystemResource fileSystemResource = new FileSystemResource(zipFile);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + zipFile.getName());
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileSystemResource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(fileSystemResource);
    }

    /**
     * 返回版本号
     * @return
     */
    @GetMapping("/version")
    public String getVersion(){
        return version;
    }
}
