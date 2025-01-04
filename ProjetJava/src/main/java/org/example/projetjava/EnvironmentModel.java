package org.example.projetjava;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EnvironmentModel {
    private StringProperty environmentName;

    public EnvironmentModel() {
        this.environmentName = new SimpleStringProperty("Environnement");
    }

    public StringProperty environmentNameProperty() {
        return environmentName;
    }

    public String getEnvironmentName() {
        return environmentName.get();
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName.set(environmentName);
    }


    // Add any other methods or properties related to the environment model
}