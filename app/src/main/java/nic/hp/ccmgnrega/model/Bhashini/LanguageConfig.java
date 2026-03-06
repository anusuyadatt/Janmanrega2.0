package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

public class LanguageConfig {

	@SerializedName("language")
	private Language language;

	public Language getLanguage(){
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
}