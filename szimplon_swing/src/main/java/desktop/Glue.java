package desktop;

import src.EditAreaStart;

public class Glue {
	public static void main (String args []) {
		DesktopModel model = new DesktopModel();
		DesktopView view = new DesktopView(model);
		model.addObserver(view);
		DesktopController controller = new DesktopController(model, view);
		view.setController(controller);
		EditAreaStart.start(view.getDesktop());
			
	}
}