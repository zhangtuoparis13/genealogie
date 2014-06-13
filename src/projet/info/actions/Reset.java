package projet.info.actions;

import java.util.Vector;

import org.eclipse.swt.widgets.Display;

import projet.info.commandInterpreter.CommandLine;

public class Reset implements CommandLine.ICommand {
	@Override
	public boolean doIt(Vector v) {
		System.out.println("Running the command : " + v.elementAt(0).toString());
		Display myDisplay = HandlingBinaryTrees.getDisplay();
		myDisplay.asyncExec(new Runnable() {
			public void run() {
				HandlingBinaryTrees grapher = new HandlingBinaryTrees();
				grapher.reset();
				grapher.showGraph();
			}
		});
		return true;
	}
}