package com.eventbusdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.simple.eventbus.EventBus;

/**
 * Created by zq on 2016/11/16.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText username;
    LinearLayout linearLayout;
    Button register;
    Button exit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("注册界面");
        EventBus.getDefault().register(this);
        initView();
    }
    private void initView()
    {
        username=(EditText)findViewById(R.id.username);
        linearLayout=(LinearLayout)findViewById(R.id.linear);
        linearLayout.setVisibility(View.GONE);
        register=(Button) findViewById(R.id.login);
        register.setText("注册");
        register.setOnClickListener(this);
        exit=(Button)findViewById(R.id.register);
        exit.setText("退出");
        exit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login://注册
                String string=username.getText().toString();
                if (TextUtils.isEmpty(string))
                {
                    Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_LONG).show();
                }
                else {
                    EventBus.getDefault().post(string,Utils.RESULTDATA);
                    finish();
                }
                break;
            case R.id.register://退出
                EventBus.getDefault().post(true,Utils.EXIT);
                finish();
                break;
        }
    }
    /***
     * 解除注册
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
