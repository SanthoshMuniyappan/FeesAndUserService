package com.schoolworld.feesPayment.dto;

public class DiscountRequestDTO {

    private String studentId;
    private boolean isDiscount;
    private int discountAmount;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public boolean getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(boolean discount) {
        isDiscount = discount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }
}
