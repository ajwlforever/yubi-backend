package com.yupi.springbootinit.utils;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * EasyExcel 测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class EasyExcelTest {


    public void doImport() {

        File file = null;
        try {
            file = ResourceUtils.getFile("data.xlsx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
         List<Map<Integer, String>> list = EasyExcel.read(file)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet()
                .headRowNumber(0)
                .doReadSync();


        System.out.println(list);
    }


    public static void main(String[] args) {
        // 输出当前工作路径
            new EasyExcelTest().doImport();


    }

}