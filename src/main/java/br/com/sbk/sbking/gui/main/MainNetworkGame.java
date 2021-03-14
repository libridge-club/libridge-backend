package br.com.sbk.sbking.gui.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.gui.frames.KingNetworkClientScreen;
import br.com.sbk.sbking.gui.frames.NetworkClientScreen;

public class MainNetworkGame {

	final static Logger logger = LogManager.getLogger(MainNetworkGame.class);

	public static void main(String args[]) {
		NetworkClientScreen networkClientScreen = new KingNetworkClientScreen();
		networkClientScreen.run();
		logger.info("Exiting main thread.");
	}

}
