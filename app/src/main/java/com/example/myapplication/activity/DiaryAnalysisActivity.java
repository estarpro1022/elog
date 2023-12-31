package com.example.myapplication.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

public class DiaryAnalysisActivity extends AppCompatActivity {
    private String tag = "DiaryAnalysisActivity";
    private ImageView back;

    private LineChartView lineChartView;
    // x轴数据
    private String[] data = {"1s前", "2s前", "3s前", "4s前", "5s前", "6s前", "7s前", "8s前", "9s前", "10s前"};
    // 每个点的y轴数据
    private Float[] score = {Float.valueOf(541), Float.valueOf(429), Float.valueOf(865), Float.valueOf(901), Float.valueOf(503), Float.valueOf(787), Float.valueOf(222), Float.valueOf(666), Float.valueOf(800), Float.valueOf(525)};
    private List<AxisValue> axisValues = new ArrayList<AxisValue>();
    private List<PointValue> pointValues = new ArrayList<PointValue>();

    //折线集合  用于存放多个折线，即可以显示多条折线
    private List<Line> lines = new ArrayList<>();
    //折线图数据对象
    private LineChartData lData = new LineChartData();
    private void  initData(){
        for (int i = 0; i < 10; i++) {
            axisValues.add(new AxisValue(i).setLabel(data[i]));
            pointValues.add(new PointValue(i, score[i]));
        }
    }

    /**
     * 创建一条线   这里可以创建多条线然后添加到lines中即可
     */
    private void createLine(){
        Line mLine = new Line();
        // 为线添加点
        mLine.setValues(pointValues);

        //设置线的颜色
        mLine.setColor(Color.parseColor("#5699BB"));

        //曲线是否平滑，即是曲线还是折线
        mLine.setCubic(true);

        //设置点的形状
        mLine.setShape(ValueShape.SQUARE);

        //设置点击坐标提醒  平常不显示 点击才显示
//        mLine.setHasLabelsOnlyForSelected(true);

        //取消线
//        mLine.setHasLines(false);


        //是否填充面积
        mLine.setFilled(false);

        //设置坐标数据显示
        mLine.setHasLabels(true);
        //将所有的线都添加到线的集合中
        lines.add(mLine);
    }

    /**
     * 对x轴和y轴进行配置
     */
    private void createXAndY(){
        //设置X轴
        Axis axisX = new Axis(); //X轴
        //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setHasTiltedLabels(true);
        //设置字体颜色
        axisX.setTextColor(Color.parseColor("#1C1C1C"));
        //设置字体大小
        axisX.setTextSize(15);
        //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setMaxLabelChars(2);
        //填充X轴的坐标名称
        axisX.setValues(axisValues);

        //设置标题
        axisX.setName("test");

        //设置是否拥有轴的分界线
        axisX.setHasLines(true);

        lData.setAxisXBottom(axisX); //x 轴在底部


        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        lData.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_diary_analysis);

        back = findViewById(R.id.activity_diary_analysis_back);
        back.setOnClickListener(view -> {
            finish();
        });

        setContentView(R.layout.activity_diary_analysis);
        lineChartView = findViewById(R.id.lineChart);
        initData();
        createLine();
        createXAndY();

        lData.setLines(lines);

        //把数据对象设置到折线图中
        lineChartView.setLineChartData(lData);

        //设置是否允许平移和缩放
        lineChartView.setInteractive(false);

        //设置缩放的轴
        lineChartView.setZoomType(ZoomType.HORIZONTAL);

        //设置缩放的倍数
        lineChartView.setMaxZoom(5);
    }
}
