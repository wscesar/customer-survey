package com.bbi.pesquisa.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Alternative implements Serializable {
    private int idPergunta;
    private int idAlternativa;
    private String descricao;

    public Alternative() {
    }

    public int getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(int idPergunta) {
        this.idPergunta = idPergunta;
    }

    public int getIdAlternativa() {
        return idAlternativa;
    }

    public void setIdAlternativa(int idAlternativa) {
        this.idAlternativa = idAlternativa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
