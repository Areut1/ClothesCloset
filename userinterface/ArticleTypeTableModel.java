package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class ArticleTypeTableModel
{
    private final SimpleStringProperty articleTypeId;
    private final SimpleStringProperty description;
    private final SimpleStringProperty barcodePrefix;
    private final SimpleStringProperty alphaCode;
    private final SimpleStringProperty status;


    //----------------------------------------------------------------------------
    public ArticleTypeTableModel(Vector<String> articleTypeData)
    {
        articleTypeId =  new SimpleStringProperty(articleTypeData.elementAt(0));
        description =  new SimpleStringProperty(articleTypeData.elementAt(1));
        barcodePrefix =  new SimpleStringProperty(articleTypeData.elementAt(2));
        alphaCode =  new SimpleStringProperty(articleTypeData.elementAt(3));
        status =  new SimpleStringProperty(articleTypeData.elementAt(4));
    }

    //----------------------------------------------------------------------------
    public String getArticleTypeId() {
        return articleTypeId.get();
    }

    //----------------------------------------------------------------------------
    public void setArticleTypeId(String id) {
        articleTypeId.set(id);
    }

    //----------------------------------------------------------------------------
    public String getDescription() {
        return description.get();
    }

    //----------------------------------------------------------------------------
    public void setDescription(String desc) {
        description.set(desc);
    }

    //----------------------------------------------------------------------------
    public String getBarcodePrefix() {
        return barcodePrefix.get();
    }

    //----------------------------------------------------------------------------
    public void setBarcodePrefix(String bcprefix) {
        barcodePrefix.set(bcprefix);
    }

    //----------------------------------------------------------------------------
    public String getAlphaCode() {
        return alphaCode.get();
    }

    //----------------------------------------------------------------------------
    public void setAlphaCode(String ac)
    {
        alphaCode.set(ac);
    }
    //----------------------------------------------------------------------------
    public String getStatus() {
        return status.get();
    }

    //----------------------------------------------------------------------------
    public void setStatus(String stat)
    {
        status.set(stat);
    }
}