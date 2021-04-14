package com.example.lesson2;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.Random;
public class sample_game extends AppCompatActivity {
    /************1.定义变量、对象、洞穴坐标******************/
    private int i=0;//记录打到的地鼠个数
    private ImageView mouse;//定义 mouse 对象
    private TextView info1; //定义 info1 对象（用于查看洞穴坐标）
    private Handler handler;//声明一个 Handler 对象
    public int[][] position=new int[][]{
            {277, 200}, {535, 200}, {832, 200},
            {1067,200}, {1328, 200}, {285, 360},
            {645, 360}, {1014,360}, {1348, 360},{319, 600},{764, 600},{1229,600}
    };//创建一个表示地鼠位置的数组 @Override
    @SuppressLint({"ClickableViewAccessibility", "HandlerLeak"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置不显示顶部栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置横屏模式
        setContentView(R.layout.activity_main);

        /************2.绑定控件*****************/
        mouse = (ImageView) findViewById(R.id.imageView1);
        info1 = (TextView) findViewById(R.id.info);
        /************获取洞穴位置*****************/
        // 通过 logcat 查看 【注】：getRawY()：触摸点距离屏幕上方的长度（此长度包括程序项目名栏的）
        info1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        float x = event.getRawX();
                        float y = event.getRawY();
                        Log.i("x:" + x, "y:" + y);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        /************3.实现地鼠随机出现*****************/
        //创建 Handler 消息处理机制
        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                //需要处理的消息
                int index;
                if (msg.what == 0x101) {
                    index = msg.arg1; // 获取位置索引值
                    mouse.setX(position[index][0]);//设置 X 轴坐标
                    mouse.setY(position[index][1]);//设置 Y 轴坐标（原点为屏幕左上角（不包括程序名称栏））
                    mouse.setVisibility(View.VISIBLE);//设置地鼠显示
                }
                super.handleMessage(msg);
            }
        };
        // 创建线程
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int index = 0;// 定义一个记录地鼠位置的索引值
                while (!Thread.currentThread().isInterrupted()) {
                    index = new Random().nextInt(position.length);// 产生一个随机整数（范围：0<=index<数组长度）
                    Message m = handler.obtainMessage();//创建消息对象
                    m.what = 0x101;//设置消息标志
                    m.arg1 = index;// 保存地鼠标位置的索引值
                    handler.sendMessage(m);// 发送消息通知 Handler 处理
                    try {
                        Thread.sleep(new Random().nextInt(500) + 500); // 休眠一段时间
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
        /************4.实现点击地鼠后的事件：让地鼠不显示&显示消息*****************/
        // 添加触摸 mouse 后的事件
        mouse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setVisibility(View.INVISIBLE);//设置地鼠不显示
                i++;
                Toast.makeText(sample_game.this, "打到[ " + i + " ]只地鼠！",
                        Toast.LENGTH_SHORT).show(); // 显示消息提示框
                return false;
            }
        });
    }}
