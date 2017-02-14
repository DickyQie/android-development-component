package com.eventbus30;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zq on 2016/11/16.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText username;
    Button register;
    Button exit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("注册界面");
        initView();
    }
    private void initView()
    {
        username=(EditText) findViewById(R.id.username);
        register=(Button) findViewById(R.id.login);
        register.setText("注册");
        register.setOnClickListener(this);
        exit=(Button)findViewById(R.id.register);
        exit.setText("退出");
        exit.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        String string=username.getText().toString();
        switch (v.getId())
        {
            case R.id.login://注册
                if (TextUtils.isEmpty(string))
                {
                    Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_LONG).show();
                }
                else {
                    User user=new User();
                    user.setUsername(string);
                    EventBus.getDefault().post(user);
                    finish();
                }
                break;
            case R.id.register:
                if (TextUtils.isEmpty(string))
                {
                    Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_LONG).show();
                }
                else {
                    User user = new User();
                    user.setUsername(string);
                    EventBus.getDefault().postSticky(user);
                    finish();
                }

                break;
        }
    }
}
