import java.time.LocalDate;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CalendarTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        VBox vbox = new VBox(20);
        Scene scene = new Scene(vbox, 400, 400);
        stage.setScene(scene);
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();

        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(startDatePicker.getValue().plusDays(1));

        vbox.getChildren().add(new Label("Start Date:"));
        vbox.getChildren().add(startDatePicker);
        vbox.getChildren().add(new Label("End Date:"));
        vbox.getChildren().add(endDatePicker);
        stage.show();
    }
}