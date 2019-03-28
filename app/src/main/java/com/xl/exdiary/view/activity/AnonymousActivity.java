package com.xl.exdiary.view.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xl.exdiary.R;
import com.xl.exdiary.model.impl.Diary;
import com.xl.exdiary.model.impl.User;
import com.xl.exdiary.presenter.impl.AnonymousAPresenterImpl;
import com.xl.exdiary.presenter.impl.IEditUserPresenterImpl;
import com.xl.exdiary.presenter.inter.IAnonymousAPresenter;
import com.xl.exdiary.presenter.inter.IEditUserPresenter;
import com.xl.exdiary.view.inter.IAnonymousAView;
import com.xl.exdiary.view.specialView.DiaryAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnonymousActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IAnonymousAView {

    private RecyclerView recyclerView;
    private Diary[] data = null;

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1://异常处理、
                    AnonymousActivity.this.handleException();
                    break;
                case 1://更新数据、
                   /* final ListView listView = AnonymousActivity.this.findViewById(R.id.Listview);
                    AnonymousActivity.this.getFriendDiaryList();

                    AnonymousActivity.this.text_adapter.notifyDataSetChanged();
                    listView.setVisibility(View.GONE);

                    *//**//**
                     * 防止listview界面不刷新、
                     * *//**//*
                    AlphaAnimation animation = new AlphaAnimation(0, 1);
                    animation.setDuration(1);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            listView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    listView.startAnimation(animation);
                    listView.invalidate();

                    *//**//**
                     *
                     * *//*
                    break;*/
                default:
                    break;
            }
        }

    };

    //以上为自定义、

    private IAnonymousAPresenter mIAnonymousAPresenter;
    private IEditUserPresenter mIEditUserPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIAnonymousAPresenter = new AnonymousAPresenterImpl(this);
        mIEditUserPresenter = new IEditUserPresenterImpl(this);

        setContentView(R.layout.activity_anonymous);
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

        //以下为自定义、
        recyclerView = (RecyclerView) findViewById(R.id.nonameList);
        //使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
        StaggeredGridLayoutManager recyclerViewLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        /**
         * 测试用、需要替换、
         * */
        initData();
        /**
         * */
        DiaryAdapter adapter = new DiaryAdapter(data, this);
        //设置adapter
        recyclerView.setAdapter(adapter);
    }


    private void initData(){
        data = new Diary[1];
        Diary tmp = new Diary("", "", "");
        data[0] = tmp;
        for(int i = 0; i<20; i++){
            List<Diary> list = new ArrayList(Arrays.asList(data));
            String str = "";
            for(int j = 0; j<i%6; j++){
                str += "卢本伟牛逼、卢本伟牛逼、卢本伟牛逼、卢本伟牛逼、卢本伟牛逼、";
            }
            list.add(new Diary("卢本伟牛逼", str, "今天" + i + "点"));
            Diary[] newdata = new Diary[list.size()];
            list.toArray(newdata);
            this.data = newdata;
        }
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.anonymous, menu);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_self) {//用户信息、
            if(findViewById(R.id.blur).getVisibility() == View.VISIBLE){
                this.onBackPressed();
            }
            AlphaAnimation animation = new AlphaAnimation(0, 1);
            animation.setDuration(300);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //截图、去除状态栏和标题栏、
                    Bitmap bitmap = AnonymousActivity.this.getbmp();
                    //高斯模糊计算、
                    //展示图片、
                    ImageView imageView = AnonymousActivity.this.findViewById(R.id.blur);
                    AnonymousActivity.this.blurV2(bitmap, imageView);
                    AlphaAnimation animationBlur = new AlphaAnimation(0, 1);
                    animationBlur.setDuration(300);
                    imageView.startAnimation(animationBlur);
                    imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            View view = findViewById(R.id.userinfoCardview);
            TextView userinfoName = view.findViewById(R.id.userinfoName),
                    userinfoSign = view.findViewById(R.id.userSign),
                    userinfoUUID = view.findViewById(R.id.userinfoUUID);
            User user =  mIEditUserPresenter.getUserInfor();
            userinfoName.setText(user.getName());
            userinfoSign.setText(user.getSignature());
            userinfoUUID.setText(user.getDeviceNumber());

            userinfoUUID.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData

                    ClipData mClipData = ClipData.newPlainText("UUID", ((TextView) v).getText().toString());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);

                    Toast.makeText(AnonymousActivity.this, "设备号已复制到剪切板、", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            view.startAnimation(animation);
            view.setVisibility(View.VISIBLE);

        } else if (id == R.id.nav_friend) {//好友日记、
            Intent intent = new Intent(AnonymousActivity.this, FriendActivity.class);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.nav_mine) {//我的日记、
            Intent intent = new Intent(AnonymousActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.nav_noname) {//树洞、

        } else if (id == R.id.nav_setting) {//设置、
            Intent intent = new Intent(AnonymousActivity.this, SettingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {//关于、

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void handleException(){
        Toast.makeText(this, "程序出错了、您可以尝试重启App、", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void exception() {

    }


    @Override
    public <T> T request(int requestFlag) {
        return null;
    }

    @Override
    public <T> void response(T response, int responseFlag) {

    }
}
