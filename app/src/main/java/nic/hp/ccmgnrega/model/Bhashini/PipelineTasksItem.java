package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

public class PipelineTasksItem{

	@SerializedName("taskType")
	private String taskType;

	@SerializedName("config")
	private Config config;

	public String getTaskType(){
		return taskType;
	}

	public Config getConfig(){
		return config;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
}