package org.example.projetjava;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.chart.LineChart;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;

public class EnvironmentController {

    @FXML
    private Spinner<Integer> numberOfAnimals;

    @FXML
    private TextField animalName;

    @FXML
    private CheckBox carnivoreCheckbox;

    @FXML
    private CheckBox humainCheckbox;

    @FXML
    private CheckBox planteCheckbox;

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
    private Spinner<Integer> niveauSpinner;

    @FXML
    private Spinner<Integer> intelligenceSpinner;

    @FXML
    private Spinner<Double> cooperationSpinner;

    @FXML
    private Spinner<Double> aggressionSpinner;

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
    private LineChart<Number, Number> stats;

    private EnvironmentViewModel viewModel;

    private Timeline simulationTimeline;
    private boolean isSimulationRunning = false;
    private int frameCounter = 0; // Keeps track of simulation frames
    private final List<Saison> seasons = List.of(
            new Saison("Été", 1.2, 1.0),       // Higher resource availability
            new Saison("Automne", 1.0, 1.0),   // Balanced
            new Saison("Hiver", 0.8, 0.7),     // Scarce resources
            new Saison("Printemps", 1.1, 1.2)  // Moderate increase
    );
    private int currentSeasonIndex = 0; // Tracks the current season

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
        niveauSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3, 1));
        intelligenceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200, 100));
        cooperationSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, 0.1));
        aggressionSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, 0.1));


        // Automatically check carnivoreCheckbox if humainCheckbox is selected
        humainCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            // Automatically tick carnivoreCheckbox
            carnivoreCheckbox.setSelected(newValue); // Automatically untick carnivoreCheckbox
        });

        // Disable planteCheckbox when humainCheckbox or carnivoreCheckbox is selected
        BooleanExpression isPlantDisabled = humainCheckbox.selectedProperty()
                .or(carnivoreCheckbox.selectedProperty());
        planteCheckbox.disableProperty().bind(isPlantDisabled);
//Disable rationEau and rationNourriture when carnivoreCheckbox is selected
        BooleanExpression rationsDisable = carnivoreCheckbox.selectedProperty().or(planteCheckbox.selectedProperty());
        rationEau.disableProperty().bind(rationsDisable);
        rationNourriture.disableProperty().bind(rationsDisable);

        // Disable all advanced fields when planteCheckbox is selected
        BooleanExpression isPlant = planteCheckbox.selectedProperty();
        ageMax.disableProperty().bind(isPlant);
        vitesse.disableProperty().bind(isPlant);
        champDeVision.disableProperty().bind(isPlant);
        coutChasse.disableProperty().bind(isPlant);
        gainChasse.disableProperty().bind(isPlant);
        niveauSpinner.disableProperty().bind(isPlant);
        intelligenceSpinner.disableProperty().bind(isPlant);
        cooperationSpinner.disableProperty().bind(isPlant);
        aggressionSpinner.disableProperty().bind(isPlant);
        humainCheckbox.disableProperty().bind(isPlant);
        carnivoreCheckbox.disableProperty().bind(isPlant);

        // Disable intelligence, cooperation, and aggression when humainCheckbox is unchecked
        BooleanExpression isNotHuman = humainCheckbox.selectedProperty().not();
        intelligenceSpinner.disableProperty().bind(isNotHuman);
        cooperationSpinner.disableProperty().bind(isNotHuman);
        aggressionSpinner.disableProperty().bind(isNotHuman);



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
            humainCheckbox.selectedProperty().bindBidirectional(viewModel.isHumanProperty());
            planteCheckbox.selectedProperty().bindBidirectional(viewModel.isVegetalProperty());

            // Bind the StackedAreaChart to the ViewModel data
            stats.setData(viewModel.getSpeciesData());

            // Set the series colors using CSS
            Map<String, Color> speciesColors = viewModel.getSpeciesColors();
            for (int i = 0; i < stats.getData().size(); i++) {
                XYChart.Series<Number, Number> series = stats.getData().get(i);
                String animalName = series.getName();
                Color color = speciesColors.getOrDefault(animalName, Color.GRAY); // Default to gray if not found

                // Apply CSS for line color
                String rgbColor = String.format("rgba(%d, %d, %d, 0.8)",
                        (int) (color.getRed() * 255),
                        (int) (color.getGreen() * 255),
                        (int) (color.getBlue() * 255));
                series.getNode().setStyle("-fx-stroke: " + rgbColor + "; -fx-fill: " + rgbColor + ";");
            }

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
            coutChasse.getValueFactory().valueProperty().bindBidirectional(viewModel.huntCostProperty().asObject());
            gainChasse.getValueFactory().valueProperty().bindBidirectional(viewModel.huntGainProperty().asObject());
            niveauSpinner.getValueFactory().valueProperty().bindBidirectional(viewModel.niveauProperty().asObject());
            intelligenceSpinner.getValueFactory().valueProperty().bindBidirectional(viewModel.intelligenceProperty().asObject());
            cooperationSpinner.getValueFactory().valueProperty().bindBidirectional(viewModel.cooperationProperty().asObject());
            aggressionSpinner.getValueFactory().valueProperty().bindBidirectional(viewModel.aggressionProperty().asObject());

            // Update canvas dimensions based on ecosystem size
            Ecosysteme.tailleCarte = viewModel.getTailleCarte(); // Ensure the static variable is set
            //canvas.setWidth(tailleCarte * 10); // Example: 10 pixels per cell
            //canvas.setHeight(tailleCarte * 10);
            System.out.println("Ecosysteme.tailleCarte: " + Ecosysteme.tailleCarte);

            addLargeResourceZones(5, 15, "Eau");
            addLargeResourceZones(15, 3, "Nourriture");

        } else {
            System.err.println("Cannot bind ColorSelector. Either ViewModel or ColorSelector is null.");
        }
    }



    private void simulateDay() {
        System.out.println("Simulating a day in the ecosystem.");
        viewModel.getEcosystem().simulerJournee();
        viewModel.updateSpeciesData(); // Update chart data
        renderEcosystem();
        frameCounter++;
        if (frameCounter % 3 == 0) {
            changeSeason();
        }
    }

    private void simulateEcosystem() {
        if (simulationTimeline == null) {
            simulationTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {

                simulateDay();

                // Stop simulation if all animals are dead
                if (viewModel.getEcosystem().getAnimaux().isEmpty()) {
                    System.out.println("Simulation stopped: No animals left in the ecosystem.");
                    stopSimulation();
                }
            }));
            simulationTimeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
        }

        startSimulation();
    }

    private void changeSeason() {
        // Cycle to the next season
        currentSeasonIndex = (currentSeasonIndex + 1) % seasons.size();
        Saison newSeason = seasons.get(currentSeasonIndex);

        // Apply the new season to the ecosystem
        viewModel.getEcosystem().changerSaison(newSeason);

        System.out.println("Season changed to: " + newSeason);
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

        // Reset frame counter
        frameCounter = 0;

        // Reapply the first season
        currentSeasonIndex = 0;
        Saison initialSeason = seasons.get(currentSeasonIndex);
        viewModel.getEcosystem().changerSaison(initialSeason);
        System.out.println("Season reset to: " + initialSeason);

        // Optionally, reinitialize zones or animals
        addLargeResourceZones(5, 15, "Eau");
        addLargeResourceZones(15, 3, "Nourriture");

        // Re-render the ecosystem
        renderEcosystem();

        System.out.println("Simulation has been reset.");
    }

    private void renderEcosystem() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Calculate square cell size
        double cellSize = Math.min(canvas.getWidth() / Ecosysteme.tailleCarte, canvas.getHeight() / Ecosysteme.tailleCarte);
        double xOffset = (canvas.getWidth() - (cellSize * Ecosysteme.tailleCarte)) / 2;
        double yOffset = (canvas.getHeight() - (cellSize * Ecosysteme.tailleCarte)) / 2;

        // Clear the canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Determine the background color based on the current season
        Saison currentSeason = seasons.get(currentSeasonIndex);
        Color backgroundColor = switch (currentSeason.getNom()) { // Use getNom instead of toString
            case "Hiver" -> Color.LIGHTGRAY;
            case "Printemps" -> Color.GREEN;
            case "Été" -> Color.PALEGOLDENROD; // Light brown
            case "Automne" -> Color.ORANGE;
            default -> Color.WHITE; // Default color
        };

        // Draw the background
        gc.setFill(backgroundColor);
        gc.fillRect(xOffset, yOffset, cellSize * Ecosysteme.tailleCarte, cellSize * Ecosysteme.tailleCarte);

        // Draw resource zones
        for (ZoneRessource zone : viewModel.getResourceZones()) {
            gc.setFill(zone.getType().equals("Eau") ? Color.BLUE : Color.BROWN);
            double x = xOffset + zone.getX() * cellSize;
            double y = yOffset + zone.getY() * cellSize;
            gc.fillRect(x, y, cellSize, cellSize);
        }

        // Draw animals
        for (Animal animal : viewModel.getAnimals()) {
            System.out.println("Rendering " + animal.getNom() + " with color: " + animal.getColor());
            gc.setFill(animal.getColor());
            double x = xOffset + animal.getX() * cellSize;
            double y = yOffset + animal.getY() * cellSize;
            gc.fillRect(x, y, cellSize, cellSize);
        }

        // Render vegetals
        for (Vegetal vegetal : viewModel.getVegetals()) {
            gc.setFill(vegetal.getColor());
            double x = xOffset + vegetal.getX() * cellSize;
            double y = yOffset + vegetal.getY() * cellSize;
            gc.fillRect(x, y, cellSize, cellSize); // Draw vegetals as squares
        }

    }



    @FXML
    private void onGenererButtonClick() {
        // Sync spinner values to ViewModel
        viewModel.animalEnergyProperty().set(energieInit.getValue());
        viewModel.numberOfAnimalsProperty().set(numberOfAnimals.getValue());
        viewModel.animalMaxAgeProperty().set(ageMax.getValue());
        viewModel.animalSpeedProperty().set(vitesse.getValue());
        viewModel.animalVisionProperty().set(champDeVision.getValue());
        viewModel.animalWaterRationProperty().set(rationEau.getValue());
        viewModel.animalFoodRationProperty().set(rationNourriture.getValue());
        viewModel.aggressionProperty().set(aggressionSpinner.getValue());
        viewModel.cooperationProperty().set(cooperationSpinner.getValue());
        viewModel.niveauProperty().set(niveauSpinner.getValue());
        viewModel.intelligenceProperty().set(intelligenceSpinner.getValue());



        System.out.println("Générer button clicked!");

        try {
            if (!viewModel.isVegetalProperty().getValue()){

                viewModel.createAnimals();
            }
            else{

                viewModel.createVegetal();
            }
            renderEcosystem();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private void addLargeResourceZones(int numberOfZones, int maxZoneSize, String resourceType) {
        System.out.println("Adding large resource zones...");

        // Ensure valid resource type
        if (!"Eau".equals(resourceType) && !"Nourriture".equals(resourceType)) {
            System.err.println("Invalid resource type: " + resourceType);
            return;
        }

        for (int i = 0; i < numberOfZones; i++) {
            // Randomize the center of the zone
            int centerX = (int) (Math.random() * Ecosysteme.tailleCarte);
            int centerY = (int) (Math.random() * Ecosysteme.tailleCarte);

            // Randomize the size of the zone
            int radius = (int) (Math.random() * maxZoneSize) + 1;

            // Iterate over a square bounding box around the center
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    // Calculate the absolute position
                    int x = centerX + dx;
                    int y = centerY + dy;

                    // Check bounds
                    if (x >= 0 && x < Ecosysteme.tailleCarte && y >= 0 && y < Ecosysteme.tailleCarte) {
                        // Check if this position is already occupied by a conflicting resource type
                        boolean conflict = viewModel.getResourceZones().stream()
                                .anyMatch(zone -> zone.getX() == x && zone.getY() == y && !zone.getType().equals(resourceType));

                        if (!conflict && Math.sqrt(dx * dx + dy * dy) <= radius) { // Circle boundary check
                            // Add this part of the resource zone
                            double quantity = Math.random() * 10 + 10; // Small quantity per tile
                            viewModel.getEcosystem().ajouterZoneRessource(new ZoneRessource(resourceType, x, y, quantity));
                        }
                    }
                }
            }

            System.out.println("Added " + resourceType + " zone with center (" + centerX + ", " + centerY + ") and radius: " + radius);
        }

    }


}

