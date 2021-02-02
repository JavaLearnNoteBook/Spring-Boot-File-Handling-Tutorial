package com.ramostear.application.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.ramostear.application.model.FileInfo;
import com.ramostear.application.util.WriteDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ExcelController {
    @Autowired
    private WriteDelegate writeDelegate;

    @GetMapping("/easyexcel/")
    @ResponseBody
    public void downloadFile(HttpServletResponse response) throws IOException {

        response.setHeader("Content-Disposition", "attachment;filename=" + "EasyExcelTemplate.xlsx");
        //通知客服文件的MIME类型
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");

        // Student 对象中包含模板占位符中的属性
        List<FileInfo> list = writeDelegate.initData();
        // 模板注意 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
        // {} 代表普通变量 {.} 代表是list的变量
        // 这里模板 删除了list以后的数据，也就是统计的这一行
        InputStream templateInputStream = getClass().getClassLoader().getResourceAsStream("EasyExcelTemplate.xlsx");
//        ByteArrayOutputStream out = new ByteArrayOutputStream(); 可以获取inputstream
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
//        // 直接写入数据
//        excelWriter.fill(list, writeSheet);
//        excelWriter.fill(list, writeSheet);
        // 这里注意 入参用了forceNewRow 代表在写入list的时候不管list下面有没有空行 都会创建一行，然后下面的数据往后移动。默认 是false，会直接使用下一行，如果没有则创建。但是这个就会把所有数据放到内存 会很耗内存
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(list, fillConfig, writeSheet);

        // 写入list之前的数据
        Map<String, Object> map = new HashMap<>();
        map.put("remark", "王梦颖");
        map.put("remarkDate", "2019年10月9日13:28:28");
        excelWriter.fill(map, writeSheet);

        // list 后面还有个统计 想办法手动写入
        // 这里偷懒直接用list 也可以用对象
        List<List<String>> totalListList = new ArrayList<>();
        List<String> totalList = new ArrayList<>();
        totalListList.add(totalList);
        totalList.add(null);
        totalList.add(null);
        totalList.add(null);
        // 第四列
        totalList.add("统计:1000");
        // 这里是write 别和fill 搞错了
        excelWriter.write(totalListList, writeSheet);
        excelWriter.finish();
        // 总体上写法比较复杂 但是也没有想到好的版本 异步的去写入excel 不支持行的删除和移动，也不支持备注这种的写入，所以也排除了可以
        // 新建一个 然后一点点复制过来的方案，最后导致list需要新增行的时候，后面的列的数据没法后移，后续会继续想想解决方案
//        try {

//            //获取文件的路径
//            InputStream input = new ByteArrayInputStream(out.toByteArray());
        // 这里的 InputStream 可以上传到文件存储服务器，异步导出
//            OutputStream outStream = response.getOutputStream();
//
//            byte[] b = new byte[2048];
//            int len;
//            while ((len = input.read(b)) != -1) {
//                outStream.write(b, 0, len);
//            }
//            outStream.flush();
//            outStream.close();
//            input.close();
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }
        /**
         * 实战中，我们导出一个 Excel 文件的时候，
         * 并不想也不需要写到服务器磁盘里，而是做为请求的响应，让用户去下载。
         * 或者是上传到 OSS 对象存储仓库中，然后返回一个下载链接。如何实现呢？
         *
         * 这里演示一下，从 resources 目录读取模板，填充数据，
         * 然后得到一个 InputStream，用于响应用户请求或者上传 OSS。
         */
    }
}
