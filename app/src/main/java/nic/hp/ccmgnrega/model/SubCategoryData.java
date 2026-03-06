package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategoryData {
    @SerializedName("Data")
    private List<SubCategoryModel> subCatList;
    @SerializedName("Remarks")
    private String remarks;

    public List<SubCategoryModel> getSubCategoryList() {
        return subCatList;
    }
    public String getRemarks() {
        return remarks;
    }
}
