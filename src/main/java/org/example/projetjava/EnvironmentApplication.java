package org.example.projetjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EnvironmentApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("environment-view.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the controller and initialize the ViewModel
            EnvironmentController controller = loader.getController();
            EnvironmentViewModel viewModel = new EnvironmentViewModel(new EnvironmentModel());
            controller.setViewModel(viewModel);

            // Add listeners or other application-level logic here if needed

            // Set up the primary stage
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ecosysteme Simulation");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load the application.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}