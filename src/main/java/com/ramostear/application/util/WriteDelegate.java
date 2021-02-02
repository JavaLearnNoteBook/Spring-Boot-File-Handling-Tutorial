package com.ramostear.application.util;

import com.ramostear.application.model.FileInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class WriteDelegate {
    public List<FileInfo> initData(){
        List<FileInfo> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName("名字"+i);
            fileInfo.setIgnore("忽略"+i);
            fileInfo.setUploadTime(new Date());
            fileInfo.setBigDecimal(new BigDecimal(1.0009999+i));
            list.add(fileInfo);
        }
        return list;
    }
}
