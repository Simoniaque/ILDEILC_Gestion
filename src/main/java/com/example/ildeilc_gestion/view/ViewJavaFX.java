package com.example.ildeilc_gestion.view;

import com.example.ildeilc_gestion.DateFR;
import com.example.ildeilc_gestion.controller.I_Controleur;
import com.example.ildeilc_gestion.model.BonDeLivraison;
import com.example.ildeilc_gestion.model.Client;
import com.example.ildeilc_gestion.model.Commande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ViewJavaFX extends HBox implements I_Vue {

    private I_Controleur controleur = null;

    ObservableList<String> observableListClients = FXCollections.observableArrayList();
    ListView<String> listViewClients;
    TableView<Commande> tableViewCommandes;
    private Label labelNbArticles;
    private TextField stockTextField;
    private Button submitButton;
    private Label titleCommandes;

    public ViewJavaFX() {}

    public void initVue() {
        Label titleClients = new Label("Liste des Clients");
        titleClients.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        titleCommandes = new Label("Liste des Commandes");
        titleCommandes.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        initListViewClients();
        initTableViewCommandes();
        initStockInputSection();

        setSpacing(10);
        setPadding(new Insets(10));

        List<Node> vboxLeftElements = Arrays.asList(titleClients, listViewClients, labelNbArticles, stockTextField, submitButton);
        VBox vboxLeft = new VBox(10);
        vboxLeft.getChildren().addAll(vboxLeftElements);

        VBox vboxRight = new VBox(10, titleCommandes, tableViewCommandes);
        getChildren().addAll(vboxLeft, vboxRight);

        afficherClients(controleur.getModele().getClients());
        if (!controleur.getModele().getClients().isEmpty()) {
            afficherCommandes(controleur.getModele().getClients().get(0));
        }
        updateNbArticles();

        Button openModalButton = new Button("Commandes Finalisées");
        openModalButton.setOnAction(e -> openModal(controleur.getClientSelectionne()));
        vboxLeft.getChildren().add(openModalButton);
    }

    private void initListViewClients() {
        listViewClients = new ListView<>(observableListClients);
        listViewClients.setPrefWidth(300);
        listViewClients.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listViewClients.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                controleur.selectionnerClient(controleur.getModele().getClient(newValue));
            }
        });
    }

    private void initTableViewCommandes() {
        tableViewCommandes = createTableView();
        tableViewCommandes.setRowFactory(tv -> {
            TableRow<Commande> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
                    int numCommande = row.getItem().getNumCommande();
                    Commande commande = controleur.getClientSelectionne().getCommande(numCommande);
                    afficherMenuContextuel(event.getScreenX(), event.getScreenY(), commande);
                }
            });
            return row;
        });
    }

    private TableView<Commande> createTableView() {
        TableView<Commande> tableView = new TableView<>();

        TableColumn<Commande, Integer> colonneNumero = new TableColumn<>("Numéro commande");
        colonneNumero.setCellValueFactory(new PropertyValueFactory<>("numCommande"));

        TableColumn<Commande, DateFR> colonneDate = new TableColumn<>("Date");
        colonneDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colonneDate.setStyle("-fx-alignment: CENTER;");

        TableColumn<Commande, Integer> colonneNbArticles = new TableColumn<>("Nombre d'articles");
        colonneNbArticles.setCellValueFactory(new PropertyValueFactory<>("nombreArticles"));
        colonneNbArticles.setStyle("-fx-alignment: CENTER;");

        TableColumn<Commande, Commande.EtatCommande> colonneEtat = new TableColumn<>("État");
        colonneEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        tableView.getColumns().addAll(colonneNumero, colonneDate, colonneNbArticles, colonneEtat);

        tableView.setPrefWidth(1200);
        tableView.getColumns().forEach(col -> col.setPrefWidth(284));

        return tableView;
    }

    private void initStockInputSection() {
        labelNbArticles = new Label();
        labelNbArticles.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: lightgrey;");
        labelNbArticles.setPrefWidth(300);

        stockTextField = new TextField();
        stockTextField.setPromptText("Nombre d'articles");
        stockTextField.setMaxWidth(200);

        submitButton = new Button("Valider");
        submitButton.setOnAction(e -> {
            try {
                int nouveauNbArticles = Integer.parseInt(stockTextField.getText());
                controleur.modifierStock(nouveauNbArticles);
                updateNbArticles();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Veuillez entrer un nombre valide.");
                alert.showAndWait();
            }
        });
    }

    public void afficherBonDeLivraison(Commande commande) {
        if (commande.getBonDeLivraison() == null) {
            System.out.println("Le bon de livraison n'a pas été généré.");
            return;
        }

        List<BonDeLivraison> bonsDeLivraison = new ArrayList<>();
        bonsDeLivraison.add(commande.getBonDeLivraison());

        Stage modalStage = createModalStage("Bon de Livraison", createBonDeLivraisonTableView(bonsDeLivraison));
        modalStage.show();
    }

    private void openModal(Client client) {
        if (client == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Aucun client sélectionné.");
            alert.showAndWait();
            return;
        }

        List<Commande> commandesFinalisees = client.getCommandes().stream()
                .filter(commande -> commande.getEtat() == Commande.EtatCommande.EN_COURS_DE_LIVRAISON)
                .collect(Collectors.toList());

        if (commandesFinalisees.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Aucun bon de livraison généré.");
            alert.showAndWait();
            return;
        }

        List<BonDeLivraison> bonsDeLivraison = commandesFinalisees.stream()
                .map(Commande::getBonDeLivraison)
                .collect(Collectors.toList());

        Stage modalStage = createModalStage("Bons de Livraison", createBonDeLivraisonTableView(bonsDeLivraison));
        modalStage.show();
    }

    private TableView<BonDeLivraison> createBonDeLivraisonTableView(List<BonDeLivraison> bonsDeLivraison) {
        TableView<BonDeLivraison> tableView = new TableView<>();
        ObservableList<BonDeLivraison> data = FXCollections.observableArrayList(bonsDeLivraison);

        TableColumn<BonDeLivraison, String> colClientNom = new TableColumn<>("Nom du Client");
        colClientNom.setCellValueFactory(new PropertyValueFactory<>("nomClient"));

        TableColumn<BonDeLivraison, Integer> colCommandeId = new TableColumn<>("ID de la Commande");
        colCommandeId.setCellValueFactory(new PropertyValueFactory<>("numCommande"));

        TableColumn<BonDeLivraison, String> colAdresse = new TableColumn<>("Adresse");
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        TableColumn<BonDeLivraison, DateFR> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        tableView.getColumns().addAll(colClientNom, colCommandeId, colAdresse, colDate);
        tableView.setItems(data);

        return tableView;
    }

    private Stage createModalStage(String title, Node content) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle(title);

        VBox vbox = new VBox(content);
        Scene scene = new Scene(vbox);

        modalStage.setScene(scene);

        return modalStage;
    }

    @Override
    public void updateNbArticles() {
        labelNbArticles.setText("Nombre d'articles disponibles: " + controleur.getModele().getNbArticlesDispo());
    }

    @Override
    public void update() {
        afficherClients(controleur.getModele().getClients());
        if (controleur.getClientSelectionne() == null) {
            if (!controleur.getModele().getClients().isEmpty()) {
                controleur.selectionnerClient(controleur.getModele().getClients().get(0));
            }
        } else {
            afficherCommandes(controleur.getClientSelectionne());
        }
        updateNbArticles();
    }

    @Override
    public void afficherClients(List<Client> clients) {
        observableListClients.addAll(clients.stream().map(Client::getNom).filter(nom -> !observableListClients.contains(nom)).collect(Collectors.toList()));
        if (controleur.getClientSelectionne() == null && !clients.isEmpty()) {
            listViewClients.getSelectionModel().select(0);
        }
    }

    @Override
    public void afficherCommandes(Client client) {
        if (client != null) {
            titleCommandes.setText("Commandes de " + client.getNom());
        }
        tableViewCommandes.getItems().clear();
        tableViewCommandes.getItems().addAll(client.getCommandes());
        tableViewCommandes.getSortOrder().add(tableViewCommandes.getColumns().get(1));
    }

    @Override
    public void setControleur(I_Controleur controleur) {
        this.controleur = controleur;
    }

    private void afficherMenuContextuel(double x, double y, Commande commande) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem actionPossible = null;

        switch (commande.getEtat()) {
            case EN_ATTENTE_DE_STOCK:
                if (controleur.getModele().getNbArticlesDispo() < commande.getNombreArticles()) {
                    return;
                }
                actionPossible = new MenuItem("Valider Stock");
                actionPossible.setOnAction(contextMenuEvent -> {
                    controleur.validerStock(commande);
                });
                break;
            case EN_COURS_DE_LIVRAISON:
                actionPossible = new MenuItem("Valider Livraison");
                actionPossible.setOnAction(contextMenuEvent -> {
                    controleur.validerLivraison(commande);
                });
                break;
            case EN_ATTENTE_DE_PAIEMENT:
                actionPossible = new MenuItem("Valider Paiement");
                actionPossible.setOnAction(contextMenuEvent -> {
                    controleur.validerPaiement(commande);
                });
                break;
            case FINALISEE:
                return;
        }

        contextMenu.getItems().add(actionPossible);
        contextMenu.show(tableViewCommandes, x, y);
    }
}