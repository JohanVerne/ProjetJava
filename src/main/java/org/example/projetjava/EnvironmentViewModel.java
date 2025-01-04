package org.example.projetjava;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


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
    private final BooleanProperty isHuman;
    private final BooleanProperty isVegetal;
    private final DoubleProperty rationEau;
    private final DoubleProperty rationNourriture;
    private final IntegerProperty niveau; // Level (1-3)
    private final IntegerProperty intelligence; // Integer value
    private final DoubleProperty cooperation; // Cooperation value (double)
    private final DoubleProperty aggression; // Aggression value (double)
    private final BooleanProperty isCarnivore;
    private final DoubleProperty huntCost;
    private final DoubleProperty huntGain;
    private final ObjectProperty<javafx.scene.paint.Color> animalColor;

    private final Ecosysteme ecosystem;
    private final ObservableList<XYChart.Series<Number, Number>> speciesData;
    private final Map<String, Color> speciesColors; // Maps animalName to their colors

    private int timeStep; // Keeps track of the simulation time

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
        this.isCarnivore = new SimpleBooleanProperty(false); // Default to Herbivore
        this.huntCost = new SimpleDoubleProperty(20);
        this.huntGain = new SimpleDoubleProperty(50);
        this.isHuman = new SimpleBooleanProperty(false); // Default animal
        this.isVegetal = new SimpleBooleanProperty(false); // Default animal
        this.rationEau = new SimpleDoubleProperty(50);
        this.rationNourriture = new SimpleDoubleProperty(30);
        this.niveau = new SimpleIntegerProperty(1);
        this.intelligence = new SimpleIntegerProperty(100);
        this.cooperation = new SimpleDoubleProperty(0.5);
        this.aggression = new SimpleDoubleProperty(0.5);

        this.animalColor = new SimpleObjectProperty<>(javafx.scene.paint.Color.WHITE); // Default to white

        this.ecosystem = new Ecosysteme(100); // Initialize ecosystem with a default size
        //this.ecosystem.ajouterZoneRessource(new ZoneRessource("Nourriture", 50, 50, 100));
        //this.ecosystem.ajouterZoneRessource(new ZoneRessource("Eau", 20, 30, 80));

        // Initialize species data and color map
        this.speciesData = FXCollections.observableArrayList();
        this.speciesColors = new HashMap<>();
        this.timeStep = 0;


        // Populate speciesColors with initial species in the ecosystem
        ecosystem.getAnimaux().forEach(animal -> {
            String animalName = animal.getNom(); // Use animalName as the key
            speciesColors.putIfAbsent(animalName, animal.getColor());
        });

        // Create series for each species
        speciesColors.forEach((animalName, color) -> {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(animalName); // Use animalName for the chart series
            speciesData.add(series);
        });

    }

    public Map<String, Color> getSpeciesColors() {
        return speciesColors;
    }

    public ObservableList<XYChart.Series<Number, Number>> getSpeciesData() {
        return speciesData;
    }

    public void updateSpeciesData() {
        // Count the population of each species
        Map<String, Long> populationCount = ecosystem.getAnimaux().stream()
                .collect(Collectors.groupingBy(Animal::getSpecies, Collectors.counting()));

        // Add missing series for new species
        populationCount.keySet().forEach(speciesName -> {
            if (speciesData.stream().noneMatch(series -> series.getName().equals(speciesName))) {
                XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();
                newSeries.setName(speciesName);
                speciesData.add(newSeries);

                // Assign a color if the species is new
                ecosystem.getAnimaux().stream()
                        .filter(animal -> animal.getSpecies().equals(speciesName))
                        .findFirst()
                        .ifPresent(animal -> speciesColors.put(speciesName, animal.getColor()));
            }
        });

        // Update the chart data
        for (XYChart.Series<Number, Number> series : speciesData) {
            String speciesName = series.getName();
            long count = populationCount.getOrDefault(speciesName, 0L);
            series.getData().add(new XYChart.Data<>(timeStep, count));
        }

        // Increment the time step
        timeStep++;
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


    public BooleanProperty isCarnivoreProperty() {
        return isCarnivore;
    }

    public BooleanProperty isHumanProperty() {
        return isHuman;
    }

    public BooleanProperty isVegetalProperty() {
        return isVegetal;
    }

    public DoubleProperty huntCostProperty() {
        return huntCost;
    }

    public DoubleProperty huntGainProperty() {
        return huntGain;
    }

    public DoubleProperty animalWaterRationProperty() {
        return rationEau;
    }

    public DoubleProperty animalFoodRationProperty() {
        return rationNourriture;
    }

    public IntegerProperty niveauProperty() {
        return niveau;
    }

    public IntegerProperty intelligenceProperty() {
        return intelligence;
    }

    public DoubleProperty cooperationProperty() {
        return cooperation;
    }

    public DoubleProperty aggressionProperty() {
        return aggression;
    }

    public ObjectProperty<javafx.scene.paint.Color> animalColorProperty() {
        return animalColor;
    }


    public ObservableList<Animal> getAnimals() {
        return FXCollections.observableArrayList(ecosystem.getAnimaux());
    }

    public ObservableList<Vegetal> getVegetals() {
        return FXCollections.observableArrayList(ecosystem.getVegetaux());
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
        return animalName.get() != null && !animalName.get().isEmpty() &&
                animalEnergy.get() > 0 &&
                animalMaxAge.get() > 0 &&
                animalSpeed.get() > 0 &&
                animalVision.get() > 0 &&
                rationEau.get() > 0 &&
                rationNourriture.get() > 0 &&
                aggression.get() >= 0 && aggression.get() <= 1 &&
                cooperation.get() >= 0 && cooperation.get() <= 1;
    }

    // Creation method
    public void createAnimals() {
        if (validateAnimalInput()) {
            for (int i = 0; i < numberOfAnimals.get(); i++) {
                Animal animal;
                String speciesName = animalName.get(); // Use the species name
                Color assignedColor = animalColor.get(); // Retrieve selected color

                // Ensure each species has a unique color
                if (!speciesColors.containsKey(speciesName)) {
                    speciesColors.put(speciesName, assignedColor);
                }

                // Assign the color to the current animal
                Color animalColor = speciesColors.get(speciesName);
                if (!isVegetal.get()) {
                    if (isHuman.get()) {
                        animal = new Humain(
                                animalName.get() + " " + (i + 1),
                                Animal.sexealeatoire(),
                                animalName.get(),
                                animalEnergy.get(),
                                niveau.get(),
                                animalMaxAge.get(),
                                animalSpeed.get(),
                                animalVision.get(),
                                huntCost.get(),
                                huntGain.get(),
                                intelligence.get(),
                                cooperation.get(),
                                aggression.get(),
                                animalColor);
                    } else if (isCarnivore.get()) {
                        animal = new Predateur(
                                animalName.get() + " " + (i + 1),
                                Animal.sexealeatoire(), // Randomize sex
                                animalName.get(), // Species name
                                animalEnergy.get(),
                                niveau.get(),
                                animalMaxAge.get(),
                                animalSpeed.get(),
                                animalVision.get(),
                                huntCost.get(),
                                huntGain.get(),
                                animalColor
                        );
                    } else {
                        animal = new Herbivore(
                                animalName.get() + " " + (i + 1),
                                Animal.sexealeatoire(), // Randomize sex
                                animalName.get(), // Species name
                                animalEnergy.get(),
                                niveau.get(),
                                animalMaxAge.get(),
                                animalSpeed.get(),
                                animalVision.get(),
                                rationEau.get(),
                                rationNourriture.get(),
                                animalColor
                        );
                    }


                    // Set a random position for the animal
                    animal.setPosition(
                            (int) (Math.random() * Ecosysteme.tailleCarte),
                            (int) (Math.random() * Ecosysteme.tailleCarte)
                    );

                    // Add the animal to the ecosystem
                    ecosystem.ajouterAnimal(animal);
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid input for animal creation");
        }
    }

    public void createVegetal() {
        for (int i = 0; i < numberOfAnimals.get(); i++) {

            Vegetal vegetal = new Vegetal(
                    animalName.get() + " " + (i + 1),
                    animalEnergy.get(),
                    (int) (Math.random() * Ecosysteme.tailleCarte), // Random X position
                    (int) (Math.random() * Ecosysteme.tailleCarte),  // Random Y position
                    animalColor.get()
            );
            ecosystem.ajouterVegetal(vegetal);
        }
    }
}

