package nic.hp.ccmgnrega;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import nic.hp.ccmgnrega.common.MyAlert;
import nic.hp.ccmgnrega.model.ConnectionDetector;

public class GiveFeedbackWorkerReportActivity extends AppCompatActivity {
    protected String cLong = "", cLat = "";
    Button btnFeedback, btnWorkerQuery,btnViewComplaintStatus,btnViewUploadPhoto;
    protected ConnectionDetector connectionDetector;
    String strActivityCode="06-";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback_worker_report);
        loadActionBar();
        connectionDetector = new ConnectionDetector(GiveFeedbackWorkerReportActivity.this);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            if (args.getString("cLong") != null)
                cLong = args.getString("cLong");
            if (args.getString("cLat") != null)
                cLat = args.getString("cLat");
        }
        btnFeedback = findViewById(R.id.btnFeedback);
        btnWorkerQuery = findViewById(R.id.btnWorkerQuery);
        btnViewComplaintStatus = findViewById(R.id.btnViewComplaintStatus);
        btnViewUploadPhoto = findViewById(R.id.btnViewUploadPhoto);
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    Intent intent = new Intent(getApplicationContext(), SendFeedBackActivity.class);
                    intent.putExtra("cLong", cLong);
                    intent.putExtra("cLat", cLat);
                    startActivity(intent);
                } else
                    MyAlert.showAlert(GiveFeedbackWorkerReportActivity.this, R.mipmap.icon_warning, getApplicationContext().getString(R.string.warning), getApplicationContext().getString(R.string.no_internet), strActivityCode+"01");

            }
        });

        btnWorkerQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    Intent intent = new Intent(getApplicationContext(), SearchByJobCardActivity.class);
                    startActivity(intent);
                }
                else
                    MyAlert.showAlert(GiveFeedbackWorkerReportActivity.this, R.mipmap.icon_warning, getApplicationContext().getString(R.string.warning), getApplicationContext().getString(R.string.no_internet), strActivityCode+"02");

            }
        });

        btnViewComplaintStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    Intent intent = new Intent(getApplicationContext(), ViewComplaintSection.class);
                    startActivity(intent);
                } else
                    MyAlert.showAlert(GiveFeedbackWorkerReportActivity.this, R.mipmap.icon_warning, getApplicationContext().getString(R.string.warning), getApplicationContext().getString(R.string.no_internet), strActivityCode+"02");

            }
        });
        btnViewUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    Intent intent = new Intent(getApplicationContext(), UploadWorksitePhoto.class);
                    startActivity(intent);
                } else
                    MyAlert.showAlert(GiveFeedbackWorkerReportActivity.this, R.mipmap.icon_warning, getApplicationContext().getString(R.string.warning), getApplicationContext().getString(R.string.no_internet), strActivityCode+"02");

            }
        });

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