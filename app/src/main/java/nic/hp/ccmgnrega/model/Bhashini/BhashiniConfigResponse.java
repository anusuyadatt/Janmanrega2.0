package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BhashiniConfigResponse {

	@SerializedName("pipelineResponseConfig")
	private List<PipelineResponseConfigItem> pipelineResponseConfig;

	public List<PipelineResponseConfigItem> getPipelineResponseConfig() {
		return pipelineResponseConfig;
	}
}