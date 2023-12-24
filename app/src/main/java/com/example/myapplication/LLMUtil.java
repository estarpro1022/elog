package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LLMUtil extends AsyncTask<String, Void, Void> {
    public static String DEFAULT = "还没有数据哦";
    public static String MOOD_DESC = "{‘活力小狗’：‘兴奋、快乐、乐观和喜悦心情偏多’，" +
            "‘幸福小兔’：‘幸福、心动、轻松、温暖心情偏多’，" + "‘困困考拉’：‘困、懒、晕、懵、发呆心情偏多’，" +
            "‘无语羊驼’：‘无语、烦躁、郁闷心情偏多’，" + "‘摆烂乌龟’：‘疲惫、劳累、躺平、摆烂心情偏多’，" +
            "‘悲伤青蛙’：‘悲伤、难过、崩溃心情偏多’，‘爆炸河豚’：‘恼火、生气、愤怒心情偏多’，" +
            "‘庄子的鲲鹏’：‘拼搏、奋进、努力、奋斗、积极心情偏多’，}";
    private boolean isPersonalityExists = false;
    private String personality = null;
    private String cache =
            "{\"role\": \"user\",\"content\": \"以下是我为你提供的心情人格数据，以json格式给出。```心情人格开始\"" + MOOD_DESC + "```心情人格结束}," +
            "{\"role\": \"assistant\",\"content\": \"xxx\"\n}";


    private AsyncTaskListener mListener;

    public LLMUtil(AsyncTaskListener listener) {
        this.mListener = listener;
    }

    @Override
    protected Void doInBackground(String... strings) {
        OkHttpClient client=new OkHttpClient();
        String messages = cache;
        if(isPersonalityExists){

        }else{

        }
        String parms = "{" +
                "\"model\": \"ChatGLM3-6B\",\n" +
                "\"max_tokens\": 2048,\n" +
                "\"top_p\": 1,\n" +
                "\"temperature\": 1,\n" +
                "\"messages\": [" +
                    "{\"role\": \"user\",\"content\": \"以下是我为你提供的心情人格数据，以json格式给出。```心情人格开始\"" + MOOD_DESC + "```心情人格结束},\n" +
                    "{\"role\": \"assistant\",\"content\": \"Hello!\"\n},\n" +
                    "{\"role\": \"user\",\"content\": \"以下是我为你提供的用户周心情数据，以列表形式给出。```用户心情数据开始" + strings[0] + "\"},\n" +
                "]\n" +
                "};";
        // 构建请求对象
        String SERVER = "http://10.58.0.2:6678/v1/chat/completions";
        Request request = new Request.Builder()
                .url(SERVER)
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), parms))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "openaikey123456")  // 替换为实际的 API 密钥
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 处理请求失败的情况
                Log.e("HTTP Request", "Failure");
                mListener.onTaskComplete(DEFAULT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    // 在这里处理响应数据
                    // 在 UI 线程更新 UI
                    mListener.onTaskComplete(responseData);
                } else {
                    Log.e("HTTP Request", "Error: " + response.code());
                    mListener.onTaskComplete(DEFAULT);
                }
            }
        });
        return null;
    }

    public interface AsyncTaskListener {
        void onTaskComplete(String result);
    }

    //public class MainActivity extends AppCompatActivity implements MyAsyncTask.AsyncTaskListener {
    //
    //    private TextView mTextView;
    //
    //    @Override
    //    protected void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        setContentView(R.layout.activity_main);
    //
    //        mTextView = findViewById(R.id.textView);
    //
    //        // 创建并执行 AsyncTask
    //        MyAsyncTask myAsyncTask = new MyAsyncTask(this);
    //        myAsyncTask.execute();
    //    }
    //
    //    @Override
    //    public void onTaskComplete(String result) {
    //        // 在任务完成后更新 UI
    //        mTextView.setText(result);
//    // 创建一个SpannableString
//            SpannableString content = new SpannableString("这是一行带下划线的文字");
//
//    // 添加UnderlineSpan到SpannableString
//            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//
//// 将SpannableString设置给TextView
//            textView.setText(content);
    //    }
    //}
}
