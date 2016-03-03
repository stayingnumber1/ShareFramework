package com.ray.balloon.view.main;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.ListView;

import com.corelibs.base.BaseActivity;
import com.corelibs.common.AppManager;
import com.corelibs.utils.ToastMgr;
import com.ray.balloon.R;
import com.ray.balloon.adapter.HomePageTestAdapter;
import com.ray.balloon.authority.AuthorityContext;
import com.ray.balloon.presenter.MainPresenter;
import com.ray.balloon.view.bluetooth.BluetoothActivity;
import com.yalantis.taurus.PullToRefreshView;

import butterknife.Bind;
import butterknife.OnClick;
import carbon.widget.FloatingActionButton;
import carbon.widget.ImageView;
import carbon.widget.Toolbar;

/**
 * Created by Administrator on 2016/2/25.
 */
public class MainActivity extends BaseActivity<MainView, MainPresenter> implements MainView {
    @Bind(R.id.list_view)
    ListView list_view;

    @Bind(R.id.refresh_widget)
    PullToRefreshView mRefreshLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab_mine)
    FloatingActionButton fab_mine;
    @Bind(R.id.icon_connect)
    ImageView icon_connect;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        toolbar.setText(R.string.main_title);
        toolbar.setBackgroundColor(getResources().getColor(R.color.main));

        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, 3000);
        list_view.setAdapter(new HomePageTestAdapter(this));

    }

    @Override
    protected MainPresenter createPresenter() {
        return null;
    }

    @Override
    public void getDataSuccess() {

    }

    @OnClick(R.id.fab_mine)
    protected void gotoMine() {
        if (AuthorityContext.getContext().showPersonCenter(this)) {
            ToastMgr.show("您已经登陆啦");
        }
    }
    @OnClick(R.id.icon_connect)
    protected void connect() {
        if (AuthorityContext.getContext().showPersonCenter(this)) {
            Intent it =new Intent(this, BluetoothActivity.class);
            startActivity(it);
        }
    }

    private boolean isBackPressed;

    /**
     * 双击退出
     */
    private void doublePressBackToast() {
        if (!isBackPressed) {
            isBackPressed = true;
            ToastMgr.show("双击退出程序");
        } else {
            AppManager.getAppManager().appExit();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressed = false;
            }
        }, 2000);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
                return false;
            } else {
                doublePressBackToast();
            }
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }
}