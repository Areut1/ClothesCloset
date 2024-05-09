package userinterface;

// system imports
import exception.InvalidPrimaryKeyException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
import java.util.Vector;
import java.util.Enumeration;

// project imports
import impresario.IModel;
//import model.Color;
import model.ColorCollection;
//==============================================================================
public class SelectColorView extends View
{
    protected TableView<ColorTableModel> tableOfColors;
    protected Button cancelButton;
    protected Button submitButton;
    protected MessageView statusLog;
    //--------------------------------------------------------------------------
    public SelectColorView(IModel clerk)
    {
        super(clerk, "ArticleTypeCollectionView");

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
        ObservableList<ColorTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            ColorCollection colorCollection = (ColorCollection) myModel.getState("ColorCollection");

            Vector entryList = (Vector)colorCollection.getState("Colors");
            Enumeration entries = entryList.elements();

            while (entries.hasMoreElements() == true)
            {
                model.Color nextColor = (model.Color) entries.nextElement();
                Vector<String> view = nextColor.getEntryListView();

                // add this list entry to the list
                ColorTableModel nextTableRowData = new ColorTableModel(view);
                tableData.add(nextTableRowData);

            }

            tableOfColors.setItems(tableData);

        }
        catch (Exception e) {//SQLException e) {
            // Need to handle this exception
        }
    }
    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Color Collection ");
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

        Text prompt = new Text("LIST OF COLORS");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        tableOfColors = new TableView<ColorTableModel>();
        tableOfColors.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn colorIdColumn = new TableColumn("Color Id") ;
        colorIdColumn.setMinWidth(100);
        colorIdColumn.setCellValueFactory(
                new PropertyValueFactory<ColorTableModel, String>("colorId"));

        TableColumn descriptionColumn = new TableColumn("Description") ;
        descriptionColumn.setMinWidth(100);
        descriptionColumn.setCellValueFactory(
                new PropertyValueFactory<ColorTableModel, String>("description"));

        TableColumn barcodePrefixColumn = new TableColumn("Barcode Prefix") ;
        barcodePrefixColumn.setMinWidth(100);
        barcodePrefixColumn.setCellValueFactory(
                new PropertyValueFactory<ColorTableModel, String>("barcodePrefix"));

        TableColumn alphaCodeColumn = new TableColumn("Alpha Code") ;
        alphaCodeColumn.setMinWidth(100);
        alphaCodeColumn.setCellValueFactory(
                new PropertyValueFactory<ColorTableModel, String>("alphaCode"));

        TableColumn statusColumn = new TableColumn("Status");
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<ColorTableModel, String>("status"));

        tableOfColors.getColumns().addAll(colorIdColumn,
                descriptionColumn, barcodePrefixColumn, alphaCodeColumn, statusColumn);

        tableOfColors.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
                    processColorSelected();
                }
            }
        });

        //ScrollPane scrollPane = new ScrollPane();
        //scrollPane.setPrefSize(520, 150);
        //scrollPane.setContent(tableOfColors);

        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                // do the inquiry
                processColorSelected();

            }
        });

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
        btnContainer.getChildren().add(submitButton);
        btnContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(tableOfColors);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }
    //--------------------------------------------------------------------------
    public void updateState(String key, Object value)
    {
    }
    //--------------------------------------------------------------------------
    protected void processColorSelected()
    {
        ColorTableModel selectedItem = tableOfColors.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String selectedColorId = selectedItem.getColorId();
            Properties props = new Properties();
            props.setProperty("colorId", selectedColorId);

            try {
                myModel.stateChangeRequest("ConfirmColorChoice", props);
            } catch (InvalidPrimaryKeyException e) {
                throw new RuntimeException(e);
            }
        }
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
    public void mouseClicked(MouseEvent click)
    {
        if(click.getClickCount() >= 2)
        {
            processColorSelected();
        }
    }
}
//End of Class-----------------------------------------------------------------------------------------------