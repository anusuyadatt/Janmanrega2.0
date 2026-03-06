package nic.hp.ccmgnrega.common;

public interface ResponseCallback {

    void onResponse(String serviceIdOrTranslatedStringOrBase64FormattedString);
    void onFailure();
}
