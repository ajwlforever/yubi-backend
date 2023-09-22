package com.yupi.springbootinit.manager;


import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.common.XfunListener;
import com.yupi.springbootinit.config.dto.Xfun.MsgDTO;
import okhttp3.WebSocket;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供AI的服务
 */
@Service
public class AiManager {

    public String pompt = "\n" +
            "\n" +
            "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容： \n" +
            "\n" +
            "分析需求：\n" +
            "\n" +
            " {数据分析的需求或者目标} \n" +
            "\n" +
            "原始数据：\n" +
            "\n" +
            " {csv格式的原始数据，用,作为分隔符} \n" +
            "\n" +
            "请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释，请用【【【【【将这两部分隔开）：1. {前端 Echarts V5 的 option 配置对象的json格式代码，合理地将数据进行可视化，不要生 成任何多余的内容，比如注释}2. {明确的数据分析结论、越详细越好，不要生成多余的注释}";

    @Resource
    private XfunListener xfunListener;  // 实现发送接受消息的websockect

    public List<String> doChat(String goal, String chartType, String csvData){
        StringBuilder question = new StringBuilder();
        question.append(pompt).append('\n');
        question.append("分析需求：").append('\n');
        question.append(goal).append('\n');
        // 图标类型
        if(StringUtils.isNotEmpty(chartType)){
            question.append("请用").append(chartType).append("的形式展示").append('\n');
        }
        question.append("原始数据：").append('\n');
        question.append(csvData).append('\n');
        //8位随机数
        String random = String.valueOf((int)((Math.random()*9+1)*10000000));

        List<MsgDTO> msgs = new ArrayList<>();
        MsgDTO msgDTO = new MsgDTO( );
        msgDTO.setRole("user");
        msgDTO.setContent(question.toString());
        msgDTO.setIndex(0);
        msgs.add(msgDTO);

        // 发送消息
        try {
            XfunListener webSocket = xfunListener.sendMsg(random , msgs, xfunListener );
            //等待weSocked返回消息
            int cnt = 30;
            //最长等待30S
            while (!webSocket.isFinished() && cnt > 0){
                Thread.sleep(1000);  //休息1S
                cnt--;
            }
            if(cnt == 0){
                return null;
            }
            String answer = webSocket.getAnswer();
            //answer 分开
            return parseAnswer(answer);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static List<String> parseAnswer(String answer){
        // answer转化为分析答案+图表代码
        List<String> ans = new ArrayList<>();
        String[] strs = answer.split("```");
        String genResult = strs[2];
        genResult.replace('{', ' ');
        genResult.replace('}', ' ');
        String genChart = strs[1];
        //genChart去掉前4个字符
        genChart = genChart.substring(4);
        ans.add(genChart);
        ans.add(genResult);
        return ans;
    }

    public static void main(String[] args) {
        AiManager.parseAnswer("1. {前端 Echarts V5 的 option 配置对象的json格式代码，合理地将数据进行可视化}\n" +
                "\n" +
                "```json\n" +
                "{\n" +
                "  \"title\": {\n" +
                "    \"text\": \"网站用户变化\"\n" +
                "  },\n" +
                "  \"tooltip\": {},\n" +
                "  \"legend\": {\n" +
                "    \"data\":[\"人数\"]\n" +
                "  },\n" +
                "  \"xAxis\": {\n" +
                "    \"data\": [\"1\",\"2\",\"3\",\"4\",\"5\"]\n" +
                "  },\n" +
                "  \"yAxis\": {},\n" +
                "  \"series\": [{\n" +
                "    \"name\": \"人数\",\n" +
                "    \"type\": \"line\",\n" +
                "    \"data\": [10, 11, 12, 13, 14]\n" +
                "  }]\n" +
                "}\n" +
                "```\n" +
                "\n" +
                "2. {明确的数据分析结论、越详细越好，不要生成多余的注释}\n" +
                "\n" +
                "从给定的数据中，我们可以看出网站的用户数量在逐渐增长。具体来说，从第一天的10人增长到第五天的14人，整体呈现出上升趋势。这种趋势可能是由于网站的内容吸引了更多的用户，或者是由于有效的营销策略吸引了新的用户。为了进一步了解这种趋势，我们可以进行更深入的分析，例如分析每天的用户增长量，或者比较不同日期的用户数量。\n");
    }



}
