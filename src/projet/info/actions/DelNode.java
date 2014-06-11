package projet.info.actions;

import java.util.Vector;

import org.eclipse.swt.widgets.Display;

import projet.info.commandInterpreter.CommandLine;

public class DelNode implements CommandLine.ICommand {
	@Override
	public boolean doIt(Vector v) {
		System.out.println("Running the command : "+v.elementAt(0).toString());
		Display myDisplay = HandlingBinaryTrees.getDisplay();
		if(v.size()==1) {
			myDisplay.asyncExec(new Runnable() {
				public void run() {
					HandlingBinaryTrees grapher = new HandlingBinaryTrees();
					grapher.delNode();
					grapher.showGraph();
				}
			});
		} else {
			int i=1;
			while(i<v.size()) {
				final String arg=v.get(i).toString();
				myDisplay.asyncExec(new Runnable() {
						public void run() {
							HandlingBinaryTrees grapher = new HandlingBinaryTrees();
							grapher.delNode(arg);
							grapher.showGraph();
						}
					});
				++i;
			}
		}
		return true;
	}
}
