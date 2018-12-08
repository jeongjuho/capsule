package com.example.my.recapsule;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReplyCustomAdapter extends LinearLayout {

    TextView id,text;


    public ReplyCustomAdapter(Context context) {
        super(context);

        init(context);
    }

    public ReplyCustomAdapter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutInflater.inflate(R.layout.reply_item,this,true);

        id = (TextView)findViewById(R.id.id);
        text = (TextView)findViewById(R.id.Text);
    }

    public void setid(String id1){
        id.setText(id1);

    }
    public void setText(String text1){
        text.setText(text1);
    }
}
