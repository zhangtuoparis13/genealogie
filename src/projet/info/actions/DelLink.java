package projet.info.actions;

import java.util.Vector;

import org.eclipse.swt.widgets.Display;

import projet.info.commandInterpreter.CommandLine;

public class DelLink implements CommandLine.ICommand {
	@Override
	public boolean doIt(Vector v) {
		System.out.println("Running the command : " + v.elementAt(0).toString());
		Display myDisplay = HandlingBinaryTrees.getDisplay();
		if(v.size()>2) {
			int i=2;
			while(i<v.size()) {
				if(v.get(1).equals(v.get(i))) {
					System.out.println("Erreur "+v.get(0).toString() +" : Arguments identiques !");
				} else {
					final String arg1 = v.get(1).toString();
					final String arg2 = v.get(2).toString();
					myDisplay.asyncExec(new Runnable() {
						public void run() {
							HandlingBinaryTrees grapher = new HandlingBinaryTrees();
							grapher.delLink(arg1,arg2);
							grapher.showGraph();
						}
					});
				}
				++i;
			}
		} else {
			System.out.println("Erreur "+v.elementAt(0).toString() +" : Pas assez d'arguments !");
		}	
		return true;
	}
}
