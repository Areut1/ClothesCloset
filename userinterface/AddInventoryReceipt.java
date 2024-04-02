// specify the package
package userinterface;

// system imports

import impresario.IModel;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/** The class containing the Add Article Type Receipt  for the Clothes Coset application */
//==============================================================
public class AddInventoryReceipt extends View
{

    // Model

    private	String todaysDateAndTimeString;
    private Text gender;
    private Text size;
    private Text articleType;
    private Text color1;
    private Text color2;
    private Text brand;
    private Text notes;
    private Text status;
    private Text donorLastName;
    private Text donorFirstName;
    private Text donorPhone;
    private Text donorEmail;
    private Text receiverNetId;
    private Text receiverLastName;
    private Text receiverFirstName;
    private Text dateDonated;
    private Text dateTaken;
    // GUI controls
    private Text todaysDateAndTime;

    private Button okButton;
    // constructor for this class
    //----------------------------------------------------------
    public AddInventoryReceipt(IModel trans)
    {
        super(trans, "AddInventoryReceipt");

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

        Text titleText = new Text(" Added Inventory Receipt ");
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

        grid.add(createText("Date/Time : "), 0, 0);
        todaysDateAndTime = new Text("                       ");
        grid.add(todaysDateAndTime, 1, 0);

        grid.add(createText("Gender : "), 0, 1);
        gender = new Text("                       ");
        grid.add(gender, 1, 1);

        grid.add(createText("Size : "), 0, 2);
        size = new Text("                       ");
        grid.add(size, 1, 2);

        grid.add(createText("Article Type : "), 0, 3);
        articleType = new Text("                       ");
        grid.add(articleType, 1, 3);

        grid.add(createText("Color 1 : "), 0, 4);
        color1 = new Text("                       ");
        grid.add(color1, 1, 4);

        grid.add(createText("Color 2 : "), 0, 5);
        color2 = new Text("                       ");
        grid.add(color2, 1, 5);

        grid.add(createText("Brand : "), 0, 6);
        brand = new Text("                       ");
        grid.add(brand, 1, 6);

        grid.add(createText("Notes : "), 0, 7);
        notes = new Text("                       ");
        grid.add(notes, 1, 7);

        grid.add(createText("Status : "), 0, 8);
        status = new Text("                       ");
        grid.add(status, 1, 8);

        grid.add(createText("Donor Lastname : "), 0, 9);
        donorLastName = new Text("                       ");
        grid.add(donorLastName, 1, 9);

        grid.add(createText("Donor Firstname : "), 0, 10);
        donorFirstName = new Text("                       ");
        grid.add(donorFirstName, 1, 10);

        grid.add(createText("Donor Phone : "), 0, 11);
        donorPhone = new Text("                       ");
        grid.add(donorPhone, 1, 11);

        grid.add(createText("Donor Email : "), 0, 12);
        donorEmail = new Text("                       ");
        grid.add(donorEmail, 1, 12);

        grid.add(createText("Receiver NetId : "), 0, 13);
        receiverNetId = new Text("                       ");
        grid.add(receiverNetId, 1, 13);

        grid.add(createText("Receiver Lastname : "), 0, 14);
        receiverLastName = new Text("                       ");
        grid.add(receiverLastName, 1, 14);

        grid.add(createText("Receiver Firstname : "), 0, 15);
        receiverFirstName = new Text("                       ");
        grid.add(receiverFirstName, 1, 15);

        grid.add(createText("Date donated : "), 0, 16);
        dateDonated = new Text("                       ");
        grid.add(dateDonated, 1, 16);

        grid.add(createText("Date taken : "), 0, 17);
        dateTaken = new Text("                       ");
        grid.add(dateTaken, 1, 17);


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


    private Text createText(String text) {
        Text t = new Text(text);

        t.setWrappingWidth(150);
        t.setTextAlignment(TextAlignment.RIGHT);

        return t;
    }


    //-------------------------------------------------------------
    public void populateFields()
    {
        IModel newInventoryInfo = (IModel)myModel.getState("Inventory");

        String genderSt = (String)newInventoryInfo.getState("gender");
        gender.setText(genderSt);
        String sizeSt = (String)newInventoryInfo.getState("size");
        size.setText(sizeSt);
        String articleTypeSt = (String)newInventoryInfo.getState("articleType");
        articleType.setText(articleTypeSt);
        String color1St = (String)newInventoryInfo.getState("color1");
        color1.setText(color1St);
        String color2St = (String)newInventoryInfo.getState("color2");
        color2.setText(color2St);
        String brandSt = (String)newInventoryInfo.getState("brand");
        brand.setText(brandSt);
        String notesSt = (String)newInventoryInfo.getState("notes");
        notes.setText(notesSt);
        String statusSt = (String)newInventoryInfo.getState("status");
        status.setText(statusSt);
        String donorLastNameSt = (String)newInventoryInfo.getState("donorLastName");
        donorLastName.setText(donorLastNameSt);
        String donorFirstNameSt = (String)newInventoryInfo.getState("donorFirstName");
        donorFirstName.setText(donorFirstNameSt);
        String donorPhoneSt = (String)newInventoryInfo.getState("donorPhone");
        donorPhone.setText(donorPhoneSt);
        String donorEmailSt = (String)newInventoryInfo.getState("donorEmail");
        donorEmail.setText(donorEmailSt);
        String receiverNetIdSt = (String)newInventoryInfo.getState("receiverNetId");
        receiverNetId.setText(receiverNetIdSt);
        String receiverLastNameSt = (String)newInventoryInfo.getState("receiverLastName");
        receiverLastName.setText(receiverLastNameSt);
        String receiverFirstNameSt = (String)newInventoryInfo.getState("receiverFirstName");
        receiverFirstName.setText(receiverFirstNameSt);
        String dateDonatedSt = (String)newInventoryInfo.getState("dateDonated");
        dateDonated.setText(dateDonatedSt);
        String dateTakenSt = (String)newInventoryInfo.getState("dateTaken");
        dateTaken.setText(dateTakenSt);

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
