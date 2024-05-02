// specify the package
package userinterface;

// system imports
import exception.InvalidPrimaryKeyException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

// project imports
import impresario.IModel;
/** The class containing the Transaction Choice View  for the Clothes Closet application */
//==============================================================
public class TransactionChoiceView extends View
{
    // other private data
    private final int labelWidth = 120;
    private final int labelHeight = 25;
    // GUI components
    private Button addArticleTypeButton;
    private Button updateArticleTypeButton;
    private Button deleteArticleTypeButton;
    private Button addColorButton;
    private Button updateColorButton;
    private Button deleteColorButton;
    private Button addInventoryButton;
    private Button updateInventoryButton;
    private Button deleteInventoryButton;
    private Button checkOutInventoryButton;
    private Button listInventoryButton;
    private Button listCheckedOutInventoryButton;
    private Button cancelButton;
    private MessageView statusLog;
    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public TransactionChoiceView(IModel teller)
    {
        super(teller, "TransactionChoiceView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.getStyleClass().add("Vbox");
        container.setPadding(new Insets(15, 5, 5, 5));

        // Add a title for this panel
        container.getChildren().add(createTitle());

        // how do you add white space?
        //container.getChildren().add(new Label(" "));

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContents());

        container.getChildren().add(createStatusLog("             "));

        getChildren().add(container);

        populateFields();

        myModel.subscribe("TransactionError", this);
    }
    // Create the labels and fields
    //-------------------------------------------------------------
    private VBox createTitle()
    {
        VBox container = new VBox(10);
        VBox container2 = new VBox();

        Image image = new Image("images/Brockport_Logo.PNG");
        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(200);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        container2.getChildren().add(iv);

        Image image2 = new Image("images/Clothes_Closet_Title.PNG");
        ImageView iv2 = new ImageView();
        iv2.setImage(image2);
        iv2.setFitWidth(150);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        iv2.setCache(true);
        container2.getChildren().add(iv2);
/**
        Text titleText = new Text("       Brockport Clothes Closet          ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(600);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        String accountHolderGreetingName = (String)myModel.getState("Name");
        Text welcomeText = new Text("Welcome, " + "User!");
        welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        welcomeText.setWrappingWidth(600);
        welcomeText.setTextAlignment(TextAlignment.CENTER);
        welcomeText.setFill(Color.DARKGREEN);
        container.getChildren().add(welcomeText);
*/
        Text inquiryText = new Text("                 What do you wish to do today?");
        inquiryText.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
        inquiryText.setWrappingWidth(600);
        inquiryText.setTextAlignment(TextAlignment.CENTER);
        inquiryText.setFill(Color.BLACK);
        //grid.add(inquiryText, 1, 1);
        container2.getChildren().add(new Label(" "));
        container2.getChildren().add(inquiryText);
        container.getChildren().add(container2);



        return container;
    }
    // Create the navigation buttons
    //-------------------------------------------------------------
    private VBox createFormContents()
    {

        VBox container = new VBox(15);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // create the buttons, listen for events, add them to the container
        // Add Article Type Buttons
        addArticleTypeButton = new Button("      Add Article Type      ");
        addArticleTypeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    myModel.stateChangeRequest("AddArticleType", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        grid.add(addArticleTypeButton, 0, 0);
        grid.setHalignment(addArticleTypeButton, HPos.CENTER);

        updateArticleTypeButton = new Button("      Modify Article Type      ");
        updateArticleTypeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    myModel.stateChangeRequest("ModifyArticleType", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        grid.add(updateArticleTypeButton, 1, 0);
        grid.setHalignment(updateArticleTypeButton, HPos.CENTER);

        deleteArticleTypeButton = new Button("       Delete Article Type        ");
        deleteArticleTypeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    myModel.stateChangeRequest("DeleteArticleType", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        grid.add(deleteArticleTypeButton, 2, 0);
        grid.setHalignment(deleteArticleTypeButton, HPos.CENTER);

        // Add Color Buttons
        addColorButton = new Button("           Add Color            ");
        addColorButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    myModel.stateChangeRequest("AddColor", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        grid.add(addColorButton, 0, 1);
        grid.setHalignment(addColorButton, HPos.CENTER);

        updateColorButton = new Button("           Modify Color            ");
        updateColorButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    myModel.stateChangeRequest("ModifyColor", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        grid.add(updateColorButton, 1, 1);
        grid.setHalignment(updateColorButton, HPos.CENTER);

        deleteColorButton = new Button("            Delete Color              ");
        deleteColorButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    myModel.stateChangeRequest("DeleteColor", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        grid.add(deleteColorButton, 2, 1);
        grid.setHalignment(deleteColorButton, HPos.CENTER);

        // Add Inventory Buttons
        addInventoryButton = new Button("        Add Inventory        ");
        addInventoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    myModel.stateChangeRequest("AddInventory", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        grid.add(addInventoryButton, 0, 2);
        grid.setHalignment(addInventoryButton, HPos.CENTER);

        updateInventoryButton = new Button("        Modify Inventory        ");
        updateInventoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    myModel.stateChangeRequest("ModifyInventory", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        grid.add(updateInventoryButton, 1, 2);
        grid.setHalignment(updateInventoryButton, HPos.CENTER);

        deleteInventoryButton = new Button("          Delete Inventory         ");
        deleteInventoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    myModel.stateChangeRequest("DeleteInventory", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        grid.add(deleteInventoryButton, 2, 2);
        grid.setHalignment(deleteInventoryButton, HPos.CENTER);

        // Other Inventory Buttons
        checkOutInventoryButton = new Button("   Check Out Inventory  ");
        checkOutInventoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    myModel.stateChangeRequest("CheckOut", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        grid.add(checkOutInventoryButton, 0, 3);
        grid.setHalignment(checkOutInventoryButton, HPos.CENTER);

        listInventoryButton = new Button("           List Inventory          ");
        listInventoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    myModel.stateChangeRequest("ListAllAvailable", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        grid.add(listInventoryButton, 1, 3);
        grid.setHalignment(listInventoryButton, HPos.CENTER);

        listCheckedOutInventoryButton = new Button("List Checked Out Inventory");
        listCheckedOutInventoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    myModel.stateChangeRequest("ListAllCheckOut", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        grid.add(listCheckedOutInventoryButton, 2, 3);
        grid.setHalignment(listCheckedOutInventoryButton, HPos.CENTER);

        cancelButton = new Button("Logout");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                try {
                    myModel.stateChangeRequest("Logout", null);
                } catch (InvalidPrimaryKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        grid.add(cancelButton, 1, 6);
        grid.setHalignment(cancelButton, HPos.CENTER);

        container.getChildren().add(grid);

        return container;
    }
    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage)
    {

        statusLog = new MessageView(initialMessage);

        return statusLog;
    }
    //-------------------------------------------------------------
    public void populateFields()
    {
    }
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        if (key.equals("TransactionError") == true)
        {
            // display the passed text
            displayErrorMessage((String)value);
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
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }
}