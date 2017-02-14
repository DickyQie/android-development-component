package com.eventbusdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/****
 * eventbus通信
 * 1.0版本
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("登录界面");
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initView();
    }
    private void initView()
    {
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);

    }

    /***
     * (String)->这里可以是任意类型--（在返回的时候对应就行）
     * @param title
     */
    @Subscriber(tag = Utils.RESULTDATA)
    public void resultData(String title)
    {
        username.setText(title);
    }
    //退出
    @Subscriber(tag=Utils.EXIT)
    public void exit(boolean isFang)
    {
        if (isFang) {
            finish();
        }
    }
    @Override
    public void onClick(View v) {
        String name=username.getText().toString();
        String pwd=password.getText().toString();
        switch (v.getId())
        {
            case R.id.login:
                break;
            case R.id.register:
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
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
