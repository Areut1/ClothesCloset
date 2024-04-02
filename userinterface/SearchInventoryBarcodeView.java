package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
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

// project imports
import impresario.IModel;
import model.InventoryCollection;

import java.util.Properties;

public class SearchInventoryBarcodeView extends View{

    private Button subButton;
    private Button cancelButton;
    private TextField InventoryBarcode;

    //Show error message
    private MessageView statusLog;

    //---------------------------------------------
    /*CONSTRUCTOR
     * Takes model object from ViewFactory
     */

    public SearchInventoryBarcodeView(IModel clerk){
        super(clerk, "SearchInventoryBarcodeView");

        //Create container
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        //Create GUI components, and add
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        //Error message area
        container.getChildren().add(createStatusLog(""));

        getChildren().add(container);

        populateFields();

        myModel.subscribe("InventoryBarcodeMessage", this);
        myModel.subscribe("UpdateStatusMessage", this);
    }//END CONSTRUCTOR--------------------------------

    //-------------------------------------------------
    /*createTitle
     * Create title field for the view
     */
    private Node createTitle(){
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" BARCODE SEARCH ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;

    }//End createTitle-------------------------------------

    //---------------------------------------------------
    /*createFormContent
     * Method creates actual form for user input
     */
    private VBox createFormContent(){
        VBox vbox = new VBox(10);

        //Establish grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //SearchLabel and Text Field------------
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);

        Text addInventoryBarcodeLabel = new Text("Barcode: ");
        addInventoryBarcodeLabel.setFont(myFont);
        addInventoryBarcodeLabel.setWrappingWidth(150);
        addInventoryBarcodeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(addInventoryBarcodeLabel, 0, 0);

        InventoryBarcode = new TextField();
        InventoryBarcode.setEditable(true);
        grid.add(InventoryBarcode, 0, 1);

        //Setup separate hbox for submit and back buttons
        HBox subBack = new HBox(10);
        subBack.setAlignment(Pos.BOTTOM_CENTER);

        //Submit Button---------------------------------
        subButton = new Button("Submit");
        subButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        subButton.setOnAction(new EventHandler<ActionEvent>(){

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
                myModel.stateChangeRequest("CancelTransaction", null);
            }
        });
        subBack.getChildren().add(cancelButton);

        //Add form and buttons
        vbox.getChildren().add(grid);
        vbox.getChildren().add(subBack);

        return vbox;

    }//End createFormContent----------------------------

    //----------------------------------------------------
    /*processSubAction
     * On submit click method will create barcode and set up initial gender, article type,
     * and color data for new inventory item
     */
    public void processSubAction(Event evt){

        String barcodeEntered = InventoryBarcode.getText();
        Properties props = new Properties();
        props.setProperty("barcode", barcodeEntered);

        InventoryCollection iCol = new InventoryCollection();
        try {
            iCol.findInventory(props);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String transaction = (String) myModel.getState("Transaction");
        System.out.println(transaction);

        //Validate user input
        if ((barcodeEntered == null) || (barcodeEntered.length() != 8)){
            clearErrorMessage();
            displayErrorMessage("Please enter appropriate barcode");
        }
        else {
            if (iCol.size() != 0 && transaction.equals("AddInventory")) {
                displayMessage("Barcode already exists");
            }
            else if (iCol.size() == 0 && (transaction.equals("ModifyInventory") || transaction.equals("DeleteInventory"))){
                displayErrorMessage("Barcode does not exist");
            }
            else {
                populateFields();
                myModel.stateChangeRequest("SubmitBarcode", barcodeEntered);
            }
        }

    }//End processSubAction------------------------------

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
        //Clears field after search is done
        InventoryBarcode.setText("");
    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        clearErrorMessage();
        String temp = ((String)value);

        if (key.equals("InventoryBarcodeMessage"))
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


}//END CLASS===============================================