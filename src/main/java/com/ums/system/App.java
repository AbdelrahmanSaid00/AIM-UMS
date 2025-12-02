package com.ums.system;

import com.ums.system.util.ServiceLocator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * App - Main JavaFX Application Entry Point
 * Launches the University Management System GUI
 * Loads the login screen first
 */
public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        try {
            primaryStage = stage;

            // Initialize ServiceLocator (loads all services)
            ServiceLocator.getInstance();

            // Load login screen
            showLoginScreen();

            // Configure primary stage
            primaryStage.setTitle("University Management System");
            primaryStage.setResizable(true);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);

            // Handle application close
            primaryStage.setOnCloseRequest(event -> {
                ServiceLocator.getInstance().shutdown();
            });

            primaryStage.show();

            System.out.println("JavaFX Application started successfully");

        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Show login screen
     */
    public static void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/view/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 400, 500);
            primaryStage.setScene(scene);
            primaryStage.setTitle("UMS - Login");
        } catch (Exception e) {
            System.err.println("Error loading login screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void stop() {
        ServiceLocator.getInstance().shutdown();
        System.out.println("Application stopped");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

