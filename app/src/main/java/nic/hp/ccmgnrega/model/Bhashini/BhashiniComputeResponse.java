package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class 	BhashiniComputeResponse {

	@SerializedName("pipelineResponse")
	private List<PipelineResponseItem> pipelineResponse;

	@SerializedName("pipelineResponseConfig")
	private List<PipelineResponseItem> pipelineResponseConfig;

	public List<PipelineResponseItem> getPipelineResponse(){
		return pipelineResponse;
	}

	public List<PipelineResponseItem> getPipelineResponseConfig() {
		return pipelineResponseConfig;
	}
}