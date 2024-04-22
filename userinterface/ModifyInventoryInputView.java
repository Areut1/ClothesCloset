package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import model.Inventory;

import java.util.Properties;

public class ModifyInventoryInputView extends View {
    // GUI components
    private TextField gender;
    private TextField size;
    private TextField articleType;
    private TextField color1;
    private TextField color2;
    private TextField brand;
    private TextField notes;
    private TextField donorLastName;
    private TextField donorFirstName;
    private TextField donorPhone;
    private TextField donorEmail;
    private TextField receiverNetId;
    private TextField receiverLastName;
    private TextField receiverFirstName;
    private TextField dateDonated;
    private TextField dateTaken;
    protected Button cancelButton;
    protected Button submitButton;
    protected Inventory i = (Inventory)myModel.getState("Inventory");

    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ModifyInventoryInputView(IModel clerk)
    {
        super(clerk, "ModifyInventoryInputView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

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
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Modify Inventory ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
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
        Text prompt = new Text("MODIFY INVENTORY INFO");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);

        Text genderLabel = new Text(" Gender : ");
        genderLabel.setFont(myFont);
        genderLabel.setWrappingWidth(150);
        genderLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(genderLabel, 0, 1);

        gender = new TextField();
        gender.setEditable(true);
        grid.add(gender, 1, 1);

        Text sizeLabel = new Text(" Size : ");
        sizeLabel.setFont(myFont);
        sizeLabel.setWrappingWidth(150);
        sizeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(sizeLabel, 0, 2);

        size = new TextField();
        size.setEditable(true);
        grid.add(size, 1, 2);

        Text articleTypeLabel = new Text(" Article Type : ");
        articleTypeLabel.setFont(myFont);
        articleTypeLabel.setWrappingWidth(150);
        articleTypeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(articleTypeLabel, 0, 3);

        articleType = new TextField();
        articleType.setEditable(true);
        grid.add(articleType, 1, 3);

        Text color1Label = new Text(" Color 1 : ");
        color1Label.setFont(myFont);
        color1Label.setWrappingWidth(150);
        color1Label.setTextAlignment(TextAlignment.RIGHT);
        grid.add(color1Label, 0, 4);

        color1 = new TextField();
        color1.setEditable(true);
        grid.add(color1, 1, 4);

        Text color2Label = new Text(" Color 2 : ");
        color2Label.setFont(myFont);
        color2Label.setWrappingWidth(150);
        color2Label.setTextAlignment(TextAlignment.RIGHT);
        grid.add(color2Label, 0, 5);

        color2 = new TextField();
        color2.setEditable(true);
        grid.add(color2, 1, 5);

        Text brandLabel = new Text(" Brand : ");
        brandLabel.setFont(myFont);
        brandLabel.setWrappingWidth(150);
        brandLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(brandLabel, 0, 6);

        brand = new TextField();
        brand.setEditable(true);
        grid.add(brand, 1, 6);

        Text notesLabel = new Text(" Notes : ");
        notesLabel.setFont(myFont);
        notesLabel.setWrappingWidth(150);
        notesLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(notesLabel, 0, 7);

        notes = new TextField();
        notes.setEditable(true);
        grid.add(notes, 1, 7);

        Text donorLastNameLabel = new Text(" Donor LastName : ");
        donorLastNameLabel.setFont(myFont);
        donorLastNameLabel.setWrappingWidth(150);
        donorLastNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(donorLastNameLabel, 0, 8);

        donorLastName = new TextField();
        donorLastName.setEditable(true);
        grid.add(donorLastName, 1, 8);

        Text donorFirstNameLabel = new Text(" Donor FirstName : ");
        donorFirstNameLabel.setFont(myFont);
        donorFirstNameLabel.setWrappingWidth(150);
        donorFirstNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(donorFirstNameLabel, 0, 9);

        donorFirstName = new TextField();
        donorFirstName.setEditable(true);
        grid.add(donorFirstName, 1, 9);

        Text donorPhoneLabel = new Text(" Donor Phone : ");
        donorPhoneLabel.setFont(myFont);
        donorPhoneLabel.setWrappingWidth(150);
        donorPhoneLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(donorPhoneLabel, 0, 10);

        donorPhone = new TextField();
        donorPhone.setEditable(true);
        grid.add(donorPhone, 1, 10);

        Text donorEmailLabel = new Text(" Donor Email : ");
        donorEmailLabel.setFont(myFont);
        donorEmailLabel.setWrappingWidth(150);
        donorEmailLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(donorEmailLabel, 0, 11);

        donorEmail = new TextField();
        donorEmail.setEditable(true);
        grid.add(donorEmail, 1, 11);

        Text receiverNetIdLabel = new Text(" Receiver NetId : ");
        receiverNetIdLabel.setFont(myFont);
        receiverNetIdLabel.setWrappingWidth(150);
        receiverNetIdLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(receiverNetIdLabel, 0, 12);

        receiverNetId = new TextField();
        receiverNetId.setEditable(true);
        grid.add(receiverNetId, 1, 12);

        Text receiverLastNameLabel = new Text(" Receiver LastName : ");
        receiverLastNameLabel.setFont(myFont);
        receiverLastNameLabel.setWrappingWidth(150);
        receiverLastNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(receiverLastNameLabel, 0, 13);

        receiverLastName = new TextField();
        receiverLastName.setEditable(true);
        grid.add(receiverLastName, 1, 13);

        Text receiverFirstNameLabel = new Text(" Receiver FirstName : ");
        receiverFirstNameLabel.setFont(myFont);
        receiverFirstNameLabel.setWrappingWidth(150);
        receiverFirstNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(receiverFirstNameLabel, 0, 14);

        receiverFirstName = new TextField();
        receiverFirstName.setEditable(true);
        grid.add(receiverFirstName, 1, 14);

        Text dateDonatedLabel = new Text(" Date Donated : ");
        dateDonatedLabel.setFont(myFont);
        dateDonatedLabel.setWrappingWidth(150);
        dateDonatedLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(dateDonatedLabel, 0, 15);

        dateDonated = new TextField();
        dateDonated.setEditable(true);
        grid.add(dateDonated, 1, 15);

        Text dateTakenLabel = new Text(" Date Taken : ");
        dateTakenLabel.setFont(myFont);
        dateTakenLabel.setWrappingWidth(150);
        dateTakenLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(dateTakenLabel, 0, 16);

        dateTaken = new TextField();
        dateTaken.setEditable(true);
        grid.add(dateTaken, 1, 16);


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
     * Passes the properties object to a newly created Color.
     * Calls the Color's constructor and insert method.
     */
    public void processSubmitAction(Event evt) {
        //validate user input
        if (gender.getText() == null || size.getText() == null || articleType.getText() == null ||
            color1.getText() == null || color2.getText() == null || brand.getText() == null ||
            donorLastName.getText() == null || donorFirstName.getText() == null ||
            donorPhone.getText() == null || donorEmail.getText() == null || receiverNetId.getText() == null ||
            receiverFirstName.getText() == null || receiverLastName.getText() == null || dateDonated.getText() == null ||
            dateTaken.getText() == null) {
            clearErrorMessage();
            displayErrorMessage("Please completly fill in all fields");
        } else {
            //Convert properties to string
            String genderString = gender.getText();
            String sizeString = size.getText();
            String articleTypeString = articleType.getText();
            String color1String = color1.getText();
            String color2String = color2.getText();
            String brandString = brand.getText();
            String notesString = notes.getText();
            String donorLastNameString = donorLastName.getText();
            String donorFirstNameString = donorFirstName.getText();
            String donorPhoneString = donorPhone.getText();
            String donorEmailString = donorEmail.getText();
            String receiverNetIdString = receiverNetId.getText();
            String receiverLastNameString = receiverLastName.getText();
            String receiverFirstNameString = receiverFirstName.getText();
            String dateDonatedString = dateDonated.getText();
            String dateTakenString = dateTaken.getText();

            //Create properties and keys
            Properties insertProp = new Properties();

            String barcodeString52 = genderString + articleTypeString + color1String;
            String barcodeString2;

            String barcodeOld5 = i.oldBarcode.substring(0,6);

            if (!barcodeString52.equals(barcodeOld5)){
                myModel.stateChangeRequest("GetID", barcodeString52);
                barcodeString2 = barcodeString52 + myModel.getState("ID");
            }
            else{
                barcodeString2 = i.oldBarcode;
            }

            insertProp.setProperty("barcode", barcodeString2);
            insertProp.setProperty("gender", genderString);
            insertProp.setProperty("size", sizeString);
            insertProp.setProperty("articleType", articleTypeString);
            insertProp.setProperty("color1", color1String);
            insertProp.setProperty("color2", color2String);
            insertProp.setProperty("brand", brandString);
            insertProp.setProperty("notes", notesString);
            insertProp.setProperty("donorLastName", donorLastNameString);
            insertProp.setProperty("donorFirstName", donorFirstNameString);
            insertProp.setProperty("donorPhone", donorPhoneString);
            insertProp.setProperty("donorEmail", donorEmailString);
            insertProp.setProperty("receiverNetId", receiverNetIdString);
            insertProp.setProperty("receiverLastName", receiverLastNameString);
            insertProp.setProperty("receiverFirstName", receiverFirstNameString);
            insertProp.setProperty("dateDonated", dateDonatedString);
            insertProp.setProperty("dateTaken", dateTakenString);

//            System.out.println(insertProp);

            //Call Librarian method to create and save book
            myModel.stateChangeRequest("ModifyInventory", insertProp);

            //Print confirmation
            displayMessage("Inventory was updated!");
        }
    }

    // Create the status log field
    //-------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    public void populateFields() {
        gender.setText((String)i.getValue("gender"));
        size.setText((String)i.getValue("size"));
        articleType.setText("0" + (String)i.getValue("articleType"));
        color1.setText("0" + (String)i.getValue("color1"));
        color2.setText("0" + (String)i.getValue("color2"));
        brand.setText((String)i.getValue("brand"));
        notes.setText((String)i.getValue("notes"));
        donorLastName.setText((String)i.getValue("donorLastName"));
        donorFirstName.setText((String)i.getValue("donorFirstName"));
        donorPhone.setText((String)i.getValue("donorPhone"));
        donorEmail.setText((String)i.getValue("donorEmail"));
        receiverNetId.setText((String)i.getValue("receiverNetId"));
        receiverLastName.setText((String)i.getValue("receiverLastName"));
        receiverFirstName.setText((String)i.getValue("receiverFirstName"));
        dateDonated.setText((String)i.getValue("dateDonated"));
        dateTaken.setText((String)i.getValue("dateTaken"));
    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value) {
        clearErrorMessage();
        String temp = ((String)value);

        if (key.equals("inventoryMessage") == true) {
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
