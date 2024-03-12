package userinterface;
import impresario.IModel;
//==============================================================================
public class ViewFactory {
	public static View createView(String viewName, IModel model)
	{
		switch (viewName) {
//			case "LibrarianView":
//				return  new LibrarianView(model);
//			case "TransactionChoiceView":
//				return  new TransactionChoiceView(model);
//			case "InsertBookView":
//				return new InsertBookView(model);
//			case "InsertBookReceipt":
//				return  new InsertBookReceiptView(model);
//			case "InsertPatronView":
//				return new InsertPatronView(model);
//			case "InsertPatronReceipt":
//				return new InsertPatronReceiptView(model);
//			case "SearchBooksView":
//				return new SearchBooksView(model);
//			case "SearchPatronsView":
//				return new SearchPatronsView(model);
//			case "SearchBooksResults":
//				return new BookCollectionView(model);
//			case "SearchPatronsResults":
//				return new PatronCollectionView(model);
			default: return null;
		}
	}
}
