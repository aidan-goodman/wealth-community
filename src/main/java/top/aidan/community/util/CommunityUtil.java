package top.aidan.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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
    public static String generateUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成 MD5 加密
     *
     * @param key 需要加密的 键
     * @return 加密后的结果
     */
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 封装 JSON 字符串，多个方法进行重载
     *
     * @param code 码
     * @param msg  信息
     * @param map  对象
     * @return JSON 字符串
     */
    public static String getJsonString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (!map.isEmpty()) {
            json.putAll(map);
        }
        return json.toJSONString();
    }

    public static String getJsonString(int code, String msg) {
        return getJsonString(code, msg, new HashMap<>(0));
    }

    public static String getJsonString(int code) {
        return getJsonString(code, null);
    }
}
