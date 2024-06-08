package com.example.ildeilc_gestion.model;

import com.example.ildeilc_gestion.DateFR;

public class BonDeLivraison {
    private Integer numCommande;
    private String nomClient;
    private String adresse;
    private DateFR date;

    public BonDeLivraison(Integer numCommande, String nomClient, String adresse, DateFR date) {
        this.numCommande = numCommande;
        this.nomClient = nomClient;
        this.adresse = adresse;
        this.date = date;
    }

    public int getNumCommande() {
        return numCommande;
    }

    public String getNomClient() {
        return nomClient;
    }

    public String getAdresse() {
        return adresse;
    }

    public DateFR getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        return "BonDeLivraison{" +
                "commandeId='" + numCommande + '\'' +
                ", clientNom='" + nomClient + '\'' +
                ", adresse='" + adresse + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
