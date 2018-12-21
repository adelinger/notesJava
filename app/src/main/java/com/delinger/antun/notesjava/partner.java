package com.delinger.antun.notesjava;

import android.net.sip.SipSession;

import java.util.List;

public class partner {
    public List<String> firstnameList;
    public List<String> lastnameList;
    public List<String> emailList;
    public List<String> phoneList;
    public List<String> idList;

    private String firstname;
    private String lastName;
    private String email;
    private String phone;
    private Integer id;


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
