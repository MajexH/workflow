package xyz.majexh.workflow.Utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

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
}
