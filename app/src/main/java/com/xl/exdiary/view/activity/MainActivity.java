package com.xl.exdiary.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;


import com.xl.exdiary.R;
import com.xl.exdiary.presenter.impl.MainAPresenterImpl;
import com.xl.exdiary.presenter.inter.IMainAPresenter;
import com.xl.exdiary.view.inter.IMainAView;

import java.lang.annotation.Target;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainAView {

    AlphaAnimation appearAnimation;


    int data_list_count = 25;
    BaseAdapter text_adapter = new BaseAdapter() {
        @Override
        public int getCount() {   //getCount-------用来指定到底有多少个条目
            return MainActivity.this.data_list_count;
        }


        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) { //GetView------- 用来 显示 具体的条目的内容

            View view;
            if (convertView == null)
                view = View.inflate(MainActivity.this, R.layout.listview_item, null);
            else
                view = convertView;
            TextView tv = view.findViewById(R.id.TextItem_data);
            String str = "卢本伟牛逼、";
            tv.setText("\n        " + str + "\n");
            return view;
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    };

    //以上为自定义属性、
    private IMainAPresenter mIMainAPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIMainAPresenter = new MainAPresenterImpl(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //以下为自定义方法、

        ListView lv = findViewById(R.id.Listview);
        lv.setAdapter(this.text_adapter);

        //设置对item的点击事件、
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "" + id +"  " + position, Toast.LENGTH_SHORT).show();

                MainActivity.this.findViewById(R.id.redit).invalidate();

                //截图、去除状态栏和标题栏、
                Bitmap bitmap = MainActivity.this.getbmp();
                //高斯模糊计算、
                //展示图片、
                ImageView imageView = MainActivity.this.findViewById(R.id.blur);

                View redit = MainActivity.this.findViewById(R.id.redit);
                redit.startAnimation(appearAnimation);
                redit.setVisibility(View.VISIBLE);


                MainActivity.this.blurV2(bitmap, imageView);
                imageView.setVisibility(View.VISIBLE);


            }
        });

        //设置blur图片的点击事件防止展示时穿透点击、同时设置点击后返回、
        findViewById(R.id.blur).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.findViewById(R.id.redit).setVisibility(View.INVISIBLE);
                MainActivity.this.findViewById(R.id.blur).setVisibility(View.INVISIBLE);
                MainActivity.this.findViewById(R.id.edit).setVisibility(View.INVISIBLE);

                //清除掉动画、否贼下一次打开edit会出现透明布局、
                MainActivity.this.findViewById(R.id.redit).clearAnimation();
                MainActivity.this.findViewById(R.id.redit).invalidate();

                View view = MainActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

            }
        });


        //设置edit卡片出现的动画、
        View redit = this.findViewById(R.id.redit);
        appearAnimation = new AlphaAnimation(0, 1);
        appearAnimation.setDuration(500);


        //设置edit卡片中  内容部分控件  的点击事件、使布局放大进入编辑模式、
        findViewById(R.id.TextItem_data).setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点
                    Toast.makeText(MainActivity.this, "编辑模式、", Toast.LENGTH_SHORT).show();
                    /** 设置缩放动画 */
                    final ScaleAnimation animation = new ScaleAnimation(1f, 1.2f, 1f, 1.12f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);// 从相对于自身0.5倍的位置开始缩放，也就是从控件的位置缩放
                    animation.setDuration(500);//设置动画持续时间

                    // 常用方法
                    //animation.setRepeatCount(int repeatCount);//设置重复次数
                    animation.setFillAfter(false);//动画执行完后是否停留在执行完的状态
                    animation.setFillBefore(true);
                    //animation.setStartOffset(long startOffset);//执行前的等待时间

                    View view = MainActivity.this.findViewById(R.id.redit);
                    view.startAnimation(animation);
                    // 开始动画
                    //animation.startNow();

                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            MainActivity.this.findViewById(R.id.redit).clearAnimation();
                            MainActivity.this.findViewById(R.id.redit).invalidate();

                            MainActivity.this.findViewById(R.id.edit).setVisibility(View.VISIBLE);
                            MainActivity.this.findViewById(R.id.redit).setVisibility(View.GONE);

                            /** 设置缩放动画 */
                            final ScaleAnimation anime = new ScaleAnimation(1f, 1f, 1f, 1f,
                                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);// 从相对于自身0.5倍的位置开始缩放，也就是从控件的位置缩放
                            anime.setDuration(1);//设置动画持续时间
                            MainActivity.this.findViewById(R.id.redit).startAnimation(anime);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                } else {
                    // 失去焦点

                }
            }
        });
    }

    public void blurV2(Bitmap bitmap, View view){

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(getApplicationContext());

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur: 0 < radius <= 25
        blurScript.setRadius(15.0f);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        view.setBackground(new BitmapDrawable(getResources(), outBitmap));

    }


    private Bitmap getbmp() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        /**
         * 获取当前窗口快照，相当于截屏
         */
        Bitmap bmp1 = view.getDrawingCache();
        int height = getOtherHeight();
        /**
         * 除去状态栏和标题栏
         */
        Bitmap bmp2 = Bitmap.createBitmap(bmp1,0, height,bmp1.getWidth(), bmp1.getHeight() - height);
        return bmp2;
    }

    /**
     * 获取系统状态栏和软件标题栏，部分软件没有标题栏，看自己软件的配置；
     * @return
     */
    private int getOtherHeight() {
        int statusBarHeight = getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android"));
        TypedValue tv = new TypedValue();
        int actionBarHeight;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        else
            return 0;

        return statusBarHeight + actionBarHeight;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(MainActivity.this.findViewById(R.id.redit).getVisibility() == View.VISIBLE || MainActivity.this.findViewById(R.id.edit).getVisibility() == View.VISIBLE){//退出编辑查看模式、
            //该处所有代码其实与  点击高斯模糊图片    的代码完全相同、但是那个地方是内部类、    暂使用复制方式实现调用、
            MainActivity.this.findViewById(R.id.redit).setVisibility(View.INVISIBLE);
            MainActivity.this.findViewById(R.id.blur).setVisibility(View.INVISIBLE);
            MainActivity.this.findViewById(R.id.edit).setVisibility(View.INVISIBLE);

            //清除掉动画、否贼下一次打开edit会出现透明布局、
            MainActivity.this.findViewById(R.id.redit).clearAnimation();
            MainActivity.this.findViewById(R.id.redit).invalidate();

            return;
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_self) {//用户信息、
            // Handle the camera action

        } else if (id == R.id.nav_friend) {//好友日记、
            Intent intent = new Intent(MainActivity.this, FriendActivity.class);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.nav_mine) {//我的日记、

        } else if (id == R.id.nav_noname) {//树洞、

        } else if (id == R.id.nav_setting) {//设置、
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            this.finish();

        } else if (id == R.id.nav_about) {//关于、

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public <T> T request(int requestFlag) {
        return null;
    }

    @Override
    public <T> void response(T response, int responseFlag) {

    }
}
