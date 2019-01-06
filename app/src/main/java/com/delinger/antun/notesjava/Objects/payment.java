package com.delinger.antun.notesjava.Objects;

import java.io.Serializable;
import java.util.List;

public class payment implements Serializable {
    public List<Integer> idList;
    public List<Double> debitList;
    public List<Double> claimList;
    public List<String>  dateList;
    public List<Integer> partnerIdList;
    public List<Integer> carIdList;
    public List<Integer> userIdList;

    private Integer id;
    private Double debit;
    private Double claim;
    private String date;
    private Integer partnerID;
    private Integer carID;
    private Integer userID;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getClaim() {
        return claim;
    }

    public void setClaim(Double claim) {
        this.claim = claim;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(Integer partnerID) {
        this.partnerID = partnerID;
    }

    public Integer getCarID() {
        return carID;
    }

    public void setCarID(Integer carID) {
        this.carID = carID;
    }


}
