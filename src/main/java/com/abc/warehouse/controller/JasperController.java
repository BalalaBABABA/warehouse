package com.abc.warehouse.controller;

import com.abc.warehouse.pojo.Deliver;
import com.abc.warehouse.service.DeliverService;
import com.abc.warehouse.utils.JasperReportsUtil;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/jasper")
@RestController
public class JasperController {

    @Autowired
    private DeliverService deliverService;
    @GetMapping
    public void test5(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Resource resource = new ClassPathResource("static/test10.jasper");
        String templatePath = resource.getFile().getPath();
        String fileName = "Jasper导出文件";
        Map<String, Object> parameters = new HashMap<>();
        List<Deliver> list = deliverService.list();
        Deliver emptyDeliver = new Deliver(); // 创建一个空的Deliver对象，假设有Deliver类并且有一个无参构造函数
        list.add(0, emptyDeliver); // 将空的Deliver对象添加到列表的开头
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
        parameters.put("tableData", dataSource);
        JasperPrint jasperPrint = JasperFillManager.fillReport(templatePath, parameters, dataSource);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".pdf");

        try (OutputStream outputStream = response.getOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            outputStream.flush();
        }
    }


}
