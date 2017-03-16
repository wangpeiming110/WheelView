package com.wangpm.wheelviewdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import wheelview.Common;
import wheelview.WheelView;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};
    private BottomDialog bottomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WheelView wva = (WheelView) findViewById(R.id.wheel_view);

        wva.setItems(Arrays.asList(PLANETS),1);
        wva.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
            }

        });
        findViewById(R.id.main_show_dialog_btn).setOnClickListener(this);
        findViewById(R.id.main_show_bottom_dialog_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_show_dialog_btn:
                View outerView = LayoutInflater.from(this).inflate(R.layout.dialog_content_view, null);
                final WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                wv.setItems(getNumbers(),0);
                wv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                    }
                });

                new AlertDialog.Builder(this)
                        .setTitle("WheelView in Dialog")
                        .setView(outerView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this,
                                        "selectedIndex: "+ wv.getSelectedPosition() +"  selectedItem: "+ wv.getSelectedItem(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();

                break;
            case R.id.main_show_bottom_dialog_btn:
                View outerView1 = LayoutInflater.from(this).inflate(R.layout.dialog_select_date_time, null);
                //日期滚轮
                final WheelView wv1 = (WheelView) outerView1.findViewById(R.id.wv1);
                //小时滚轮
                final WheelView wv2 = (WheelView) outerView1.findViewById(R.id.wv2);
                //分钟滚轮
                final WheelView wv3 = (WheelView) outerView1.findViewById(R.id.wv3);

                final TimeRange timeRange = getTimeRange();
                wv1.setItems(Common.buildDays(timeRange),0);
                wv2.setItems(Common.buildHourListStart(timeRange),0);
                wv3.setItems(Common.buildMinuteListStart(timeRange),0);

                //联动逻辑效果
                wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index,String item) {
                        List hourStrList = Common.buildHoursByDay(wv1, timeRange);
                        int newIndexHour = hourStrList.indexOf(wv2.getSelectedItem());
                        wv2.setItems(hourStrList,newIndexHour);
                        List minStrList = Common.buildMinutesByDayHour(wv1, wv2, timeRange);
                        int newIndexMin = minStrList.indexOf(wv3.getSelectedItem());
                        wv3.setItems(minStrList,newIndexMin);
                    }
                });
                wv2.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index,String item) {
                        List minStrList = Common.buildMinutesByDayHour(wv1, wv2, timeRange);
                        int newIndexMin = minStrList.indexOf(wv3.getSelectedItem());
                        wv3.setItems(minStrList,newIndexMin);
                    }
                });

                TextView tv_ok = (TextView) outerView1.findViewById(R.id.tv_ok);
                TextView tv_cancel = (TextView) outerView1.findViewById(R.id.tv_cancel);
                //点击确定
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        bottomDialog.dismiss();
                        String selectDateTimeStrToShow;
                        String mSelectDate = wv1.getSelectedItem();
                        String mSelectHour = wv2.getSelectedItem();
                        String mSelectMin = wv3.getSelectedItem();
                        String time = mSelectHour + mSelectMin;
                        time = Common.timeToStr(Common.dateTimeFromCustomStr( mSelectDate, time));
                        selectDateTimeStrToShow = mSelectDate + "  " + time;
                        Toast.makeText(MainActivity.this, "selectDateTime: "+selectDateTimeStrToShow, Toast.LENGTH_SHORT).show();
                    }
                });
                //点击取消
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        bottomDialog.dismiss();
                    }
                });
                //防止弹出两个窗口
                if (bottomDialog !=null && bottomDialog.isShowing()) {
                    return;
                }

                bottomDialog = new BottomDialog(this, R.style.ActionSheetDialogStyle);
                //将布局设置给Dialog
                bottomDialog.setContentView(outerView1);
                bottomDialog.show();//显示对话框
                break;
        }
    }

    private ArrayList getNumbers() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            list.add(i + "");
        }
        return  list;
    }

    //取一个30天内的时间范围进行显示
    private TimeRange getTimeRange() {
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.add(Calendar.DAY_OF_YEAR,30);
        TimeRange timeRange = new TimeRange();
        timeRange.setStart_time(calendarStart.getTime());
        timeRange.setEnd_time(calendarEnd.getTime());
        return timeRange;
    }
}
