package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class ColorTableModel
{
    private final SimpleStringProperty colorId;
    private final SimpleStringProperty description;
    private final SimpleStringProperty barcodePrefix;
    private final SimpleStringProperty alphaCode;
    private final SimpleStringProperty status;


    //----------------------------------------------------------------------------
    public ColorTableModel(Vector<String> colorData)
    {
        colorId =  new SimpleStringProperty(colorData.elementAt(0));
        description =  new SimpleStringProperty(colorData.elementAt(1));
        barcodePrefix =  new SimpleStringProperty(colorData.elementAt(2));
        alphaCode =  new SimpleStringProperty(colorData.elementAt(3));
        status =  new SimpleStringProperty(colorData.elementAt(4));
    }

    //----------------------------------------------------------------------------
    public String getColorId() {
        return colorId.get();
    }

    //----------------------------------------------------------------------------
    public void setColorId(String id) {
        colorId.set(id);
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