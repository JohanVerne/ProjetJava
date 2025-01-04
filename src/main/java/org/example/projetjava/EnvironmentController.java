package org.example.projetjava;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class EnvironmentController {

    @FXML
    private BorderPane simulationPane;

    @FXML
    private Spinner<Integer> numberOfAnimals;

    @FXML
    private TextField animalName;

    @FXML
    private CheckBox carnivoreCheckbox;

    @FXML
    private Spinner<Double> energieInit;

    @FXML
    private Spinner<Integer> ageMax;

    @FXML
    private Spinner<Double> vitesse;

    @FXML
    private Spinner<Double> champDeVision;

    @FXML
    private Spinner<Double> rationEau;

    @FXML
    private Spinner<Double> rationNourriture;

    @FXML
    private Spinner<Double> coutChasse;

    @FXML
    private Spinner<Double> gainChasse;

    @FXML
    private Canvas canvas;

    @FXML
    private Button simulerJournee;

    @FXML
    private Button simulerEcosysteme;

    @FXML
    private Button startStop;

    @FXML
    private Button reset;

    @FXML
    private Button genererButton;

    @FXML
    private ColorPicker ColorSelector;

    @FXML
    private StackedAreaChart<Number, Number> stats;

    private EnvironmentViewModel viewModel;

    private Timeline simulationTimeline;
    private boolean isSimulationRunning = false;

    public void initialize() {
        // Set up Spinner value factories
        numberOfAnimals.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1));
        energieInit.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, 50, 5));
        ageMax.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10));
        vitesse.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 50, 10, 1));
        champDeVision.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 50, 5));
        rationEau.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 5, 1));
        rationNourriture.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 10, 1));
        coutChasse.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 20, 1));
        gainChasse.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 200, 50, 5));
        //ColorSelector.valueProperty().bindBidirectional(viewModel.animalColorProperty());
        //TODO : ne compile pas si cette ligne est décommentée !! A réparer (le chat est paumé lui aussi)
        //FIX : le meme code est en dessous dans setViewModel, maybe c'est bon du coup
        // Après test, ça marche bien donc on ne panique pas

        // Disable coutChasse and gainChasse when carnivoreCheckbox is unchecked
        coutChasse.disableProperty().bind(carnivoreCheckbox.selectedProperty().not());
        gainChasse.disableProperty().bind(carnivoreCheckbox.selectedProperty().not());

        // Placeholder actions for buttons
        simulerJournee.setOnAction(event -> simulateDay());
        simulerEcosysteme.setOnAction(event -> simulateEcosystem());
        startStop.setOnAction(event -> toggleSimulation());
        reset.setOnAction(event -> resetSimulation());
        genererButton.setOnAction(event -> onGenererButtonClick());

        System.out.println("Controller initialized.");


    }

    public void setViewModel(EnvironmentViewModel viewModel) {
        this.viewModel = viewModel;

        if (viewModel != null && ColorSelector != null) {
            System.out.println("ViewModel and ColorSelector are ready. Establishing bindings...");
            // Bind ColorSelector
            ColorSelector.valueProperty().bindBidirectional(viewModel.animalColorProperty());
            // Bind TextField
            animalName.textProperty().bindBidirectional(viewModel.animalNameProperty());
            //Bind Checkbox
            carnivoreCheckbox.selectedProperty().bindBidirectional(viewModel.isCarnivoreProperty());

            // Bind Spinners
            energieInit.getValueFactory().valueProperty().bindBidirectional(viewModel.animalEnergyProperty().asObject());
            // Debug log to confirm initial value
            System.out.println("Initial ViewModel Value - Energy: " + viewModel.animalEnergyProperty().get());

            ageMax.getValueFactory().valueProperty().bindBidirectional(viewModel.animalMaxAgeProperty().asObject());
            vitesse.getValueFactory().valueProperty().bindBidirectional(viewModel.animalSpeedProperty().asObject());
            champDeVision.getValueFactory().valueProperty().bindBidirectional(viewModel.animalVisionProperty().asObject());
            rationEau.getValueFactory().valueProperty().bindBidirectional(viewModel.animalWaterRationProperty().asObject());
            rationNourriture.getValueFactory().valueProperty().bindBidirectional(viewModel.animalFoodRationProperty().asObject());
            numberOfAnimals.getValueFactory().valueProperty().bindBidirectional(viewModel.numberOfAnimalsProperty().asObject());

            // Update canvas dimensions based on ecosystem size
            Ecosysteme.tailleCarte = viewModel.getTailleCarte(); // Ensure the static variable is set
            //canvas.setWidth(tailleCarte * 10); // Example: 10 pixels per cell
            //canvas.setHeight(tailleCarte * 10);
            System.out.println("Ecosysteme.tailleCarte: " + Ecosysteme.tailleCarte);

            addLargeResourceZones(5, 10, "Eau");

        } else {
            System.err.println("Cannot bind ColorSelector. Either ViewModel or ColorSelector is null.");
        }
    }



    private void simulateDay() {
        System.out.println("Simulating a day in the ecosystem.");
        viewModel.getEcosystem().simulerJournee();
        renderEcosystem();
    }

    private void simulateEcosystem() {
        if (simulationTimeline == null) {
            simulationTimeline = new Timeline(new KeyFrame(Duration.seconds(.8), event -> {
                System.out.println("Simulating a day in the ecosystem.");
                viewModel.getEcosystem().simulerJournee();
                renderEcosystem();

                // Example: Add a condition to stop if certain criteria are met
                if (viewModel.getEcosystem().getAnimaux().isEmpty()) {
                    System.out.println("Simulation stopped: No animals left in the ecosystem.");
                    stopSimulation();
                }
            }));
            simulationTimeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
        }

        startSimulation(); // Start simulation when this method is called
    }

    private void toggleSimulation() {
        if (isSimulationRunning) {
            stopSimulation();
        } else {
            startSimulation();
        }
    }

    private void startSimulation() {
        if (simulationTimeline != null) {
            simulationTimeline.play();
            isSimulationRunning = true;
            System.out.println("Simulation started.");
        }
    }

    private void stopSimulation() {
        if (simulationTimeline != null) {
            simulationTimeline.pause();
            isSimulationRunning = false;
            System.out.println("Simulation paused.");
        }
    }

    private void resetSimulation() {
        if (isSimulationRunning) {
            System.out.println("Cannot reset simulation while it is running. Pause the simulation first.");
            return;
        }

        System.out.println("Resetting simulation.");

        // Clear the ecosystem
        viewModel.getEcosystem().getAnimaux().clear();
        viewModel.getEcosystem().getZonesRessources().clear();

        // Optionally, reinitialize zones or animals
        viewModel.getEcosystem().ajouterZoneRessource(new ZoneRessource("Nourriture", 50, 50, 100));
        viewModel.getEcosystem().ajouterZoneRessource(new ZoneRessource("Eau", 20, 30, 80));

        // Re-render the ecosystem
        renderEcosystem();

        System.out.println("Simulation has been reset.");
    }

    private void renderEcosystem() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Calculate the square cell size based on the smaller canvas dimension
        double cellSize = Math.min(canvas.getWidth() / Ecosysteme.tailleCarte, canvas.getHeight() / Ecosysteme.tailleCarte);
        double xOffset = (canvas.getWidth() - (cellSize * Ecosysteme.tailleCarte)) / 2;
        double yOffset = (canvas.getHeight() - (cellSize * Ecosysteme.tailleCarte)) / 2;

        // Clear the canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw ground (green background)
        gc.setFill(Color.GREEN);
        gc.fillRect(xOffset, yOffset, cellSize * Ecosysteme.tailleCarte, cellSize * Ecosysteme.tailleCarte);

        // Draw zones of resources
        for (ZoneRessource zone : viewModel.getResourceZones()) {
            if ("Eau".equals(zone.getType())) {
                gc.setFill(Color.BLUE); // Blue for water
            } else if ("Nourriture".equals(zone.getType())) {
                gc.setFill(Color.BROWN); // Brown-red for food
            }

            // Draw the resource zone as a rectangle
            double x = xOffset + zone.getX() * cellSize;
            double y = yOffset + zone.getY() * cellSize;
            gc.fillRect(x, y, cellSize, cellSize);
        }

        // Draw animals
        for (Animal animal : viewModel.getAnimals()) {
            gc.setFill(animal.getColor());
            double x = xOffset + animal.getX() * cellSize;
            double y = yOffset + animal.getY() * cellSize;
            gc.fillRect(x, y, cellSize, cellSize); // Ensure square cells
        }
    }


    @FXML
    private void onGenererButtonClick() {
        // Manually sync spinner values to ViewModel
        viewModel.animalEnergyProperty().set(energieInit.getValue());
        viewModel.numberOfAnimalsProperty().set(numberOfAnimals.getValue());
        viewModel.animalMaxAgeProperty().set(ageMax.getValue());
        viewModel.animalSpeedProperty().set(vitesse.getValue());
        viewModel.animalVisionProperty().set(champDeVision.getValue());
        viewModel.animalWaterRationProperty().set(rationEau.getValue());
        viewModel.animalFoodRationProperty().set(rationNourriture.getValue());
        System.out.println("Générer button clicked!");

        // Debug spinner and property values
        System.out.println("Spinner Value - Energy: " + energieInit.getValue());
        System.out.println("ViewModel Value - Energy: " + viewModel.animalEnergyProperty().get());


        // Create animals
            viewModel.createAnimals();
            System.out.println("Animals created successfully!");
            System.out.println("Current Animals in the Ecosystem:");
            viewModel.getAnimals().forEach(animal -> {
                System.out.println(animal.toString());
            });
    }

    private void addLargeResourceZones(int numberOfZones, int maxZoneSize, String resourceType) {
        System.out.println("Adding large resource zones...");

        // Ensure valid resource type
        if (!"Eau".equals(resourceType) && !"Nourriture".equals(resourceType)) {
            System.err.println("Invalid resource type: " + resourceType);
            return;
        }

        for (int i = 0; i < numberOfZones; i++) {
            // Randomize zone size and position
            int zoneSize = (int) (Math.random() * maxZoneSize) + 1; // Size between 1 and maxZoneSize
            int x = (int) (Math.random() * Ecosysteme.tailleCarte);
            int y = (int) (Math.random() * Ecosysteme.tailleCarte);

            // Ensure zone stays within ecosystem boundaries
            int width = Math.min(zoneSize, Ecosysteme.tailleCarte - x);
            int height = Math.min(zoneSize, Ecosysteme.tailleCarte - y);

            // Add the resource zone
            double quantity = Math.random() * 200 + 100; // Random quantity between 100 and 300
            viewModel.getEcosystem().ajouterZoneRessource(new ZoneRessource(resourceType, x, y, quantity));

            System.out.println("Added " + resourceType + " zone at (" + x + ", " + y + ") with size (" + width + "x" + height + ") and quantity: " + quantity);
        }
    }
}

