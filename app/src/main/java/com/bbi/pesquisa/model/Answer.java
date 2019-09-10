package com.bbi.pesquisa.model;

import java.io.Serializable;
import java.util.List;

public class Answer implements Serializable {
    private String orderId;
    private String name, phone, email, city, birthday, customerOpinion;
    private List<Alternative> alternativeList;

    public Answer() {
    }

    public String getCustomerOpinion() {
        return customerOpinion;
    }

    public void setCustomerOpinion(String customerOpinion) {
        this.customerOpinion = customerOpinion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

//    public void reset() {
//        this.orderId = null;
//        this.setAlternativeList(null);
//    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<Alternative> getAlternativeList() {
        return alternativeList;
    }

    public void setAlternativeList(List<Alternative> alternativeList) {
        this.alternativeList = alternativeList;
    }

}
