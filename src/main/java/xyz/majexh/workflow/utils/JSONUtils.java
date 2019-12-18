package xyz.majexh.workflow.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@Slf4j
public class JSONUtils {

    /**
     * 从inputstream中解析json
     * @param inputStream
     * @return
     */
    public static JSONObject jsonReader(InputStream inputStream) {
        JSONObject json = null;
        try {
            int ch;
            StringBuilder buffer = new StringBuilder();
            while ((ch = inputStream.read()) != -1) {
                buffer.append((char) ch);
            }
            inputStream.close();
            json = JSONArray.parseObject(buffer.toString());
        } catch (IOException e) {
            log.warn("文件读取失败：" + e.toString());
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("关闭流失败");
            }
        }
        return json;
    }

    public static HashMap<String, Object> json2HashMap(JSON params) {
        return JSON.parseObject(params.toString(), new TypeReference<>(){});
    }

    public static JSON hashMap2Json(HashMap<String, Object> hashMap) {
        return new JSONObject(hashMap);
    }
}
