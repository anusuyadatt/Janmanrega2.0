package nic.hp.ccmgnrega.common;

public interface TtsResponseCallback {

    void onResponse(String serviceId, String audioVoice);
    void onFailure();
}
