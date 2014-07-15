package org.peerbox.controller;


import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class MainNavigator {
	
	private static MainController mainController;
	private static ObservableList<Node> pages = FXCollections.observableArrayList();
	
	public static void setMainController(MainController controller) {
		mainController = controller;
	}
	
	public static void navigate(String fxmlFile) {
		Node content = null;
		try {
			content = FXMLLoader.load(MainController.class.getResource(fxmlFile));
			mainController.setContent(content);
			pages.add(content);
		} catch(IOException e) {
			System.err.println(String.format("Could not load fxml file: %s", e.getMessage()));
		}
	}
	
	
	public static boolean canGoBack() {
		return pages.size() >= 2;
	}
	
	public static void goBack() {
		if(canGoBack()) {
			int previous = pages.size()-2;
			pages.remove(pages.size()-1);
			mainController.setContent(pages.get(previous));
		} else {
			// TODO: what should we do here? Throw exception, do nothgin, log, ... :)
			System.err.println("Cannot go back.");
		}
	}
}
