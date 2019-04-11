package com.xl.exdiary.view.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
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
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;


import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xl.exdiary.R;
import com.xl.exdiary.model.impl.Diary;
import com.xl.exdiary.model.impl.User;
import com.xl.exdiary.presenter.impl.AnonymousAPresenterImpl;
import com.xl.exdiary.presenter.impl.FriendAPresenterImpl;
import com.xl.exdiary.presenter.impl.IEditUserPresenterImpl;
import com.xl.exdiary.presenter.inter.IAnonymousAPresenter;
import com.xl.exdiary.presenter.inter.IEditUserPresenter;
import com.xl.exdiary.presenter.inter.IFriendAPresenter;
import com.xl.exdiary.view.inter.IAnonymousAView;
import com.xl.exdiary.view.specialView.DiaryAdapter;
import com.xl.exdiary.view.specialView.LocalSetting;
import com.xl.exdiary.view.specialView.LocalSettingFileHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnonymousActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IAnonymousAView {

    private AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
    private AlphaAnimation deleteAnimation = new AlphaAnimation(1, 0);

    private LocalSetting localSetting = null;
    private User oneMakeFriend = null;

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
                    break;
                case 3://处理好友申请、
                    if(oneMakeFriend == null){
                        Toast.makeText(AnonymousActivity.this, "您当前没有需要处理的好友申请、", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    TextView makeFriendText = new TextView(AnonymousActivity.this);
                    makeFriendText.setText(oneMakeFriend.getName());

                    //对话框添加好友、
                    android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(AnonymousActivity.this)
                            .setIcon(R.mipmap.ic_launcher)//设置标题的图片
                            .setTitle("好友申请")//设置对话框的标题
                            .setMessage("您要同意该用户的好友申请么、")//设置对话框的内容
                            //设置编辑框、
                            .setView(makeFriendText)
                            //设置对话框的按钮
                            .setNegativeButton("同意申请", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(AnonymousActivity.this, "已同意好友请求、", Toast.LENGTH_SHORT).show();
                                    mIFriendAPresenter.acceptFriend(
                                            oneMakeFriend.getName(),
                                            oneMakeFriend.getDeviceNumber(),
                                            oneMakeFriend.getMail(),
                                            oneMakeFriend.getSignature()
                                    );
                                    dialog.dismiss();
                                    oneMakeFriend = null;
                                }
                            })
                            .setPositiveButton("拒不同意", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Toast.makeText(AnonymousActivity.this, "拒绝申请、", Toast.LENGTH_SHORT).show();
                                    mIFriendAPresenter.rejectFriend(oneMakeFriend.getDeviceNumber());
                                    oneMakeFriend = null;
                                }
                            })
                            .setCancelable(false)
                            .create();
                    dialog.show();
                    break;
                case 4:
                    //更新好友列表界面、
                    NavigationView rightNav = findViewById(R.id.right_nav_view);
                    View headView = rightNav.getHeaderView(0);
                    final ListView friendList = headView.findViewById(R.id.friendList);

                    AnonymousActivity.this.friend_adapter.notifyDataSetChanged();
                    friendList.invalidate();
                    break;
                default:
                    break;
            }
        }

    };

    private User[] friends = null;
    BaseAdapter friend_adapter = new BaseAdapter() {
        @Override
        public int getCount() {   //getCount-------用来指定到底有多少个条目
            return AnonymousActivity.this.friends.length;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) { //GetView------- 用来 显示 具体的条目的内容

            View view;
            if (convertView == null)
                view = View.inflate(AnonymousActivity.this, R.layout.listview_item, null);
            else
                view = convertView;
            TextView tv = view.findViewById(R.id.TextItem_data);
            String str = friends[position].getDeviceNumber();
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
    //以上为自定义、

    private IAnonymousAPresenter mIAnonymousAPresenter;
    private IEditUserPresenter mIEditUserPresenter;
    private IFriendAPresenter mIFriendAPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIAnonymousAPresenter = new AnonymousAPresenterImpl(this);
        mIEditUserPresenter = new IEditUserPresenterImpl(this);
        mIFriendAPresenter = new FriendAPresenterImpl(this);

        //设置卡片逐渐出现和消失的动画、
        appearAnimation.setDuration(500);
        deleteAnimation.setDuration(500);

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

        this.initOther();
        this.setListener();


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


    private void setListener(){

        //设置blur图片的点击事件防止展示时穿透点击、同时设置点击后返回、
        findViewById(R.id.blur).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                FloatingActionButton fab = findViewById(R.id.fab);
                fab.setVisibility(View.VISIBLE);

                if(findViewById(R.id.userinfoCardview).getVisibility() == View.VISIBLE) {
                    View view = findViewById(R.id.userinfoCardview);
                    view.startAnimation(deleteAnimation);
                    view.setVisibility(View.GONE);
                    AnonymousActivity.this.findViewById(R.id.blur).setVisibility(View.GONE);
                    return;
                }


                AnonymousActivity.this.findViewById(R.id.blur).setVisibility(View.INVISIBLE);
                //AnonymousActivity.this.findViewById(R.id.read_viewpager).setVisibility(View.INVISIBLE);

            }
        });


        //右侧滑设置、
        NavigationView rightNav = findViewById(R.id.right_nav_view);
        View headView = rightNav.getHeaderView(0);
        final ListView friendList = headView.findViewById(R.id.friendList);
        //长按删除、
        friendList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });
        //点击显示好友完整信息、
        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        //设置添加好友按钮、
        Button addFriend = headView.findViewById(R.id.addFriend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭侧滑栏、
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.END);

                final EditText friendUUIDEdit = new EditText(AnonymousActivity.this);
                friendUUIDEdit.setHint("此处输入好友UUID、");
                //对话框添加好友、
                android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(AnonymousActivity.this)
                        .setIcon(R.mipmap.ic_launcher)//设置标题的图片
                        .setTitle("添加好友")//设置对话框的标题
                        .setMessage("请输入您需要添加的好友设备号UUID、")//设置对话框的内容
                        //设置编辑框、
                        .setView(friendUUIDEdit)
                        //设置对话框的按钮
                        .setNegativeButton("发起申请", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AnonymousActivity.this, "已发起好友申请、", Toast.LENGTH_SHORT).show();
                                final String friendUUID = friendUUIDEdit.getText().toString();


                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mIFriendAPresenter.addFriends("", friendUUID);
                                    }
                                }).start();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("取消操作", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Toast.makeText(AnonymousActivity.this, "取消操作、", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setCancelable(false)
                        .create();
                dialog.show();
            }
        });

        //设置好友申请按钮、
        final Button makeFriend = headView.findViewById(R.id.makeFriend);
        makeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭侧滑栏、
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.END);

                //获得待处理好友列表、
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        User[] makeFriends = mIFriendAPresenter.getFriendToSure();
                        if(makeFriends == null || makeFriends.length == 0){
                            mHandler.sendEmptyMessage(3);
                            return;
                        }
                        oneMakeFriend = new User("","","","");
                        oneMakeFriend.setDeviceNumber(makeFriends[0].getDeviceNumber());
                        oneMakeFriend.setMail(makeFriends[0].getMail());
                        oneMakeFriend.setName(makeFriends[0].getName());
                        oneMakeFriend.setSignature(makeFriends[0].getSignature());

                        mHandler.sendEmptyMessage(3);
                    }
                }).start();

            }
        });
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
                findViewById(R.id.anonymousContent).setBackgroundColor(Color.WHITE);
            }
            else if(localSetting.settingBackground != null){
                //设置自定义的图片为背景、
                setDiyBackground();
            }
        }

        //初始化好友列表、
        this.getFriendList();

        NavigationView rightNav = findViewById(R.id.right_nav_view);
        View headView = rightNav.getHeaderView(0);
        ListView friendList = headView.findViewById(R.id.friendList);
        friendList.setAdapter(this.friend_adapter);
    }

    private void getFriendList(){
        this.friends = this.mIFriendAPresenter.getAllFriend();
        this.friends = (this.friends == null) ? new User[0] : this.friends;
        this.mHandler.sendEmptyMessage(4);//更新好友列表界面、
    }

    private void setDiyBackground(){
        Bitmap bitmap = BitmapFactory.decodeFile(this.localSetting.anonymousBackground);
        findViewById(R.id.anonymousContent).setBackground(new BitmapDrawable(getResources(), bitmap));//把bitmap转为drawable,layout为xml文件里的主layout
        findViewById(R.id.anonymousContent).invalidate();
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
        int navigationBarHeight = getNavigationBarHeight();
        /**
         * 除去状态栏和标题栏
         */
        Bitmap bmp2 = Bitmap.createBitmap(bmp1,0, height, bmp1.getWidth(), bmp1.getHeight() - height - navigationBarHeight);
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



    //获取虚拟按键的高度
    public int getNavigationBarHeight() {
        int result = 0;
        if (hasNavBar()) {
            Resources res = this.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public boolean hasNavBar() {
        Resources res = this.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(this).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }



    @Override
    public void onBackPressed() {
         if(findViewById(R.id.userinfoCardview).getVisibility() == View.VISIBLE){
            View view = findViewById(R.id.userinfoCardview);
            view.startAnimation(deleteAnimation);
            view.setVisibility(View.GONE);
            this.findViewById(R.id.blur).setVisibility(View.GONE);
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
            startActivity(new Intent(AnonymousActivity.this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_self) {//用户信息、
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setVisibility(View.GONE);
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
        this.mHandler.sendEmptyMessage(-1);//子线程通知主线程  出现了异常、
    }


    @Override
    public <T> T request(int requestFlag) {
        return null;
    }

    @Override
    public <T> void response(T response, int responseFlag) {

    }
}
