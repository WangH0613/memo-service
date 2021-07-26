package cn.com.koriesh.memo.common;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
public class HttpUtil {

    public static String httpGet(String url) {
        String result = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * @Description:
     *
     * 1、url参数说明   -  直接将url和参数拼接到url中 /tickets/create?apiKye=asdasdasd&ssssssss
     * 2、data参数说明   -  将json字符串格式的内容放入data
     *
     * @CreateDate:     2020/12/17 2020/12/17
     * @UpdateDate:     2020/12/17 2020/12/17
     */
    public static String httpPost(String url, String data) {
        String result = null;
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), data);
        Request request = new Request.Builder()
                .url(url)
                .header("'Content-Type", "application/json; charset=utf-8")
                .post(requestBody)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            result = response == null ? "" : response.body().string();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return result;
    }
}
