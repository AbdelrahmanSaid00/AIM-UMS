package com.ums.system;

/**
 * Launcher class to start the JavaFX application
 * This class is needed to avoid "JavaFX runtime components are missing" error
 * when running from IDE without proper VM arguments
 */
public class Launcher {
    public static void main(String[] args) {
        App.main(args);
    }
}

