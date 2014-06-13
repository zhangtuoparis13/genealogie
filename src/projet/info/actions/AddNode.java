package projet.info.actions;

import java.util.Vector;

import org.eclipse.swt.widgets.Display;

import projet.info.commandInterpreter.CommandLine;

public class AddNode implements CommandLine.ICommand {

	@Override
	public boolean doIt(Vector v) {
		System.out.println("Running the command : " + v.elementAt(0).toString());
		Display myDisplay = HandlingBinaryTrees.getDisplay();
		if (v.size() == 1) {
			myDisplay.asyncExec(new Runnable() {
				public void run() {
					HandlingBinaryTrees grapher = new HandlingBinaryTrees();
					grapher.addNode();
					grapher.showGraph();
				}
			});
		} else {
			int i = 1;
			while (i < v.size()) {
				final String arg = ((v.get(i) instanceof Double) ? new StringBuilder().append(""+(((Double) v.get(i)).intValue())).toString() : v.get(i).toString());
				// Nouvelle fonction : si l'on tape "*<chiffre>", on créé plusieurs nodes prénommés
				// C'est demandé par Timothée
				if(arg.charAt(0)=='x') {
					try {
						// size contient le nombre de nodes désirés
						int size = Integer.parseInt(arg.substring(1));
						for(int tim=0;tim<size;tim++) {
							myDisplay.asyncExec(new Runnable() {
								public void run() {
									HandlingBinaryTrees grapher = new HandlingBinaryTrees();
									grapher.addNode();
									grapher.showGraph();
								}
							});
						}
					} catch (Exception e) {
						// C'est un nom normal !
						myDisplay.asyncExec(new Runnable() {
							public void run() {
								HandlingBinaryTrees grapher = new HandlingBinaryTrees();
								grapher.addNode(arg);
								grapher.showGraph();
							}
						});
					}
				} else {
					myDisplay.asyncExec(new Runnable() {
						public void run() {
							HandlingBinaryTrees grapher = new HandlingBinaryTrees();
							grapher.addNode(arg);
							grapher.showGraph();
						}
					});
				}
				++i;
			}
		}
		return true;
	}
}
