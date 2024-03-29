
// specify the package
package userinterface;

// system imports
import java.util.Properties;

import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// project imports
import impresario.IModel;

/** The class containing the Teller View  for the ATM application */
//==============================================================
public class ClerkView extends View
{

    // GUI stuff

    private Button enterButton;
    private Button quitButton;

    // For showing error message
    private MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ClerkView( IModel librarian)
    {

        super(librarian, "ClerkView");

        // create a container for showing the contents
        VBox container = new VBox(10);

        //container.(Color.rgb(240, 235,230, 255));
        container.setStyle("-fx-background-color: #F0EBE6;");
        container.setPadding(new Insets(15, 5, 5, 5));

        // create a Node (Text) for showing the title
        //container.getChildren().add(createTitle());

        // create images
        Image image1 = new Image("images/Brockport_Logo.PNG");
        ImageView iv1 = createImage(image1);
        container.getChildren().add(iv1);

        Image image2 = new Image("images/Logo2.PNG");
        ImageView iv2 = createImage(image2);
        iv2.setFitWidth(300);
        container.getChildren().add(iv2);

        // create a Node (GridPane) for showing data entry fields
        container.getChildren().add(createFormContents());

        // Error message area
        container.getChildren().add(createStatusLog("                          "));

        getChildren().add(container);


        // STEP 0: Be sure you tell your model what keys you are interested in
        myModel.subscribe("LoginError", this);
    }

    // Create the label (Text) for the title of the screen
    //-------------------------------------------------------------
    private Node createTitle()
    {

        Text titleText = new Text("       Brockport Clothes Closet          ");
        titleText.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);


        return titleText;
    }

    private ImageView createImage(Image image)
    {
        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(350);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        return iv;
    }
    // Create the main form contents
    //-------------------------------------------------------------
    private GridPane createFormContents()
    {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        //grid.setHgap(10);
        //grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        enterButton = new Button("   Enter   ");
        enterButton.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        enterButton.setStyle("-fx-background-radius:5;\n" +
                "-fx-focus-color:#00533E;");
        enterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });

        quitButton = new Button("   Quit   ");
        quitButton.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        quitButton.setStyle("-fx-background-radius:5;\n" +
                "-fx-focus-color:#00533E;");
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {System.exit(0);}
        });

        HBox btnContainer = new HBox(10);
        btnContainer.setAlignment(Pos.BOTTOM_RIGHT);
        btnContainer.getChildren().add(enterButton);
        btnContainer.getChildren().add(quitButton);
        grid.add(btnContainer, 1, 3);

        return grid;
    }



    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage)
    {

        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------

    // This method processes events generated from our GUI components.
    // Make the ActionListeners delegate to this method
    //-------------------------------------------------------------
    public void processAction(Event evt)
    {
        // DEBUG: System.out.println("TellerView.actionPerformed()");

        clearErrorMessage();

        processUserIDAndPassword("1", "123");

    }

    /**
     * Process userid and pwd supplied when Submit button is hit.
     * Action is to pass this info on to the teller object
     */
    //----------------------------------------------------------
    private void processUserIDAndPassword(String useridString,
                                          String passwordString)
    {
        Properties props = new Properties();
        props.setProperty("ID", useridString);
        props.setProperty("Password", passwordString);

        myModel.stateChangeRequest("Login", props);
    }

    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        // STEP 6: Be sure to finish the end of the 'perturbation'
        // by indicating how the view state gets updated.
        if (key.equals("LoginError") == true)
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

