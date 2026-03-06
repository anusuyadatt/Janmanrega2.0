package nic.hp.ccmgnrega.fragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import nic.hp.ccmgnrega.MainActivity;
import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.common.MySharedPref;
import nic.hp.ccmgnrega.model.ConnectionDetector;
import nic.hp.ccmgnrega.common.DbHelper;

@SuppressLint("Range")
public class SettingFragment extends BaseFragment {
    protected View rootView;
    protected Spinner spinnerLanguage;
    List<String> listLangCode = new ArrayList<>();
    List<String> listLangName = new ArrayList<>();
    String langCode="en" ;


    @Override
    public View onCreateView(LayoutInflater inflater2, ViewGroup container, Bundle savedInstanceState) {
       // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        connectionDetector = new ConnectionDetector(getActivity());
        dbHelper = new DbHelper(getActivity());
        dbReader = dbHelper.getReadableDatabase();
        dbWriter = dbHelper.getWritableDatabase();
        Bundle args = getArguments();
        if (args != null && args.getString("menu") != null) {
            menu = args.getString("menu");
        }
        rootView = inflater2.inflate(R.layout.fragment_setting, container, false);
        TextView headingTv = rootView.findViewById(R.id.heading);
        spinnerLanguage = rootView.findViewById(R.id.spinnerLanguage);
        Button submitButton = rootView.findViewById(R.id.btn_submit);

        headingTv.setText(" "+menu);
        populateLanguageSpinner();
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                langCode = listLangCode.get(spinnerLanguage.getSelectedItemPosition());
                MySharedPref.setAppLangCode(getContext(),langCode);
                Locale locale = new Locale(langCode);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getContext().getResources().updateConfiguration(config, getContext().getResources().getDisplayMetrics());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("displayPosition", "0");
                startActivity(intent);
            }
        });
        spinnerLanguage.setSelection(listLangCode.indexOf(MySharedPref.getAppLangCode(getContext())));
        return rootView;
    }

    private void populateLanguageSpinner() {
        listLangName.clear();
        listLangCode.clear();
        listLangName.add("English"); listLangCode.add("en");
        listLangName.add("हिन्दी (Hindi)"); listLangCode.add("hi");
        listLangName.add("বাংলা (Bengali)"); listLangCode.add("bn");
        listLangName.add("मराठी (Marathi)"); listLangCode.add("mr");
        listLangName.add("ਪੰਜਾਬੀ (Punjabi)"); listLangCode.add("pa");
        listLangName.add("ગુજરાતી (Gujrati)"); listLangCode.add("gu");
        listLangName.add("ଓଡ଼ିଆ (Oriya)"); listLangCode.add("or");
        listLangName.add("தமிழ் (Tamil)"); listLangCode.add("ta");
        listLangName.add("తెలుగు (Telugu)"); listLangCode.add("te");
        listLangName.add("ಕನ್ನಡ (Kannada)"); listLangCode.add("kn");
        listLangName.add("മലയാളം (Malyalam)"); listLangCode.add("ml");
        listLangName.add("اردو (Urdu)"); listLangCode.add("ur");
        
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listLangName);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(spinnerArrayAdapter);

    }
    @Override
    public void onDestroy() {
        dbWriter.close();
        super.onDestroy();
    }


}


             /* English,en
            हिन्दी (Hindi),hi
            বাংলা (Bengali),bn
            मराठी (Marathi),mr
            தமிழ் (Tamil),ta
            తెలుగు (Telugu),te
            മലയാളം (Malyalam),ml
            ಕನ್ನಡ (Kannada),kn
            ગુજરાતી (Gujrati),gu
            ਪੰਜਾਬੀ (Punjabi),pa
            অসমীয়া (Assamese),as
            ଓଡ଼ିଆ (Oriya),or
            اردو (Urdu),ur
            মণিপুরী (Manipuri),mn
            Mizo,mi*/