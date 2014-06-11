package projet.info.actions;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import projet.info.commandInterpreter.CommandLine;
import projet.info.parser.MyHandler;

import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Document;

public class LoadData implements CommandLine.ICommand {
	@Override
	public boolean doIt(Vector v) {
		if (v.size() > 1) {
			System.out.println("Chargement du fichier...");
			File XMLFile = new File(v.get(1).toString());
			if (XMLFile.exists()) {
				try {
					final Document FinalXML = DocumentBuilderFactory
							.newInstance().newDocumentBuilder().parse(XMLFile);
					System.out.println("Chargement du graph...");
					Display myDisplay = HandlingBinaryTrees.getDisplay();
					myDisplay.asyncExec(new Runnable() {
						public void run() {
							HandlingBinaryTrees grapher = new HandlingBinaryTrees();
							grapher.loadData(FinalXML);
							grapher.showGraph();
						}
					});
				} catch (Exception e) {
					System.out.println("Erreur " + v.elementAt(0).toString()
							+ " : Impossible de parser " + v.get(1).toString()
							+ " !");
				}
			} else {
				System.out.println("Erreur " + v.elementAt(0).toString()
						+ " : Le fichier " + v.get(1) + " est inexistant");
			}
		} else {
			System.out.println("Erreur " + v.elementAt(0).toString()
					+ " : Pas assez d'arguments !");
		}
		return true;
	}
}
