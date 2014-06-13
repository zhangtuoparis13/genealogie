package projet.info.actions;

import java.util.Vector;

import org.eclipse.swt.widgets.Display;

import projet.info.commandInterpreter.CommandLine;

public class ShowOncles implements CommandLine.ICommand {
	@Override
	public boolean doIt(Vector v) {
		System.out.println("Running the command : " + v.elementAt(0).toString());
		if(v.size()>1) {
			Display myDisplay = HandlingBinaryTrees.getDisplay();
			final String arg = ((v.get(1) instanceof Double) ? new StringBuilder().append(""+(((Double) v.get(1)).intValue())).toString() : v.get(1).toString());
			myDisplay.asyncExec(new Runnable() {
				public void run() {
					HandlingBinaryTrees grapher = new HandlingBinaryTrees();
					grapher.cleanColor();
					grapher.oncles(arg);
					grapher.showGraph();
				}
			});
		} else {
			System.out.println("Erreur " + v.elementAt(0).toString() + " : Pas assez d'arguments !");
		}
		return true;
	}
}
