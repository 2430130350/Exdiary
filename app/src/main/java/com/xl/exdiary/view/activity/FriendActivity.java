package com.xl.exdiary.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xl.exdiary.R;
import com.xl.exdiary.presenter.impl.FriendAPresenterImpl;
import com.xl.exdiary.presenter.inter.IFriendAPresenter;
import com.xl.exdiary.view.inter.IFriendAView;
import com.xl.exdiary.view.specialView.LocalSetting;
import com.xl.exdiary.view.specialView.LocalSettingFileHandler;

public class FriendActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IFriendAView {

    private LocalSetting localSetting = null;
    //以上为自定义、
    int data_list_count = 25;
    BaseAdapter text_adapter = new BaseAdapter() {
        @Override
        public int getCount() {   //getCount-------用来指定到底有多少个条目
            return FriendActivity.this.data_list_count;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) { //GetView------- 用来 显示 具体的条目的内容

            View view;
            if (convertView == null)
                view = View.inflate(FriendActivity.this, R.layout.listview_item, null);
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

    PagerAdapter read_card_adapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return FriendActivity.this.data_list_count;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(FriendActivity.this, R.layout.read_card_view,null);

            container.addView(view);
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container,position,object); 这一句要删除，否则报错
            container.removeView((View)object);
        }
    };

    //以上为自定义属性、

    private IFriendAPresenter mIFriendAPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIFriendAPresenter = new FriendAPresenterImpl(this);
        setContentView(R.layout.activity_friend);
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



        //以下为自定义代码、
        ListView lv = findViewById(R.id.Listview);
        lv.setAdapter(this.text_adapter);

        ViewPager viewPager = findViewById(R.id.read_viewpager);
        viewPager.setAdapter(this.read_card_adapter);

        //设置对item的点击事件、
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "" + id +"  " + position, Toast.LENGTH_SHORT).show();
                //截图、去除状态栏和标题栏、
                Bitmap bitmap = FriendActivity.this.getbmp();
                //高斯模糊计算、
                //展示图片、
                ImageView imageView = FriendActivity.this.findViewById(R.id.blur);
                FriendActivity.this.blurV2(bitmap, imageView);
                imageView.setVisibility(View.VISIBLE);
                FriendActivity.this.findViewById(R.id.read_viewpager).setVisibility(View.VISIBLE);
            }
        });

        //设置blur图片的点击事件防止展示时穿透点击、同时设置点击后返回、
        findViewById(R.id.blur).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendActivity.this.findViewById(R.id.blur).setVisibility(View.INVISIBLE);
                FriendActivity.this.findViewById(R.id.read_viewpager).setVisibility(View.INVISIBLE);

            }
        });

        this.initOther();
    }

    private void initOther(){
        LocalSettingFileHandler localSettingFileHandler = new LocalSettingFileHandler(this, null);
        this.localSetting = localSettingFileHandler.getLocalSetting();
        if(localSetting == null){
            localSettingFileHandler.setLocalSetting();
        }
        else{
            //初始化背景、
            if(localSetting.isNoBackground){
                findViewById(R.id.friendContent).setBackgroundColor(Color.WHITE);
            }
            else if(localSetting.settingBackground != null){
                //设置自定义的图片为背景、
                setDiyBackground();
            }
        }

    }

    private void setDiyBackground(){
        Bitmap bitmap = BitmapFactory.decodeFile(this.localSetting.friendBackground);
        findViewById(R.id.friendContent).setBackground(new BitmapDrawable(getResources(), bitmap));//把bitmap转为drawable,layout为xml文件里的主layout
        findViewById(R.id.friendContent).invalidate();
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
        if(FriendActivity.this.findViewById(R.id.blur).getVisibility() == View.VISIBLE){
            FriendActivity.this.findViewById(R.id.blur).setVisibility(View.INVISIBLE);
            FriendActivity.this.findViewById(R.id.read_viewpager).setVisibility(View.INVISIBLE);
            return;
        }

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
        getMenuInflater().inflate(R.menu.friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {//右上角菜单中设置、
            startActivity(new Intent(FriendActivity.this, SettingActivity.class));
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
            // Handle the camera action


        } else if (id == R.id.nav_friend) {//好友日记、

        } else if (id == R.id.nav_mine) {//我的日记、
            Intent intent = new Intent(FriendActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.nav_noname) {//树洞、

        } else if (id == R.id.nav_setting) {//设置、
            Intent intent = new Intent(FriendActivity.this, SettingActivity.class);
            startActivity(intent);

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
