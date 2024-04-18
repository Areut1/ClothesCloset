package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Properties;

//---------------------------------------------------------------
public class ReceiverInfoInputView extends View {
    // GUI components
    protected TextField receiverNetId;
    protected TextField receiverLastName;
    protected TextField receiverFirstName;
    protected Button cancelButton;
    protected Button submitButton;
    // For showing error message
    protected MessageView statusLog;
    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ReceiverInfoInputView(IModel clerk)
    {
        super(clerk, "ReceiverInfoInputView");

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

        myModel.subscribe("inventoryMessage", this);
        myModel.subscribe("UpdateStatusMessage", this);
    }
    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Customer Information Input ");
        titleText.getStyleClass().add("title");
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        container.getChildren().add(titleText);

        // Add white space to follow design spec
        container.getChildren().add(new Label(" "));

        //Add Article Type page title---------------------------------------
        Text prompt = new Text("Enter Additional Information:");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        container.getChildren().add(prompt);

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

        //Inventory Size Label and Text Field-----------------------------
        Text receiverNetIdLabel = new Text(" Receiver NetId : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        receiverNetIdLabel.setFont(myFont);
        receiverNetIdLabel.setWrappingWidth(150);
        receiverNetIdLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(receiverNetIdLabel, 0, 0);

        receiverNetId = new TextField();
        receiverNetId.setEditable(true);
        grid.add(receiverNetId, 1, 0);

        //Inventory Color 2 Label and Text Field-----------------------------
        Text receiverLastNameLabel = new Text(" Receiver LastName : ");
        receiverLastNameLabel.setFont(myFont);
        receiverLastNameLabel.setWrappingWidth(150);
        receiverLastNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(receiverLastNameLabel, 0, 1);

        receiverLastName = new TextField();
        receiverLastName.setEditable(true);
        grid.add(receiverLastName, 1, 1);

        //Inventory Brand Name Label and Text Field------------------------
        Text receiverFirstNameLabel = new Text(" Receiver FirstName : ");
        receiverFirstNameLabel.setFont(myFont);
        receiverFirstNameLabel.setWrappingWidth(150);
        receiverFirstNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(receiverFirstNameLabel, 0, 2);

        receiverFirstName = new TextField();
        receiverFirstName.setEditable(true);
        grid.add(receiverFirstName, 1, 2);

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
                myModel.stateChangeRequest("CancelTransaction", null);
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
     * Passes the properties object to a newly created Article Type.
     * Calls the Article Type's constructor and insert method.
     */
    public void processSubmitAction(Event evt){
        //validate user input
        if (receiverNetId.getText() == null || receiverLastName.getText() == null || receiverFirstName.getText() == null) {
            clearErrorMessage();
            displayErrorMessage("Please completely fill in all fields");
        } else {

            // Convert properties to string
            String receiverNetIdString = receiverNetId.getText();
            String receiverLastNameString = receiverLastName.getText();
            String receiverFirstNameString = receiverFirstName.getText();

            //Create properties and keys
            Properties insertProp = new Properties();
            insertProp.setProperty("receiverNetId", receiverNetIdString);
            insertProp.setProperty("receiverLastName", receiverLastNameString);
            insertProp.setProperty("receiverFirstName", receiverFirstNameString);

            //Call Librarian method to create and save book
            myModel.stateChangeRequest("CustomerInfoInput", insertProp);

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
        receiverNetId.setText("");
        receiverLastName.setText("");
        receiverFirstName.setText("");
    }
    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        clearErrorMessage();
        String temp = ((String)value);

        if (key.equals("inventoryMessage"))
        {
            String val = (String)value;
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