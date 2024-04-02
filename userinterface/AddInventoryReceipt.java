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
    private Text todaysDateAndTime;
    private Text barcode;
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
    private Text dateDonated;

    // private Text dateReceived;

    // GUI controls
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

        Text dateAndTimeLabel = new Text("Date/Time : ");
        dateAndTimeLabel.setWrappingWidth(150);
        dateAndTimeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(dateAndTimeLabel, 0, 0);

        todaysDateAndTime = new Text("                       ");
        grid.add(todaysDateAndTime, 1, 0);

        Text barcodeLabel = new Text("Barcode : ");
        barcodeLabel.setWrappingWidth(150);
        barcodeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(barcodeLabel, 0, 1);

        barcode = new Text("                       ");
        grid.add(barcode, 1, 1);

        Text genderLabel = new Text("Gender : ");
        genderLabel.setWrappingWidth(150);
        genderLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(genderLabel, 0, 2);

        gender = new Text("                       ");
        grid.add(gender, 1, 2);

        Text sizeLabel = new Text("Size : ");
        sizeLabel.setWrappingWidth(150);
        sizeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(sizeLabel, 0, 3);

        size = new Text("                       ");
        grid.add(size, 1, 3);

        Text articleTypeLabel = new Text("Article Type : ");
        articleTypeLabel.setWrappingWidth(150);
        articleTypeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(articleTypeLabel, 0, 4);

        articleType = new Text("                       ");
        grid.add(articleType, 1, 4);

        Text color1Label = new Text("Color 1 : ");
        color1Label.setWrappingWidth(150);
        color1Label.setTextAlignment(TextAlignment.RIGHT);
        grid.add(color1Label, 0, 5);

        color1 = new Text("                       ");
        grid.add(color1, 1, 5);

        Text color2Label = new Text("Color 2 : ");
        color2Label.setWrappingWidth(150);
        color2Label.setTextAlignment(TextAlignment.RIGHT);
        grid.add(color2Label, 0, 6);

        color2 = new Text("                       ");
        grid.add(color2, 1, 6);

        Text brandLabel = new Text("Brand : ");
        brandLabel.setWrappingWidth(150);
        brandLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(brandLabel, 0, 7);

        brand = new Text("                       ");
        grid.add(brand, 1, 7);

        Text notesLabel = new Text("Notes : ");
        notesLabel.setWrappingWidth(150);
        notesLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(notesLabel, 0, 8);

        notes = new Text("                       ");
        grid.add(notes, 1, 8);

        Text statusLabel = new Text("Status : ");
        statusLabel.setWrappingWidth(150);
        statusLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(statusLabel, 0, 9);

        status = new Text("                       ");
        grid.add(status, 1, 9);

        Text donorLastNameLabel = new Text("Donor Last Name : ");
        donorLastNameLabel.setWrappingWidth(150);
        donorLastNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(donorLastNameLabel, 0, 10);

        donorLastName = new Text("                       ");
        grid.add(donorLastName, 1, 10);

        Text donorFirstNameLabel = new Text("Donor First Name : ");
        donorFirstNameLabel.setWrappingWidth(150);
        donorFirstNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(donorFirstNameLabel, 0, 11);

        donorFirstName = new Text("                       ");
        grid.add(donorFirstName, 1, 11);

        Text donorPhoneLabel = new Text("Donor Phone : ");
        donorPhoneLabel.setWrappingWidth(150);
        donorPhoneLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(donorPhoneLabel, 0, 12);

        donorPhone = new Text("                       ");
        grid.add(donorPhone, 1, 12);

        Text donorEmailLabel = new Text("Donor Email : ");
        donorEmailLabel.setWrappingWidth(150);
        donorEmailLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(donorEmailLabel, 0, 13);

        donorEmail = new Text("                       ");
        grid.add(donorEmail, 1, 13);

        Text dateDonatedLabel = new Text("Date Donated : ");
        dateDonatedLabel.setWrappingWidth(150);
        dateDonatedLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(dateDonatedLabel, 0, 14);

        dateDonated = new Text("                       ");
        grid.add(dateDonated, 1, 14);

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
        IModel newInventoryInfo = (IModel)myModel.getState("Inventory");

        String barcodeSt = (String)newInventoryInfo.getState("barcode");
        barcode.setText(barcodeSt);
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
        String dateDonatedSt = (String)newInventoryInfo.getState("dateDonated");
        dateDonated.setText(dateDonatedSt);

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
