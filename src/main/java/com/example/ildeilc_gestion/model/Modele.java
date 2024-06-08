
package com.example.ildeilc_gestion.model;

import java.util.ArrayList;
import java.util.List;

public class Modele {

    List<Client> clients;
    private List<Commande> commandes;
    int nombreArticlesDisponibles;

    public Modele(int nombreArticlesDisponibles) {
        clients = new ArrayList<>();
        this.nombreArticlesDisponibles = nombreArticlesDisponibles;
        this.commandes = new ArrayList<>(); // Initialisation de la liste des commandes
    }

    public List<Client> getClients(){
        return clients;
    }

    public List<Commande> getCommandes() {
        if (this.commandes == null) {
            this.commandes = new ArrayList<>();
        }
        return this.commandes;
    }

    public void ajouterCommande(Commande commande) {
        if (this.commandes == null) {
            this.commandes = new ArrayList<>();
        }
        this.commandes.add(commande);
    }

    public void ajouterClient(Client client){
        clients.add(client);
    }

    public Client getClient(String nom){
        for(Client client : clients){
            if(client.getNom().equals(nom)){
                return client;
            }
        }
        return null;
    }

    public Commande getCommande(int id){
        for(Commande commande : commandes){
            if(commande.getNumCommande() == id){
                return commande;
            }
        }
        return null;
    }


    public int getNbArticlesDispo(){
        return nombreArticlesDisponibles;
    }

    public void modifierStock(int nombreArticles){
        nombreArticlesDisponibles = nombreArticles;
    }
}
