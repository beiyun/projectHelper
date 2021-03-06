package com.beiyun.projecthelper;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.beiyun.library.entity.TimeType;
import com.beiyun.library.util.Apps;
import com.beiyun.library.util.Nets;
import com.beiyun.library.util.Regexs;
import com.beiyun.library.util.Sps;
import com.beiyun.library.util.Times;
import com.beiyun.library.view.Toast;
import com.beiyun.projecthelper.adapter.MainAdapter;
import com.beiyun.projecthelper.base.BaseActivity;
import com.beiyun.projecthelper.entity.Account;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends BaseActivity implements MainAdapter.OnItemClickListener {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("test");
            actionBar.setDisplayHomeAsUpEnabled(true);


        }


        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);


        MainAdapter  adapter = new MainAdapter(getItemData());
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);


    }

    private ArrayList<String> getItemData() {
        ArrayList<String> data = new ArrayList<>();
        data.add("test App");
        data.add("test Toast");
        data.add("当前网络状态");
        data.add("exit App");
        data.add("sps.save");
        data.add("sp.get");
        data.add("Keyboards");
        data.add("Windows");
        data.add("Times");
        data.add("Views");
        data.add("EventBus");
        data.add("regexs");
        data.add("dps");
        data.add("Dialogs");
        data.add("sps remove");
        data.add("sps clear");
        return data;
    }


    @Override
    public void onItemClick(int position, String data) {
        switch (position){
            case 0:
                PackageManager manager = Apps.getPackageManager();
                Toast.show("getPackageManager = "+manager, Toast.LENGTH_SHORT);
                break;
            case 1:
                Toast.showShort("测试Toast");
                break;
            case 2:
                Toast.showShort("当前网络状态"+ Nets.getNetState());
                break;
            case 3:
                Apps.exit();
                break;
            case 4:
                Account a = getAccount();
                boolean save = false;
                if (a != null) {
                    save = Sps.save(a);
                }
                if(save){
                    Toast.showShort("保存成功");
                }

                break;
            case 5:
                Account o = (Account) Sps.get(Account.class);
                if (o != null) {
                    Toast.show(o.toString(),Toast.LENGTH_LONG);
                }
                break;
            case 6:
                startActivity(new Intent(this,KeyboardActivity.class));
                overridePendingTransition(R.anim.toast_in,R.anim.toast_out);
                break;
            case 7:
               startActivity(new Intent(this,WindowActivity.class));
                break;
            case 8:
                Toast.showShort(Times.getCurrentTime(TimeType.CN_yyyy_MM_dd_a_K_mm_ss_E));
                break;
            case 9:
                startActivity(new Intent(this, ViewsActivity.class));
                break;
            case 10:
                startActivity(new Intent(this,EventBusActivity.class));
                break;
            case 11:
                boolean chinaMobile = Regexs.isChinaMobile("18310428956");
                boolean chinaTelecommounications = Regexs.isChinaTelecommounications("18310428956");
                boolean chinaUnicom = Regexs.isChinaUnicom("18310428956");
                Toast.showShort("18310428956是移动？"+chinaMobile +"   联通？"+chinaUnicom+"  电信？"+chinaTelecommounications);
                break;
            case 12:
                startActivity(new Intent(this,DpsActivity.class));
                break;
            case 13:
                startActivity(new Intent(this,DialogActivity.class));
                break;
            case 14:
                Sps.remove(Account.class,"password");
                Toast.showShort("remove 结果 ="+Sps.get(Account.class).toString());
                break;
            case 15:
                Sps.clear(Account.class);
                Toast.showShort("clear结果 ="+Sps.get(Account.class).toString());
                break;
                default:

        }
    }

    private Account getAccount() {
        Account a = new Account();
        a.setAge(10);
        a.setIdentify("41022319900214423X");
        a.setName("北云");
        a.setPassword("beiyun");
        a.setPhone("18087172012");
        a.setLogin(true);
        a.setAge(26);
        a.setaFloat(2f);
        a.setaLong(123123123123L);
        Set<String> stringSet = new HashSet<>();
        stringSet.add("stringSet item 1");
        stringSet.add("stringSet item 2");
        stringSet.add("stringSet item 3");
        stringSet.add("stringSet item 4");
        a.setStringSet(stringSet);

        return a;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       menu.add("go");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }
}


