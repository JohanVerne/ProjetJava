module org.example.projetjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.projetjava to javafx.fxml;
    exports org.example.projetjava;
}