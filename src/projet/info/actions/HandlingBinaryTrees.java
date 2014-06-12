package projet.info.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import projet.info.commandInterpreter.CommandLine;
import projet.info.graph.core.Link;
import projet.info.graph.core.Network;
import projet.info.graph.core.Node;
import projet.info.parser.MyHandler;

public class HandlingBinaryTrees {

	private static Network myNetwork = new Network();
	private static Display myDisplay = new Display();
	private static Shell myShell = new Shell();
	private static Graph myGraph = new Graph(myShell, SWT.NONE);
	private static ArrayList<GraphNode> listeNoeuds = new ArrayList<GraphNode>();
	private static ArrayList<GraphConnection> listeLinks = new ArrayList<GraphConnection>();
	private static int nbrNodes = 0;

	public HandlingBinaryTrees(Network network) {
		myNetwork = network;
	}

	public HandlingBinaryTrees() {
	}

	public static Display getDisplay() {
		return myDisplay;
	}

	public static Graph getGrapher() {
		return myGraph;
	}

	public void open() {

		myShell.setText("Arbre généalogique");
		myShell.setLayout(new FillLayout());
		myShell.setSize(1080, 640);
		myShell.setVisible(true);
		myShell.open();

		myShell.layout();

		while (!myShell.isDisposed()) {
			while (!myDisplay.readAndDispatch()) {
				myDisplay.sleep();
			}
		}

		myDisplay.dispose();
		System.out.println("Programme terminé");

	}

	public void interpretCommands(String[] args) throws FileNotFoundException {

		Reader inputSrc = null;
		boolean interactive;

		if (args.length > 0) {
			inputSrc = new BufferedReader(new FileReader(args[0]));
			interactive = false;
		} else {
			inputSrc = new InputStreamReader(System.in);
			interactive = true;
		}

		// initialize the command line object.
		CommandLine jr = new CommandLine();
		jr.setCommandLinePrompt("Commande > ");
		jr.setCommandLineVersion("Ligne de commande v0.05P");
		jr.assignClassToCommand("addNode", "projet.info.actions.AddNode");
		jr.assignClassToCommand("delNode", "projet.info.actions.DelNode");
		jr.assignClassToCommand("addLink", "projet.info.actions.AddLink");
		jr.assignClassToCommand("delLink", "projet.info.actions.DelLink");
		jr.assignClassToCommand("loadData", "projet.info.actions.LoadData");
		jr.assignClassToCommand("saveData", "projet.info.actions.SaveData");
		jr.assignClassToCommand("sons", "projet.info.actions.ShowSons");
		jr.assignClassToCommand("desc", "projet.info.actions.ShowDescendants");
		jr.assignClassToCommand("asc", "projet.info.actions.ShowAscendants");
		jr.assignClassToCommand("oncles", "projet.info.actions.ShowOncles");
		jr.assignClassToCommand("cousins", "projet.info.actions.ShowCousins");
		jr.assignClassToCommand("onclesCom", "projet.info.actions.OnclesCom");
		jr.assignClassToCommand("cousinsCom", "projet.info.actions.CousinsCom");
		jr.init();
		jr.setIsInteractive(interactive);
		// parse and execute commands.
		jr.parseStream(new StreamTokenizer(inputSrc));
		System.out.println("\nFini.\n");
	}

	public void showGraph() {

		myGraph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NONE),
				true);
		myGraph.applyLayout();
		myShell.layout();
	}

	public void addNode() {
		addNode(String.valueOf(nbrNodes));
	}

	public void addNode(String nom) {
		GraphNode gn = new GraphNode(myGraph, ZestStyles.NONE, nom);
		gn.setBackgroundColor(ColorConstants.cyan);
		listeNoeuds.add(gn);
		Node patate = new Node(nom);
		myNetwork.addNode(patate);
		++nbrNodes;
	}

	private Node findNode(String toFind) {
		Node FoundNode = null;
		int i = 0;
		if (!myNetwork.nodes().isEmpty() && toFind != null) {
			while (FoundNode == null && i < myNetwork.nodes().size()) {
				if (FoundNode == null) {
					if (toFind.equals(myNetwork.nodes().get(i).getName())) {
						FoundNode = myNetwork.nodes().get(i);
					}
				}
				++i;
			}
		}
		return FoundNode;
	}

	private GraphNode findGNode(String toFind) {
		GraphNode FoundGNode = null;
		int i = 0;
		if (nbrNodes > 0 && toFind != null) {
			while (FoundGNode == null && i < nbrNodes) {
				if (FoundGNode == null)
					if (toFind.equals(listeNoeuds.get(i).getText())) {
						FoundGNode = listeNoeuds.get(i);
						break; // stopper la boucle car égalité trouvée
					}
				++i;
			}
		}
		return FoundGNode;
	}

	// Sans paramètre, on détruit le dernier créé (Ctrl + Z ?)
	public void delNode() {
		delNode(myNetwork.nodes().get(myNetwork.nodes().size() - 1).getName());
	}

	// On assume que "nodes" n'est pas vide
	public void delNode(String yodel) {
		Node Ndel = findNode(yodel);
		GraphNode GNdel = findGNode(yodel);
		
		if(Ndel == null)
			System.out.println("Erreur delNode : "+yodel+" n'existe pas !");
		else {
			// On détruit le node en interne
			for (Node l : myNetwork.nodes())
				myNetwork.disconnect(Ndel, l);
			myNetwork.nodes().remove(Ndel);
			// On détruit le node externe
			int i = 0;
			for (GraphConnection l : listeLinks) {
				if (l.getSource().equals(GNdel)) {
					listeLinks.get(i).dispose();
					listeLinks.remove(i);
				}
				++i;
			}
			GNdel.dispose();
			listeNoeuds.remove(GNdel);
			--nbrNodes;
		}
	}

	public String[] ListToStatic(ArrayList<String> T) {
		String resultfinal[] = new String[T.size()];
		T.toArray(resultfinal);
		return resultfinal;
	}
	
/*	private ArrayList<String> StaticToList(String T[]) {
		
		return null;
	}*/

	public String[] sons(String which) {
		return sons(which, true);
	}

	public String[] sons(String which, Boolean display) {
		int i = 0;
		ArrayList<String> T = new ArrayList<String>();
		Node Nwhich = this.findNode(which);

		if (Nwhich == null) {
			System.out.println("Erreur sons : Personne"
					+ ((Nwhich == null) ? "s" : "") + " non existante"
					+ ((Nwhich == null) ? "s" : "") + " !");
		} else {
			GraphNode GNwhich = this.findGNode(which);

			for (GraphConnection l : listeLinks)
				if (l.getSource().equals(GNwhich)) {
					T.add(l.getDestination().getText());
					++i;
				}
			if (i == 0)
				System.out.println("Cette personne ne possède aucun enfant...");
			else if (display) {
				if (this.findNode(which) == null)
					addNode("." + which + ".");
				for (i = 0; i < listeLinks.size() && i < T.size(); i++) {
					addNode("." + T.get(i).toString() + ".");
					addLink("." + which + ".", "." + T.get(i).toString() + ".");
				}
			}
		}
		return T;
	}

	public String[] father(String which) {
		int i = 0;
		ArrayList<String> T = new ArrayList<String>();
		Node Nwhich = this.findNode(which);

		if (Nwhich == null) {
			System.out.println("Erreur sons : Personne non existante !");
		} else {
			GraphNode GNwhich = this.findGNode(which);

			for (GraphConnection l : listeLinks)
				if (l.getDestination().equals(GNwhich)) {
					T.add(l.getSource().getText());
					++i;
				}
			if (i == 0)
				System.out.println("Cette personne ne possede aucun enfant...");
			else {
				if (this.findNode(which) == null)
					addNode("." + which + ".");
				for (i = 0; i < listeLinks.size() && i < T.size(); ++i) {
					addNode("." + T.get(i).toString() + ".");
					addLink("." + T.get(i).toString() + ".", "." + which + ".");
				}
			}
		}
		return T;
	}

	public void desc(String which) {
		int i = 0;
		String T[] = sons(which);

		while (T[i] != "-1")
			desc(T[i++]);
	}

	public void asc(String which) {
		int i = 0;
		String T[] = father(which);
		while (T[i] != "-1")
			asc(T[i++]);
	}

	public ArrayList<String> oncles(String which) {
		ArrayList<String> Result = null;
		if(findNode(which)!=null) {
			ArrayList<String> T = father(which); // liste des pÃ¨res
			ArrayList<String> GP = new ArrayList<String>();
			for (String papa : T) {
				ArrayList<String> temp = father(papa);
				for (String output : temp)
					if (!GP.contains(output))
						GP.add(output);
			}
			// on a tous les grands pÃ¨res en liste unique
			Result = new ArrayList<String>();
			for (String papi : GP) {
				ArrayList<String> temp = sons(papi, false);
				for (String marredeslocales : temp)
					// Ai-je dÃ©jÃ  stockÃ© le rÃ©sultat ? Suis-je mon propre pÃ¨re ?
					if (!(Result.contains(marredeslocales) || T.contains(marredeslocales)))
						Result.add(marredeslocales);
			}
		} else {
			System.out.println("Erreur oncles : Node cible non trouvÃ©");
		}
		return Result;
	}
	
	public ArrayList<String> cousins(String which) {
		ArrayList<String> Result = null;
		
		if(findNode(which)!=null) {
			// On rÃ©utilise oncles() pour sauter des Ã©tapes dans la fonction
			ArrayList<String> T = oncles(which);
			Result=new ArrayList<String>();
			for(String papi : T) {
				ArrayList<String> temp = sons(papi, false);
				for(String locale : temp)
				// Ne rajoutons que si ce n'est pas dÃ©jÃ  fait
				// On a dÃ©jÃ  supprimÃ© les pÃ¨res via la fonction oncles(), donc, inutile en thÃ©orie de vÃ©rifier que nous ne sommes pas notre propre cousin
					if(!Result.contains(locale))
						Result.add(locale);
			}
		} else {
			System.out.println("Erreur cousins : Node cible non trouvÃ©");
		}
		return Result;
	}
	
	public ArrayList<String> onclesCom(String which,String who) {
		ArrayList<String> Result = null;
		if(findNode(which)==null || findNode(who)==null) {
			System.out.println("Erreur compOncles : Node introuvable");
		} else {	
			ArrayList<String> onclesWho = oncles(who);
			ArrayList<String> onclesWhich = oncles(which);
			Result = new ArrayList<String>();
			// Le tableau parcouru n'a que peu d'importance
			for(String comp : onclesWho)
				if(onclesWhich.contains(comp))
					Result.add(comp);
		}
		return Result;
	}
	
	public ArrayList<String> cousinsCom(String which,String who) {
		ArrayList<String> Result = null;
		if(findNode(which)==null || findNode(who)==null) {
			System.out.println("Erreur compOncles : Node introuvable");
		} else {
			ArrayList<String> onclesWho = cousins(who);
			ArrayList<String> onclesWhich = cousins(which);
			Result = new ArrayList<String>();
			// Le tableau parcouru n'a que peu d'importance
			for(String comp : onclesWho)
				if(onclesWhich.contains(comp))
					Result.add(comp);
		}
		return Result;
	}

	public void addLink(String from, String to) {
		Node Nfrom = this.findNode(from), Nto = this.findNode(to);
		if (Nfrom == null || Nto == null) {
			System.out.println("Erreur addLink : Personne"
					+ ((Nfrom == null && Nto == null) ? "s" : "")
					+ " non existante"
					+ ((Nfrom == null && Nto == null) ? "s" : "") + " !");
		} else {
			myNetwork.connect(Nfrom, Nto);
			GraphNode GNfrom = this.findGNode(from);
			GraphNode GNto = this.findGNode(to);
			listeLinks.add(new GraphConnection(myGraph, ZestStyles.NONE,
					GNfrom, GNto));
		}
	}

	public void delLink(String from, String to) {
		int i = 0;
		Node Nfrom = findNode(from), Nto = findNode(to);
		GraphNode GNfrom = findGNode(from);
		
		if (Nfrom == null || Nto == null)
			System.out.println("Erreur delLink : Personne"
					+ ((Nfrom == null && Nto == null) ? "s" : "") + " non existante"
					+ ((Nfrom == null && Nto == null) ? "s" : "") + " !");
		else {
			for (GraphConnection l : listeLinks) {
				if (l.getSource().equals(GNfrom)) {
					break;
				}
				++i;
			}
			if (i < listeLinks.size()) {
				myNetwork.disconnect(Nfrom, Nto);
				listeLinks.get(i).dispose();
				listeLinks.remove(i);
			} else {
				System.out.println("Erreur delLink : Lien non existant !");
			}
		}
	}

	private String formatText(String unf_str) {
		String for_str = unf_str;
		for_str = for_str.replace("\n", "");
		for_str = for_str.replace("\t", "");
		return for_str;
	}

	public void loadData(Document XMLFile) {
		XMLFile.getDocumentElement().normalize(); // Permet d'éviter les XML mal
													// écrits
		NodeList personnes = XMLFile.getElementsByTagName("personne");
		NodeList liens = XMLFile.getElementsByTagName("fils");
		// Chaque node défini est rajouté
		for (int i = 0; i < personnes.getLength(); ++i)
			addNode(formatText(personnes.item(i).getTextContent()));
		// On rajoute les liens après
		for (int i = 0; i < liens.getLength(); ++i)
			addLink(formatText(((Element) liens.item(i)).getAttribute("pere")),
					formatText(((Element) liens.item(i)).getAttribute("enfant")));
	}

	public void saveData() {
		saveData("temp.xml");
	}
	
	public void saveData(String Fils) {
		// si pas d'extension XML, l'ajouter
		if(!Fils.substring(Fils.length()-4,Fils.length()).equalsIgnoreCase(".xml")) {
			StringBuilder pattEx = new StringBuilder();
			pattEx.append(Fils+".xml");
			Fils=pattEx.toString();
		}
		// Sauvegarde
		try {
			Document SaveXML = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			// créer le root
			Element root = SaveXML.createElement("company");
			SaveXML.appendChild(root);
			// On enregistre les nodes...
			for(Node N : myNetwork.nodes()) {
				Element temp = SaveXML.createElement("personne");
				temp.setAttribute("id", N.getName());
				temp.appendChild(SaveXML.createElement("personne").appendChild(SaveXML.createTextNode(N.getName())));
				root.appendChild(temp);
			}
			// Maintenant, les links !
			for(Link l : myNetwork.links()) {
				Element temp = SaveXML.createElement("fils");
				temp.setAttribute("pere",l.source().toString().substring(1,l.source().toString().length()-1));
				temp.setAttribute("enfant",l.destination().toString().substring(1,l.destination().toString().length()-1));
				root.appendChild(temp);
			}
			// Enfin, on écrit tout cela
			Transformer useless = TransformerFactory.newInstance().newTransformer();
			useless.transform(new DOMSource(SaveXML), new StreamResult(new File(Fils)));
		} catch (Exception e) {
			System.out.println("Erreur saveData : Impossible de sauvegarder dans "+Fils+" !");
		}
	}
}
