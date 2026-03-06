package nic.hp.ccmgnrega.model;

import android.content.Context;


import nic.hp.ccmgnrega.R;

public class GetAssetMap {
    private final Context _activity;
    public GetAssetMap(Context activity) {
        this._activity = activity;

    }

    public String internentNotEnable() {
        return "<html>\n" +
                "<head>\n" +
                "    \n" +
                "</head>\n" +
                "<body >\n" +
                "<div>" +  _activity.getString(R.string.no_internet) + "</div>\n" +
                "</body>\n" +
                "</html>";
    }

    public String gpsNotEnable() {
        return "<html>\n" +
                "<head>\n" +
                "    \n" +
                "</head>\n" +
                "<body >\n" +
                "<div>" + _activity.getString(R.string.enable_gps)+ "</div>\n" +
                "</body>\n" +
                "</html>";
    }



}