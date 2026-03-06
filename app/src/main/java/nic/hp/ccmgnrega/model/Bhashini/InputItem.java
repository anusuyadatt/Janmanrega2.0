package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

public class InputItem{

	@SerializedName("source")
	private String source;

	public String getSource(){
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}