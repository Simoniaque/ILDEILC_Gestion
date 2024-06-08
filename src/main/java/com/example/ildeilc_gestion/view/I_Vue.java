package com.example.ildeilc_gestion.view;

import com.example.ildeilc_gestion.controller.I_Controleur;
import com.example.ildeilc_gestion.model.Client;
import java.util.List;


public interface I_Vue {

    void update();

    void afficherClients(List<Client> clients);

    void afficherCommandes(Client client);

    void updateNbArticles();

    void setControleur(I_Controleur controleur);

    void initVue();

}