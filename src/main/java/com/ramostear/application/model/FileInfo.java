package com.ramostear.application.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.ramostear.application.util.CustomStringStringConverter;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * @author : ramostear
 * @date  : 2019/3/8 0008-15:25
 */
@Data
public class FileInfo {
    @ExcelProperty(value="字符串类型",converter = CustomStringStringConverter.class)
    private String name;
    @ExcelProperty("日期类型")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date uploadTime = new Date();
    @ExcelProperty("数字类型")
    @NumberFormat(roundingMode= RoundingMode.CEILING) /*百分比数字*/
    private BigDecimal bigDecimal;
    @ExcelIgnore
    private String ignore;

    public FileInfo setFileName(String name){
        this.setName ( name );
        return this;
    }


}
