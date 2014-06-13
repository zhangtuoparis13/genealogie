package projet.info.actions;

import java.util.Vector;

import org.eclipse.swt.widgets.Display;

import projet.info.commandInterpreter.CommandLine;

public class SpaceDoge implements CommandLine.ICommand {
	@Override
	public boolean doIt(Vector v) {
		Display myDisplay = HandlingBinaryTrees.getDisplay();
		myDisplay.asyncExec(new Runnable() {
			public void run() {
				HandlingBinaryTrees grapher = new HandlingBinaryTrees();
				grapher.doge();
				grapher.showGraph();
			}
		});
		return true;
	}
}