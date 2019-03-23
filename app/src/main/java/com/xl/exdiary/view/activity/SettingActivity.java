package com.xl.exdiary.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.xl.exdiary.R;
import com.xl.exdiary.presenter.impl.SettingAPresenterImpl;
import com.xl.exdiary.presenter.inter.ISettingAPresenter;
import com.xl.exdiary.view.inter.ISettingAView;
import com.xl.exdiary.view.specialView.LocalSetting;
import com.xl.exdiary.view.specialView.LocalSettingFileHandler;

import java.io.File;
import java.io.FileDescriptor;
import java.util.Set;

public class SettingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ISettingAView {

    private LocalSetting localSetting = null;

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1://异常处理、
                    SettingActivity.this.handleException();
                    break;
                case 1://正常完成、
                    SettingActivity.this.success();
                    break;
                default:
                    break;
            }
        }

    };

    private void handleException(){
        Toast.makeText(this, "程序出错了、您可以尝试重启App、", Toast.LENGTH_SHORT).show();
    }

    private void success(){
        Toast.makeText(this, "操作完成、", Toast.LENGTH_SHORT).show();
    }

    //以上是自定义、
    private ISettingAPresenter mISettingAPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mISettingAPresenter = new SettingAPresenterImpl(this);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //navigationView.setNavigationItemSelectedListener(this);

        //以下为自定义、
        this.initOther();
    }

    private void initOther(){
        Switch isNoBackground = (Switch) findViewById(R.id.isNoBackground);
        LocalSettingFileHandler localSettingFileHandler = new LocalSettingFileHandler(this, null);
        this.localSetting = localSettingFileHandler.getLocalSetting();
        if(localSetting == null){
            //首次进入设置页面、
            localSetting = new LocalSetting(false);
        }
        else {
            isNoBackground.setChecked(localSetting.isNoBackground);
            setDiyBackground();
        }
        isNoBackground.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingActivity.this.localSetting.isNoBackground = isChecked;
                SettingActivity.this.setLocalSetting();
                setDiyBackground();
                Toast.makeText(SettingActivity.this, "设置页面仅作预览、全局设置重启App生效、", Toast.LENGTH_SHORT).show();
            }
        });

        View diy = findViewById(R.id.diyBackground);
        diy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //测试、

                AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this)
                        .setIcon(R.mipmap.ic_launcher)//设置标题的图片
                        .setTitle("自定义背景")//设置对话框的标题
                        .setMessage("请在接下来的图库中一次选择一张、依次选择设置界面、主界面、好友界面的背景图、")//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SettingActivity.this, "取消操作、", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, 0001);

                            }
                        }).create();
                dialog.show();

                //

            }
        });
    }

    private void setLocalSetting(){
        LocalSettingFileHandler localSettingFileHandler = new LocalSettingFileHandler(//设置保存到本地文件、
                SettingActivity.this,
                this.localSetting);
        localSettingFileHandler.setLocalSetting();
        Toast.makeText(SettingActivity.this, "设置已经保存、重启App生效、", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK || data == null)
            return;
        Uri selectedImage = data.getData();//返回的是uri
        String path = LocalSettingFileHandler.getRealPathFromUriMAX(this, selectedImage);
        Intent intent = null;
        switch (requestCode){
            case 0001:
                //设置页面的背景图、

                //设置写入本地文件、
                this.localSetting.settingBackground = path;
                this.setDiyBackground();
                setLocalSetting();

                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 0002);
                break;
            case 0002:
                //主界面的背景图、
                //设置写入本地文件、
                this.localSetting.mainBackground = path;
                setLocalSetting();

                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 0003);
                break;
            case 0003:
                //好友界面的背景图、
                //设置写入本地文件、
                this.localSetting.friendBackground = path;
                setLocalSetting();
                Toast.makeText(SettingActivity.this, "设置页面仅作预览、全局设置重启App生效、", Toast.LENGTH_SHORT).show();
                break;
        }
        if (requestCode == 0001) {


        }
        else if(requestCode == 0002){


        }

    }


    private void setDiyBackground(){
        View view = findViewById(R.id.settingContent);
        if(localSetting.isNoBackground){
            view.setBackgroundColor(Color.WHITE);
        }
        else if(localSetting.settingBackground != null){
            //设置自定义的图片为背景、
            Bitmap bitmap = BitmapFactory.decodeFile(this.localSetting.settingBackground);
            view.setBackground(new BitmapDrawable(getResources(), bitmap));//把bitmap转为drawable,layout为xml文件里的主layout

        }
        view.invalidate();

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
        //getMenuInflater().inflate(R.menu.setting, menu);
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


        return true;
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
