package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class ModifyColorTransaction extends Transaction{
    // GUI Components

    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private Color oldColor;

    private ColorCollection colCol;


    protected ModifyColorTransaction() throws Exception {
        super();
    }

    @Override
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelModify", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
    }
    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("SearchColorView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchColorView", this);
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
            View newView = ViewFactory.createView(view, this);
            newScene = new Scene(newView);
            myViews.put(view, newScene);
            newScene.getStylesheets().add("userinterface/stylesheet.css");

        }
        swapToView(newScene);
    }
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
    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();
            case "ModifyColor" -> processTransaction((Properties) value);
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
    public void processTransaction(Properties props)
    {
        oldColor.changeValue("description", props.getProperty("description"));
        oldColor.changeValue("barcodePrefix", props.getProperty("barcodePrefix"));
        oldColor.changeValue("alphaCode", props.getProperty("alphaCode"));


        oldColor.update();
        createAndShowView("ModifyColorReceipt");
    }
    public void processSearch(Properties props) throws Exception {

        colCol = new ColorCollection();
        try {
            colCol.findColors(props);
        } catch (Exception e) {
            throw new Exception("Unable to search for Color");
        }

        createAndShowView("SelectColorView");

    }
    public void processConfirm(Properties props) {

        String id = props.getProperty("colorId");

        try {
            oldColor = new Color(id);
        } catch (InvalidPrimaryKeyException e) {
            throw new RuntimeException(e);
        }
        createAndShowView("ModifyColorView");
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
