package com.example.myapplication.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LLMUtil extends AsyncTask<String, Void, Void> {
    public static String DEFAULT = "人生不是一场竞赛，有时候放慢脚步，适当休息，反而能够更好地迎接挑战和充实自己";
    private static final String SERVER = "http://10.58.0.2:6678/v1/chat/completions";
    public static String MOOD_DESC = "{‘活力小狗’：‘兴奋、快乐、乐观和喜悦心情偏多’，" +
            "‘困困考拉’：‘困、懒、晕、懵、发呆心情偏多’，" +
            "‘无语羊驼’：‘无语、烦躁、郁闷心情偏多’，" + "‘摆烂乌龟’：‘疲惫、劳累、躺平、摆烂心情偏多’，" +
            "‘悲伤青蛙’：‘悲伤、难过、崩溃心情偏多’，‘爆炸河豚’：‘恼火、生气、愤怒心情偏多’" +
            "}";
    private String messages;
    private WeakReference<AsyncTaskListener> mListenerReference;
    private int number = -1;
    private String llm;

    public LLMUtil(AsyncTaskListener listener) {
        this.mListenerReference = new WeakReference<>(listener);
    }

    @Override
    protected Void doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(3000, TimeUnit.SECONDS) // 设置整个调用的超时时间
                .connectTimeout(1500, TimeUnit.SECONDS) // 设置连接超时时间
                .readTimeout(1500, TimeUnit.SECONDS) // 设置读取超时时间
                .build();

        messages = "{\"role\": \"user\",\"content\": \"以下是我为你提供的心情人格数据，以json格式给出。```心情人格开始" + MOOD_DESC + "```心情人格结束。" +
                "以下是我为你提供的用户周心情数据，以列表形式给出，'无'代表当日没有用户心情数据。```用户心情数据开始" + strings[0] + "```用户心情数据结束。" +
                "作为一个合格的心理专家，请根据用户这一周的心情数据，从活力小狗、困困考拉、无语羊驼、摆烂乌龟、悲伤青蛙、爆炸河豚中选择最贴近用户心情的一个人格返回，要求用中文返回，内容仅包含最符合人格的名字\"}\n";
        String parms = "{\n" +
                "\"model\": \"ChatGLM3-6B\",\n" +
                "\"max_tokens\": 4096,\n" +
                "\"top_p\": 1,\n" +
                "\"temperature\": 1,\n" +
                "\"messages\": [" + messages + "]\n" +
                "}";
        System.out.println(parms);

        // 构建请求对象
        Request request = new Request.Builder()
                .url(SERVER)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parms))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "elog")  // 替换为实际的 API 密钥
                .build();

        // 设置为默认值
        number = -1;
        llm = DEFAULT;

        // 发送请求
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseData = extractFromJsonObject(response.body().string());
                Log.i("大模型返回", responseData);
                // 在这里处理响应数据
                if(responseData.contains("无语羊驼")){
                    number = 0;
                    responseData = "无语羊驼";
                }else if(responseData.contains("活力小狗")){
                    number = 1;
                    responseData = "活力小狗";
                }else if(responseData.contains("悲伤青蛙")){
                    number = 2;
                    responseData = "悲伤青蛙";
                }else if(responseData.contains("困困考拉")){
                    number = 3;
                    responseData = "困困考拉";
                }else if(responseData.contains("爆炸河豚")){
                    number = 4;
                    responseData = "爆炸河豚";
                }else if(responseData.contains("摆烂乌龟")){
                    number = 5;
                    responseData = "摆烂乌龟";
                }else{
                    return null;
                }
                Log.i("大模型返回", String.valueOf(number));
                // 继续询问大语言模型
                messages += ",{\"role\": \"assistant\",\"content\": \"" + responseData + "\"}\n" +
                        ",{\"role\": \"user\",\"content\": \"请你用一句中文来安慰或鼓励用户。\"}";
                String parms2 = "{\n" +
                        "\"model\": \"ChatGLM3-6B\",\n" +
                        "\"max_tokens\": 4096,\n" +
                        "\"top_p\": 1,\n" +
                        "\"temperature\": 1,\n" +
                        "\"messages\": [" + messages + "]\n" +
                        "}";
                System.out.println(parms2);

                // 构建请求对象
                Request request2 = new Request.Builder()
                        .url(SERVER)
                        .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), parms2))
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "elog")  // 替换为实际的 API 密钥
                        .build();

                Response response2 = client.newCall(request2).execute();
                if (response2.isSuccessful()) {
                    // 在这里处理响应数据
                    llm = extractFromJsonObject(response2.body().string());
                    Log.i("大模型返回", llm);
                } else {
                    Log.e("HTTP Request2", "Error: " + response2.code());
                }
            } else {
                Log.e("HTTP Request1", "Error: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        if(number == -1){
            Random random = new Random();
            number = random.nextInt(6);
        }
        mListenerReference.get().onPersonalityComplete(number, llm);
    }

    public interface AsyncTaskListener {
        void onPersonalityComplete(int result, String llm);
    }

    private String extractFromJsonObject(String body){
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
        // 获取 "choices" 数组
        JsonArray jsonArray = jsonObject.getAsJsonArray("choices");
        if(jsonArray == null){
            return DEFAULT;
        }
        // 获取数组中第一个元素的 "message" 对象
        JsonObject messageObject = jsonArray.get(0).getAsJsonObject().getAsJsonObject("message");
        // 获取 "content" 字段的值
        body = messageObject.getAsJsonPrimitive("content").getAsString();
        return body;
    }
}
