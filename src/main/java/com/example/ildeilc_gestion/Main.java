package com.example.ildeilc_gestion;

import com.example.ildeilc_gestion.controller.Controleur;
import com.example.ildeilc_gestion.model.Commande;
import com.example.ildeilc_gestion.model.GestionnaireFichierStock;
import com.example.ildeilc_gestion.model.GestionnaireXML;
import com.example.ildeilc_gestion.model.Modele;
import com.example.ildeilc_gestion.view.ViewJavaFX;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import java.io.IOException;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);

        String filePath = "src/main/java/com/example/ildeilc_gestion/data/clients.xml";
        int nbArticles = GestionnaireFichierStock.recupNbArticlesDispo();
        Modele modele = new Modele(nbArticles);
        GestionnaireXML gestionnaireXML = new GestionnaireXML();
        gestionnaireXML.deserialiser(filePath, modele);

        Commande commande = modele.getClients().get(0).getCommandes().get(0);

        validerCommandeEtAfficherBonDeLivraison(commande, filePath);
    }

    @Override
    public void start(Stage stage) throws IOException {

        int nbArticles = GestionnaireFichierStock.recupNbArticlesDispo();

        Modele modele = new Modele(nbArticles);
        ViewJavaFX vue = new ViewJavaFX();
        Controleur controleur = new Controleur(modele, vue);

        vue.setControleur(controleur);
        vue.initVue();

        Scene scene = new Scene(vue, 1400, 800);

        stage.setTitle("Gestion des commandes");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();

    }

    public static void validerCommandeEtAfficherBonDeLivraison(Commande commande, String filePath) {
        commande.validerStock();
        GestionnaireXML gestionnaireXML = new GestionnaireXML();
        gestionnaireXML.ajouterBonDeLivraison(commande.getNumCommande(), commande.getBonDeLivraison());

        Platform.runLater(() -> {
            ViewJavaFX view = new ViewJavaFX();
            view.afficherBonDeLivraison(commande);
        });
    }
}
