package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BhashiniConfigRequest {

    @SerializedName("pipelineTasks")
    private List<ConfigCallPipelineTasksItem> pipelineTasks;

    @SerializedName("pipelineRequestConfig")
    private PipelineRequestConfig pipelineRequestConfig;

    public void setPipelineTasks(List<ConfigCallPipelineTasksItem> pipelineTasks) {
        this.pipelineTasks = pipelineTasks;
    }

    public void setPipelineRequestConfig(PipelineRequestConfig pipelineRequestConfig) {
        this.pipelineRequestConfig = pipelineRequestConfig;
    }
}
