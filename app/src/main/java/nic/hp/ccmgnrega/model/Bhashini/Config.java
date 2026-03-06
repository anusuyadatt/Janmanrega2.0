package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Config{

	@SerializedName("samplingRate")
	private int samplingRate;

	@SerializedName("audioFormat")
	private String audioFormat;

	@SerializedName("language")
	private Language language;

	@SerializedName("encoding")
	private String encoding;

	@SerializedName("serviceId")
	private String serviceId;

	@SerializedName("gender")
	private String gender;

	@SerializedName("supportedVoices")
	private List<String> supportedVoices;

	public int getSamplingRate(){
		return samplingRate;
	}

	public String getAudioFormat(){
		return audioFormat;
	}

	public Language getLanguage(){
		return language;
	}

	public String getEncoding(){
		return encoding;
	}

	public String getServiceId() {
		return serviceId;
	}

	public String getGender() {
		return gender;
	}

	public void setSamplingRate(int samplingRate) {
		this.samplingRate = samplingRate;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<String> getSupportedVoices() {
		return supportedVoices;
	}
}