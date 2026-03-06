package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BhashiniComputeRequest {

	@SerializedName("inputData")
	private InputData inputData;

	@SerializedName("pipelineTasks")
	private List<PipelineTasksItem> pipelineTasks;

	public void setInputData(InputData inputData) {
		this.inputData = inputData;
	}

	public void setPipelineTasks(List<PipelineTasksItem> pipelineTasks) {
		this.pipelineTasks = pipelineTasks;
	}

}