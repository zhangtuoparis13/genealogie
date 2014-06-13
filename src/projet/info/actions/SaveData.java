package projet.info.actions;

import java.util.Vector;

import projet.info.commandInterpreter.CommandLine;
import org.eclipse.swt.widgets.Display;

public class SaveData implements CommandLine.ICommand {
	@Override
	public boolean doIt(Vector v) {
		Display myDisplay = HandlingBinaryTrees.getDisplay();
		if (v.size() > 1) {
			int i=1;
			while(i<v.size()) {
				final String arg=v.get(i).toString();
				System.out.println(arg+" : Sauvegarde...");
				myDisplay.asyncExec(new Runnable() {
					public void run() {
						HandlingBinaryTrees grapher = new HandlingBinaryTrees();
						grapher.cleanColor();
						grapher.saveData(arg);
						grapher.showGraph();
					}
				});
				++i;
			}	
		} else {
			System.out.println("Sauvegarde...");
			myDisplay.asyncExec(new Runnable() {
				public void run() {
					HandlingBinaryTrees grapher = new HandlingBinaryTrees();
					grapher.cleanColor();
					grapher.saveData();
					grapher.showGraph();
				}
			});
		}
		return true;
	}
}
