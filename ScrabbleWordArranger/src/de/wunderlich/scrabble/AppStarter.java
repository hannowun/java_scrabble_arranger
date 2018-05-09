package de.wunderlich.scrabble;

public class AppStarter {

	public static void main(String[] args) {
		Arranger a=new Arranger(args);
		a.arrange();
		a.printResults();
	}

}
