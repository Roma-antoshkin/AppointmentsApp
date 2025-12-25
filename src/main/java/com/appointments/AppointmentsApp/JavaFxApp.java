package com.appointments.AppointmentsApp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxApp extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(AppointmentsAppApplication.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        var url = getClass().getResource("/com/appointments/AppointmentsApp/view/main.fxml");
        System.out.println(url);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/appointments/AppointmentsApp/view/main.fxml")
        );

        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("Учет записей клиентов");
        stage.setHeight(700);
        stage.setWidth(1100);
        stage.show();
    }

    @Override
    public void stop() {
        context.close();
        Platform.exit();
    }
}
