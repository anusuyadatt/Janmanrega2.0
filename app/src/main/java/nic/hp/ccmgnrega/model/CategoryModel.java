package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

public class CategoryModel {
    @SerializedName("complain_cat_code")
    String categoryCode;
    @SerializedName("category_name")
    String   categoryName;

    public CategoryModel(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryModel(String categoryCode, String categoryName) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
