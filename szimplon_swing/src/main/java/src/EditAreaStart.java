package src;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.text.BadLocationException;

import com.szimplon.editArea.ColorizedStyledDocument;
import com.szimplon.editArea.Controller;
import com.szimplon.editArea.Frame;
import com.szimplon.editArea.Model;
import com.szimplon.lex.Lex;
import com.szimplon.model.MainModel;
import com.szimplon.xmlParser.XMLParser;

public class EditAreaStart {
	
	public static Frame frame;
	public static Model model;
	
	public static void start (JDesktopPane desktop) {
		List<Lex> lexes = new ArrayList<>();
		
		try {
			lexes = XMLParser.parseLexes("lexes.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model = new Model(lexes);
		frame = new Frame(model, desktop);
		model.addObserver(frame);
				
		Controller controller = new Controller(model, frame);
		frame.setController(controller);
		
		//ezt az egészet kikommenteltem, és látszólag semmi sem változott
		//lehet, hogy erre semmi szükség nem is volt???
		
//		String text = MainModel.loadTextFile("test4.lml");
		
		//TODO ezt javítani, hogy a controller minden változást jelezzen a modellnek, ne csak a billentyűleütést
		//TODO ez nagyon gány megoldás, mindenképpen javítani
		
//		frame.editArea.setText(text);
//		frame.editArea.requestFocus();
//		model.setEditAreaText(frame.editArea.getText());
		
//		try {
//			frame.doc.insertString(0, "", ColorizedStyledDocument.heading2Style);
//		} catch (BadLocationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
}
