package com.yupi.springbootinit.controller;


import com.alibaba.fastjson2.JSON;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 队列测试
 */
@RestController
@RequestMapping("/queue")
@Slf4j
// 不对外暴露
@Profile({ "dev", "local" })
@Api(tags = "QueueController")
@CrossOrigin(origins = "http://localhost:8101", allowCredentials = "true")
public class QueueController {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @GetMapping("/add")
    public String add(String name){
        // 异步任务
        CompletableFuture.runAsync(()->{
            log.info("Task Running："+name+"  thread running"+Thread.currentThread().getName());
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, threadPoolExecutor);
        return "success";
    }

    @GetMapping("/get")
    public String get(){
        Map<String, Object> map = new HashMap<>();

        int size = threadPoolExecutor.getQueue().size();
        map.put("队列长度",size);
        long taskCount = threadPoolExecutor.getTaskCount();
        map.put("任务数量",taskCount);
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        map.put("已完成任务数", completedTaskCount);
        long activeCount = threadPoolExecutor.getActiveCount();
        map.put("当前活动的线程数", activeCount);



        return JSON.toJSONString(map);
    }
}
