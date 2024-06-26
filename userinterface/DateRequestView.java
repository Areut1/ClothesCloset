package userinterface;

// system imports

import exception.InvalidPrimaryKeyException;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Vector;

// project imports
import impresario.IModel;

//---------------------------------------------------------------
public class DateRequestView extends View {

    private Button subButton;
    private Button cancelButton;
    private DatePicker startDate;
    private DatePicker endDate;
    //Show error message
    private MessageView statusLog;

    //---------------------------------------------
    /*CONSTRUCTOR
     * Takes model object from ViewFactory
     */
    public DateRequestView(IModel clerk) {
        super(clerk, "DateRequestView");

        //Create container
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getStyleClass().add("Vbox");
        //Create GUI components, and add
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        //Error message area
        container.getChildren().add(createStatusLog(""));

        getChildren().add(container);

        populateFields();

        myModel.subscribe("dateRequestMessage", this);
        myModel.subscribe("UpdateStatusMessage", this);
    }//END CONSTRUCTOR--------------------------------

    //-------------------------------------------------
    /*createTitle
     * Create title field for the view
     */
    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Enter Date: ");
        titleText.getStyleClass().add("title");
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        container.getChildren().add(titleText);

        return container;

    }

    //End createTitle-------------------------------------
    //---------------------------------------------------
    /*createFormContent
     * Method creates actual form for user input
     */
    private VBox createFormContent() {
        VBox vbox = new VBox(10);

        //Establish grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //SearchLabel and Text Field------------
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);

        Text startDateLabel = new Text("Start Date: ");
        startDateLabel.setFont(myFont);
        startDateLabel.setWrappingWidth(150);
        startDateLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(startDateLabel, 0, 0);

        startDate = new DatePicker();
        startDate.getEditor().setDisable(true);
        startDate.setPromptText("YYYY-MM-DD");
        grid.add(startDate, 0, 1);

        Text endDateLabel = new Text("End Date: ");
        endDateLabel.setFont(myFont);
        endDateLabel.setWrappingWidth(150);
        endDateLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(endDateLabel, 1, 0);

        endDate = new DatePicker();
        endDate.getEditor().setDisable(true);
        endDate.setPromptText("YYYY-MM-DD");
        grid.add(endDate, 1, 1);

        //Setup separate hbox for submit and back buttons
        HBox subBack = new HBox(10);
        subBack.setAlignment(Pos.BOTTOM_CENTER);

        //Submit Button---------------------------------
        subButton = new Button("Submit");
        subButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        subButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                processSubAction(e);
            }
        });
        subBack.getChildren().add(subButton);

        //Back Button-----------------------------------
        cancelButton = new Button("Back");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                populateFields();
                try {
                    myModel.stateChangeRequest("CancelTransaction", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        subBack.getChildren().add(cancelButton);

        //Add form and buttons
        vbox.getChildren().add(grid);
        vbox.getChildren().add(subBack);

        return vbox;

    }

    //End createFormContent----------------------------
    //----------------------------------------------------
    /*processSubAction
     */
    public void processSubAction(Event evt) {

        LocalDate startDateDate = startDate.getValue();
        LocalDate endDateDate = endDate.getValue();

        if (startDateDate.compareTo(endDateDate) <= 0) {
            String startDateEntered = startDateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
            String endDateEntered = endDateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();

            //Validate user input

            Properties props = new Properties();
            props.setProperty("startDate", startDateEntered);
            props.setProperty("endDate", endDateEntered);

            populateFields();

            try {
                myModel.stateChangeRequest("DateRequest", props);
            } catch (InvalidPrimaryKeyException e) {
                throw new RuntimeException(e);
            }

        } else {
            clearErrorMessage();
            displayErrorMessage("Start date must be before end date!");
        }

    }//End processSubAction------------------------------

    // Create the status log field
    //-------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    public void populateFields() {
        //If test does not work change to match searchBook or searchPatron for this method
        //Intended result clears fields after search is done
        endDate.setValue(LocalDate.now());
        startDate.setValue(endDate.getValue().minusDays(365));
    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value) {
        clearErrorMessage();
        String temp = ((String) value);

        if (key.equals("dateRequestMessage") == true) {
            String val = (String) value;
            displayMessage(val);
        }
    }

    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message) {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Display info message
     */
    //----------------------------------------------------------
    public void displayMessage(String message) {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage() {
        statusLog.clearErrorMessage();
    }
}//END CLASS===============================================