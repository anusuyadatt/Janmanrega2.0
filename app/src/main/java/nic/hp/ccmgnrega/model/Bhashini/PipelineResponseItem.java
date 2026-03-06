package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PipelineResponseItem{

	@SerializedName("output")
	private List<Output> output;

	@SerializedName("taskType")
	private String taskType;

	@SerializedName("audio")
	private List<AudioItem> audio;

	@SerializedName("config")
	private Config config;

	public List<Output> getOutput(){
		return output;
	}

	public String getTaskType(){
		return taskType;
	}

	public List<AudioItem> getAudio(){
		return audio;
	}

	public Config getConfig(){
		return config;
	}
}