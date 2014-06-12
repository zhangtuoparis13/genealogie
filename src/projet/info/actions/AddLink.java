package projet.info.actions;

import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.swt.widgets.Display;

import projet.info.commandInterpreter.CommandLine;

public class AddLink implements CommandLine.ICommand {
	@Override
	public boolean doIt(Vector v) {
		System.out.println("Running the command : " + v.elementAt(0).toString());
		Display myDisplay = HandlingBinaryTrees.getDisplay();
		if (v.size() > 2) {
			int i = 2;
			while (i < v.size()) {
				if (v.get(1).equals(v.get(i))) {
					System.out.println("Erreur " + v.get(0).toString()
							+ " : Arguments identiques !");
				} else {
					/*
					 * On déclare deux nouvelles variables car le nouveau thread
					 * est instable si on accède directement aux paramètres
					 */
					ArrayList<String[]> test;
					final String arg1 = ((v.get(1) instanceof Double) ? new StringBuilder().append(""+(((Double) v.get(1)).intValue())).toString() : v.get(1).toString());
					// Pas du tout recommandé dans un "bon" programme, mais indispensable car Vector ne peut pas être fixé à "String"
					final String arg2 = ((v.get(i) instanceof Double) ? new StringBuilder().append(""+(((Double) v.get(i)).intValue())).toString() : v.get(i).toString());
					myDisplay.asyncExec(new Runnable() {
						public void run() {
							HandlingBinaryTrees grapher = new HandlingBinaryTrees();
							grapher.addLink(arg1, arg2);
							grapher.showGraph();
						}
					});
				}
				++i;
			}
		} else {
			System.out.println("Erreur " + v.elementAt(0).toString()
					+ " : Pas assez d'arguments !");
		}
		return true;
	}
}
