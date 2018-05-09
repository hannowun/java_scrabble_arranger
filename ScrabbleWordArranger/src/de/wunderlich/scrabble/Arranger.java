package de.wunderlich.scrabble;

import java.util.ArrayList;
import java.util.HashSet;

public class Arranger {

	public static int BOARD_SIZE=15;
	
	ArrayList<String> words;
	
	ScrabbleBoard board;
	
	HashSet<ScrabbleBoard> foundResults;
	
	public Arranger(String[] args) {
		words=new ArrayList<>();
		for(String arg:args) {
			words.add(arg.toUpperCase());
		}
		board=new ScrabbleBoard(BOARD_SIZE);
		foundResults=new HashSet<>();
	}

	public void arrange() {
		recArrange(board, words);
		
	}
	
	public void recArrange(ScrabbleBoard b, ArrayList<String> wordsLeft) {
		if(wordsLeft.isEmpty()) {
			foundResults.add(b);
		}
		for(int orientation=0; orientation<=1;orientation++) {
			for(String word:wordsLeft) {
				ArrayList<String> tmpWordsLeft=new ArrayList<>(wordsLeft);
				tmpWordsLeft.remove(word);
				//System.out.println("RecArrange:");
				//System.out.println(b.toString());
				//System.out.println("Orientation:"+orientation+" word:"+word+" wordsLeft:"+tmpWordsLeft);
				if(b.isEmpty()) {
					ScrabbleBoard tmpBoard=b.getCopy();
					try {
						tmpBoard.placeWord(word,orientation,BOARD_SIZE/2+1,BOARD_SIZE/2+1,word.length()/2+1);
						recArrange(tmpBoard, tmpWordsLeft);
					}catch(CollisionException ce) {
						//System.out.println(ce.getMessage());
					}
				}else {
					for(int x=0;x<BOARD_SIZE;x++) {
						for (int y=0;y<BOARD_SIZE;y++) {
							char boardChar=b.getCharAt(x, y);
							if(boardChar==' ') {
								continue;
							}
							for(int i=0;i<word.length();i++) {
								if(word.charAt(i)==boardChar) {
									ScrabbleBoard tmpBoard=b.getCopy();
									int hash=tmpBoard.hashCode();
									try {
										tmpBoard.placeWord(word, orientation, x, y, i);
										if(tmpBoard.hashCode()==hash) {
											break;
										}
										recArrange(tmpBoard, tmpWordsLeft);
									}catch(CollisionException ce) {
										//System.out.println(ce.getMessage());
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void printResults() {
		int index=0;
		for(ScrabbleBoard oneBoard:foundResults) {
			index++;
			System.out.println("Board :"+index);
			System.out.println(oneBoard.toString());
		}
		
	}

}

