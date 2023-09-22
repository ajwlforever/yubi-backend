package com.yupi.springbootinit.config.dto.chart;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class GenChartByAiRequest {

    /**
     * 分析目标
     */
    private String goal;
    /**
     * 图表名称
     */
    private String name;


    /**
     * 图表类型
     */
    private String chartType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
