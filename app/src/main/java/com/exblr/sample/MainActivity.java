package com.exblr.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.exblr.rangeseekbar.RangeSeekBar;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DecimalFormat df = new DecimalFormat("0.0");

        final TextView tv = (TextView) findViewById(R.id.text_view);

        final RangeSeekBar range = (RangeSeekBar) findViewById(R.id.range_seek_bar);
        range.setScales(new float[]{0f, 2.5f, 5f, 7.5f, 10f, 12.5f, 15f});
        range.setOnScaleTextFormat(new RangeSeekBar.OnScaleTextFormat() {
            @Override
            public String format(float value, int index) {
                return df.format(value);
            }
        });

        range.setOnRangeChanged(new RangeSeekBar.OnRangeChanged() {
            @Override
            public void onChange(float min, float max) {
                if (min == range.getLimitMin() && max == range.getLimitMax()) {
                    tv.setText("不限");
                } else if (min == range.getLimitMin()) {
                    tv.setText(String.format("%s万以下", df.format(max)));
                } else if (max == range.getLimitMax()) {
                    tv.setText(String.format("%s万以上", df.format(min)));
                } else {
                    tv.setText(String.format("%s万 - %s万", df.format(min), df.format(max)));
                }
            }
        });
    }
}
