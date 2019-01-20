package com.delinger.antun.notesjava.Objects;

import java.io.Serializable;
import java.util.List;

public class car implements Serializable {

    private String name;
    private String workRequired;
    private Integer id;
    private String receiptDate;
    private String dispatchDate;
    private Integer partnerID;
    private String note;
    private Double cost;
    private Integer finished;


    public List<String> nameList;
    public List<String> workRequiredList;
    public List<Integer> idList;
    public List<String> receiptDateList;
    public List<String> dispatchDateList;
    public List<Integer> partnerIDList;
    public List<String> noteList;
    public List<Double> costList;
    public List<Integer> finishedList;

    public Integer getFinished() {
        return finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }


    public Double getCost() {
        return cost;
    }

    public void   setCost(Double cost) {
        this.cost = cost;
    }

    public List<Double> getCostList() {
        return costList;
    }

    public void setCostList(List<Double> costList) {
        this.costList = costList;
    }

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

    public Integer getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(Integer partnerID) {
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

    public List<Integer> getPartnerIDList() {
        return partnerIDList;
    }

    public void setPartnerIDList(List<Integer> partnerIDList) {
        this.partnerIDList = partnerIDList;
    }
    public List<String> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<String> noteList) {
        this.noteList = noteList;
    }
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
