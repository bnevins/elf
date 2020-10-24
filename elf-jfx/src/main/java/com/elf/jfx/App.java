package com.elf.jfx;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        var nameLabel = new Label("Enter your name, Holmes");
        var nameField = new TextField();
        var msg = new Label();
        msg.setStyle("-fx-text-fill: blue;");
        var sayHelloButton = new Button("Say Hello");
        var exitButton = new Button("Exit");
        sayHelloButton.setOnAction(e -> msg.setText("Hello " + nameField.getText()));
        exitButton.setOnAction(e -> Platform.exit());
        var root = new VBox();
        root.setSpacing(5);
        root.getChildren().addAll(nameLabel, nameField, msg, sayHelloButton, exitButton);
        var scene = new Scene(root, 350, 150);
        stage.setScene(scene);
        stage.setTitle("Yo, Dude!!!");
        stage.show();
    }

    public void startxx(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();
        var exitButton = new Button("exit");
        exitButton.setOnAction(e -> Platform.exit());
        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var scene = new Scene(new StackPane(label, exitButton), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
