package nic.hp.ccmgnrega.model.Bhashini;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InputData{

	@SerializedName("input")
	private List<InputItem> input;

	public List<InputItem> getInput(){
		return input;
	}

	public void setInput(List<InputItem> input) {
		this.input = input;
	}
}