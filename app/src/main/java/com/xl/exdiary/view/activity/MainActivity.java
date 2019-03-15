package com.xl.exdiary.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;


import com.xl.exdiary.R;
import com.xl.exdiary.presenter.impl.MainAPresenterImpl;
import com.xl.exdiary.presenter.inter.IMainAPresenter;
import com.xl.exdiary.view.inter.IMainAView;


import com.xl.exdiary.view.specialView.FastBlur;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainAView {
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



        ListView lv = findViewById(R.id.Listview);
        lv.setAdapter(this.text_adapter);

        //设置对item的点击事件、
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "" + id +"  " + position, Toast.LENGTH_SHORT).show();

                //截图、去除状态栏和标题栏、
                Bitmap bitmap = MainActivity.this.getbmp();

                //高斯模糊计算、

                //展示图片、
                ImageView imageView = MainActivity.this.findViewById(R.id.blur);

                MainActivity.this.findViewById(R.id.redit).setVisibility(View.VISIBLE);

                MainActivity.this.blur(bitmap, imageView);
                imageView.setVisibility(View.VISIBLE);


            }
        });

        //设置blur图片的点击事件防止展示时穿透点击、同时设置点击后返回、
        findViewById(R.id.blur).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.findViewById(R.id.redit).setVisibility(View.INVISIBLE);
                MainActivity.this.findViewById(R.id.blur).setVisibility(View.INVISIBLE);
            }
        });
    }

    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 1;//图片缩放比例；
        float radius = 15;//模糊程度

        Bitmap overlay = Bitmap.createBitmap(
                (int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()/ scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);


        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
        /**
         * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。
        */
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
        if(MainActivity.this.findViewById(R.id.redit).getVisibility() == View.VISIBLE){
            MainActivity.this.findViewById(R.id.redit).setVisibility(View.INVISIBLE);
            MainActivity.this.findViewById(R.id.blur).setVisibility(View.INVISIBLE);
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


        } else if (id == R.id.nav_mine) {//我的日记、

        } else if (id == R.id.nav_noname) {//树洞、

        } else if (id == R.id.nav_setting) {//设置、

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
