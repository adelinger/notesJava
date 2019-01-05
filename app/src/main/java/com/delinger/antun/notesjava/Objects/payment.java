package com.delinger.antun.notesjava.Objects;

import java.io.Serializable;
import java.util.List;

public class payment implements Serializable {
    public List<Integer> idList;
    public List<Integer> debitList;
    public List<Integer> claimList;
    public List<String>  dateList;
    public List<Integer> partnerIdList;
    public List<Integer> carIdList;
    public List<Integer> userIdList;

    private Integer id;
    private Integer debit;
    private Integer claim;
    private Integer date;
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

    public Integer getDebit() {
        return debit;
    }

    public void setDebit(Integer debit) {
        this.debit = debit;
    }

    public Integer getClaim() {
        return claim;
    }

    public void setClaim(Integer claim) {
        this.claim = claim;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
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
