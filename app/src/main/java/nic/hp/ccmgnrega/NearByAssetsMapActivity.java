package nic.hp.ccmgnrega;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import nic.hp.ccmgnrega.common.MyAlert;
import nic.hp.ccmgnrega.model.ConnectionDetector;

public class NearByAssetsMapActivity extends AppCompatActivity {
    protected String cLong = "", cLat = "";
    protected ConnectionDetector connectionDetector;
    TabLayout tabLayout;
    ViewPager viewPager;
    String strActivityCode="11-";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_assets_map);
        loadActionBar();
        findById();
        connectionDetector = new ConnectionDetector(NearByAssetsMapActivity.this);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            if (args.getString("cLong") != null)
                cLong = args.getString("cLong");
            if (args.getString("cLat") != null)
                cLat = args.getString("cLat");
        }
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.map_view)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.list_view)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    }

    private void findById() {
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }

    public void loadActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_white_back);
        @SuppressWarnings("deprecation")
        Drawable d = getResources().getDrawable(R.drawable.header_bg2);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setTitle(Html.fromHtml("<center><font color='#FFFFFF'><b>&nbsp;" + getApplicationContext().getString(R.string.app_name) + "</b></font></center>"));
        getSupportActionBar().setIcon(R.mipmap.ic_india_emblem);

    }

}