package com.example.myapplication.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Random;

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
            "‘幸福小兔’：‘幸福、心动、轻松、温暖心情偏多’，" + "‘困困考拉’：‘困、懒、晕、懵、发呆心情偏多’，" +
            "‘无语羊驼’：‘无语、烦躁、郁闷心情偏多’，" + "‘摆烂乌龟’：‘疲惫、劳累、躺平、摆烂心情偏多’，" +
            "‘悲伤青蛙’：‘悲伤、难过、崩溃心情偏多’，‘爆炸河豚’：‘恼火、生气、愤怒心情偏多’，" +
            "‘庄子的鲲鹏’：‘拼搏、奋进、努力、奋斗、积极心情偏多’，}";
    private String cache =
            "{\"role\": \"user\",\"content\": \"以下是我为你提供的心情人格数据，以json格式给出。```心情人格开始\"" + MOOD_DESC + "```心情人格结束}," +
            "{\"role\": \"assistant\",\"content\": \"xxx\"\n}";
    private WeakReference<AsyncTaskListener> mListenerReference;
    private int number = -1;
    private String llm = "人生不是一场竞赛，有时候放慢脚步，适当休息，反而能够更好地迎接挑战和充实自己";

    public LLMUtil(AsyncTaskListener listener) {
        this.mListenerReference = new WeakReference<>(listener);
    }

    @Override
    protected Void doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();
        String messages = cache +
                ",\"{\"role\": \"user\",\"content\": \"以下是我为你提供的用户周心情数据，以列表形式给出，\"无\"代表当日没有用户心情数据。```用户心情数据开始" + "" + "```用户心情数据结束\n" +
                "作为一个合格的心理专家，请根据用户这一周的心情数据，从上述的心情人格中选择最符合的一个人格，以<--心情人格-->的形式返回\"}";
        String parms = "{" +
                "\"model\": \"ChatGLM3-6B\",\n" +
                "\"max_tokens\": 2048,\n" +
                "\"top_p\": 1,\n" +
                "\"temperature\": 1,\n" +
                "\"messages\": [" + messages + "]\n" +
                "};";
        // 构建请求对象
        Request request = new Request.Builder()
                .url(SERVER)
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), parms))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "openaikey123456")  // 替换为实际的 API 密钥
                .build();
        System.out.println(parms);
        number = -1; //

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 处理请求失败的情况
                Log.e("HTTP Request", "Failure");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    System.out.println(responseData);
                    // 在这里处理响应数据
                    if(responseData.matches("无语羊驼")){
                        number = 0;
                    }else if(responseData.matches("活力小狗")){
                        number = 1;
                    }else if(responseData.matches("悲伤青蛙")){
                        number = 2;
                    }else if(responseData.matches("困困考拉")){
                        number = 3;
                    }else if(responseData.matches("爆炸河豚")){
                        number = 4;
                    }else if(responseData.matches("摆烂乌龟")){
                        number = 5;
                    }else{
                        llm = DEFAULT;
                        return;
                    }
                    // 继续询问大语言模型
                    String messages = cache +
                            ",\"{\"role\": \"user\",\"content\": \"以下是我为你提供的用户周心情数据，以列表形式给出，\"无\"代表当日没有用户心情数据。```用户心情数据开始" + strings[0] + "```用户心情数据结束\n" +
                            "作为一个合格的心理专家，请根据用户这一周的心情数据，从上述的心情人格中选择最符合的一个人格，以<--心情人格-->的形式返回\"}";
                    messages += ",{\"role\": \"assistant\",\"content\": \"" + llm + "\"\n},{\"role\": \"user\",\"content\": \"请你用一句话来安慰或鼓励用户\"}";
                    String parms2 = "{" +
                            "\"model\": \"ChatGLM3-6B\",\n" +
                            "\"max_tokens\": 2048,\n" +
                            "\"top_p\": 1,\n" +
                            "\"temperature\": 1,\n" +
                            "\"messages\": [" + messages + "]\n" +
                            "};";
                    System.out.println(parms2);

                    // 构建请求对象
                    Request request2 = new Request.Builder()
                            .url(SERVER)
                            .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), parms2))
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "openaikey123456")  // 替换为实际的 API 密钥
                            .build();

                    client.newCall(request2).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            // 处理请求失败的情况
                            Log.e("HTTP Request", "Failure");
                            llm = DEFAULT;
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                // 在这里处理响应数据
                                llm = response.body().string();
                            } else {
                                Log.e("HTTP Request", "Error: " + response.code());
                                llm = DEFAULT;
                            }
                        }
                    });
                } else {
                    Log.e("HTTP Request", "Error: " + response.code());
                }
            }
        });
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
}
