package model;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class DeleteColorTransaction extends Transaction{
    // GUI Components

    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private Color color;
    private Color oldColor;

    private ColorCollection colCol;


    protected DeleteColorTransaction() throws Exception {
        super();
    }

    @Override
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelDelete", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
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

        }
        swapToView(newScene);
    }
    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("DeleteColorView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("DeleteColorView", this);
            currentScene = new Scene(newView);
            myViews.put("DeletColorView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------
    protected void createAndShowReceiptView()
    {
        Scene newScene = myViews.get("DeleteColorReceipt");

        if (newScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("DeleteColorReceipt", this);
            newScene = new Scene(newView);
            myViews.put("DeleteColorReceipt", newScene);

        }
        swapToView(newScene);
    }

    protected void createAndShowSearchView(){

    }

    protected void createAndShowSelectView(){

    }

    protected void createAndShowConfirmView(){

    }


    @Override
    public Object getState(String key) {
        return switch (key) {
            case "TransactionError" -> transactionErrorMessage;
            case "UpdateStatusMessage" -> updateStatusMessage;
            case "Color" -> oldColor;
            default -> null;
        };
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();
            case "DeleteColor" -> processTransaction((Properties) value);
        }

        myRegistry.updateSubscribers(key, this);
    }

    //TODO: work on processTransaction to delete color
    public void processTransaction(Properties props)
    {
        oldColor = new Color(props);
        oldColor.changeValue("status", "Inactive");
        createAndShowReceiptView();
    }

    public void processSearch(Properties props) throws Exception {

        colCol = new ColorCollection();
        try {
            colCol.findColors(props);
        } catch (Exception e) {
            throw new Exception("Unable to search for Color");
        }

        createAndShowView("SearchColorView");

    }

    public void processConfirm(Properties props){

        createAndShowView("SearchColorConfirm");

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
