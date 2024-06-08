package com.example.ildeilc_gestion.model;

public class Facture {

    public enum EtatFacture {
        EN_ATTENTE_DE_PAIEMENT,
        PAYEE
    }

    private EtatFacture etat;

    public Facture() {
        etat = EtatFacture.EN_ATTENTE_DE_PAIEMENT;
    }

    public void setEtat(EtatFacture etat) {
        this.etat = etat;
    }

    public EtatFacture getEtat() {
        return etat;
    }
}
