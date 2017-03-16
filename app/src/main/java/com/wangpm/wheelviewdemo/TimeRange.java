package com.wangpm.wheelviewdemo;


import java.util.Date;

import wheelview.Common;

/**
 * Created by wpm on 2017/1/13.
 */

public class TimeRange {

    /**
     * start_time : 2017-01-13 15:05:25
     * end_time : 2017-01-13 17:00:00
     */

    private String start_time;
    private String end_time;

    public Date getStart_time() {
        return Common.dateTimeFromStr(start_time);
    }

    public void setStart_time(Date start_time) {
        this.start_time = Common.dateTimeToStr(start_time);
    }

    public Date getEnd_time() {
        return Common.dateTimeFromStr(end_time);
    }

    public void setEnd_time(Date end_time) {
        this.end_time = Common.dateTimeToStr(end_time);
    }
}
