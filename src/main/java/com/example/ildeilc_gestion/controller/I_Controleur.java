
package com.example.ildeilc_gestion.controller;

import com.example.ildeilc_gestion.model.Client;
import com.example.ildeilc_gestion.model.Commande;
import com.example.ildeilc_gestion.model.Modele;

public interface I_Controleur {

    void selectionnerClient(Client client);

    void deselectionnerClient();

    void modifierStock(int nombreArticles);

    boolean validerStock(Commande commande);

    void validerLivraison(Commande commande);

    void validerPaiement(Commande commande);

    Modele getModele();

    Client getClientSelectionne();
}
