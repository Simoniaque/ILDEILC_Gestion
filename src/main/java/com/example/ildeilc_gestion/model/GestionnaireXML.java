package com.example.ildeilc_gestion.model;

import com.example.ildeilc_gestion.DateFR;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class GestionnaireXML {

    private Document document;

    public void chargerFichier(String cheminFichier) {
        try {
            File fichierXML = new File(cheminFichier);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(fichierXML);
            document.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sauvegarderFichier(String cheminFichier) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(cheminFichier));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modifierEtatCommande(int idCommande, String nouvelEtat) {
        chargerFichier("src/main/java/com/example/ildeilc_gestion/data/clients.xml");
        try {
            NodeList listeCommandes = document.getElementsByTagName("commande");
            Element commande = (Element) listeCommandes.item(idCommande - 1); // ID commence à 1, alors que l'index du NodeList commence à 0
            if (commande != null) {
                commande.setAttribute("etat", nouvelEtat);
                sauvegarderFichier("src/main/java/com/example/ildeilc_gestion/data/clients.xml"); // Sauvegarder le fichier après la modification
            } else {
                System.out.println("Commande introuvable pour l'ID spécifié.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ajouterBonDeLivraison(int idCommande, BonDeLivraison bonDeLivraison) {
        chargerFichier("src/main/java/com/example/ildeilc_gestion/data/clients.xml");
        try {
            NodeList listeCommandes = document.getElementsByTagName("commande");
            Element commande = (Element) listeCommandes.item(idCommande - 1); // ID commence à 1, alors que l'index du NodeList commence à 0

            if (commande != null) {
                Element bonDeLivraisonElement = document.createElement("bonDeLivraison");

                Element commandeId = document.createElement("commandeId");
                commandeId.appendChild(document.createTextNode(String.valueOf(bonDeLivraison.getNumCommande())));

                Element clientNom = document.createElement("clientNom");
                clientNom.appendChild(document.createTextNode(bonDeLivraison.getNomClient()));

                Element adresse = document.createElement("adresse");
                adresse.appendChild(document.createTextNode(bonDeLivraison.getAdresse()));

                Element date = document.createElement("date");
                date.appendChild(document.createTextNode(bonDeLivraison.getDate().toString()));

                bonDeLivraisonElement.appendChild(commandeId);
                bonDeLivraisonElement.appendChild(clientNom);
                bonDeLivraisonElement.appendChild(adresse);
                bonDeLivraisonElement.appendChild(date);

                commande.appendChild(bonDeLivraisonElement);

                sauvegarderFichier("src/main/java/com/example/ildeilc_gestion/data/clients.xml"); // Sauvegarder le fichier après la modification
            } else {
                System.out.println("Commande introuvable pour l'ID spécifié.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deserialiser(String cheminFichier, Modele modele) {
        try {
            File fichierXML = new File(cheminFichier);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(fichierXML);
            document.getDocumentElement().normalize();

            NodeList listeClients = document.getElementsByTagName("client");

            for (int i = 0; i < listeClients.getLength(); i++) {
                Node nodeClient = listeClients.item(i);

                if (nodeClient.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementClient = (Element) nodeClient;
                    String nomClient = elementClient.getAttribute("nom");
                    Client client = new Client(nomClient);

                    NodeList listeCommandes = elementClient.getElementsByTagName("commande");

                    for (int j = 0; j < listeCommandes.getLength(); j++) {
                        Node nodeCommande = listeCommandes.item(j);

                        if (nodeCommande.getNodeType() == Node.ELEMENT_NODE) {
                            Element elementCommande = (Element) nodeCommande;

                            int numCommande = Integer.parseInt(elementCommande.getAttribute("id"));
                            String[] dateElements = elementCommande.getAttribute("date").split("-");
                            String etatCommande = elementCommande.getAttribute("etat");
                            int nbArticles = Integer.parseInt(elementCommande.getAttribute("nbArticles"));

                            DateFR date = new DateFR(Integer.parseInt(dateElements[2]),
                                    Integer.parseInt(dateElements[1]),
                                    Integer.parseInt(dateElements[0]));
                            Commande commande = new Commande(numCommande, date, nbArticles);

                            switch (etatCommande) {
                                case "EN_COURS_DE_LIVRAISON":
                                    commande.validerStock();
                                    break;
                                case "EN_ATTENTE_DE_PAIEMENT":
                                    commande.validerLivraison();
                                    break;
                                case "FINALISEE":
                                    commande.validerPaiement();
                                    break;
                            }

                            client.ajouterCommande(commande);
                        }
                    }

                    modele.ajouterClient(client);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

