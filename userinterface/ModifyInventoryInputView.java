package userinterface;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import model.ArticleTypeCollection;
import model.ColorCollection;
import model.Inventory;
import model.InventoryCollection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyInventoryInputView extends View {
    // GUI components
    private TextField size;
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
    protected Inventory inventory = (Inventory) myModel.getState("Inventory");

    private ArticleTypeCollection atCol;
    private ColorCollection cCol;
    private InventoryCollection iCol;
    // GUI Components
    protected ComboBox<String> genderComboBox;
    protected ComboBox<String> articleTypeComboBox;
    protected ComboBox<String> primaryColorComboBox;
    protected ComboBox<String> secondaryColorComboBox;
    protected Text barcodeText;
    // Properties object containing all the barcode mappings
    public Properties genderBarcodeMapping;
    public Properties articleTypeBarcodeMapping;
    public Properties primaryColorBarcodeMapping;

    String barcodeSubmit;

    // To access their values elsewhere
    String articleTypeBarcode;
    String primaryColorBarcode;
    String secondaryColorBarcode;
    String genderBarcode;

    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ModifyInventoryInputView(IModel clerk) {
        super(clerk, "ModifyInventoryInputView");

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

        initBarcodeMappings();

        populateFields();

        myModel.subscribe("inventoryMessage", this);
        myModel.subscribe("UpdateStatusMessage", this);
    }


    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle() {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Modify Inventory ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        container.getChildren().add(new Label(" "));

        barcodeText = new Text("...DEFAULT...this gets updated later...");
        barcodeText.setWrappingWidth(350);
        barcodeText.setTextAlignment(TextAlignment.CENTER);
        barcodeText.setFill(Color.BLACK);
        //grid.add(barcodeText, 0, 1);
        container.getChildren().add(barcodeText);

        return container;
    }

    // Create the main form content
    //-------------------------------------------------------------
    private VBox createFormContent() {
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

        ObservableList<String> data = FXCollections.observableArrayList("Active", "Inactive");
        genderComboBox = new ComboBox<>(data);
        genderComboBox.setMinSize(100, 20);

        // Add a listener, so we can monitor the new barcode as the selection changes
        genderComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            // Update barcode with new gender
            updateBarcodeFromFields(articleTypeComboBox.getValue(), primaryColorComboBox.getValue(), newValue);
        });
        grid.add(genderComboBox, 1, 1);

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

        articleTypeComboBox = new ComboBox<>();
        articleTypeComboBox.setMinSize(100, 20);

        // Add a listener, so we can monitor the new barcode as the selection changes
        articleTypeComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            // Update barcode with new article type
            updateBarcodeFromFields(newValue, primaryColorComboBox.getValue(), genderComboBox.getValue());
        });

        grid.add(articleTypeComboBox, 1, 3);

        Text color1Label = new Text(" Color 1 : ");
        color1Label.setFont(myFont);
        color1Label.setWrappingWidth(150);
        color1Label.setTextAlignment(TextAlignment.RIGHT);
        grid.add(color1Label, 0, 4);

        primaryColorComboBox = new ComboBox<>();
        primaryColorComboBox.setMinSize(100, 20);

        // Add a listener, so we can monitor the new barcode as the selection changes
        primaryColorComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            // Update barcode with new primary color
            updateBarcodeFromFields(articleTypeComboBox.getValue(), newValue, genderComboBox.getValue());
        });

        grid.add(primaryColorComboBox, 1, 4);

        Text color2Label = new Text(" Color 2 : ");
        color2Label.setFont(myFont);
        color2Label.setWrappingWidth(150);
        color2Label.setTextAlignment(TextAlignment.RIGHT);
        grid.add(color2Label, 0, 5);

        secondaryColorComboBox = new ComboBox<>();
        secondaryColorComboBox.setMinSize(100, 20);

        // Add a listener, so we can monitor the new secondary color as the selection changes
        secondaryColorComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secondaryColorBarcode = getBarcodeFromMapping(primaryColorBarcodeMapping, newValue);
        });

        grid.add(secondaryColorComboBox, 1, 5);

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
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

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

    private void updateBarcodeFromFields(String articleType, String primaryColor, String gender) {
        // Update the barcode text with new information.

        articleTypeBarcode = getBarcodeFromMapping(articleTypeBarcodeMapping, articleType);
        primaryColorBarcode = getBarcodeFromMapping(primaryColorBarcodeMapping, primaryColor);
        genderBarcode = getBarcodeFromMapping(genderBarcodeMapping, gender);

        if (articleTypeBarcode.length() == 1)
            articleTypeBarcode = "0" + articleTypeBarcode;
        if (primaryColorBarcode.length() == 1)
            primaryColorBarcode = "0" + primaryColorBarcode;

        if (articleTypeBarcode.isEmpty() || primaryColorBarcode.isEmpty() || genderBarcode.isEmpty()) {
            Properties barcode2 = (Properties) myModel.getState("Barcode");
            String genderBarcode2 = barcode2.getProperty("gender");
            String articleTypeBarcode2 = barcode2.getProperty("articleType");
            String primaryColorBarcode2 = barcode2.getProperty("color1");
            String idBarcode2 = barcode2.getProperty("id");

            barcodeText.setText("The Inserted Barcode is: " + genderBarcode2 + articleTypeBarcode2 + primaryColorBarcode2 + idBarcode2);
            return;
        }

        // Retrieve the ID

        String barcode = genderBarcode + articleTypeBarcode + primaryColorBarcode;
        if(!inventory.oldBarcode.substring(0, 5).equals(barcode)){
            try {
                myModel.stateChangeRequest("GetID", barcode);
            } catch (InvalidPrimaryKeyException e) {
                throw new RuntimeException(e);
            }
            String barcodeWithID = barcode + (String) myModel.getState("ID");

            barcodeText.setText("The New Barcode is: " + barcodeWithID);
            barcodeSubmit = barcodeWithID;
            clearErrorMessage();
        }
        else{
            barcodeSubmit = inventory.oldBarcode;
        }

    }

    //---------------------------------------------------------------
    public static String getBarcodeFromMapping(Properties props, String name) {
        // Get the key (barcode) of propValues at name
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            if (entry.getValue().equals(name))
                return (String) entry.getKey();
        }
        return "";
    }


    private void initBarcodeMappings() {
        // Get the tables
        atCol = (ArticleTypeCollection) myModel.getState("ArticleTypeList");
        cCol = (ColorCollection) myModel.getState("ColorList");

        articleTypeBarcodeMapping = new Properties();
        primaryColorBarcodeMapping = new Properties();
        genderBarcodeMapping = new Properties();

        // Loop over and get the barcodes.
        for (int i = 0; i < atCol.size(); i++) {
            articleTypeBarcodeMapping.setProperty(atCol.get(i).getValue("barcodePrefix"), atCol.get(i).getValue("description"));
        }
        for (int i = 0; i < cCol.size(); i++) {
            primaryColorBarcodeMapping.setProperty(cCol.get(i).getValue("barcodePrefix"), cCol.get(i).getValue("description"));
        }

        genderBarcodeMapping.setProperty("0", "Male");
        genderBarcodeMapping.setProperty("1", "Female");


        // SET ARTICLE TYPE COMBO BOX FIELDS
        List<String> values = new ArrayList<>();
        for (Object value : articleTypeBarcodeMapping.values()) {
            values.add((String) value);
        }
        ObservableList<String> articleTypes = FXCollections.observableList(values);
        articleTypeComboBox.setItems(articleTypes);

        // SET GENDER BARCODE TYPE COMBO BOX FIELDS
        values = new ArrayList<>();
        for (Object value : genderBarcodeMapping.values()) {
            values.add((String) value);
        }
        ObservableList<String> genderTypes = FXCollections.observableList(values);
        genderComboBox.setItems(genderTypes);

        // SET PRIMARY COLOR BARCODE COMBO BOX FIELDS
        values = new ArrayList<>();
        for (Object value : primaryColorBarcodeMapping.values()) {
            values.add((String) value);
        }
        ObservableList<String> primaryColors = FXCollections.observableList(values);
        primaryColorComboBox.setItems(primaryColors);

        values.add("");
        ObservableList<String> secondaryColors = FXCollections.observableList(values);
        secondaryColorComboBox.setItems(secondaryColors);


        // Selecting the items according to inserted barcode
        Properties barcode = (Properties) myModel.getState("Barcode");
        String genderBarcode = barcode.getProperty("gender");
        String articleTypeBarcode = barcode.getProperty("articleType");
        String primaryColorBarcode = barcode.getProperty("color1");
        String idBarcode = barcode.getProperty("id");

        articleTypeBarcode = Integer.toString(Integer.parseInt(articleTypeBarcode));
        primaryColorBarcode = Integer.toString(Integer.parseInt(primaryColorBarcode));

        barcodeText.setText("The Inserted Barcode is: " + genderBarcode + articleTypeBarcode + primaryColorBarcode + idBarcode);
        barcodeSubmit = genderBarcode + articleTypeBarcode + primaryColorBarcode + idBarcode;

        try {
            String genderPick = (String) genderBarcodeMapping.get(genderBarcode);
            String articleTypePick = (String) articleTypeBarcodeMapping.get(articleTypeBarcode);
            String primaryColorPick = (String) primaryColorBarcodeMapping.get(primaryColorBarcode);


            genderComboBox.setValue(genderPick);
            articleTypeComboBox.setValue(articleTypePick);
            primaryColorComboBox.setValue(primaryColorPick);

            if (genderPick == null) {
                throw new Exception("bad-gender");
            } else if (articleTypePick == null) {
                throw new Exception("bad-article-type");
            } else if (primaryColorPick == null) {
                throw new Exception("bad-primary-color");
            }
        } catch (Exception e) {
            System.out.println("ERROR! " + e.getMessage());

            if (e.getMessage().equals("bad-gender")) {
                displayErrorMessage("Invalid gender from barcode!");
            } else if (e.getMessage().equals("bad-article-type")) {
                displayErrorMessage("Invalid article type from barcode!");
            } else if (e.getMessage().equals("bad-primary-color")) {
                displayErrorMessage("Invalid primary color from barcode!");
            } else {
                displayErrorMessage("Uh oh.. An error occurred!");
            }
        }
    }


    //--------------------------------------------------------------------------------------------
    /*processAction
     * On submit button click, method will set up properties object with values taken from
     * form.
     * Passes the properties object to a newly created Color.
     * Calls the Color's constructor and insert method.
     */
    public void processSubmitAction(Event evt) {
        String barcodeString = barcodeSubmit;
        String genderString = genderBarcode;
        String color1String = primaryColorBarcode;
        String color2String = secondaryColorBarcode;
        String articleTypeString = articleTypeBarcode;

        String sizeString = "" + size.getText();
        String brandString = "" + brand.getText();
        String notesString = notes.getText() == null ? "" : notes.getText();
        String donorLastNameString = "" + donorLastName.getText();
        String donorFirstNameString = "" + donorFirstName.getText();
        String donorPhoneString = "" + donorPhone.getText();
        String donorEmailString = "" + donorEmail.getText();
        String receiverNetIdString = receiverNetId.getText() == null ? "" : receiverNetId.getText();
        String receiverLastNameString = receiverLastName.getText() == null ? "" : receiverLastName.getText();
        String receiverFirstNameString = "" + receiverFirstName.getText();
        String dateDonatedString = "" + dateDonated.getText();
        String dateTakenString = "" + dateTaken.getText();

        System.out.println("\"" + dateTakenString + "\"");
        System.out.println(dateTakenString.isEmpty());

        Pattern patternPhone = Pattern.compile("^(\\d{3}-){2}\\d{4}$");
        Matcher matcherPhone = patternPhone.matcher(donorPhoneString);

        Pattern patternEmail = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcherEmail = patternEmail.matcher(donorEmailString);



        if (!matcherPhone.find() && !donorPhoneString.equals("")){
            clearErrorMessage();
            displayErrorMessage("Phone not formatted correctly");
        }
        else if (!matcherEmail.find() && !donorEmailString.equals("")){
            clearErrorMessage();
            displayErrorMessage("Email not formatted correctly");
        }
        else if (!isValidDate(dateDonatedString) && !dateDonatedString.equals("")){
            clearErrorMessage();
            displayErrorMessage("Donated date is not valid");
        } else {
            //Create properties and keys
            Properties insertProp = new Properties();

            insertProp.setProperty("barcode", barcodeString);
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
            try {
                myModel.stateChangeRequest("ModifyInventory", insertProp);
            } catch (InvalidPrimaryKeyException e) {
                throw new RuntimeException(e);
            }

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

//        System.out.println("color2 is: " + (String) i.getValue("color2"));
//        System.out.println("is color2 empty? " + ((String) i.getValue("color2")).isEmpty());
        String secondaryColorPick;
        if ((String) inventory.getValue("color2") == null) {
            secondaryColorComboBox.setValue("");
        }
        else{
            secondaryColorPick = (String) primaryColorBarcodeMapping.get((String) inventory.getValue("color2"));
            secondaryColorComboBox.setValue(secondaryColorPick);
        }
        size.setText((String) inventory.getValue("size"));
        brand.setText((String) inventory.getValue("brand"));
        notes.setText((String) inventory.getValue("notes"));
        donorLastName.setText((String) inventory.getValue("donorLastName"));
        donorFirstName.setText((String) inventory.getValue("donorFirstName"));
        donorPhone.setText((String) inventory.getValue("donorPhone"));
        donorEmail.setText((String) inventory.getValue("donorEmail"));
        receiverNetId.setText((String) inventory.getValue("receiverNetId"));
        receiverLastName.setText((String) inventory.getValue("receiverLastName"));
        receiverFirstName.setText((String) inventory.getValue("receiverFirstName"));
        dateDonated.setText((String) inventory.getValue("dateDonated"));
        dateTaken.setText((String) inventory.getValue("dateTaken"));

        if(!inventory.getValue("status").equals("Received")){
            receiverNetId.setDisable(true);
            receiverLastName.setDisable(true);
            receiverFirstName.setDisable(true);
            dateTaken.setDisable(true);
        }

    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value) {
        clearErrorMessage();
        String temp = ((String) value);

        if (key.equals("inventoryMessage")) {
            String val = (String) value;
            //serviceCharge.setText(val);
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

    public static boolean isValidDate(String text) {
        if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d"))
            return false;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        try {
            df.parse(text);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

}
