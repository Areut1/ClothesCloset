package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;
//---------------------------------------------------------------
public class DeleteColorTransaction extends Transaction{
    // GUI Components
    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private Color oldColor;
    private ColorCollection colCol;
    //---------------------------------------------------------------
    protected DeleteColorTransaction() throws Exception {
        super();
    }
    //---------------------------------------------------------------
    @Override
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelDelete", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
    }
    //---------------------------------------------------------------
    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("SearchColorView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = null;
            try {
                newView = ViewFactory.createView("SearchColorView", this);
            } catch (InvalidPrimaryKeyException e) {
                throw new RuntimeException(e);
            }
            currentScene = new Scene(newView);
            myViews.put("SearchColorView", currentScene);
            currentScene.getStylesheets().add("userinterface/stylesheet.css");
            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }
    //------------------------------------------------------
    protected void createAndShowView(String view)
    {
        Scene newScene = myViews.get(view);

        if (newScene == null)
        {
            // create our initial view
            View newView = null;
            try {
                newView = ViewFactory.createView(view, this);
            } catch (InvalidPrimaryKeyException e) {
                throw new RuntimeException(e);
            }
            newScene = new Scene(newView);
            myViews.put(view, newScene);
            newScene.getStylesheets().add("userinterface/stylesheet.css");
        }
        swapToView(newScene);
    }
    //---------------------------------------------------------------
    @Override
    public Object getState(String key) {
        return switch (key) {
            case "TransactionError" -> transactionErrorMessage;
            case "UpdateStatusMessage" -> updateStatusMessage;
            case "Color" -> oldColor;
            case "ColorCollection" -> colCol;
            default -> null;
        };
    }
    //---------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();
            case "DeleteColor" -> processTransaction((Properties) value);
            case "SearchTableColor" -> {
                try {
                    processSearch((Properties) value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            case "ConfirmColorChoice" -> processConfirm((Properties) value);
        }
        myRegistry.updateSubscribers(key, this);
    }
    //---------------------------------------------------------------
    public void processTransaction(Properties props)
    {
        oldColor.changeValue("status", "Inactive");

        oldColor.update();
        createAndShowView("DeleteColorReceipt");
    }
    //---------------------------------------------------------------
    public void processSearch(Properties props) throws Exception {

        colCol = new ColorCollection();
        try {
            colCol.findColors(props);
        } catch (Exception e) {
            throw new Exception("Unable to search for Color");
        }
        createAndShowView("SelectColorView");
    }
    //---------------------------------------------------------------
    public void processConfirm(Properties props) {

        String id = props.getProperty("colorId");

        try {
            oldColor = new Color(id);
        } catch (InvalidPrimaryKeyException e) {
            throw new RuntimeException(e);
        }
        createAndShowView("DeleteColorView");
    }





    /*
    Delete process:

        User selects delete transaction
        transaction class is made
        search view is created - text fields to search for color to delete
        button is clicked to change state
        process search transaction to get color collection
        create selection view with collection displayed
        user selects record and hits button
        state change request to show confirm view
        confirm button is pressed to process full transaction, deleting color
        receipt view is shown
        button on receipt view takes screen back to transaction choice view

     */
}
