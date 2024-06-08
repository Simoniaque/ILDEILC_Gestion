package com.example.ildeilc_gestion.controller;

import com.example.ildeilc_gestion.model.*;
import com.example.ildeilc_gestion.view.I_Vue;
import com.example.ildeilc_gestion.view.ViewJavaFX;

public class Controleur implements I_Controleur {

    private final Modele modele;
    private final I_Vue vue;
    private Client clientSelectionne = null;
    private final GestionnaireXML gestionnaireXML;

    public Controleur(Modele modele, I_Vue vue) {
        this.modele = modele;
        this.vue = vue;
        vue.setControleur(this);
        gestionnaireXML = new GestionnaireXML();
        gestionnaireXML.deserialiser("src/main/java/com/example/ildeilc_gestion/data/clients.xml", modele);
    }

    @Override
    public Modele getModele() {
        return this.modele;
    }

    @Override
    public void selectionnerClient(Client client) {
        clientSelectionne = client;
        vue.update();
    }

    @Override
    public void deselectionnerClient() {
        clientSelectionne = null;
        vue.update();
    }

    @Override
    public boolean validerStock(Commande commande) {
        if (commande.getEtat() != Commande.EtatCommande.EN_ATTENTE_DE_STOCK) {
            vue.update();
            return false;
        }

        if (clientSelectionne == null) {
            return false;
        }

        if (modele.getNbArticlesDispo() < commande.getNombreArticles()) {
            return false;
        }

        commande.validerStock();
        gestionnaireXML.modifierEtatCommande(commande.getNumCommande(), commande.getEtat().name());
        modifierStock(modele.getNbArticlesDispo() - commande.getNombreArticles());
        vue.update();
        return true;


    }

    public void validerCommandeEtAfficherBonDeLivraison(Commande commande) {
        commande.validerStock();
        ViewJavaFX view = new ViewJavaFX();
        view.afficherBonDeLivraison(commande);
    }


    @Override
    public void validerLivraison(Commande commande) {
        if (commande.getEtat() != Commande.EtatCommande.EN_COURS_DE_LIVRAISON) {
            vue.update();
            return;
        }

        if (clientSelectionne == null) {
            return;
        }

        commande.validerLivraison();
        gestionnaireXML.modifierEtatCommande(commande.getNumCommande(), commande.getEtat().name());
        vue.update();
    }

    @Override
    public void validerPaiement(Commande commande) {
        if (commande.getEtat() != Commande.EtatCommande.EN_ATTENTE_DE_PAIEMENT) {
            vue.update();
            return;
        }

        if (clientSelectionne == null) {
            return;
        }
        commande.validerPaiement();
        gestionnaireXML.modifierEtatCommande(commande.getNumCommande(), commande.getEtat().name());
        vue.update(); // Mettre à jour la vue
    }

    @Override
    public Client getClientSelectionne() {
        return clientSelectionne;
    }

    @Override
    public void modifierStock(int nombreArticles) {
        modele.modifierStock(nombreArticles);
        GestionnaireFichierStock.modifierNbArticlesDispo(nombreArticles);
        vue.update();
    }
}
