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

/** The class containing the Deposit Receipt  for the ATM application */
//==============================================================
public class AddColorReceipt extends View
{

    // Model

    private	String todaysDateAndTimeString;
//    private Text authorName;
//    private Text bookTitle;
//    private Text publishYear;
//    private Text status;

    // GUI controls
    private Text todaysDateAndTime;

    private Button okButton;
    // constructor for this class
    //----------------------------------------------------------
    public AddColorReceipt(IModel trans)
    {
        super(trans, "AddColorReceipt");

        Calendar todaysCalendar = Calendar.getInstance();	// creation date and time
        Date todaysDateAndTime = todaysCalendar.getTime();

        DateFormat theFormatter = DateFormat.getDateTimeInstance();
        todaysDateAndTimeString = theFormatter.format(todaysDateAndTime);

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

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

        Text titleText = new Text(" Brockport Clothes Closet ");
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

//        Text authorLabel = new Text("Author : ");
//        authorLabel.setWrappingWidth(150);
//        authorLabel.setTextAlignment(TextAlignment.RIGHT);
//        grid.add(authorLabel, 0, 1);
//
//        authorName = new Text("                       ");
//        grid.add(authorName, 1, 1);
//
//        Text titleLabel = new Text("Title : ");
//        titleLabel.setWrappingWidth(150);
//        titleLabel.setTextAlignment(TextAlignment.RIGHT);
//        grid.add(titleLabel, 0, 2);
//
//        bookTitle = new Text("                       ");
//        grid.add(bookTitle, 1, 2);
//
//        Text publishLabel = new Text("Published : ");
//        publishLabel.setWrappingWidth(150);
//        publishLabel.setTextAlignment(TextAlignment.RIGHT);
//        grid.add(publishLabel, 0, 3);
//
//        publishYear = new Text("                       ");
//        grid.add(publishYear, 1, 3);
//
//        Text statusLabel = new Text("Status : ");
//        statusLabel.setWrappingWidth(150);
//        statusLabel.setTextAlignment(TextAlignment.RIGHT);
//        grid.add(statusLabel, 0, 4);
//
//        status = new Text("                       ");
//        grid.add(status, 1, 4);

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
//        IModel newBookInfo = (IModel)myModel.getState("Book");
//        String title = (String)newBookInfo.getState("bookTitle");
//        bookTitle.setText(title);
//        String author = (String)newBookInfo.getState("author");
//        authorName.setText(author);
//        String pubYear = (String)newBookInfo.getState("pubYear");
//        publishYear.setText(pubYear);
//        String stat = (String)newBookInfo.getState("status");
//        status.setText(stat);

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