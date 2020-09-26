package com.nowcoder.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    @Override
    public void afterPropertiesSet() {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
            System.out.println(SensitiveService.class.getClassLoader().getResource(""));
            System.out.println(ClassLoader.getSystemClassLoader().getResource(""));
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                addWord(lineTxt.trim());
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

    // 增加关键词
    private void addWord(String lineTxt) {
        TrieNode tempNode = rootNode;   // 当前节点 == 根节点
        for (int i = 0; i < lineTxt.length(); i++) {
            Character c = lineTxt.charAt(i);
            TrieNode node = tempNode.getSubNode(c);
            if (node == null) {      // 当前节点下无该节点
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;
            if (i == lineTxt.length() - 1) {
                tempNode.setWordEnd(true);
            }
        }
    }


    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        String replacement = "**";
        TrieNode tempNode = rootNode;   //指针1
        int begin = 0;      //指针2
        int postion = 0;    //指针3
        while (postion < text.length()) {
            char c = text.charAt(postion);
//            if (isSymbol(c)) {
//                if (tempNode == rootNode) {  // 敏感词内部被加非法字符才忽视
//                    result.append(c);
//                    begin++;
//                }
//                postion++;
//                continue;
//            }
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                result.append(text.charAt(begin));
                begin++;
                postion = begin;
                tempNode = rootNode;
            } else if (tempNode.isWordEnd()) {
                // 发现敏感词
                result.append(replacement);
                postion++;
                begin = postion;
                tempNode = rootNode;
            } else {
                postion++;
            }
        }
        result.append(text.substring(begin)); // 加上最后一串
        return result.toString();
    }

    // 判断是否是非法字符
    public boolean isSymbol(char c) {
        int ic = (int) c;
        // 不是英文和东亚文字的话判定为非法符号           东亚文字 0x2E80 ~ 0x9FFF
        return !(CharUtils.isAsciiAlphanumeric(c) || (ic > 0x2E80 && ic < 0x9FFF));
    }

    private class TrieNode {
        // 是不是敏感词结尾
        private boolean end;
        // 当前节点下所有的子节点  abc ac ad
        //   a     Map {B,TrieNodeB}  Map {C,TrieNodeC}  Map {D,TrieNodeD}
        // b c d
        // c
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        public TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        public boolean isWordEnd() {
            return end;
        }

        public void setWordEnd(boolean end) {
            this.end = end;
        }
    }

    private TrieNode rootNode = new TrieNode();

}
