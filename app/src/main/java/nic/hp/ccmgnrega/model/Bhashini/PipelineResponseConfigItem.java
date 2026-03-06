package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PipelineResponseConfigItem {

	@SerializedName("taskType")
	private String taskType;

	@SerializedName("config")
	private List<Config> configList;


	public String getTaskType(){
		return taskType;
	}

	public List<Config> getConfigList(){
		return configList;
	}
}