package com.sky.chains.tools;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.chat.ChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//情绪识别工具
@Component
@Slf4j
public class EmotionTool {
    @Autowired
    private ChatModel qwenChatModel;

    @Tool(name = "detectEmotion", value = "识别一句话的情绪类型，只返回一个词：怒气、投诉、催促、焦虑、中性、满意")
    public String detectEmotion(String userText){
        String prompt = """
            请从下面类别中判断用户语气：怒气、投诉、催促、焦虑、中性、满意
            只输出类别名称。
            用户内容：%s
        """.formatted(userText);
        String s = qwenChatModel.chat(prompt);
        log.debug("用户内容：%s, 的情绪类型：%s", userText, s);
        return qwenChatModel.chat(prompt).trim();
    }

}
