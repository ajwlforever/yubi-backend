package com.yupi.springbootinit.service;

import com.yupi.springbootinit.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ajwlforever
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2023-09-13 21:44:18
*/
public interface ChartService extends IService<Chart> {

    public void sendMsgToQueue(String msg);
}
