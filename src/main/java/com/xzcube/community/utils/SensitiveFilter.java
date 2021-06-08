package com.xzcube.community.utils;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xzcube
 * @date 2021/6/8 20:10
 *
 * 敏感词过滤 (使用前缀树)
 */
@Component
public class SensitiveFilter {
    private static final String REPLACEMENT = "***"; // 替换敏感词的字符串
    private TrieNode rootNode = new TrieNode(); // 根节点

    // 初始化前缀树
    @PostConstruct // 注解表示这个bean被初始化后调用此方法
    public void init(){
        // 获取敏感词文件的输入流(小括号中的数据流会在try执行完毕后关闭)
        try (
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)); // 创建缓冲流
        ) {
            String keyWord;
            while ((keyWord = reader.readLine()) != null){
                // 当reader读取到数据的时候，就添加到前缀树中
                this.addKeyWord(keyWord);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将敏感词添加到前缀树中
     * @param keyWord
     */
    private void addKeyWord(String keyWord){
        TrieNode tempNode = rootNode;
        // 遍历keyWord中的每个字符，将它们挂在前缀树上面
        for (int i = 0; i < keyWord.length(); i++) {
            char c = keyWord.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            if(subNode == null){
                // 初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            // 让指针指向子节点
            tempNode = subNode;

            if(i == keyWord.length() - 1){
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        // 指针1
        TrieNode tempNode = rootNode;
        // 指针2
        int begin = 0;
        // 指针3
        int position = 0;
        // 封装结果
        StringBuilder sb = new StringBuilder();

        while (position < text.length()){
            char c = text.charAt(position);
            // 跳过特殊符号（防止别人在敏感词中穿插特殊符号） 但是加上以后上传图片的url会被破坏，所以注释掉
            /*if(isSymbol(c)){
                // 若指针1处于根节点，将此符号计入结果，让指针2向下走一步
                if(tempNode == rootNode){
                    begin++;
                }
                // 无论特殊符号在开头还是中间，指针3都向下走一步
                position++;
                continue;
            }*/
            // 检查下一级节点
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
                // 以begin为开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
            }else if(tempNode.isKeyWordEnd){
                // 发现敏感词 将begin到position之间的字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                tempNode = rootNode;
            }else {
                // 继续检查下一个字符
                position++;
            }
        }

        // 将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    // 判断是否为特殊符号
    private boolean isSymbol(Character c){
        // 0x2E80-0x9FFF是东亚文字范围，在这些东亚文字范围以及Ascii码之外才是特殊符号
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }


    // 前缀树
    public class TrieNode{
        private boolean isKeyWordEnd = false;
        // 子节点 key是下级字符，value是下级节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();
        public boolean isKeyWordEnd(){
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean isKeyWordEnd){
            this.isKeyWordEnd = isKeyWordEnd;
        }

        // 添加子节点
        public void addSubNode(Character c, TrieNode node){
            subNodes.put(c, node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
