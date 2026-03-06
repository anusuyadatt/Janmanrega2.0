package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

public class AudioItem{

	@SerializedName("audioContent")
	private String audioContent;

	@SerializedName("audioUri")
	private Object audioUri;

	public String getAudioContent(){
		return audioContent;
	}

	public Object getAudioUri(){
		return audioUri;
	}
}