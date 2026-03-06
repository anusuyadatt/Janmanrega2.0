package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

public class PipelineRequestConfig {

    @SerializedName("pipelineId")
    private String pipelineId;

    public void setPipelineId(String pipelineId) {
        this.pipelineId = pipelineId;
    }
}
