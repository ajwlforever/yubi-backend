package com.yupi.springbootinit.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
public class ExcelUtils {

    /**
     * Excel 转 CSV
     *
     * @param multipartFile
     * @return csv格式的字符串
     */
    public static String excelToCSV(MultipartFile multipartFile) {


//        try {
//            file = ResourceUtils.getFile("data.xlsx");
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }

        // excel转换成list
        List<Map<Integer, String>> list = null;
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("文件读取失败",e);
        }
        // System.out.println(list);

        if(CollUtil.isEmpty(list)){
            return "";
        }
        // 结果值
        StringBuilder res = new StringBuilder();

        LinkedHashMap<Integer, String> headerMap =(LinkedHashMap) list.get(0);
        // map转换成list，并且过滤掉空字符串
        List<String> headerList = CollUtil.newArrayList(headerMap.values().stream().filter(header -> StrUtil.isNotBlank(header)).collect(Collectors.toList()));
        res.append(CollUtil.join(headerList, ",")).append("\n");
        // 读取每一行
        for (int i = 1; i < list.size(); i++) {
            LinkedHashMap<Integer, String> rowMap =(LinkedHashMap) list.get(i);
            List<String> rowList = CollUtil.newArrayList(rowMap.values().stream().filter(row -> StrUtil.isNotBlank(row)).collect(Collectors.toList()));


            // 每一行的值加到res上
            res.append(CollUtil.join(rowList, ",")).append("\n");

        }

        return res.toString();

    }

    public static void main(String[] args) {
        ExcelUtils.excelToCSV(null);
    }
}
