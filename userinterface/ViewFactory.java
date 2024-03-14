package userinterface;
import impresario.IModel;
//==============================================================================
public class ViewFactory {
	public static View createView(String viewName, IModel model)
	{
		switch (viewName) {
			case "ClerkView":
				return  new ClerkView(model);
			case "TransactionChoiceView":
				return  new TransactionChoiceView(model);
//			case "AddColorView":
//				return new AddColorView(model);
//			case "AddColorReceipt":
//				return  new AddColorReceipt(model);
			case "AddArticleTypeView":
				return new AddArticleTypeView(model);
			case "AddArticleTypeReceipt":
				return new AddArticleTypeReceipt(model);
//			case "ModifyColorView":
//				return new ModifyColorView(model);
//			case "ModifyArticleTypeView":
//				return new ModifyArticleTypeView(model);
//			case "DeleteColorView":
//				return new DeleteColorView(model);
//			case "DeleteArticleTypeView":
//				return new DeleteArticleTypeView(model);
			default: return null;
		}
	}
}
