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

import java.util.Properties;

public class AddInventoryConfirmView extends View {

    protected Button cancelButton;
    protected Button confirmButton;

    protected MessageView statusLog;

    // GUI Components
    ComboBox<String> genderComboBox;
    ComboBox<String> articleTypeComboBox;
    ComboBox<String> primaryColorComboBox;

    // Properties object containing all the barcode mappings
    Properties genderBarcodeMapping;
    Properties articleTypeBarcodeMapping;
    Properties primaryColorBarcodeMapping;


    public AddInventoryConfirmView(IModel clerk)
    {
        super(clerk, "AddInventoryConfirm");

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

        populateFields();
    }

    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Confirm Barcode Information ");
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

        // START MAIN FORM CONTENTS


        // ----- Prompt ----------------------------------------------
        Text prompt = new Text(" Is this the correct? ");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        // ----- Gender ----------------------------------------------
        Label genderLabel = new Label("Gender:");
        grid.add(genderLabel, 0, 2);

        ObservableList<String> data = FXCollections.observableArrayList("Active", "Inactive");
        genderComboBox = new ComboBox<>(data);
        genderComboBox.getSelectionModel().select("Active");
        genderComboBox.setMinSize(100, 20);

        // TESTING
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
        grid.add(articleTypeComboBox, 1, 3);

        // ----- Primary Color (color 1) -----------------------------
        Label primaryColorLabel = new Label("Primary Color:");
        grid.add(primaryColorLabel, 0, 4);

        primaryColorComboBox = new ComboBox<>();
        primaryColorComboBox.getSelectionModel().select("Active");
        primaryColorComboBox.setMinSize(100, 20);
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


    private void populateFields() {
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
         */


        // Get user inputted barcode from prev view
        String barcode = (String) myModel.getState("barcode");

        // If barcode length is 8 then we want to autofill information
        if (barcode.length() == 8) {
            String genderBarcode = barcode.substring(0,1);
            String articleTypeBarcode = barcode.substring(1,3);
            String primaryColorBarcode = barcode.substring(3,5);

            System.out.println("Gender barcode is: " + genderBarcode);
            System.out.println("ArticleType is: " + articleTypeBarcode);
            System.out.println("PrimaryColorBarcode is: " + primaryColorBarcode);

            // Update combo boxes
        }

    }

    private void initBarcodeMappings() {
        // Get the tables
        // Properties genderTable = ()

        // Loop over and get the barcodes.
        genderBarcodeMapping = (Properties) myModel.getState("genderBarcodes");
        articleTypeBarcodeMapping = (Properties) myModel.getState("articleType");
        primaryColorBarcodeMapping = (Properties) myModel.getState("primaryColorBarcodes");
    }


    public void processConfirm() {
        myModel.stateChangeRequest("SubmitBarcode", null);
    }

    public void updateState(String key, Object value) {    }

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

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }

}
