package userinterface;

import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
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
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import impresario.IModel;
//---------------------------------------------------------------
public class AddInventoryInputView extends View {
    // GUI components
    protected TextField size;
    protected TextField color2;
    protected TextField brand;
    protected TextField notes;
    protected TextField donorFirstName;
    protected TextField donorLastName;
    protected TextField donorPhone;
    protected TextField donorEmail;
    protected Button cancelButton;
    protected Button submitButton;
    // For showing error message
    protected MessageView statusLog;
    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public AddInventoryInputView(IModel clerk)
    {
        super(clerk, "AddInventoryInputView");

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

        Text titleText = new Text(" Add Inventory Input ");
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
        Text sizeLabel = new Text(" Size : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        sizeLabel.setFont(myFont);
        sizeLabel.setWrappingWidth(150);
        sizeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(sizeLabel, 0, 0);

        size = new TextField();
        size.setEditable(true);
        grid.add(size, 1, 0);

        //Inventory Color 2 Label and Text Field-----------------------------
        Text color2Label = new Text(" Color 2 : ");
        color2Label.setFont(myFont);
        color2Label.setWrappingWidth(150);
        color2Label.setTextAlignment(TextAlignment.RIGHT);
        grid.add(color2Label, 0, 1);

        color2 = new TextField();
        color2.setEditable(true);
        grid.add(color2, 1, 1);

        //Inventory Brand Name Label and Text Field------------------------
        Text brandLabel = new Text(" Brand Name : ");
        brandLabel.setFont(myFont);
        brandLabel.setWrappingWidth(150);
        brandLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(brandLabel, 0, 2);

        brand = new TextField();
        brand.setEditable(true);
        grid.add(brand, 1, 2);

        //Inventory Notes Label and Text Field------------------------
        Text notesLabel = new Text(" Notes : ");
        notesLabel.setFont(myFont);
        notesLabel.setWrappingWidth(150);
        notesLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(notesLabel, 0, 3);

        notes = new TextField();
        notes.setEditable(true);
        grid.add(notes, 1, 3);

        //Inventory Donor First Name Label and Text Field------------------------
        Text donorFirstNameLabel = new Text(" Donor First Name : ");
        donorFirstNameLabel.setFont(myFont);
        donorFirstNameLabel.setWrappingWidth(150);
        donorFirstNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(donorFirstNameLabel, 0, 4);

        donorFirstName = new TextField();
        donorFirstName.setEditable(true);
        grid.add(donorFirstName, 1, 4);

        //Inventory Donor Last Name Label and Text Field------------------------
        Text donorLastNameLabel = new Text(" Donor Last Name : ");
        donorLastNameLabel.setFont(myFont);
        donorLastNameLabel.setWrappingWidth(150);
        donorLastNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(donorLastNameLabel, 0, 5);

        donorLastName = new TextField();
        donorLastName.setEditable(true);
        grid.add(donorLastName, 1, 5);

        //Inventory Brand Name Label and Text Field------------------------
        Text donorPhoneLabel = new Text(" Donor Phone Number : ");
        donorPhoneLabel.setFont(myFont);
        donorPhoneLabel.setWrappingWidth(150);
        donorPhoneLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(donorPhoneLabel, 0, 6);

        donorPhone = new TextField();
        donorPhone.setEditable(true);
        grid.add(donorPhone, 1, 6);

        //Inventory Brand Name Label and Text Field------------------------
        Text donorEmailLabel = new Text(" Donor Email : ");
        donorEmailLabel.setFont(myFont);
        donorEmailLabel.setWrappingWidth(150);
        donorEmailLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(donorEmailLabel, 0, 7);

        donorEmail = new TextField();
        donorEmail.setEditable(true);
        grid.add(donorEmail, 1, 7);

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
        if (size.getText() == null || color2.getText() == null || brand.getText() == null ||
        notes.getText() == null || donorFirstName.getText() == null || donorLastName.getText() == null ||
        donorPhone.getText() == null || donorEmail.getText() == null){
            clearErrorMessage();
            displayErrorMessage("Please completely fill in all fields");
        } else if (size.getText().length() > 2) {
            clearErrorMessage();
            displayErrorMessage("Error: Size must be less than 2 characters.");
        } else if (color2.getText().length() > 2 || !color2.getText().matches("[0-9]+")) {
            // Checks it's not greater than 3 digits and that it is only digits i.e. no letters.
            clearErrorMessage();
            displayErrorMessage("Error: Color2 must be a number less than 100.");
        } else {
            // Convert properties to string
            String sizeString = size.getText();
            String color2String = color2.getText();
            String brandString = brand.getText();
            String notesString = notes.getText();
            String donorFirstNameString = donorFirstName.getText();
            String donorLastNameString = donorLastName.getText();
            String donorPhoneString = donorPhone.getText();
            String donorEmailString = donorEmail.getText();

            String receivedDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

            //Create properties and keys
            Properties insertProp = new Properties();
            insertProp.setProperty("size", sizeString);
            insertProp.setProperty("color2", color2String);
            insertProp.setProperty("brand", brandString);
            insertProp.setProperty("notes", notesString);
            insertProp.setProperty("donorFirstName", donorFirstNameString);
            insertProp.setProperty("donorLastName", donorLastNameString);
            insertProp.setProperty("donorPhone", donorPhoneString);
            insertProp.setProperty("donorEmail", donorEmailString);
            insertProp.setProperty("dateDonated", receivedDate);

            //Call Librarian method to create and save book
            myModel.stateChangeRequest("AddInventory", insertProp);

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
        size.setText("");
        color2.setText("");
        brand.setText("");
        notes.setText("");
        donorFirstName.setText("");
        donorLastName.setText("");
        donorPhone.setText("");
        donorEmail.setText("");
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