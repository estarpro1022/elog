package com.example.myapplication.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.data.Diary;
import com.example.myapplication.data.DiaryDao;
import com.example.myapplication.data.DiaryDatabase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class DiaryAnalysisActivity extends AppCompatActivity {
    private String tag = "DiaryAnalysisActivity";
    private ImageView back;

    private TextView pieTitle;

    private TextView columnTitle;

    private PieChartView pieChart;     //View
    private PieChartData pieData;

    private boolean pieLabels = true;                   //是否有标语
    private boolean hasLabelsOutside = false;           //扇形外面是否有标语
    private boolean hasCenterCircle = false;            //是否有中心圆
    private boolean hasCenterText1 = false;             //是否有中心的文字
    private boolean hasCenterText2 = false;             //是否有中心的文字
    private boolean isExploded = true;                  //是否是炸开的图像
    private boolean pieLabelForSelected = false;         //选中的扇形显示标语

    private ColumnChartView columnChart;            //柱状图的自定义View
    private ColumnChartData data;             //存放柱状图数据的对象
    private boolean hasAxes = true;            //是否有坐标轴
    private boolean hasAxesNames = true;       //是否有坐标轴的名字
    private boolean hasLabels = false;          //柱子上是否显示标识文字
    private boolean hasLabelForSelected = false;    //柱子被点击时，是否显示标识的文字

    private DiaryDao diaryDao;

    private List<Diary> diaries = new ArrayList<>();
    private List<String> moods = new ArrayList<>();

    private String[] emotionTextList = {"好", "非常棒", "害羞", "呵呵", "困觉", "晕",
            "生气", "惊吓", "委屈", "颓废"};

    private Map<String, Integer> colorData = Map.of("好", Color.parseColor("#ffc038"),
            "非常棒", Color.parseColor("#fb9139"),
            "害羞", Color.parseColor("#faa0bc"),
            "呵呵", Color.parseColor("#e8e193"),
            "困觉", Color.parseColor("#3b5182"),
            "晕", Color.parseColor("#a0ceaf"),
            "生气", Color.parseColor("#ee5856"),
            "惊吓", Color.parseColor("#43889a"),
            "委屈", Color.parseColor("#7d8cab"),
            "颓废", Color.parseColor("#9a77df"));

    private Map<String, Integer> map = new HashMap<>();
    {
        map.put("好", 0);
        map.put("非常棒", 0);
        map.put("害羞", 0);
        map.put("呵呵", 0);
        map.put("困觉", 0);
        map.put("晕", 0);
        map.put("生气", 0);
        map.put("惊吓", 0);
        map.put("委屈", 0);
        map.put("颓废", 0);
    }

    private void initView() {
        pieChart = (PieChartView) findViewById(R.id.pieChart);
        columnChart = (ColumnChartView) findViewById(R.id.columnChart);
    }

    private void initEvent() {
        pieChart.setOnValueTouchListener(new PieTouchListener());
        columnChart.setOnValueTouchListener(new ColumnTouchListener());
    }

    private void initData() {
        generatePieData();
        generateColumnData();
    }

    private void generatePieData() {
        int numValues = 10;   //扇形的数量

        //存放扇形数据的集合
        List<SliceValue> values = new ArrayList<SliceValue>();
        for (String emotion: emotionTextList) {
            if (map.get(emotion) != 0) {
                SliceValue sliceValue = new SliceValue((float) map.get(emotion), colorData.get(emotion));
                sliceValue.setLabel(emotion);
                values.add(sliceValue);
            }
        }

        pieData = new PieChartData(values);
        pieData.setHasLabels(pieLabels);
        pieData.setHasLabelsOnlyForSelected(pieLabelForSelected);
        pieData.setHasLabelsOutside(hasLabelsOutside);
        pieData.setHasCenterCircle(hasCenterCircle);

        pieChart.setPieChartData(pieData);

        if(isExploded) {
            pieData.setSlicesSpacing(5);
        }
    }

    private void generateColumnData() {
        int numSubcolumns = 1;
        int numColumns = 10;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;

        for (String emotion: emotionTextList) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) map.get(emotion), colorData.get(emotion)));
            }

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }

        data = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("心情");
                axisY.setName("数量");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        columnChart.setColumnChartData(data);

    }

    private class PieTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            Toast.makeText(DiaryAnalysisActivity.this, "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {}
    }

    private class ColumnTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            showToast("Selected: " + value);
        }

        @Override
        public void onValueDeselected() {}
    }

    Toast toast;

    public void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_analysis);

        back = findViewById(R.id.activity_diary_analysis_back);
        back.setOnClickListener(view -> {
            finish();
        });

        diaryDao = DiaryDatabase.getInstance(this).getDiaryDao();

        pieTitle = findViewById(R.id.tv_title);
        columnTitle = findViewById(R.id.tv_title_2);

        diaries = diaryDao.queryAllDiaries();
        diaries.sort(Comparator.comparing(Diary::getDate));
        diaries.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        for (Diary diary: diaries) {
            moods.add(diary.getMoodText());
        }

        for (String mood: moods) {
            if (map.containsKey(mood)) {
                map.put(mood, map.get(mood) + 1);
            }
        }

        initView();
        initEvent();
        initData();
    }
}
