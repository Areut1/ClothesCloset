package userinterface;
import exception.InvalidPrimaryKeyException;
import impresario.IModel;
//==============================================================================
public class ViewFactory {
	public static View createView(String viewName, IModel model) throws InvalidPrimaryKeyException {
		switch (viewName) {
			case "ClerkView":
				return  new ClerkView(model);
			case "TransactionChoiceView":
				return  new TransactionChoiceView(model);
			case "AddColorView":
				return new AddColorView(model);
			case "AddColorReceipt":
				return  new AddColorReceipt(model);
			case "AddArticleTypeView":
				return new AddArticleTypeView(model);
			case "AddArticleTypeReceipt":
				return new AddArticleTypeReceipt(model);
			case "ModifyColorView":
				return new ModifyColorView(model);
			case "ModifyArticleTypeView":
				return new ModifyArticleTypeView(model);
			case "DeleteColorView":
				return new ConfirmDeleteColorView(model);
			case "DeleteArticleTypeView":
				return new ConfirmDeleteArticleTypeView(model);
			case "DeleteColorReceipt":
				return new DeleteColorReceipt(model);
			case "DeleteArticleTypeReceipt":
				return new DeleteArticleTypeReceipt(model);
			case "ModifyColorReceipt":
				return new ModifyColorReceipt(model);
			case "ModifyArticleTypeReceipt":
				return new ModifyArticleTypeReceipt(model);
			case "SearchArticleTypeView":
					return new SearchArticleTypeView(model);
			case "SelectArticleTypeView":
					return new SelectArticleTypeView(model);
			case "SearchColorView":
					return new SearchColorView(model);
			case "SelectColorView":
					return new SelectColorView(model);
			case "SearchInventoryBarcodeView":
				return new SearchInventoryBarcodeView(model);
			case "AddInventoryConfirmView":
				return new AddInventoryConfirmView(model);
			case "AddInventoryInputView":
				return new AddInventoryInputView(model);
			case "AddInventoryReceipt":
				return new AddInventoryReceipt(model);
			case "ModifyInventoryInputView":
				return new ModifyInventoryInputView(model);
			case "DeleteInventoryView":
				return new ConfirmDeleteInventoryView(model);
			case "InventoryReceipt":
				return new InventoryReceipt(model);
			case "ReceiverInfoInputView":
				return new ReceiverInfoInputView(model);
			case "InventoryCollectionView":
				return new DonatedInventoryCollectionView(model);
			case "DateRequestView":
				return new DateRequestView(model);
			case "CheckedOutInventoryCollectionView":
				return new CheckedOutInventoryCollectionView(model);
			default: return null;
		}
	}
}
