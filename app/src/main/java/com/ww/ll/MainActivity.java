package com.ww.ll;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.mapapi.SDKInitializer;
import com.ww.ll.adapter.LeftListAdapter;
import com.ww.ll.base.BaseActivity;
import com.ww.ll.db.LeftList;

import org.litepal.LitePal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * @author Ww
 */
public class MainActivity extends BaseActivity {
    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ListView leftList;

    private List<LeftList> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        SDKInitializer.initialize(getApplicationContext());
        LitePal.getDatabase();
        toolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.nav_view);

        initToolbar();
        initDrawerLayout();
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_main,homeFragment);
        transaction.commit();
        leftList = findViewById(R.id.left_list);
        LeftListAdapter adapter = new LeftListAdapter(getContext(), R.layout.left_list_item, dataList);
        leftList.setAdapter(adapter);

        listListener();
        getPermissions();
    }

    private void listListener() {
        leftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mDrawerLayout.closeDrawers();
                LeftList list = dataList.get(i);
                if (list.getName().contains("地图与位置")){
                    toolbar.setTitle("地图与位置");
                    MapFragment mapFragment = new MapFragment();

                    replaceContent(mapFragment,true);
                }
                if (list.getName().contains("主页")){
                    toolbar.setTitle("主页");
                    HomeFragment homeFragment = new HomeFragment();
                    replaceContent(homeFragment,true);
                }
                if (list.getName().contains("帮助")){
                    toolbar.setTitle("帮助");
                    HelpFragment helpFragment = new HelpFragment();
                    replaceContent(helpFragment,true);
                }
                if (list.getName().contains("关于")){
                    toolbar.setTitle("关于");
                    AboutFragment aboutFragment = new AboutFragment();
                    replaceContent(aboutFragment,true);
                }
            }
        });
    }


    private void initToolbar() {

        //设置Toolbar的背景颜色
        toolbar.setBackgroundColor(getResources().getColor(R.color.深褐));

        toolbar.setNavigationIcon(R.mipmap.ic_menu);//设置导航的图标
//        toolbar.setLogo(R.mipmap.ic_launcher);//设置logo

//        toolbar.setTitle("title");//设置标题
//        toolbar.setSubtitle("subtitle");//设置子标题
        toolbar.setTitle("主页");

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));//设置标题的字体颜色
        toolbar.setSubtitleTextColor(getResources().getColor(android.R.color.white));//设置子标题的字体颜色

        setSupportActionBar(toolbar);
    }

    @TargetApi(23)
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /**
             * 短信权限
             */
            if(checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.SEND_SMS);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
//        Intent intent = new Intent(MainActivity.this, LocationActivity.class);
//        intent.putExtra("from", 0);
//        startActivity(intent);
//        Intent intent = new Intent(MainActivity.this,ForegroundActivity.class);
//        startActivity(intent);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        LeftList leftList1 = new LeftList("主页",R.drawable.ic_home);
        dataList.add(leftList1);
        LeftList leftList2 = new LeftList("地图与位置",R.drawable.ic_place);
        dataList.add(leftList2);
        LeftList leftList3 = new LeftList("帮助",R.drawable.ic_settings);
        dataList.add(leftList3);
        LeftList leftList4 = new LeftList("关于",R.drawable.ic_about);
        dataList.add(leftList4);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tool_bar, menu);
        return true;
    }
    // 让菜单同时显示图标和文字
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_setting:
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
                case R.id.action_help:
                    toolbar.setTitle("帮助");
                    HelpFragment helpFragment = new HelpFragment();
                    replaceContent(helpFragment,true);
                    break;
                    case R.id.action_add:
                        showMessage("click add",2000);
                        break;
                        case R.id.action_search:
                            showMessage("click search",2000);

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化DrawerLayout
     */
    private void initDrawerLayout() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar, R.string.open, R.string.close);

        mDrawerToggle.syncState();;//同步

        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }

}
