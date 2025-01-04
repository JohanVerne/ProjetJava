package org.example.projetjava;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.paint.Color;

public class EnvironmentViewModel {
    private final EnvironmentModel environmentModel;

    // Properties for the FXML controls
    private final StringProperty environmentName;
    private final IntegerProperty numberOfAnimals;
    private final StringProperty animalName;
    private final DoubleProperty animalEnergy;
    private final IntegerProperty animalMaxAge;
    private final DoubleProperty animalSpeed;
    private final DoubleProperty animalVision;
    private final DoubleProperty animalWaterRation;
    private final DoubleProperty animalFoodRation;
    private final BooleanProperty isCarnivore;
    private final DoubleProperty huntCost;
    private final DoubleProperty huntGain;
    private final ObjectProperty<javafx.scene.paint.Color> animalColor;

    private final Ecosysteme ecosystem;

    public EnvironmentViewModel(EnvironmentModel environmentModel) {
        this.environmentModel = environmentModel;

        // Bind environment name with model
        this.environmentName = new SimpleStringProperty();
        this.environmentName.bindBidirectional(environmentModel.environmentNameProperty());

        // Initialize properties
        this.numberOfAnimals = new SimpleIntegerProperty(1); // Default value
        this.animalName = new SimpleStringProperty();
        this.animalEnergy = new SimpleDoubleProperty(50);
        this.animalMaxAge = new SimpleIntegerProperty(10);
        this.animalSpeed = new SimpleDoubleProperty(10);
        this.animalVision = new SimpleDoubleProperty(50);
        this.animalWaterRation = new SimpleDoubleProperty(5);
        this.animalFoodRation = new SimpleDoubleProperty(10);
        this.isCarnivore = new SimpleBooleanProperty(false); // Default to Herbivore
        this.huntCost = new SimpleDoubleProperty(20);
        this.huntGain = new SimpleDoubleProperty(50);
        this.animalColor = new SimpleObjectProperty<>(javafx.scene.paint.Color.WHITE); // Default to white

        this.ecosystem = new Ecosysteme(100); // Initialize ecosystem with a default size
        //this.ecosystem.ajouterZoneRessource(new ZoneRessource("Nourriture", 50, 50, 100));
        //this.ecosystem.ajouterZoneRessource(new ZoneRessource("Eau", 20, 30, 80));

    }

    // Getters for the FXML properties
    public StringProperty environmentNameProperty() {
        return environmentName;
    }

    public IntegerProperty numberOfAnimalsProperty() {
        return numberOfAnimals;
    }

    public StringProperty animalNameProperty() {
        return animalName;
    }

    public DoubleProperty animalEnergyProperty() {
        return animalEnergy;
    }

    public IntegerProperty animalMaxAgeProperty() {
        return animalMaxAge;
    }

    public DoubleProperty animalSpeedProperty() {
        return animalSpeed;
    }

    public DoubleProperty animalVisionProperty() {
        return animalVision;
    }

    public DoubleProperty animalWaterRationProperty() {
        return animalWaterRation;
    }

    public DoubleProperty animalFoodRationProperty() {
        return animalFoodRation;
    }

    public BooleanProperty isCarnivoreProperty() {
        return isCarnivore;
    }

    public DoubleProperty huntCostProperty() {
        return huntCost;
    }

    public DoubleProperty huntGainProperty() {
        return huntGain;
    }

    public ObjectProperty<javafx.scene.paint.Color> animalColorProperty() {
        return animalColor;
    }


    public ObservableList<Animal> getAnimals() {
        return FXCollections.observableArrayList(ecosystem.getAnimaux());
    }

    public ObservableList<ZoneRessource> getResourceZones() {
        return FXCollections.observableArrayList(ecosystem.getZonesRessources());
    }

    public Ecosysteme getEcosystem() {
        return ecosystem;
    }

    public int getTailleCarte() {
        return Ecosysteme.tailleCarte;
    }


    // Validation method
    public boolean validateAnimalInput() {
        System.out.println("Validating input...");
        System.out.println("Animal Name: " + animalName.get());
        System.out.println("Animal Energy: " + animalEnergy.get());
        System.out.println("Animal Max Age: " + animalMaxAge.get());
        System.out.println("Animal Speed: " + animalSpeed.get());
        System.out.println("Animal Vision: " + animalVision.get());
        System.out.println("Animal Water Ration: " + animalWaterRation.get());
        System.out.println("Animal Food Ration: " + animalFoodRation.get());

        boolean isValid = animalName.get() != null && !animalName.get().isEmpty() &&
                animalEnergy.get() > 0 &&
                animalMaxAge.get() > 0 &&
                animalSpeed.get() > 0 &&
                animalVision.get() > 0 &&
                animalWaterRation.get() > 0 &&
                animalFoodRation.get() > 0;

        System.out.println("Validation result: " + isValid);
        return isValid;
    }

    // Creation method
    public void createAnimals() {
        if (validateAnimalInput()) {
            for (int i = 0; i < numberOfAnimals.get(); i++) {
                Animal animal;
                Color selectedColor = animalColor.get();
                if (isCarnivore.get()) {
                    animal = new Predateur(
                            animalName.get() + " " + (i + 1),
                            animalEnergy.get(),
                            animalMaxAge.get(),
                            animalSpeed.get(),
                            animalVision.get(),
                            animalWaterRation.get(),
                            animalFoodRation.get(),
                            huntCost.get(),
                            huntGain.get(),
                            selectedColor
                    );
                } else {
                    animal = new Herbivore(
                            animalName.get() + " " + (i + 1),
                            animalEnergy.get(),
                            animalMaxAge.get(),
                            animalSpeed.get(),
                            animalVision.get(),
                            animalWaterRation.get(),
                            animalFoodRation.get(),
                            selectedColor
                    );
                }
                // Assign a random position or specific logic for placement
                animal.setPosition(
                        (int) (Math.random() * Ecosysteme.tailleCarte),
                        (int) (Math.random() * Ecosysteme.tailleCarte)
                );
                // Add the animal to the observable list
                ecosystem.ajouterAnimal(animal);
            }
        } else {
            throw new IllegalArgumentException("Invalid input for animal creation");
        }
    }
}

