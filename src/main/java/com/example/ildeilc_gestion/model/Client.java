package com.example.ildeilc_gestion.model;

import java.util.ArrayList;
import java.util.List;


public class Client {
    private String nom;
    private List<Commande> commandes;

    public Client(String nom) {
        commandes = new ArrayList<>();
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    public List<Commande> getCommandes(Commande.EtatCommande etat) {
        List<Commande> commandesEtat = new ArrayList<>();
        for (Commande commande : commandes) {
            if (commande.getEtat() == etat) {
                commandesEtat.add(commande);
            }
        }
        return commandesEtat;
    }

    public void ajouterCommande(Commande commande) {
        commandes.add(commande);
    }

    public Commande getCommande(Integer numCommande) {
        for (Commande commande : commandes) {
            if (commande.getNumCommande().equals(numCommande)) {
                return commande;
            }
        }
        return null;
    }
}
