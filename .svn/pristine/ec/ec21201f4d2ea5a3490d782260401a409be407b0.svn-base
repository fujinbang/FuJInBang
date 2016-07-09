package com.fujinbang.presenter;

import com.fujinbang.ui.activity.iactivity.IQuietView;
import com.fujinbang.presenter.ipresenter.IQuietPresenter;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

/**
 * Created by Administrator on 2016/6/2.
 */
public class QuietPresenter implements IQuietPresenter {

    private IQuietView view;

    public QuietPresenter(IQuietView view) {
        this.view = view;
    }

    @Override
    public void setStartTime() {
        view.showTimePickDialog(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout layout, int hourOfDay, int minute) {
                String min = minute + "";
                String hour = hourOfDay + "";
                if (minute < 10) min = "0" + min;
                if (hourOfDay < 10) hour = "0" + hour;
                view.showStartTime(hour + ": " + min);
            }
        });
    }

    @Override
    public void setEndTime() {
        view.showTimePickDialog(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout layout, int hourOfDay, int minute) {
                String min = minute + "";
                String hour = hourOfDay + "";
                if (minute < 10) min = "0" + min;
                if (hourOfDay < 10) hour = "0" + hour;
                view.showEndTime(hour + ": " + min);
            }
        });
    }
}
