package userinterface;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
//---------------------------------------------------------------
public class ConfirmDeleteColorView extends View{

    protected Button cancelButton;
    protected Button confirmButton;
    protected MessageView statusLog;

    private String colorDescription;
    //---------------------------------------------------------------
    public ConfirmDeleteColorView(IModel clerk)
    {
        super(clerk, "DeleteColorView");

        getColorDescription();

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

    }
    //---------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Brockport Clothes Closet ");
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

        Text prompt = new Text("Are you sure you would like to delete the color '" + colorDescription + "'?");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);


        confirmButton = new Button("Yes");
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                // do the inquiry
                processConfirm();

            }
        });

        cancelButton = new Button("No");
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
        btnContainer.getChildren().add(confirmButton);
        btnContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }
    //---------------------------------------------------------------
    private void getColorDescription() {
        model.Color color = (model.Color) myModel.getState("Color");

        colorDescription = (String) color.getState("description");
    }
    public void processConfirm(){
        try {
            myModel.stateChangeRequest("DeleteColor", null);
        } catch (InvalidPrimaryKeyException e) {
            throw new RuntimeException(e);
        }
    }
    //---------------------------------------------------------------
    public void updateState(String key, Object value) { }
    //---------------------------------------------------------------
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
}