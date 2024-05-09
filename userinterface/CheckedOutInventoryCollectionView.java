package userinterface;

// system imports

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.ArticleTypeCollection;
import model.ColorCollection;
import model.Inventory;
import model.InventoryCollection;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

//==============================================================================
public class CheckedOutInventoryCollectionView extends View
{
    Properties articleTypeBarcodeMapping;
    Properties primaryColorBarcodeMapping;
    Properties genderBarcodeMapping;

    protected TableView<CheckedOutInventoryTableModel> tableOfInventory;
    protected Button cancelButton;

    protected MessageView statusLog;
    //--------------------------------------------------------------------------
    public CheckedOutInventoryCollectionView(IModel clerk)
    {
        super(clerk, "CheckedOutInventoryCollectionView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getStyleClass().add("Vbox");
        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                                            "));

        getChildren().add(container);

        populateFields();
    }
    //--------------------------------------------------------------------------
    protected void populateFields()
    {
        getEntryTableModelValues();
    }
    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues()
    {
        initBarcodeMappings();

        ObservableList<CheckedOutInventoryTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            InventoryCollection inventoryCollection = (InventoryCollection) myModel.getState("InventoryCollection");

            Vector<Inventory> entryList = (Vector<Inventory>) inventoryCollection.getState("Inventory");
            Enumeration<Inventory> entries = entryList.elements();

            while (entries.hasMoreElements())
            {
                Inventory nextInventory = entries.nextElement();
                Vector<String> view = nextInventory.getEntryListView();

                // -------------------------------------------------------------------------
                // Switching from INTs to String values for Gender, Article type, and Colors
                // To see where indices are being pulled from, see the `Inventory.java` file

                // Gender: Located at index 1
                view.set(1, (String) genderBarcodeMapping.get(view.get(1)));

                // Article type: Located at index 3
                view.set(3, (String) articleTypeBarcodeMapping.get(view.get(3)));

                // Colors: Located at index 4 & 5
                view.set(4, (String) primaryColorBarcodeMapping.get(view.get(4)));
                if (view.get(5) == null)
                    view.set(5, "");
                else
                    view.set(5, (String) primaryColorBarcodeMapping.get(view.get(5)));
                // -------------------------------------------------------------------------

                // add this list entry to the list
                CheckedOutInventoryTableModel nextTableRowData = new CheckedOutInventoryTableModel(view);
                tableData.add(nextTableRowData);

            }

            tableOfInventory.setItems(tableData);

        }
        catch (Exception e) {//SQLException e) {
            // Need to handle this exception
        }
    }


    /**
     * Initialize the Barcode mapping fun stuff
     */
    private void initBarcodeMappings() {
        // Get the tables
        ArticleTypeCollection articleTypeCollection = (ArticleTypeCollection) myModel.getState("ArticleTypeList");
        ColorCollection colorCollection = (ColorCollection) myModel.getState("ColorList");

        articleTypeBarcodeMapping = new Properties();
        primaryColorBarcodeMapping = new Properties();
        genderBarcodeMapping = new Properties();

        // Loop over and create the barcode mappings
        for (int i = 0; i < articleTypeCollection.size(); i++) {
            articleTypeBarcodeMapping.setProperty(
                    articleTypeCollection.get(i).getValue("barcodePrefix"),
                    articleTypeCollection.get(i).getValue("description")
            );
        }
        for (int i = 0; i < colorCollection.size(); i++){
            primaryColorBarcodeMapping.setProperty(
                    colorCollection.get(i).getValue("barcodePrefix"),
                    colorCollection.get(i).getValue("description")
            );
        }

        genderBarcodeMapping.setProperty("0", "Male");
        genderBarcodeMapping.setProperty("1", "Female");
    }










    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Checked Out Inventory Items List");
        titleText.getStyleClass().add("title");
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        container.getChildren().add(titleText);

        return container;
    }
    // Create the main form content
    //-------------------------------------------------------------
    @SuppressWarnings("unchecked")
    private VBox createFormContent()
    {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text prompt = new Text("List of Inventory Items");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        tableOfInventory = new TableView<CheckedOutInventoryTableModel>();
        tableOfInventory.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn barcodeColumn = new TableColumn("Barcode") ;
        //barcodeColumn.setMinWidth(100);
        barcodeColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("barcode"));

        TableColumn genderColumn = new TableColumn("Gender") ;
        //genderColumn.setMinWidth(100);
        genderColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("gender"));

        TableColumn sizeColumn = new TableColumn("Size") ;
        //sizeColumn.setMinWidth(100);
        sizeColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("size"));

        TableColumn articleTypeColumn = new TableColumn("Article\nType") ;
        //articleTypeColumn.setMinWidth(100);
        articleTypeColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("articleType"));

        TableColumn color1Column = new TableColumn("Color 1") ;
        //color1Column.setMinWidth(100);
        color1Column.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("color1"));

        TableColumn color2Column = new TableColumn("Color 2") ;
        //color2Column.setMinWidth(100);
        color2Column.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("color2"));

        TableColumn brandColumn = new TableColumn("Brand") ;
        //brandColumn.setMinWidth(100);
        brandColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("brand"));

        TableColumn notesColumn = new TableColumn("Notes") ;
        //notesColumn.setMinWidth(100);
        notesColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("notes"));

        TableColumn statusColumn = new TableColumn("Status") ;
        //statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("status"));

        TableColumn donorLastNameColumn = new TableColumn("Donor\nLast Name") ;
        //donorLastNameColumn.setMinWidth(100);
        donorLastNameColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("donorLastName"));

        TableColumn donorFirstNameColumn = new TableColumn("Donor\nFirst Name") ;
        //donorFirstNameColumn.setMinWidth(100);
        donorFirstNameColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("donorFirstName"));

        TableColumn donorPhoneColumn = new TableColumn("Donor Phone") ;
        //donorPhoneColumn.setMinWidth(100);
        donorPhoneColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("donorPhone"));

        TableColumn donorEmailColumn = new TableColumn("Donor Email") ;
        //donorEmailColumn.setMinWidth(100);
        donorEmailColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("donorEmail"));

        TableColumn receiverNetIdColumn = new TableColumn("Receiver\nNet ID") ;
        //receiverNetIdColumn.setMinWidth(100);
        receiverNetIdColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("receiverNetId"));

        TableColumn receiverLastNameColumn = new TableColumn("Receiver\nLast Name") ;
        //receiverLastNameColumn.setMinWidth(100);
        receiverLastNameColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("receiverLastName"));

        TableColumn receiverFirstNameColumn = new TableColumn("Receiver\nFirst Name") ;
        //receiverFirstNameColumn.setMinWidth(100);
        receiverFirstNameColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("receiverFirstName"));

        TableColumn dateDonatedColumn = new TableColumn("Date\nDonated") ;
        //dateDonatedColumn.setMinWidth(100);
        dateDonatedColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("dateDonated"));

        TableColumn dateTakenColumn = new TableColumn("Date\nTaken") ;
        //dateTakenColumn.setMinWidth(100);
        dateTakenColumn.setCellValueFactory(
                new PropertyValueFactory<CheckedOutInventoryTableModel, String>("dateTaken"));


        tableOfInventory.getColumns().addAll(barcodeColumn, genderColumn, sizeColumn, articleTypeColumn,
                color1Column, color2Column, brandColumn, notesColumn, statusColumn, donorLastNameColumn,
                donorFirstNameColumn, donorPhoneColumn, donorEmailColumn, receiverNetIdColumn,
                receiverLastNameColumn, receiverFirstNameColumn, dateDonatedColumn, dateTakenColumn);


        cancelButton = new Button("Back");
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
                try {
                    myModel.stateChangeRequest("CancelTransaction", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(tableOfInventory);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }
    //--------------------------------------------------------------------------
    public void updateState(String key, Object value)
    {
    }

    //--------------------------------------------------------------------------
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
    //--------------------------------------------------------------------------

}
//End of Class-----------------------------------------------------------------------------------