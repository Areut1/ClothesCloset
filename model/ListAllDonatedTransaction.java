package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

//---------------------------------------------------------------
public class ListAllDonatedTransaction extends Transaction{

    // GUI Components
    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";

    private InventoryCollection iCol;
    private ArticleTypeCollection atCol;
    private ColorCollection cCol;


    //help me fix this

    //---------------------------------------------------------------
    protected ListAllDonatedTransaction() throws Exception {
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
        Scene currentScene = myViews.get("InventoryCollectionView");
        if (currentScene == null)
        {
            // create our initial view
            View newView = null;
            try {
                newView = ViewFactory.createView("InventoryCollectionView", this);
            } catch (InvalidPrimaryKeyException e) {
                throw new RuntimeException(e);
            }
            currentScene = new Scene(newView);
            myViews.put("InventoryCollectionView", currentScene);
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
            case "Transaction" -> "ListAllDonated";
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
        myRegistry.updateSubscribers(key, this);
    }

    //---------------------------------------------------------------
    private void createCollection() throws Exception {
        iCol = new InventoryCollection();
        atCol = new ArticleTypeCollection();
        cCol = new ColorCollection();
        try {
            iCol.findDonated();
            atCol.findAllArticleTypes();
            cCol.findAllColors();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //---------------------------------------------------------------

}
