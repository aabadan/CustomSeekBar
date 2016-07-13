package com.example.aabadan.customseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by AAbadan on 18.6.2016.
 */
public class CustomSeekBar extends LinearLayout {

    private TextView thumbTextView;
    private String textParam;
    private SeekBar seekBar;
    private int min;
    private int max;
    private int incrementBy;
    private int progress;
    private boolean textAfterValue;
    private boolean isFloatValue;
    private int progressPos = 0;
    private DecimalFormat df;

    public CustomSeekBar(Context context) {
        super(context);
        initView(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomSeekBar);
        this.textParam = ta.getString(R.styleable.CustomSeekBar_csbThumbText);
        this.min = ta.getInteger(R.styleable.CustomSeekBar_csbMin,0);
        this.max = ta.getInteger(R.styleable.CustomSeekBar_csbMax,90);
        this.incrementBy = ta.getInt(R.styleable.CustomSeekBar_csbIncrementBy,1);
        this.textAfterValue = ta.getBoolean(R.styleable.CustomSeekBar_csbTextAfterValue,true);
        this.isFloatValue = ta.getBoolean(R.styleable.CustomSeekBar_csbIsFloatValue,false);
        initView(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

   private void initView(Context context){
       LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View view = inflater.inflate(R.layout.component_seekbar,this,true);

       thumbTextView = (TextView) view.findViewById(R.id.customSeekBarTextView);
       seekBar = (SeekBar) view.findViewById(R.id.customSeekBar);
       seekBar.setMax((max - min ) / incrementBy);
       seekBar.setThumbOffset(0);

       thumbTextView.setText(getGeneratedText());
       thumbTextView.setX(seekBar.getThumb().getBounds().exactCenterX() - seekBar.getThumb().getMinimumWidth() / 2);

       seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

           @Override
           public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
               progressPos = progressValue;

               thumbTextView.setText(getGeneratedText());

               //if thumb text overflows view
               if(seekBar.getThumb().getBounds().exactCenterX() - seekBar.getThumb().getMinimumWidth() / 2 + thumbTextView.getWidth() > seekBar.getWidth()){
                   thumbTextView.setX(seekBar.getWidth() - thumbTextView.getWidth() - seekBar.getThumb().getMinimumWidth() / 2);
               }else {
                   thumbTextView.setX(seekBar.getThumb().getBounds().exactCenterX() - seekBar.getThumb().getMinimumWidth() / 2);
               }

           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {}

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
               thumbTextView.setText(getGeneratedText());
           }
       });

   }

    private String getGeneratedText(){
        String gText = "";
        String progressTxt = "";
        df = new DecimalFormat("#,###");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);

        progress = min + progressPos * incrementBy;

        if(isFloatValue) {
            float progressF = getPercentage(progress);
            progressTxt = progressF + "";
        }else{
            progressTxt = df.format(progress);
        }

        if(textAfterValue) {
            gText =  progressTxt + " " + textParam;
        }else{
            gText = textParam + " " + progressTxt;
        }

        return gText;
    }

    public float getPercentage(int intVal){
        return intVal / 100.0f;
    }
}
