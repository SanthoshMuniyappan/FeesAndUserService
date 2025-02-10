package com.schoolworld.feesPayment.dto;

public class ScholarShipRequestDTO {

    private String studentId;
    private boolean scholarShip;
    private int scholarShipAmount;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public boolean isScholarShip() {
        return scholarShip;
    }

    public void setScholarShip(boolean scholarShip) {
        this.scholarShip = scholarShip;
    }

    public int getScholarShipAmount() {
        return scholarShipAmount;
    }

    public void setScholarShipAmount(int scholarShipAmount) {
        this.scholarShipAmount = scholarShipAmount;
    }
}
