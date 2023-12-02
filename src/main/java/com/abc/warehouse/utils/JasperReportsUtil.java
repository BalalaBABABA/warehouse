package com.abc.warehouse.utils;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.*;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

/**
 * @version 1.0.0
 * @Date: 2023/8/7 14:14
 * @Author ZhuYouBin
 * @Description: JasperReports 工具类
 */
public class JasperReportsUtil {

    /**
     * 使用 JasperReports 生成报表文件
     * @param templatePath 模板文件路径及名称
     * @param fileName 生成的文件名称
     * @param fileType 生成的文件类型,例如: pdf、html、xls 等
     * @param parameters 传递到 jrxml 模板文件中的数据参数
     * @return 返回生成的报表文件路径
     */
    public static String generateReport(String templatePath, String fileName, String fileType, Map<String, Object> parameters) throws Exception {
        // 1、获取 jasper 模板文件【采用流的方式读取】
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream in = resource.getInputStream();
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(in);
        // 2、将 parameters 数据参数填充到模板文件中
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        // 3、按照指定的 fileType 文件类型导出报表文件
        if (fileType == null || "".equals(fileType.trim())) {
            fileType = "pdf";
        }
        if (Objects.equals("pdf", fileType)) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, fileName + ".pdf");
        } else if (Objects.equals("xls", fileType)) { // 导出 xls 表格
            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint)); // 设置导出的输入源
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileName + ".xls")); // 设置导出的输出源
            // 配置信息
            SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
            configuration.setOnePagePerSheet(true); // 每一页一个sheet表格
            exporter.setConfiguration(configuration); // 设置配置对象
            exporter.exportReport(); // 执行导出
        } else if (Objects.equals("xlsx", fileType)) {  // 导出 xlsx 表格
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint)); // 设置导出的输入源
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileName + ".xlsx")); // 设置导出的输出源
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(true); // 每一页一个sheet表格
            exporter.setConfiguration(configuration);
            exporter.exportReport(); // 执行导出
        } else if (Objects.equals("html", fileType)) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, fileName + ".html");
        }
        return null;
    }

}