package com.bbi.pesquisa.model;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private int id;
    private String question;
    private List<Alternative> alternativeList;

    public Question() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Alternative> getAlternativeList() {
        return alternativeList;
    }

    public void setAlternativeList(List<Alternative> alternativeList) {
        this.alternativeList = alternativeList;
    }
}
