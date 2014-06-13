package projet.info.actions;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import projet.info.commandInterpreter.CommandLine;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Document;

public class LoadData implements CommandLine.ICommand {
	@Override
	public boolean doIt(Vector v) {
		String arg=null;
		if (v.size() > 1) {
			System.out.println("Chargement du fichier...");
			// Si l'utilisateur oublie le XML, le rajouter
			if(!v.get(1).toString().substring(v.get(1).toString().length()-4,v.get(1).toString().length()).equalsIgnoreCase(".xml")) {
				StringBuilder pattEx = new StringBuilder();
				pattEx.append(v.get(1).toString());
				pattEx.append(".xml");
				arg=pattEx.toString();
			} else {
				arg=v.get(1).toString();
			}
		} else {
			System.out.println("Warning "+v.get(0)+" : Pas de fichier pr�cis�");
			System.out.println("Chargement de temp.xml...");
			arg="temp.xml";
		}
		File XMLFile = new File(arg);
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
						+ " : Impossible de parser " + arg
						+ " !");
			}
		} else {
			System.out.println("Erreur " + v.elementAt(0).toString()
					+ " : Le fichier " + arg + " est inexistant");
		}
		return true;
	}
}
