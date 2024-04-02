package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import javafx.stage.Stage;

import java.util.Properties;
import java.util.Vector;

// project imports
import impresario.IModel;

public class SearchArticleTypeView extends View{

    private Button subButton;
    private Button cancelButton;
    private TextField searchDescription;
    private TextField searchAlphaCode;

    //Show error message
    private MessageView statusLog;

    //---------------------------------------------
    /*CONSTRUCTOR
     * Takes model object from ViewFactory
     */

    public SearchArticleTypeView(IModel clerk){
        super(clerk, "SearchArticleTypeView");

        //Create container
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getStyleClass().add("Vbox");
        //Create GUI components, and add
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        //Error message area
        container.getChildren().add(createStatusLog(""));

        getChildren().add(container);

        populateFields();

        myModel.subscribe("searchArticleTypeMessage", this);
        myModel.subscribe("UpdateStatusMessage", this);
    }//END CONSTRUCTOR--------------------------------

    //-------------------------------------------------
    /*createTitle
     * Create title field for the view
     */
    private Node createTitle(){
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" SEARCH ARTICLE TYPE ");
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

    Text searchDescriptionLabel = new Text("Description: ");
    searchDescriptionLabel.setFont(myFont);
    searchDescriptionLabel.setWrappingWidth(150);
    searchDescriptionLabel.setTextAlignment(TextAlignment.RIGHT);
    grid.add(searchDescriptionLabel, 0, 0);

    searchDescription = new TextField();
    searchDescription.setEditable(true);
    grid.add(searchDescription, 0, 1);

    Text searchAlphaCodeLabel = new Text("Alphacode: ");
    searchAlphaCodeLabel.setFont(myFont);
    searchAlphaCodeLabel.setWrappingWidth(150);
    searchAlphaCodeLabel.setTextAlignment(TextAlignment.RIGHT);
    grid.add(searchAlphaCodeLabel, 1, 0);

    searchAlphaCode = new TextField();
    searchAlphaCode.setEditable(true);
    grid.add(searchAlphaCode, 1, 1);

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
     * On submit click method will set up ArticleTypeCollection
     */
    public void processSubAction(Event evt){

        String descEntered = searchDescription.getText();
        String acEntered = searchAlphaCode.getText();

        //Validate user input
        if ((descEntered == null) && (acEntered == null)){
            clearErrorMessage();
            displayErrorMessage("Please enter a description and/or alpha code");
        }
        else {

            //Convert textfield to strings based on filled out fields
            //Call Modify/Delete ArticleTypeTransaction to create Article Type Collection
            //for an article type to be selected
            Properties props = new Properties();
            if ((acEntered != null) && (acEntered.length() != 0)) {
//                String searchDescriptionString = searchDescription.getText();
//                props.setProperty("description", searchDescriptionString);
//                props.setProperty("alphaCode", null);
                props.setProperty("alphaCode", acEntered);

            }
            //else
            if ((descEntered != null) && (descEntered.length() != 0)) {
//                String searchAlphaCodeString = searchAlphaCode.getText();
//                props.setProperty("description", null);
//                props.setProperty("alphaCode", searchAlphaCodeString);
                props.setProperty("description", descEntered);
            }
//            else {
//                String searchDescriptionString = searchDescription.getText();
//                String searchAlphaCodeString = searchAlphaCode.getText();
//                props.setProperty("description", searchDescriptionString);
//                props.setProperty("alphaCode", searchAlphaCodeString);
//            }
//            populateFields();

            searchDescription.setText("");
            searchAlphaCode.setText("");


            myModel.stateChangeRequest("SearchTableArticleType", props);
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
        //If test does not work change to match searchBook or searchPatron for this method
        //Intended result clears fields after search is done
        searchDescription.setText("");
        searchAlphaCode.setText("");
    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        clearErrorMessage();
        String temp = ((String)value);

        if (key.equals("searchArticleTypeMessage") == true)
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
