package com.xl.exdiary.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;


import com.xl.exdiary.R;
import com.xl.exdiary.model.impl.Diary;
import com.xl.exdiary.model.impl.User;
import com.xl.exdiary.presenter.impl.IEditUserPresenterImpl;
import com.xl.exdiary.presenter.impl.MainAPresenterImpl;
import com.xl.exdiary.presenter.impl.REditAPresenterImpl;
import com.xl.exdiary.presenter.inter.IEditUserPresenter;
import com.xl.exdiary.presenter.inter.IMainAPresenter;
import com.xl.exdiary.presenter.inter.IREditAPresenter;
import com.xl.exdiary.view.inter.IMainAView;
import com.xl.exdiary.view.specialView.LinedEditView;
import com.xl.exdiary.view.specialView.LocalSetting;
import com.xl.exdiary.view.specialView.LocalSettingFileHandler;

import java.io.FileDescriptor;
import java.lang.annotation.Target;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainAView {
    private AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
    private AlphaAnimation deleteAnimation = new AlphaAnimation(1, 0);

    //6.0以上sd卡读写权限动态申请、
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 5;


    private LocalSetting localSetting = null;
    private Diary[] diaries = null;
    private int nowPosition = 0;
    private BaseAdapter text_adapter = new BaseAdapter() {


        @Override
        public int getCount() {   //getCount-------用来指定到底有多少个条目
            if(diaries == null){
                return 0;
            }
            return diaries.length;
        }


        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) { //GetView------- 用来 显示 具体的条目的内容

            View view;
            if (convertView == null)
                view = View.inflate(MainActivity.this, R.layout.listview_item, null);
            else
                view = convertView;
            TextView time = view.findViewById(R.id.TextItem_time);
            TextView title = view.findViewById(R.id.TextItem_data);
            String timeStr = diaries[diaries.length - 1 - position].getStartTime();
            String titleStr = diaries[diaries.length - 1 - position].getTitle();

            time.setText(timeStr);
            title.setText(titleStr);
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

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1://异常处理、
                    MainActivity.this.handleException();
                    break;
                case 1://更新数据、
                    final ListView listView = MainActivity.this.findViewById(R.id.Listview);
                    MainActivity.this.getAllDiaryList();

                    MainActivity.this.text_adapter.notifyDataSetChanged();
                    listView.setVisibility(View.GONE);

                    /**
                     * 防止listview界面不刷新、
                     * */
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
                    updateItem(nowPosition);

                    /**
                     *
                     * */
                    break;
                default:
                    break;
            }
        }

    };

    private void updateItem(int position) {
        ListView listView = findViewById(R.id.Listview);
        /**第一个可见的位置**/
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        /**最后一个可见的位置**/
        int lastVisiblePosition = listView.getLastVisiblePosition();

        /**在看见范围内才更新，不可见的滑动后自动会调用getView方法更新**/
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            /**获取指定位置view对象**/
            View view = listView.getChildAt(position - firstVisiblePosition);
            text_adapter.getView(position, view, listView);
        }

    }


    private void handleException(){
        Toast.makeText(this, "程序出错了、您可以尝试重启App、", Toast.LENGTH_SHORT).show();
    }

    private void updateListView(){
        this.mHandler.sendEmptyMessage(1);
    }

    //以上为自定义属性、
    private IREditAPresenter mIREditAPresenter;
    private IEditUserPresenter mIEditUserPresenter;
    private IMainAPresenter mIMainAPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIMainAPresenter = new MainAPresenterImpl(this);
        mIEditUserPresenter = new IEditUserPresenterImpl(this);
        mIREditAPresenter = new REditAPresenterImpl(this);

        //6.0以上sd卡读写权限动态申请、
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }

        //假装有这么一个用户、
        mIEditUserPresenter.saveUserInfor("游客", "无", "无");


        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                FloatingActionButton fab = findViewById(R.id.fab);
                fab.setVisibility(View.GONE);

                /**
                 * 新建日记、
                 * */
                View edit = MainActivity.this.findViewById(R.id.edit);
                LinedEditView editBody = edit.findViewById(R.id.editItem_data);
                EditText editTitle = edit.findViewById(R.id.editItem_time);

                editTitle.setText("");
                editBody.setText("");

                //截图、去除状态栏和标题栏、
                Bitmap bitmap = MainActivity.this.getbmp();
                //高斯模糊计算、
                //展示图片、
                ImageView imageView = MainActivity.this.findViewById(R.id.blur);
                MainActivity.this.blurV2(bitmap, imageView);
                AlphaAnimation animationBlur = new AlphaAnimation(0, 1);
                animationBlur.setDuration(300);
                imageView.startAnimation(animationBlur);
                imageView.setVisibility(View.VISIBLE);

                edit.startAnimation(appearAnimation);
                edit.setVisibility(View.VISIBLE);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FloatingActionButton fab = findViewById(R.id.fab);
                        fab.setVisibility(View.VISIBLE);
                        //新建日记、
                        View edit = MainActivity.this.findViewById(R.id.edit);
                        LinedEditView editBody = edit.findViewById(R.id.editItem_data);
                        EditText editTitle = edit.findViewById(R.id.editItem_time);

                        ImageView imageView = MainActivity.this.findViewById(R.id.blur);
                        imageView.setVisibility(View.GONE);
                        edit.startAnimation(deleteAnimation);
                        edit.setVisibility(View.GONE);

                        MainActivity.this.mIREditAPresenter.saveDiary(editTitle.getText().toString(), editBody.getText().toString());

                        //关闭键盘、
                        View view = MainActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }


                        MainActivity.this.setListener();//重置监听、

                        //刷新列表、
                        MainActivity.this.updateListView();
                    }
                });




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

        /**
         * 从客户端获取数据、
         * */
        this.getAllDiaryList();
        this.setListener();




        //设置卡片逐渐出现和消失的动画、
        appearAnimation.setDuration(500);
        deleteAnimation.setDuration(500);



        this.initOther();

    }

    private void getAllDiaryList(){//获得数据、
        new Thread(){
          @Override
          public void run(){
              Diary[] tmp = mIMainAPresenter.getAllDiaryList();
              if(tmp != null){
                  MainActivity.this.diaries = tmp;
              }

          }
        }.start();
    }

    private void setListener(){
        ListView lv = findViewById(R.id.Listview);
        lv.setAdapter(this.text_adapter);

        //设置长按删除、
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int toDelPosition = position;
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.mipmap.ic_launcher)//设置标题的图片
                        .setTitle("删除日记")//设置对话框的标题
                        .setMessage("您确定要删除该日记么？")//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "取消操作、", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                                //执行删除日记、
                                mIMainAPresenter.delDiary(diaries[diaries.length - 1 - toDelPosition].getStartTime());
                                Toast.makeText(MainActivity.this, "您删除了一篇日记、", Toast.LENGTH_SHORT).show();
                                MainActivity.this.updateListView();

                            }
                        }).create();
                dialog.show();

                return true;

            }
        });

        //设置对item的点击事件、
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "" + id +"  " + position, Toast.LENGTH_SHORT).show();
                MainActivity.this.nowPosition = diaries.length - 1 - position;
                MainActivity.this.findViewById(R.id.redit).invalidate();

                //隐藏新建按钮、
                FloatingActionButton fab = findViewById(R.id.fab);
                fab.setVisibility(View.GONE);

                //截图、去除状态栏和标题栏、
                Bitmap bitmap = MainActivity.this.getbmp();
                //高斯模糊计算、
                //展示图片、
                ImageView imageView = MainActivity.this.findViewById(R.id.blur);

                View redit = MainActivity.this.findViewById(R.id.redit);
                LinedEditView title = redit.findViewById(R.id.TextItem_time),
                        body = redit.findViewById(R.id.TextItem_data);
                String titleStr = MainActivity.this.diaries[diaries.length - 1 - position].getTitle(),
                        bodyStr = MainActivity.this.diaries[diaries.length - 1 - position].getBody();
                //阅读卡片文字装填、
                title.setText(titleStr);
                body.setText(bodyStr);

                redit.startAnimation(appearAnimation);//阅读卡片渐现、
                redit.setVisibility(View.VISIBLE);


                MainActivity.this.blurV2(bitmap, imageView);
                imageView.setVisibility(View.VISIBLE);


            }
        });

        //设置blur图片的点击事件防止展示时穿透点击、同时设置点击后返回、
        findViewById(R.id.blur).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                //展示新建日记按钮、
                FloatingActionButton fab = findViewById(R.id.fab);
                fab.setVisibility(View.VISIBLE);

                if(findViewById(R.id.userinfoCardview).getVisibility() == View.VISIBLE) {
                    View view = findViewById(R.id.userinfoCardview);
                    view.startAnimation(deleteAnimation);
                    view.setVisibility(View.GONE);
                    MainActivity.this.findViewById(R.id.blur).setVisibility(View.GONE);
                    return;
                }

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

                //保存当前修改的日记、暂无修改函数、
                View edit = MainActivity.this.findViewById(R.id.edit);
                LinedEditView editBody = edit.findViewById(R.id.editItem_data);
                EditText editTitle = edit.findViewById(R.id.editItem_time);
                MainActivity.this.mIREditAPresenter.modifyDiary(
                        diaries[nowPosition].getStartTime(),
                        editTitle.getText().toString(),
                        editBody.getText().toString()
                );

                //更新数据、
                MainActivity.this.updateListView();

            }
        });

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
                    animation.setDuration(400);//设置动画持续时间


                    animation.setFillAfter(false);//动画执行完后是否停留在执行完的状态
                    animation.setFillBefore(true);


                    View view = MainActivity.this.findViewById(R.id.redit),
                            edit = MainActivity.this.findViewById(R.id.edit);
                    //阅读卡片数据写入编辑卡片、
                    LinedEditView readTitle = view.findViewById(R.id.TextItem_time),
                            readBody = view.findViewById(R.id.TextItem_data);
                    LinedEditView editBody = edit.findViewById(R.id.editItem_data);
                    EditText editTitle = edit.findViewById(R.id.editItem_time);

                    editTitle.setText(readTitle.getText());
                    editBody.setText(readBody.getText());

                    //阅读卡片放大、
                    view.startAnimation(animation);

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

    private void initOther(){
        LocalSettingFileHandler localSettingFileHandler = new LocalSettingFileHandler(this, null);
        this.localSetting = localSettingFileHandler.getLocalSetting();
        if(localSetting == null){
            Toast.makeText(this, "首次进入App、已应用默认设置、", Toast.LENGTH_SHORT).show();
            localSetting = new LocalSetting(false);
            this.setLocalSetting();
        }
        else{
            //初始化背景、
            if(localSetting.isNoBackground){
                findViewById(R.id.mainContent).setBackgroundColor(Color.WHITE);

            }
            else if(localSetting.settingBackground != null){
                //设置自定义的图片为背景、
                setDiyBackground();
            }
        }

    }



    private void setDiyBackground(){
        Bitmap bitmap = BitmapFactory.decodeFile(this.localSetting.mainBackground);
        findViewById(R.id.mainContent).setBackground(new BitmapDrawable(getResources(), bitmap));//把bitmap转为drawable,layout为xml文件里的主layout
        findViewById(R.id.mainContent).invalidate();
    }

    private void setLocalSetting(){
        LocalSettingFileHandler localSettingFileHandler = new LocalSettingFileHandler(//设置保存到本地文件、
                this,
                this.localSetting);
        localSettingFileHandler.setLocalSetting();

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
        }else if(findViewById(R.id.userinfoCardview).getVisibility() == View.VISIBLE){
            View view = findViewById(R.id.userinfoCardview);
            view.startAnimation(deleteAnimation);
            view.setVisibility(View.GONE);
            MainActivity.this.findViewById(R.id.blur).setVisibility(View.GONE);
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
        if (id == R.id.action_settings) {//右上角菜单中设置、
            startActivity(new Intent(MainActivity.this, SettingActivity.class));


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
                    Bitmap bitmap = MainActivity.this.getbmp();
                    //高斯模糊计算、
                    //展示图片、
                    ImageView imageView = MainActivity.this.findViewById(R.id.blur);
                    MainActivity.this.blurV2(bitmap, imageView);
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

                    Toast.makeText(MainActivity.this, "设备号已复制到剪切板、", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            view.startAnimation(animation);
            view.setVisibility(View.VISIBLE);

        } else if (id == R.id.nav_friend) {//好友日记、
            Intent intent = new Intent(MainActivity.this, FriendActivity.class);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.nav_mine) {//我的日记、

        } else if (id == R.id.nav_noname) {//树洞、

        } else if (id == R.id.nav_setting) {//设置、
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {//关于、

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void exception() {
        this.mHandler.sendEmptyMessage(-1);//子线程通知主线程  出现了异常、
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                //Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
                Toast.makeText(this, "申请权限：" + permissions[i] + ", 结果：" + grantResults[i], Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public <T> T request(int requestFlag) {
        return null;
    }

    @Override
    public <T> void response(T response, int responseFlag) {

    }
}
