package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

public class SubCategoryModel {
    @SerializedName("complain_sub_cat_code")
    String subCategoryCode;

    public SubCategoryModel(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    @SerializedName("category_sub_cat_name")
    String   subCategoryName;

    public SubCategoryModel(String subCategoryCode, String subCategoryName) {
        this.subCategoryCode = subCategoryCode;
        this.subCategoryName = subCategoryName;
    }

    public String getSubCategoryCode() {
        return subCategoryCode;
    }

    public void setSubCategoryCode(String subCategoryCode) {
        this.subCategoryCode = subCategoryCode;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }
}
