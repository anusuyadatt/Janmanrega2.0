package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

public class ConfigCallPipelineTasksItem {

	@SerializedName("taskType")
	private String taskType;
	@SerializedName("config")
	private LanguageConfig languageConfig;

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public void setLanguageConfig(LanguageConfig languageConfig) {
		this.languageConfig = languageConfig;
	}
}