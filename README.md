# Android之EventBus1.0 和EventBus3.0的使用详解 
<p>当Android项目越来越庞大的时候，应用的各个部件之间的通信变得越来越复杂，那么我们通常采用的就是Android中的解耦组件EventBus。EventBus是一款针对Android优化的发布/订阅事件总线。主要功能是替代Intent,Handler,BroadCast在Fragment，Activity，Service，线程之间传递消息.优点是开销小，代码更优雅。以及将发送者和接收者解耦。</p> 
<p>EventBus文档：http://greenrobot.org/eventbus/documentation</p> 
<p>EventBus源码： <span style="background-color:rgb(255, 255, 255); color:rgb(75, 75, 75)">https://github.com/greenrobot/EventBus.git</span></p> 
<span id="OSC_h4_1"></span>
<h4>EventBus框架中涉及四个成分</h4> 
<p>订阅者，发布者，订阅事件，事件总线 它们的关系可以用官方的图表示：</p> 
<p><img alt="" height="192" src="https://static.oschina.net/uploads/space/2017/0214/162455_FarY_2945455.png" width="516"></p> 
<p>EventBus1.0</p> 
<p>首先你要为你的app添加依赖库：</p> 
<pre><code class="language-java"> compile 'org.simple:androideventbus:1.0.5.1'</code></pre> 
<p>Activity中使用有三部：</p> 
<p>1.注册（onCreate方法下）</p> 
<pre><code class="language-java">EventBus.getDefault().register(this);</code></pre> 
<p>2.监听（自定义方法即可）</p> 
<pre><code class="language-java">@Subscriber(tag = Utils.RESULTDATA)
public void resultData(String title)
{
  username.setText(title);
}</code></pre> 
<p>3.解除注册（onDestroy方法下）</p> 
<pre><code class="language-java">EventBus.getDefault().unregister(this);</code></pre> 
<p>然后就是发布事件让监听事件接受通信信息，在更新UI操作，相关代码如下：</p> 
<p>Activity</p> 
<pre><code class="language-java">/****
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
     * (String)-&gt;这里可以是任意类型--（在返回的时候对应就行）
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
}</code></pre> 
<p>通信的Activity</p> 
<pre><code class="language-java">public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

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
                    //发布事件
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
}</code></pre> 
<p>效果如下图：（包含用户名和退出的通信）</p> 
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img alt="" src="https://static.oschina.net/uploads/space/2017/0214/153236_UdB9_2945455.gif"></p> 
<p>EventBus3.0</p> 
<p>首先你要为你的app添加依赖库：</p> 
<pre><code class="language-java">compile'org.greenrobot:eventbus:3.0.0'</code></pre> 
<p>注册和解除注册相同，监听事件有所改变</p> 
<pre><code class="language-java">@Subscribe(threadMode = ThreadMode.MAIN)
 public void onUserName(User user)
 {
   username.setText(user.getUsername());
 }</code></pre> 
<p>threadMode的之有四个：</p> 
<ol> 
 <li>NAIN UI主线程</li> 
 <li>BACKGROUND 后台线程</li> 
 <li>POSTING 和发布者处在同一个线程</li> 
 <li>ASYNC 异步线程</li> 
</ol> 
<p>也可以订阅事件的优先级，需加参数 ,如下：</p> 
<pre><code class="language-java">    /**
     * priority:事件的优先级类似广播的优先级，优先级越高优先获得消息
     * 在ui线程执行
     * @param user
     */
    @Subscribe(threadMode = ThreadMode.MAIN,priority = 100)
      public void onUserName(User user)
     {
        username.setText(user.getUsername());
     }</code></pre> 
<p>除了上面一种事件外，EventBus还有一种黏性事件，意思就是先发布事件，在注册也能监听得到，用postSticky来发布。</p> 
<pre><code class="language-java">EventBus.getDefault().postSticky(user);</code></pre> 
<p>&nbsp;</p> 
<pre><code class="language-java">
    /**
     * sticky。
     * 方式2
     */
    @Subscribe(sticky = true)
    public void onExit(User user)
    {
        username.setText(user.getUsername());
    }
</code></pre> 
<p>EventBus3.0效果如下图：（包含用户名的两种通信方式）</p> 
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img alt="" src="https://static.oschina.net/uploads/space/2017/0214/155215_cTqr_2945455.gif"></p> 
<p>&nbsp;</p> 
