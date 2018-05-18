package com.example.s1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s1.DiaryActivity.WriteDiaryActivity;
import com.example.s1.Utils.MyOkHttp;
import com.example.s1.entity.DiaryText;
import com.example.s1.entity.PopWindow;
import com.example.s1.adapter.MyPagerStateAdapter;
import com.example.s1.fragments.ControlFragment;
import com.example.s1.fragments.DiaryFragment;
import com.example.s1.fragments.NewsFragment;
import com.example.s1.fragments.ScheduleFragment;
import com.example.s1.newsActivity.NewsFirstRunActivity;
import com.example.s1.rxjava.RxBus2;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private TabLayout.Tab diary;
    private TabLayout.Tab schedule;
    private TabLayout.Tab news;
    DrawerLayout mDrawerLayout;
    DiaryFragment diaryFragment;
    ScheduleFragment scheduleFragment;
    NewsFragment newsFragment;
    MyPagerStateAdapter myPagerStateAdapter;
    List<Fragment> fragmentList;
    NavigationView navView;
    private PopWindow popWindow;
    ArrayList<Integer>delMenu;
    TextView studentNo;
    TextView MainTitle;

    MenuItem menuLogin;
    MenuItem addMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        fragmentList=new ArrayList<>();
        fragmentList.add(scheduleFragment=new ScheduleFragment());
        fragmentList.add(newsFragment=new NewsFragment());
        fragmentList.add(diaryFragment=new DiaryFragment());
        initViews();
        initEvents();
        register();
    }

    private void initEvents(){
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab==mTablayout.getTabAt(0)){
                    schedule.setIcon(R.mipmap.campus_blue);

                    MainTitle.setText("校园");
                    mViewPager.setCurrentItem(0);
                    //不显示加号
                    addMenu.setVisible(false);
                }else if(tab==mTablayout.getTabAt(1)){
                    news.setIcon(R.mipmap.news_blue);
                    MainTitle.setText("新闻");
                    mViewPager.setCurrentItem(1);
                    //显示加号
                    addMenu.setVisible(true);
                    addMenu.setIcon(R.mipmap.setting_blue);
                }else if(tab==mTablayout.getTabAt(2)){
                    diary.setIcon(R.mipmap.diary_blue);
                    MainTitle.setText("日记");
                    mViewPager.setCurrentItem(2);
                    //显示加号
                    addMenu.setVisible(true);
                    addMenu.setIcon(R.mipmap.plus);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                if(tab==mTablayout.getTabAt(0)){
                    schedule.setIcon(R.mipmap.campus_black);
                    System.out.println("tab0:");

                }else if(tab==mTablayout.getTabAt(1)){
                    news.setIcon(R.mipmap.news_black);
                    System.out.println("tab1:");
                }
                else if(tab==mTablayout.getTabAt(2)){
                    diary.setIcon(R.mipmap.diary_black);
                    System.out.println("tab2:");
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //左则滑动菜单点击事件
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.menu_setting)
                {
                    Intent intent=new Intent(MainActivity.this, NewsFirstRunActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId()==R.id.menu_login)
                {
                    SharedPreferences pre=getSharedPreferences("TJuser",MODE_PRIVATE);
                    boolean isIn=pre.getBoolean("isIn",false);
                    if(isIn)
                    {
                        //注销警告
                        new  AlertDialog.Builder(MainActivity.this)
                                .setTitle("提示" )
                                .setMessage("确定注销吗？" )
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        menuLogin.setTitle("登录");
                                        studentNo.setText("同济用户");

                                        SharedPreferences.Editor editor1;
                                        SharedPreferences pre1=getSharedPreferences("shared",MODE_PRIVATE);
                                        editor1=pre1.edit();
                                        editor1.putBoolean("FirstRun",true);
                                        editor1.apply();

                                        SharedPreferences.Editor editor;
                                        SharedPreferences pre=getSharedPreferences("TJuser",MODE_PRIVATE);
                                        editor=pre.edit();
                                        editor.putBoolean("isIn",false);
                                        editor.apply();


                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                    else
                    {
                        Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                        System.out.println("itentlogin");
                        startActivity(intent);
                    }

                }
                if(item.getItemId()==R.id.menu_useInfo)
                {
                    Intent intent=new Intent(MainActivity.this, MenuActivity.class);
                    intent.putExtra("menu_info","use_help");
                    startActivity(intent);
                }
                if(item.getItemId()==R.id.menu_about_us)
                {
                    Intent intent=new Intent(MainActivity.this, MenuActivity.class);
                    intent.putExtra("menu_info","about_us");
                    startActivity(intent);
                }
                if(item.getItemId()==R.id.menu_clear)
                {
                    Toast.makeText(MainActivity.this,"清除缓存成功！",Toast.LENGTH_SHORT).show();
                }
                if(item.getItemId()==R.id.menu_logout)
                {
                    //直接关闭
                    finish();
                }
                return true;
            }
        });

        //显示学号
        SharedPreferences pref=getSharedPreferences("TJuser",MODE_PRIVATE);
        boolean isIn=pref.getBoolean("isIn",false);
        String studentNoStr=pref.getString("username","");
        if(isIn && !studentNoStr.equals(""))
        {
            studentNo.setText(studentNoStr);
            if(menuLogin!=null)
                menuLogin.setTitle("注销");
            else
            {
                System.out.println("menu_login为空");
            }
        }

        //显示已登录




    }

    private void initViews(){
        mTablayout=(TabLayout)findViewById(R.id.tabLayout);
        mViewPager=(ViewPager)findViewById(R.id.viewPager);
        myPagerStateAdapter=new MyPagerStateAdapter(getSupportFragmentManager(),fragmentList);
        mViewPager.setAdapter(myPagerStateAdapter);
        navView=(NavigationView)findViewById(R.id.nav_view);
        delMenu=new ArrayList<>();
        MainTitle=(TextView)findViewById(R.id.main_title);
        MainTitle.setText("校园");
       // menuLogin=(MenuItem) findViewById(R.id.menu_login);

        menuLogin=navView.getMenu().findItem(R.id.menu_login);

        if(menuLogin==null)
            System.out.println("null............") ;


        //处理toolbar
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.menu_blue);
        }
        navView.setCheckedItem(R.id.menu_login);

        mTablayout.setupWithViewPager(mViewPager);
        View headerView = navView.getHeaderView(0);
        studentNo=(TextView)headerView.findViewById(R.id.menu_student_no);


        schedule=mTablayout.getTabAt(0);
        news=mTablayout.getTabAt(1);
        diary=mTablayout.getTabAt(2);

        schedule.setIcon(R.mipmap.campus_blue);
        news.setIcon(R.mipmap.news_black);
        diary.setIcon(R.mipmap.diary_black);

    }

    //顶部菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        addMenu=menu.findItem(R.id.add);
        if(addMenu!=null)
        {
            addMenu.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){//有待处理
            case R.id.add:



                if(mViewPager.getCurrentItem()==1)
                {
                    //修改为启动first_run
                    Intent intent=new Intent(MainActivity.this,NewsFirstRunActivity.class);
                    startActivity(intent);
//                    Log.d("before","before");
//                    popWindow=new PopWindow(this);
//                    for(int i=0;i<delMenu.size();i++)
//                    {
//                        popWindow.removeView(delMenu.get(i),i);
//                    }
//                    Log.d("later","later");

                  //  popWindow.register();
                   // popWindow.showPopupWindow(findViewById(R.id.add));
                }
                else
                {
                    Intent intent=new Intent(MainActivity.this,WriteDiaryActivity.class);
                    intent.putExtra("current_title","新建");
                    startActivity(intent);
                }

                break;

            case android.R.id.home:
                Log.d("open","打开");
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;


            default:
                break;
        }
        return true;
    }



    //注册RxBus
    public void register(){
        //rxbus
        RxBus2.getDefault().toObservable(ArrayList.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                //在主线程进行观察，可做UI更新操作
                .observeOn(AndroidSchedulers.mainThread())
                //观察的对象
                .subscribe(new Action1<ArrayList>() {
                    @Override
                    public void call(ArrayList newsKind) {
                        delMenu=(ArrayList<Integer>) newsKind;

                        Log.d("arraylist news:",Integer.toString(delMenu.size()));

                       // removeView(0,0);
                    }
                });
    }

}
