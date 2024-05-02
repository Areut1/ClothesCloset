package userinterface;

import exception.InvalidPrimaryKeyException;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
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

import java.util.Properties;
import impresario.IModel;
//---------------------------------------------------------------
public class ModifyColorView extends View {
    // GUI components
    protected TextField description;
    protected TextField barcodePrefix;
    protected TextField alphaCode;
    protected Button cancelButton;
    protected Button submitButton;
    // For showing error message
    protected MessageView statusLog;
    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ModifyColorView(IModel clerk)
    {
        super(clerk, "ModifyColorView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getStyleClass().add("Vbox");
        // Add a title for this panel
        container.getChildren().add(createTitle());

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContent());

        container.getChildren().add(createStatusLog("             "));

        getChildren().add(container);

        populateFields();

        myModel.subscribe("colorMessage", this);
        myModel.subscribe("UpdateStatusMessage", this);
    }
    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Modify Color ");
        titleText.getStyleClass().add("title");
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        container.getChildren().add(titleText);

        return container;
    }
    // Create the main form content
    //-------------------------------------------------------------
    private VBox createFormContent()
    {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //Add Color page title---------------------------------------
        Text prompt = new Text("MODIFY COLOR INFO");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        //Color Description Label and Text Field-----------------------------
        Text colorDescriptionLabel = new Text(" Description : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        colorDescriptionLabel.setFont(myFont);
        colorDescriptionLabel.setWrappingWidth(150);
        colorDescriptionLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(colorDescriptionLabel, 0, 1);

        description = new TextField();
        description.setEditable(true);
        grid.add(description, 1, 1);

        //Color Barcode Prefix Title Label and Text Field-----------------------------
        Text barcodePrefixLabel = new Text(" Barcode Prefix : ");
        barcodePrefixLabel.setFont(myFont);
        barcodePrefixLabel.setWrappingWidth(150);
        barcodePrefixLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(barcodePrefixLabel, 0, 2);

        barcodePrefix = new TextField();
        barcodePrefix.setEditable(true);
        grid.add(barcodePrefix, 1, 2);

        //Color Alpha Code Label and Text Field------------------------
        Text alphaCodeLabel = new Text(" Alpha Code : ");
        alphaCodeLabel.setFont(myFont);
        alphaCodeLabel.setWrappingWidth(150);
        alphaCodeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(alphaCodeLabel, 0, 3);

        alphaCode = new TextField();
        alphaCode.setEditable(true);
        grid.add(alphaCode, 1, 3);

        //Setup separate hbox for submit and back buttons
        HBox submitCancel = new HBox(10);
        submitCancel.setAlignment(Pos.BOTTOM_CENTER);

        //Submit Button---------------------------------
        submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                processSubmitAction(e);
            }
        });
        submitCancel.getChildren().add(submitButton);

        //Back Button-----------------------------------
        cancelButton = new Button("Cancel");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                try {
                    myModel.stateChangeRequest("CancelTransaction", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        submitCancel.getChildren().add(cancelButton);

        //Add form and buttons
        vbox.getChildren().add(grid);
        vbox.getChildren().add(submitCancel);

        return vbox;
    }
    //--------------------------------------------------------------------------------------------
    /*processAction
     * On submit button click, method will set up properties object with values taken from
     * form.
     * Passes the properties object to a newly created Color.
     * Calls the Color's constructor and insert method.
     */
    public void processSubmitAction(Event evt){
        //validate user input
        if (description == null || barcodePrefix == null || alphaCode == null){
            clearErrorMessage();
            displayErrorMessage("Please completly fill in all fields");
        }
        else if (description.getText().isBlank() || description.getText().isEmpty()){
            clearErrorMessage();
            displayErrorMessage("Please enter a valid description");
        }
        else if (barcodePrefix.getText().isEmpty() || barcodePrefix.getText().isBlank()
                || Integer.valueOf(barcodePrefix.getText()) < 1){
            clearErrorMessage();
            displayErrorMessage("Please enter a valid barcode prefix");
        }
        else if (alphaCode.getText().isBlank() || alphaCode.getText().isEmpty()){
            clearErrorMessage();
            displayErrorMessage("Please enter a valid alpha code");
        }
        else {

            //Convert properties to string
            String descriptionString = description.getText();
            String barcodePrefixString = barcodePrefix.getText();
            String alphaCodeString = alphaCode.getText();

            //Create properties and keys
            Properties insertProp = new Properties();
            insertProp.setProperty("description", descriptionString);
            insertProp.setProperty("barcodePrefix", barcodePrefixString);
            insertProp.setProperty("alphaCode", alphaCodeString);

            //Call Librarian method to create and save book
            try {
                myModel.stateChangeRequest("ModifyColor", insertProp);
            } catch (InvalidPrimaryKeyException e) {
                throw new RuntimeException(e);
            }

            //Print confirmation
            displayMessage("Color was updated!");
        }
    }
    // Create the status log field
    //-------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }
    //-------------------------------------------------------------
    public void populateFields()
    {
        model.Color c = (model.Color)myModel.getState("Color");
        description.setText((String)c.getValue("description"));
        barcodePrefix.setText((String)c.getValue("barcodePrefix"));
        alphaCode.setText((String)c.getValue("alphaCode"));
    }
    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        clearErrorMessage();
        String temp = ((String)value);

        if (key.equals("colorMessage") == true)
        {
            String val = (String)value;
            //serviceCharge.setText(val);
            displayMessage(val);
        }
    }
    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Display info message
     */
    //----------------------------------------------------------
    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }
}
