package com.example.cinemafunciona.model;

public class Sessao {
    private int id;

    private int id_filme;

    private int quantidade_pessoas;

    private String sala;

    public Sessao(int id, int id_filme, int quantidade_pessoas, String sala) {
        this.id = id;
        this.id_filme = id_filme;
        this.quantidade_pessoas = quantidade_pessoas;
        this.sala = sala;
    }

    public Sessao(int id_filme, int quantidade_pessoas, String sala) {
        this.id_filme = id_filme;
        this.quantidade_pessoas = quantidade_pessoas;
        this.sala = sala;
    }

    public Sessao() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_filme() {
        return id_filme;
    }

    public void setId_filme(int id_filme) {
        this.id_filme = id_filme;
    }

    public int getQuantidade_pessoas() {
        return quantidade_pessoas;
    }

    public void setQuantidade_pessoas(int quantidade_pessoas) {
        this.quantidade_pessoas = quantidade_pessoas;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    @Override
    public String toString() {
        return sala;
    }
}
