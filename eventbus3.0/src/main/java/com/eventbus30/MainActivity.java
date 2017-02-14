package com.eventbus30;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("登录界面");
        initView();
        EventBus.getDefault().register(this);
    }
    private void initView()
    {
        username=(EditText)findViewById(R.id.username);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);
    }

    /**
     * priority:事件的优先级类似广播的优先级，优先级越高优先获得消息
     * 在ui线程执行
     * @param user
     * 方式1
     */
    @Subscribe(threadMode = ThreadMode.MAIN,priority = 100)
      public void onUserName(User user)
     {
        username.setText(user.getUsername());
     }
    /**
     * sticky:黏性事件，意思就是先发布事件，在注册也能监听得到。
     * 方式2
     */
   /* @Subscribe(sticky = true)
    public void onExit(User user)
    {
        username.setText(user.getUsername());
    }
*/

    @Override
    public void onClick(View v) {
        String name=username.getText().toString();
        switch (v.getId())
        {
            case R.id.login:
                //EventBus.getDefault().register(this);//测试黏性事件
                break;
            case R.id.register:
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                break;
        }
    }

    /***
     * 反注册
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
