package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

public class Language{

	@SerializedName("sourceScriptCode")
	private String sourceScriptCode;

	@SerializedName("sourceLanguage")
	private String sourceLanguage;

	@SerializedName("targetLanguage")
	private String targetLanguage;

	public String getSourceScriptCode(){
		return sourceScriptCode;
	}

	public String getSourceLanguage(){
		return sourceLanguage;
	}

	public void setSourceLanguage(String sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
	}

	public void setTargetLanguage(String targetLanguage) {
		this.targetLanguage = targetLanguage;
	}
}