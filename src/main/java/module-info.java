module com.example.ildeilc_gestion {
        requires javafx.controls;
        requires javafx.fxml;
        requires jdk.jfr;
        requires java.xml;

        opens com.example.ildeilc_gestion.model to javafx.base;

        exports com.example.ildeilc_gestion;
        exports com.example.ildeilc_gestion.view;
        opens com.example.ildeilc_gestion.view to javafx.fxml;
        }
