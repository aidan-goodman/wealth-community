package top.aidan.community.util;

import lombok.Data;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Aidan
 * @createTime 2021/11/30 12:23
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Component
public class SensitiveFilter {

    public static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    /**
     * 对敏感词进行替换
     */
    public static final String REPLACEMENT = "***";

    /**
     * 使用空的哑结点作为根节点使用
     */
    public TrieNode rootNode = new TrieNode();

    /**
     * 敏感词的前缀树的构建（启动时构建）
     * 从类目录下进行字典的读取
     */
    @PostConstruct
    public void init() {
        try (
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 使用封装的字典树构建方法
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("敏感词加载失败：" + e.getMessage());
        }
    }

    /**
     * 对每个敏感测进行构建
     *
     * @param keyword 传入的敏感词
     */
    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            // 节点未创建时进行构建
            if (subNode == null) {
                subNode = new TrieNode();
                tempNode.setSubNode(c, subNode);
            }

            // 存在直接进入下一结点
            tempNode = subNode;

            // 判断结尾进行标识
            if (i == keyword.length() - 1) {
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 进行文本的敏感词过滤处理
     *
     * @param text 待处理文本
     * @return 处理后的结果
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        // 三个指针进行判断，curNode 标识当前结点
        TrieNode curNode = rootNode;
        // 非法词的入口
        int begin = 0;
        // 目前检验的非法词
        int position = 0;

        // 用来保存合法或替换后的词
        StringBuilder sb = new StringBuilder();

        // 当检验未到达结尾时持续判断
        while (position != text.length()) {
            char c = text.charAt(position);

            // 如果是符号进行略过
            if (isSymbol(c)) {
                // 未进入判断的符号进行保留（敏感词之外的符号）
                if (curNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            // 字符不再存在敏感，回到树的起始，结束当前判断
            if (curNode.getSubNode(c) == null) {
                sb.append(text.charAt(begin));
                position = ++begin;
                curNode = rootNode;
            }
            // 到达敏感词的结束标识，进行文本的替换并略过指针，回到树的起始
            else if (curNode.getSubNode(c).isKeyWordEnd()) {
                sb.append(REPLACEMENT);
                begin = ++position;
                curNode = rootNode;
            }
            // 待定判断进入下一轮
            else {
                position++;
                curNode = curNode.getSubNode(c);
            }
        }
        return sb.toString();
    }

    /**
     * 检验是否为符号
     *
     * @param c 当前字符
     * @return 判断
     */
    private boolean isSymbol(char c) {
        // 不是亚洲字符而且在东亚文字范围之外
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    /**
     * 构建前缀树的结点结构
     */
    @Data
    private static class TrieNode {
        // 敏感词结尾标识
        private boolean isKeyWordEnd = false;
        // 敏感字符的节点
        private Map<Character, TrieNode> subNode = new HashMap<>();

        public TrieNode getSubNode(Character c) {
            return subNode.get(c);
        }

        public void setSubNode(Character c, TrieNode node) {
            subNode.put(c, node);
        }
    }
}
