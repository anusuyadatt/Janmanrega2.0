package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

public class Output {

    @SerializedName("source")
    private String source;

    @SerializedName("target")
    private String target;

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
