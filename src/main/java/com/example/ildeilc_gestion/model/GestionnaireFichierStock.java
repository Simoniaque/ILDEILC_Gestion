
package com.example.ildeilc_gestion.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GestionnaireFichierStock {

    public static int recupNbArticlesDispo() {
        int nbArticles = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/example/ildeilc_gestion/data/nbArticles.txt"))) {
            String line = reader.readLine();
            nbArticles = Integer.parseInt(line.trim());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return nbArticles;
    }

    public static void modifierNbArticlesDispo(int entier) {
        try (FileWriter writer = new FileWriter("src/main/java/com/example/ildeilc_gestion/data/nbArticles.txt", false)) {
            // false pour écraser le contenu existant
            writer.write(String.valueOf(entier));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
