package com.example.ildeilc_gestion.model;

import com.example.ildeilc_gestion.DateFR;

public class Commande {


    public enum EtatCommande {
        EN_ATTENTE_DE_STOCK,
        EN_COURS_DE_LIVRAISON,
        EN_ATTENTE_DE_PAIEMENT,
        FINALISEE;

        @Override
        public String toString() {
            switch (this) {
                case EN_ATTENTE_DE_STOCK:
                    return "En attente de stock";
                case EN_COURS_DE_LIVRAISON:
                    return "En cours de livraison";
                case EN_ATTENTE_DE_PAIEMENT:
                    return "En attente de paiement";
                case FINALISEE:
                    return "Finalisée";
                default:
                    return "";
            }
        }
    }

    private Integer numCommande;
    private Facture facture;
    private BonDeLivraison bonDeLivraison;
    private EtatCommande etat;
    private DateFR date;
    private int nombreArticles;
    private String clientNom;
    private String adresse;
    private boolean stockValide;

    public Commande(Integer numCommande, DateFR date, int nombreArticles) {
        this.numCommande = numCommande;
        this.etat = EtatCommande.EN_ATTENTE_DE_STOCK;
        this.date = date;
        this.nombreArticles = nombreArticles;
    }

    public Integer getNumCommande() {
        return numCommande;
    }

    public EtatCommande getEtat() {
        return etat;
    }

    public int getNombreArticles() {
        return nombreArticles;
    }

    private void setEtat(EtatCommande etat) {
        this.etat = etat;
    }

    /*public void validerStock() {
        setEtat(EtatCommande.EN_COURS_DE_LIVRAISON);
        bonDeLivraison = new BonDeLivraison(numCommande, date, nombreArticles);
    }*/

    public void validerStock() {
        this.etat = EtatCommande.EN_COURS_DE_LIVRAISON;
        genererBonDeLivraison();
    }

    private void genererBonDeLivraison() {
        this.bonDeLivraison = new BonDeLivraison(this.numCommande, this.clientNom, this.adresse, this.date);
    }

    public BonDeLivraison getBonDeLivraison() {
        return bonDeLivraison;
    }

    public void validerLivraison() {
        if (etat == EtatCommande.EN_COURS_DE_LIVRAISON) {
            setEtat(EtatCommande.EN_ATTENTE_DE_PAIEMENT);
            facture = new Facture();
        }
    }

    public void validerPaiement() {
        if (etat == EtatCommande.EN_ATTENTE_DE_PAIEMENT) {
            setEtat(EtatCommande.FINALISEE);
            if (facture != null) {
                facture.setEtat(Facture.EtatFacture.PAYEE);
            }
        }
    }

    public DateFR getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        return "Commande n°" + numCommande + " - " + date + " - " + nombreArticles + " articles" + " - " + etat;
    }
}
