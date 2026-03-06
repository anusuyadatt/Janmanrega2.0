package nic.hp.ccmgnrega;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import nic.hp.ccmgnrega.adapter.VPAdapter;
import nic.hp.ccmgnrega.common.Constant;
import nic.hp.ccmgnrega.common.MediaPlayerHelper;
import nic.hp.ccmgnrega.data.JobCardDataAccess;
import nic.hp.ccmgnrega.fragment.AbpsStatusFragment;
import nic.hp.ccmgnrega.fragment.AttendanceFragment;
import nic.hp.ccmgnrega.fragment.JobCardFragment;
import nic.hp.ccmgnrega.fragment.PaymentFragment;
import nic.hp.ccmgnrega.fragment.PersonalAssetsFragment;
import nic.hp.ccmgnrega.fragment.SearchFragment;
import nic.hp.ccmgnrega.model.ConnectionDetector;
import nic.hp.ccmgnrega.model.JobCardData;

public class JobCardHolderActivity extends AppCompatActivity {
    private static int textSize = 14;
    TextView jobCardId, numDays, finYearStatement;
    RelativeLayout jobCardInfo;
    ImageView userImage;
    private static TabLayout tabLayout;
    private static ViewPager2 viewPager2;
    Context context;
    Button btnWorkerQuery;
    String strJobCardNumber="";
    protected ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_card_holder);
        context = getApplicationContext();
        loadActionBar();
        connectionDetector = new ConnectionDetector(JobCardHolderActivity.this);

        jobCardInfo = findViewById(R.id.jobCardInfo);
        jobCardId = findViewById(R.id.jobCardId);
        finYearStatement = findViewById(R.id.finYearStatement);
        userImage = findViewById(R.id.userImage);
        numDays = findViewById(R.id.numDays);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        btnWorkerQuery = findViewById(R.id.btnWorkerQuery);
        JobCardData jobCardData = SearchFragment.jobCardData;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            strJobCardNumber=bundle.getString(Constant.JOB_CARD_ID).toUpperCase().trim();
        jobCardId.setText(strJobCardNumber);
        setUserImage(userImage, context);
        setJobCardDeletedText(jobCardInfo, context, jobCardData);
        setNumDaysWorkedText(numDays, finYearStatement, context);

        viewPager2.setOffscreenPageLimit(5);
        viewPager2.setAdapter(createAdapter(tabLayout, viewPager2, this));
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                refreshExpandableListFragment(getSupportFragmentManager());
                releaseAllMediaPlayers();
            }
        });

        btnWorkerQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    Intent intent = new Intent(context, WorkerQueryNewActivity.class);
                    intent.putExtra("strJobCardNumber", strJobCardNumber);
                    intent.putExtra("strJobCardDataMode", "JCF");
                    startActivity(intent);
                }
                else
                    Toast.makeText(JobCardHolderActivity.this, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();

            }
        });
    }

    private static void setJobCardDeletedText(RelativeLayout jobCardInfo, Context context, JobCardData jobCardData) {
        TextView jobCardDeleted = new TextView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(jobCardInfo.getLayoutParams());
        layoutParams.addRule(RelativeLayout.BELOW, R.id.jobCardId);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        jobCardDeleted.setLayoutParams(layoutParams);
        jobCardDeleted.setTypeface(null, Typeface.BOLD);
        jobCardDeleted.setTextColor(Color.RED);
        jobCardDeleted.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        jobCardDeleted.setPadding(0, 15, 0, 0);

        if (jobCardData != null && jobCardData.getStatus() != null && !jobCardData.getStatus().isEmpty() && jobCardData.getStatus().equalsIgnoreCase("Deleted")) {

            String deletionText = "(" + context.getString(R.string.jobCardDeleted);
            if (jobCardData.getDeletionDate() != null && !jobCardData.getDeletionDate().isEmpty()) {
                String receivedDeletionDate = jobCardData.getDeletionDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = dateFormat.parse(receivedDeletionDate);
                    String deletionDate = dateFormat.format(date);
                    deletionText += ", " + context.getString(R.string.dateOfDeletion) + ": " + deletionDate;
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            deletionText += ")";
            jobCardDeleted.setText(deletionText);
            jobCardInfo.addView(jobCardDeleted);
        } else {
            jobCardDeleted.setText("");
        }
    }

    private static void setUserImage(ImageView userImage, Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.placeholder);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        userImage.setImageDrawable(roundedBitmapDrawable);
    }

    private static void setNumDaysWorkedText(TextView numDays, TextView finYearStatement, Context context) {
        numDays.setText(context.getString(R.string.daysWorked) + ": " + JobCardDataAccess.totalDaysWorked);
        int finYearStart = JobCardDataAccess.getFinancialYearStart();
        int finYearEnd = finYearStart + 1;
        finYearStatement.setText("(" + context.getString(R.string.financialYear) + ": " + finYearStart + "-" + finYearEnd + ")");
    }

    private static void releaseAllMediaPlayers() {
        MediaPlayerHelper.releaseMediaPlayer();
    }

    private static void refreshExpandableListFragment(FragmentManager childFragmentManager) {
        Fragment currentFragment = childFragmentManager.findFragmentByTag("f" + viewPager2.getCurrentItem());

        if (currentFragment instanceof AttendanceFragment) {
            AttendanceFragment expandableListFragment = (AttendanceFragment) currentFragment;
            expandableListFragment.refreshExpandableList();
        } else if (currentFragment instanceof PaymentFragment) {
   PaymentFragment expandableListFragment = (PaymentFragment) currentFragment;
            expandableListFragment.refreshExpandableList();
        } else if (currentFragment instanceof PersonalAssetsFragment) {
            PersonalAssetsFragment expandableListFragment = (PersonalAssetsFragment) currentFragment;
            expandableListFragment.refreshExpandableList();
        }
    }

    private static VPAdapter createAdapter(TabLayout tabLayout, ViewPager2 viewPager2, JobCardHolderActivity jobCardHolderActivity) {
        VPAdapter vpAdapter = new VPAdapter(jobCardHolderActivity);

        JobCardFragment jobCardFragment = new JobCardFragment();
        vpAdapter.addFragment(jobCardFragment, jobCardHolderActivity.context.getString(R.string.jobCardTabHeading));

        AttendanceFragment attendanceFragment = new AttendanceFragment();
        vpAdapter.addFragment(attendanceFragment, jobCardHolderActivity.context.getString(R.string.attendanceTabHeading));

        PaymentFragment paymentFragment = new PaymentFragment();
        vpAdapter.addFragment(paymentFragment, jobCardHolderActivity.context.getString(R.string.paymentTabHeading));

        PersonalAssetsFragment personalAssetsFragment = new PersonalAssetsFragment();
        vpAdapter.addFragment(personalAssetsFragment, jobCardHolderActivity.context.getString(R.string.personalAssetsTabHeading));

        AbpsStatusFragment abpsStatusFragment = new AbpsStatusFragment();
        vpAdapter.addFragment(abpsStatusFragment, jobCardHolderActivity.context.getString(R.string.abpsStatusTabHeading));

        viewPager2.setAdapter(vpAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(vpAdapter.getFragmentTitle(position));
        }).attach();
        return vpAdapter;
    }

    public void loadActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_white_back);
        @SuppressWarnings("deprecation")
        Drawable d = getResources().getDrawable(R.drawable.header_bg2);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setTitle(Html.fromHtml("<center><font color='#FFFFFF'><b>&nbsp;" + context.getString(R.string.app_name) + "</b></font></center>"));
        getSupportActionBar().setIcon(R.mipmap.ic_india_emblem);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseAllMediaPlayers();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseAllMediaPlayers();
    }
}