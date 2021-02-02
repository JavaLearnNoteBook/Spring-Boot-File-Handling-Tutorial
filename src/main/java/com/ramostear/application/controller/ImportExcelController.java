package com.ramostear.application.controller;

import com.alibaba.excel.EasyExcel;
import com.ramostear.application.model.FileInfo;
import com.ramostear.application.util.ExcelDataListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImportExcelController {

    @GetMapping("/easyexcel/11111")
    @ResponseBody
    public void downloadFile() throws IOException {
        InputStream templateInputStream = getClass().getClassLoader().getResourceAsStream("test.xlsx");
        EasyExcel.read(templateInputStream, FileInfo.class, new ExcelDataListener()).sheet().doRead();
        return ;
    }
}
