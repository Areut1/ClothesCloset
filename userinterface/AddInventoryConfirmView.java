package userinterface;

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.ArticleType;
import model.ArticleTypeCollection;
import model.ColorCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.stream.IntStream;

public class AddInventoryConfirmView extends View {

    protected Button cancelButton;
    protected Button confirmButton;

    protected MessageView statusLog;
    private ArticleTypeCollection atCol;
    private ColorCollection cCol;

    // GUI Components
    protected ComboBox<String> genderComboBox;
    protected ComboBox<String> articleTypeComboBox;
    protected ComboBox<String> primaryColorComboBox;

    // Properties object containing all the barcode mappings
    public Properties genderBarcodeMapping;
    public Properties articleTypeBarcodeMapping;
    public Properties primaryColorBarcodeMapping;


    public AddInventoryConfirmView(IModel clerk)
    {
        super(clerk, "AddInventoryConfirmView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog(" "));

        getChildren().add(container);

        // Initialize the barcode mappings
        initBarcodeMappings();

        // updateBarcodeFromFields();
    }

    private Node createTitle()
    {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Confirm Barcode Information ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

//        Text prompt = new Text("Is this the correct?");
//        prompt.setWrappingWidth(350);
//        prompt.setTextAlignment(TextAlignment.CENTER);
//        prompt.setFill(Color.BLACK);
//        container.getChildren(prompt);

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

        // START MAIN FORM CONTENTS


        // ----- Barcode ----------------------------------------------
        Text barcodeText = new Text("Inserted barcode is: " + null);
        barcodeText.setWrappingWidth(350);
        barcodeText.setTextAlignment(TextAlignment.CENTER);
        barcodeText.setFill(Color.BLACK);
        grid.add(barcodeText, 0,1);


        // ----- Gender ----------------------------------------------
        Label genderLabel = new Label("Gender:");
        grid.add(genderLabel, 0, 2);

        ObservableList<String> data = FXCollections.observableArrayList("Active", "Inactive");
        genderComboBox = new ComboBox<>(data);
        genderComboBox.getSelectionModel().select("Active");
        genderComboBox.setMinSize(100, 20);

        // Add a listener, so we can monitor the new barcode as the selection changes
        genderComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            System.out.println(newValue);
        });

        grid.add(genderComboBox, 1, 2);

        // ----- Article Type ----------------------------------------
        Label articleTypeLabel = new Label("Article Type:");
        grid.add(articleTypeLabel, 0, 3);

        articleTypeComboBox = new ComboBox<>();
        articleTypeComboBox.getSelectionModel().select("Active");
        articleTypeComboBox.setMinSize(100, 20);

        // Add a listener, so we can monitor the new barcode as the selection changes
        articleTypeComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            System.out.println("Article type changed! Updating barcode...");

            updateBarcodeFromFields(newValue, primaryColorComboBox.getValue(), genderComboBox.getValue());
        });

        grid.add(articleTypeComboBox, 1, 3);

        // ----- Primary Color (color 1) -----------------------------
        Label primaryColorLabel = new Label("Primary Color:");
        grid.add(primaryColorLabel, 0, 4);

        primaryColorComboBox = new ComboBox<>();
        primaryColorComboBox.getSelectionModel().select("Active");
        primaryColorComboBox.setMinSize(100, 20);

        // Add a listener, so we can monitor the new barcode as the selection changes
        primaryColorComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            System.out.println(newValue);
        });

        grid.add(primaryColorComboBox, 1, 4);

        // -----------------------------------------------------------
        // END MAIN FORM CONTENTS

        confirmButton = new Button("Submit");
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                // do the inquiry
                processConfirm();
            }
        });

        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                /**
                 * Process the Cancel button.
                 * The ultimate result of this action is that the transaction will tell the teller to
                 * to switch to the transaction choice view. BUT THAT IS NOT THIS VIEW'S CONCERN.
                 * It simply tells its model (controller) that the transaction was canceled, and leaves it
                 * to the model to decide to tell the teller to do the switch back.
                 */
                //----------------------------------------------------------
                clearErrorMessage();
                myModel.stateChangeRequest("CancelTransaction", null);
            }
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(confirmButton);
        btnContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }


    private void updateBarcodeFromFields(String articleType, String primaryColor, String gender) {
        /*
        IDEA:
        We can access the full table of the relevant tables using the getState() in `initBarcodeMappings`.

        Using those tables, we will populate these `genderBarcodeMapping`, ... to create the barcode mapping properties.
        The mapping will ideally be of the form:
        {
            "0": "Male",
            "1": "Female"
        }
        In this form, the "barcode" is the key of the Properties object and the contents is the value. The VALUES (contents)
        of this properties object will exactly correspond to the options inside the combo box. However, using the keys, we'll
        build the barcode and use it to initialize the selected choice in the combo boxes.

        Then, we will use those mappings to populate the combo boxes and create the barcode builder.



        NOTE 4/2:

        This runs with the new updated values every time a listener detects a change.

         */
        List<String> list = articleTypeComboBox.getItems();
        int idx = getIndexOf(list, articleType);
        System.out.println("article type idx is: " + idx);

        list = primaryColorComboBox.getItems();
        idx = getIndexOf(list, primaryColor);
        System.out.println("primary color idx is: " + idx);


        list = genderComboBox.getItems();
        idx = getIndexOf(list, gender);
        System.out.println("gender idx is: " + idx);

    }

    public static int getIndexOf(List<String> propValues, String name) {
        int pos = 0;
        for(String propValue : propValues) {
            if(name.equalsIgnoreCase(propValue))
                return pos;
            pos++;
        }
        return -1;
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
        for (int i = 0; i < cCol.size(); i++){
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


        // Selecting the items according to inserted barcode
        Properties barcode = (Properties) myModel.getState("Barcode");
        String genderBarcode = barcode.getProperty("gender");
        String articleTypeBarcode = barcode.getProperty("articleType");
        String primaryColorBarcode = barcode.getProperty("color1");

        System.out.println("this is : " + (String) primaryColorBarcodeMapping.get(primaryColorBarcode));
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
        }
        catch (Exception e) {
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

    public void processConfirm() {
        myModel.stateChangeRequest("SubmitBarcode", null);
    }

    public void updateState(String key, Object value) {

    }

    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }


    /**
     * Display info message
     */
    //----------------------------------------------------------
    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }

    public void displayErrorMessage(String message) {
        statusLog.displayErrorMessage(message);
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