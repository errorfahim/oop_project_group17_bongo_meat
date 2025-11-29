package com.group17.oop_project_group17_bongo_meat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //System.out.println(HelloApplication.class.getResource("/com/group17/oop_project_group17_bongo_meat/Zainab/LogisticManager/logisticManagerDashboard.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("BongoMeat");
        stage.setScene(scene);
    }
}
