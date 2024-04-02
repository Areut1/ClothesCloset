package userinterface;

// system imports
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
import model.ArticleType;
import model.ArticleTypeCollection;

//==============================================================================
public class SelectArticleTypeView extends View
{
    protected TableView<ArticleTypeTableModel> tableOfArticleTypes;
    protected Button cancelButton;
    protected Button submitButton;

    protected MessageView statusLog;


    //--------------------------------------------------------------------------
    public SelectArticleTypeView(IModel clerk)
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

        ObservableList<ArticleTypeTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            ArticleTypeCollection articleTypeCollection = (ArticleTypeCollection) myModel.getState("ArticleTypeCollection");

            Vector entryList = (Vector)articleTypeCollection.getState("ArticleTypes");
            Enumeration entries = entryList.elements();

            while (entries.hasMoreElements() == true)
            {
                ArticleType nextArticleType = (ArticleType) entries.nextElement();
                Vector<String> view = nextArticleType.getEntryListView();

                // add this list entry to the list
                ArticleTypeTableModel nextTableRowData = new ArticleTypeTableModel(view);
                tableData.add(nextTableRowData);

            }

            tableOfArticleTypes.setItems(tableData);

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

        Text titleText = new Text(" Article Type Collection ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
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

        Text prompt = new Text("LIST OF Article Types");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        tableOfArticleTypes = new TableView<ArticleTypeTableModel>();
        tableOfArticleTypes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn articleTypeIdColumn = new TableColumn("Article Type Id") ;
        articleTypeIdColumn.setMinWidth(100);
        articleTypeIdColumn.setCellValueFactory(
                new PropertyValueFactory<ArticleTypeTableModel, String>("articleTypeId"));

        TableColumn descriptionColumn = new TableColumn("Description") ;
        descriptionColumn.setMinWidth(100);
        descriptionColumn.setCellValueFactory(
                new PropertyValueFactory<ArticleTypeTableModel, String>("description"));

        TableColumn barcodePrefixColumn = new TableColumn("Barcode Prefix") ;
        barcodePrefixColumn.setMinWidth(100);
        barcodePrefixColumn.setCellValueFactory(
                new PropertyValueFactory<ArticleTypeTableModel, String>("barcodePrefix"));

        TableColumn alphaCodeColumn = new TableColumn("Alpha Code") ;
        alphaCodeColumn.setMinWidth(100);
        alphaCodeColumn.setCellValueFactory(
                new PropertyValueFactory<ArticleTypeTableModel, String>("alphaCode"));

        TableColumn statusColumn = new TableColumn("Status");
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<ArticleTypeTableModel, String>("status"));

        tableOfArticleTypes.getColumns().addAll(articleTypeIdColumn,
                descriptionColumn, barcodePrefixColumn, alphaCodeColumn, statusColumn);


        tableOfArticleTypes.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
                    processArticleTypeSelected();
                }
            }
        });

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(500, 150);
        scrollPane.setContent(tableOfArticleTypes);

		submitButton = new Button("Submit");
 		submitButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	clearErrorMessage();
					// do the inquiry
					processArticleTypeSelected();

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
                myModel.stateChangeRequest("CancelTransaction", null);
            }
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(submitButton);
        btnContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(scrollPane);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }



    //--------------------------------------------------------------------------
    public void updateState(String key, Object value)
    {
    }

    //--------------------------------------------------------------------------
    protected void processArticleTypeSelected()
    {
        ArticleTypeTableModel selectedItem = tableOfArticleTypes.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String selectedArticleTypeId = selectedItem.getArticleTypeId();
            Properties props = new Properties();
            props.setProperty("articleTypeId", selectedArticleTypeId);




            myModel.stateChangeRequest("ConfirmArticleTypeChoice", props);
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
			processArticleTypeSelected();
		}
	}
}
//End of Class-----------------------------------------------------------------------------------------------
