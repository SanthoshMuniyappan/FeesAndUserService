package com.schoolworld.feesPayment.dto;

public class FeesRequestDTO {

    private String standardId;
    private String feesCategory;
    private int feesAmount;

    public String getStandardId() {
        return standardId;
    }

    public void setStandardId(String standardId) {
        this.standardId = standardId;
    }

    public String getFeesCategory() {
        return feesCategory;
    }

    public void setFeesCategory(String feesCategory) {
        this.feesCategory = feesCategory;
    }

    public int getFeesAmount() {
        return feesAmount;
    }

    public void setFeesAmount(int feesAmount) {
        this.feesAmount = feesAmount;
    }
}
