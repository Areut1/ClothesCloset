import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.css.converter.StringConverter;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(endDatePicker.getValue().minusDays(1));

        vbox.getChildren().add(new Label("Start Date:"));
        vbox.getChildren().add(startDatePicker);
        vbox.getChildren().add(new Label("End Date:"));
        vbox.getChildren().add(endDatePicker);
        stage.show();

        LocalDate endDate = endDatePicker.getValue();
        String date = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();

        System.out.println(date);
    }
}