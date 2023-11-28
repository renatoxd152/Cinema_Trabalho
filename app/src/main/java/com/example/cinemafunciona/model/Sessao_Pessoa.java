package com.example.cinemafunciona.model;

public class Sessao_Pessoa {

    private int id;
    private int id_pessoa;

    private int id_sessao;

    public Sessao_Pessoa(int id, int id_pessoa, int id_sessao) {
        this.id = id;
        this.id_pessoa = id_pessoa;
        this.id_sessao = id_sessao;
    }

    public Sessao_Pessoa(int id_pessoa, int id_sessao) {
        this.id_pessoa = id_pessoa;
        this.id_sessao = id_sessao;
    }

    public Sessao_Pessoa() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_pessoa() {
        return id_pessoa;
    }

    public void setId_pessoa(int id_pessoa) {
        this.id_pessoa = id_pessoa;
    }

    public int getId_sessao() {
        return id_sessao;
    }

    public void setId_sessao(int id_sessao) {
        this.id_sessao = id_sessao;
    }
}
