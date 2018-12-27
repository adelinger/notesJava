package com.delinger.antun.notesjava.Objects;

import java.io.Serializable;
import java.util.List;

public class car implements Serializable {

    private String name;
    private String workRequired;
    private Integer id;
    private String receiptDate;
    private String dispatchDate;
    private String partnerID;

    private List<String> nameList;
    private List<String> workRequiredList;
    private List<Integer> idList;
    private List<String> receiptDateList;
    private List<String> dispatchDateList;
    private List<String> partnerIDList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkRequired() {
        return workRequired;
    }

    public void setWorkRequired(String workRequired) {
        this.workRequired = workRequired;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(String dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(String partnerID) {
        this.partnerID = partnerID;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public List<String> getWorkRequiredList() {
        return workRequiredList;
    }

    public void setWorkRequiredList(List<String> workRequiredList) {
        this.workRequiredList = workRequiredList;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }

    public List<String> getReceiptDateList() {
        return receiptDateList;
    }

    public void setReceiptDateList(List<String> receiptDateList) {
        this.receiptDateList = receiptDateList;
    }

    public List<String> getDispatchDateList() {
        return dispatchDateList;
    }

    public void setDispatchDateList(List<String> dispatchDateList) {
        this.dispatchDateList = dispatchDateList;
    }

    public List<String> getPartnerIDList() {
        return partnerIDList;
    }

    public void setPartnerIDList(List<String> partnerIDList) {
        this.partnerIDList = partnerIDList;
    }


}
