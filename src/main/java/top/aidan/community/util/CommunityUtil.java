package top.aidan.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author Aidan
 * @createTime 2021/11/11 10:11
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

public class CommunityUtil {
    /**
     * 生成随机的 UUID
     *
     * @return 去掉 `-` 的字符串值
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
    }
}
