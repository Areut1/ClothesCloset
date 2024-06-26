package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class ListAllCheckedOutTransaction extends Transaction {
    // GUI Components
    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private InventoryCollection iCol;
    private ArticleTypeCollection atCol;
    private ColorCollection cCol;

    //help me fix this

    //---------------------------------------------------------------
    protected ListAllCheckedOutTransaction() throws Exception {
        super();
        createCollection();
    }

    //---------------------------------------------------------------
    @Override
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelList", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
    }

    //---------------------------------------------------------------
    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("DateRequestView");
        if (currentScene == null) {
            // create our initial view
            View newView = null;
            try {
                newView = ViewFactory.createView("DateRequestView", this);
            } catch (InvalidPrimaryKeyException e) {
                throw new RuntimeException(e);
            }
            currentScene = new Scene(newView);
            myViews.put("DateRequestView", currentScene);
            currentScene.getStylesheets().add("userinterface/stylesheet.css");
        }
        return currentScene;
    }

    //---------------------------------------------------------------
    @Override
    public Object getState(String key) {
        return switch (key) {
            case "TransactionError" -> transactionErrorMessage;
            case "UpdateStatusMessage" -> updateStatusMessage;
            case "Transaction" -> "ListAllCheckedOut";
            case "InventoryCollection" -> iCol;
            case "ArticleTypeList" -> atCol;
            case "ColorList" -> cCol;
            default -> null;
        };
    }

    //---------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("DoYourJob")) {
            doYourJob();
        }
        if (key.equals("DateRequest")) {
            processTransaction((Properties) value);
        }
        myRegistry.updateSubscribers(key, this);
    }

    //---------------------------------------------------------------
    private void createCollection() throws Exception {
        iCol = new InventoryCollection();
        atCol = new ArticleTypeCollection();
        cCol = new ColorCollection();
        try {
            atCol.findAllArticleTypes();
            cCol.findAllColors();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void processTransaction(Properties props) {
        try {
            iCol.findCheckedOut(props.getProperty("startDate"), props.getProperty("endDate"));
            createAndShowView("CheckedOutInventoryCollectionView");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void createAndShowView(String view) {
        Scene newScene = myViews.get(view);

        if (newScene == null) {
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
}
