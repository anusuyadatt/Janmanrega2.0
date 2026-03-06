package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryData {
    @SerializedName("Data")
    private List<CategoryModel> catList;
    @SerializedName("Remarks")
    private String remarks;

    public List<CategoryModel> getCategoryList() {
        return catList;
    }
    public String getRemarks() {
        return remarks;
    }
}
