// specify the package
package userinterface;

// system imports
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

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

// project imports
import impresario.IModel;
/** The class containing the Modify Color Receipt  for the Clothes Closet application */
//==============================================================
public class ModifyColorReceipt extends View
{
    // Model
    private	String todaysDateAndTimeString;
    private Text description;
    private Text barcodePrefix;
    private Text alphaCode;
    private Text status;
    // GUI controls
    private Text todaysDateAndTime;
    private Button okButton;
    // constructor for this class
    //----------------------------------------------------------
    public ModifyColorReceipt(IModel trans)
    {
        super(trans, "ModifyColorReceipt");

        Calendar todaysCalendar = Calendar.getInstance();	// creation date and time
        Date todaysDateAndTime = todaysCalendar.getTime();

        DateFormat theFormatter = DateFormat.getDateTimeInstance();
        todaysDateAndTimeString = theFormatter.format(todaysDateAndTime);

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getStyleClass().add("Vbox");
        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        getChildren().add(container);

        populateFields();

    }
    // Create the Node (HBox) for the title
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Modified Color Receipt ");
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

        Text dateAndTimeLabel = new Text("Date/Time : ");
        dateAndTimeLabel.setWrappingWidth(150);
        dateAndTimeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(dateAndTimeLabel, 0, 0);

        todaysDateAndTime = new Text("                       ");
        grid.add(todaysDateAndTime, 1, 0);

        Text descriptionLabel = new Text("Description : ");
        descriptionLabel.setWrappingWidth(150);
        descriptionLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(descriptionLabel, 0, 1);

        description = new Text("                       ");
        grid.add(description, 1, 1);

        Text titleLabel = new Text("Barcode Prefix : ");
        titleLabel.setWrappingWidth(150);
        titleLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(titleLabel, 0, 2);

        barcodePrefix = new Text("                       ");
        grid.add(barcodePrefix, 1, 2);

        Text alphaCodeLabel = new Text("Alpha Code : ");
        alphaCodeLabel.setWrappingWidth(150);
        alphaCodeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(alphaCodeLabel, 0, 3);

        alphaCode = new Text("                       ");
        grid.add(alphaCode, 1, 3);

        Text statusLabel = new Text("Status : ");
        statusLabel.setWrappingWidth(150);
        statusLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(statusLabel, 0, 4);

        status = new Text("                       ");
        grid.add(status, 1, 4);


        okButton = new Button("OK");
        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                /**
                 * Process the Cancel button.
                 * The ultimate result of this action is that the transaction will tell the teller to
                 * to switch to the transaction choice view. BUT THAT IS NOT THIS VIEW'S CONCERN.
                 * It simply tells its model (controller) that the deposit receipt was seen, and leaves it
                 * to the model to decide to tell the teller to do the switch back.
                 */
                //----------------------------------------------------------
                myModel.stateChangeRequest("OK", null);
            }
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(okButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }
    //-------------------------------------------------------------
    public void populateFields()
    {
        IModel newArticleTypeInfo = (IModel)myModel.getState("Color");
        String desc = (String)newArticleTypeInfo.getState("description");
        description.setText(desc);
        String bcPrefix = (String)newArticleTypeInfo.getState("barcodePrefix");
        barcodePrefix.setText(bcPrefix);
        String ac = (String)newArticleTypeInfo.getState("alphaCode");
        alphaCode.setText(ac);
        String stat = (String)newArticleTypeInfo.getState("status");
        status.setText(stat);

        todaysDateAndTime.setText(todaysDateAndTimeString);

    }
    /**
     * Required by interface, but has no role here
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
    }
}