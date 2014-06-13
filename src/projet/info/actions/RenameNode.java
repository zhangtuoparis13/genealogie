package projet.info.actions;

import java.util.Vector;

import org.eclipse.swt.widgets.Display;

import projet.info.commandInterpreter.CommandLine;

public class RenameNode implements CommandLine.ICommand {

	@Override
	public boolean doIt(Vector v) {
		System.out.println("Running the command : " + v.elementAt(0).toString());
		Display myDisplay = HandlingBinaryTrees.getDisplay();
		if (v.size() < 3) {
			System.out.println("Erreur "+v.get(0)+" : Pas assez d'arguments !");
		} else {
			final String arg1 = ((v.get(1) instanceof Double) ? new StringBuilder().append(""+(((Double) v.get(1)).intValue())).toString() : v.get(1).toString());
			final String arg2 = ((v.get(2) instanceof Double) ? new StringBuilder().append(""+(((Double) v.get(2)).intValue())).toString() : v.get(2).toString());
				myDisplay.asyncExec(new Runnable() {
					public void run() {
						HandlingBinaryTrees grapher = new HandlingBinaryTrees();
						grapher.renameNode(arg1,arg2);
						grapher.showGraph();
					}
				});
			}
		return true;
	}
}
