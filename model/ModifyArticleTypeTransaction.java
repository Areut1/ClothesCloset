package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class ModifyArticleTypeTransaction extends Transaction{
    // GUI Components

    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private ArticleType oldArticleType;

    private ArticleTypeCollection atCol;


    protected ModifyArticleTypeTransaction() throws Exception {
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
        Scene currentScene = myViews.get("SearchArticleTypeView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchArticleTypeView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchArticleTypeView", currentScene);
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
            case "ArticleType" -> oldArticleType;
            case "ArticleTypeCollection" -> atCol;
            default -> null;
        };
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();
            case "ModifyArticleType" -> processTransaction((Properties) value);
            case "SearchTableArticleType" -> {
                try {
                    processSearch((Properties) value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            case "ConfirmArticleTypeChoice" -> processConfirm((Properties) value);
//            case "StartOver" -> createAndShowView("SearchArticleTypeView");
        }

        myRegistry.updateSubscribers(key, this);
    }

    public void processTransaction(Properties props)
    {
        oldArticleType.changeValue("description", props.getProperty("description"));
        oldArticleType.changeValue("barcodePrefix", props.getProperty("barcodePrefix"));
        oldArticleType.changeValue("alphaCode", props.getProperty("alphaCode"));


        oldArticleType.update();
        createAndShowView("ModifyArticleTypeReceipt");
    }

    public void processSearch(Properties props) throws Exception {

        atCol = new ArticleTypeCollection();
        try {
            atCol.findArticleTypes(props);
        } catch (Exception e) {
            throw new Exception("Unable to search for ArticleTypes");
        }

        createAndShowView("SelectArticleTypeView");

    }

    public void processConfirm(Properties props){

        String id = props.getProperty("articleTypeId");

        try {
            oldArticleType = new ArticleType(id);
        } catch (InvalidPrimaryKeyException e) {
            throw new RuntimeException(e);
        }
        createAndShowView("ModifyArticleTypeView");


    }

}

