package com.example.administrator.christie.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.fragment.FangkeFragment;
import com.example.administrator.christie.fragment.MeFragment;
import com.example.administrator.christie.fragment.MenjinFragment;
import com.example.administrator.christie.fragment.ParkFragment;

public class MainActivity extends FragmentActivity {
    MenjinFragment menjin;
    FangkeFragment fangke;
    ParkFragment park;
    MeFragment me;
    Button[] btns = new Button[4];
    Fragment[] fragments = null;
    private long exitTime = 0;
    /**
     * 当前显示的fragment
     */
    int currentIndex = 0;
    /**
     * 选中的button,显示下一个fragment
     */
    int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TApplication.listActivity.add(this);
        setViews();
        setListeners();
    }

    protected void setViews(){
        btns[0] = (Button) findViewById(R.id.btn_menjin);//门禁
        btns[1] = (Button) findViewById(R.id.btn_fangke);//访客
        btns[2] = (Button) findViewById(R.id.btn_park);//停车
        btns[3] = (Button) findViewById(R.id.btn_me);//个人中心
        btns[0].setSelected(true);

        menjin = new MenjinFragment();
        fangke = new FangkeFragment();
        park = new ParkFragment();
        me = new MeFragment();
        fragments = new Fragment[]{menjin,fangke,park,me};

        // 一开始，显示第一个fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, menjin);
        transaction.show(menjin);
        transaction.commit();
    }

    protected void setListeners(){
        for(int i=0;i<btns.length;i++){
            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        switch (view.getId()) {
                            case R.id.btn_menjin:
                                selectedIndex = 0;
                                break;
                            case R.id.btn_fangke:
                                selectedIndex = 1;
                                break;
                            case R.id.btn_park:
                                selectedIndex = 2;
                                break;
                            case R.id.btn_me:
                                selectedIndex = 3;
                                break;
                        }

                        // 判断单击是不是当前的
                        if (selectedIndex != currentIndex) {
                            // 不是当前的
                            FragmentTransaction transaction = getSupportFragmentManager()
                                    .beginTransaction();
                            // 当前hide
                            transaction.hide(fragments[currentIndex]);
                            // show你选中

                            if (!fragments[selectedIndex].isAdded()) {
                                // 以前没添加过
                                transaction.add(R.id.fragment_container,
                                        fragments[selectedIndex]);
                            }
                            // 事务
                            transaction.show(fragments[selectedIndex]);
                            transaction.commit();

                            btns[currentIndex].setSelected(false);
                            btns[selectedIndex].setSelected(true);
                            currentIndex = selectedIndex;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出应用",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            TApplication.exit();
        }
    }
}
