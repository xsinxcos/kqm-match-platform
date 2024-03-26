package com.chaos.model.word;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import com.chaos.model.AbstractModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import toolgood.words.StringMatch;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 敏感词过滤模型
 * @author: xsinxcos
 * @create: 2024-03-26 19:56
 **/
@Component
@Slf4j
public class WordFilterDataModel extends AbstractModel {
    private static StringMatch stringMatch;

    private final static String SENSITIVE_WORDS_NAME = "static/sensi_words.csv";


    public WordFilterDataModel() {
        stringMatch = new StringMatch();
        init();
    }

    private void init() {
        ClassPathResource classPathResource = new ClassPathResource(SENSITIVE_WORDS_NAME);
        try {
            // 读取文件
            InputStream inputStream = classPathResource.getStream();
            // 将文件内容读取为字符串列表
            List<String> sensitive_words = new ArrayList<>();
            IoUtil.readLines(inputStream, StandardCharsets.UTF_8, sensitive_words);
            //将词典导入
            stringMatch.SetKeywords(sensitive_words);
            log.info("敏感词模型构建成功");
        } catch (Exception e) {
            log.error("敏感词模型构建失败");
        }
    }

    public String replaceText(String text, Character replaceWord) {
        return stringMatch.Replace(text, replaceWord);
    }
}
