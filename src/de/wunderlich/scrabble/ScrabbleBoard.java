package de.wunderlich.scrabble;

public class ScrabbleBoard {
	
	public static final int HORIZONTAL=0;

	public static final int VERTICAL=1;
	
	char[][] board;
	
	boolean isEmpty;
	
	public ScrabbleBoard(int size) {
		board=new char[size][size];
		for(int x=0;x<board.length;x++) {
			for(int y=0;y<board[x].length;y++) {
				board[x][y]=' ';
			};
		}
		isEmpty=true;
	}
	
	private ScrabbleBoard(char[][] sourceBoard,boolean sourceIsEmpty) {
		board=new char[sourceBoard.length][sourceBoard.length];
		for(int x=0;x<board.length;x++) {
			for(int y=0;y<board[x].length;y++) {
				board[x][y]=sourceBoard[x][y];
			};
		}
		isEmpty=sourceIsEmpty;
	}
	
	public char getCharAt(int x, int y) {
		return board[x][y];
	}
	
	public void setCharAt(int x, int y, char c) throws CollisionException {
		if(x>=board.length) {
			throw new CollisionException("right side out of board");
		}
		if(y>=board.length) {
			throw new CollisionException("lower side out of board");
		}
		if(board[x][y]!=' '&&board[x][y]!=c) {
			throw new CollisionException("Wanted to place char:"+c+" already found char:"+board[x][y]);
		}
		board[x][y]=c;
		isEmpty=false;
	}
	
	public String toString() {
		String toReturn=" ";
		for(int x=0;x<board.length;x++) {
			toReturn+=x%10;
		}
		toReturn+="\n";
		for(int x=0;x<board.length;x++) {
			toReturn+=x%10;
			for(int y=0;y<board[x].length;y++) {
				toReturn+=board[x][y];
			}
			toReturn+="\n";
		}
		return toReturn;
	}
	
	public ScrabbleBoard getCopy() {
		ScrabbleBoard tmp=new ScrabbleBoard(board, isEmpty);
		return tmp;
	}
	
	public boolean isEmpty() {
		return isEmpty;
	}

	public void placeWord(String word, int orientation, int x, int y, int wordOffset) throws CollisionException {
		if(orientation==VERTICAL) {
			if(x-wordOffset<0) {
				throw new CollisionException("1upper side out of board");
			}else if(x-wordOffset-1>=0 && getCharAt(x-wordOffset-1, y)!=' ') {
				throw new CollisionException("1word interfering upper side");
			}else if(x-wordOffset+word.length()<board.length && getCharAt(x-wordOffset+word.length(),y)!=' ') {
				throw new CollisionException("1word interfering lower side");
			}else if(x-wordOffset+word.length()>=board.length) {
				throw new CollisionException("1lower side out of board");
			}
			for(int tx=0;tx<word.length();tx++) {
				if(wordOffset==tx) {
					setCharAt(x-wordOffset+tx, y, word.charAt(tx));
					continue;
				}
				if(y-1<0||getCharAt(x-wordOffset+tx, y-1)==' ') {
					if(y+1>=board.length||getCharAt(x-wordOffset+tx, y+1)==' ') {
						setCharAt(x-wordOffset+tx, y, word.charAt(tx));
					}else {
						throw new CollisionException("2word interfering lower side");
					}
				}else {
					throw new CollisionException("2word interfering upper side");
				}
			}
		}else {
			if(y-wordOffset<0) {
				throw new CollisionException("3left side out of board");
			}else if(y-wordOffset-1>=0 && getCharAt(x,y-wordOffset-1)!=' ') {
				throw new CollisionException("3word interfering left side");
			}else if(y-wordOffset+word.length()<board.length && getCharAt(x,y-wordOffset+word.length())!=' ') {
				throw new CollisionException("3word interfering right side");
			}else if(y-wordOffset+word.length()>=board.length) {
				throw new CollisionException("3right side out of board");
			}
			
			for(int ty=0;ty<word.length();ty++) {
				if(ty==wordOffset) {
					setCharAt(x,y-wordOffset+ty, word.charAt(ty));
					continue;
				}
				if(x-1<0||getCharAt(x-1,y-wordOffset+ty)==' ') {
					if(x+1>=board.length||getCharAt(x+1,y-wordOffset+ty)==' ') {
						setCharAt(x,y-wordOffset+ty, word.charAt(ty));
					}else if(getCharAt(x,y-wordOffset+ty)==word.charAt(ty)){
						//do nothing everything is ok
					}else {
						throw new CollisionException("4word interfering right side");
					}
				}else if(getCharAt(x,y-wordOffset+ty)==word.charAt(ty)){
					//do nothing everything is ok
				}else {
					throw new CollisionException("4word interfering left side");
				}
			}
		}
		
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.hashCode()==obj.hashCode();
	}
	
	
	@Override
	public int hashCode() {
		return getMinimalString().hashCode();
	}

	public String getMinimalString() {
		int xoffset=0;
		int yoffset=0;
		int xmax=board.length-1;
		int ymax=board.length-1;
		boolean foundXoffset=false;
		boolean foundYoffset=false;
		boolean foundXmax=false;
		boolean foundYmax=false;
		for(int x=0;x<board.length&&!foundXoffset;x++) {
			for(int y=0;y<board.length;y++) {
				if(board[x][y]!=' ') {
					xoffset=x;
					foundXoffset=true;
					break;
				}
			}
			
		}
		for(int y=0;y<board.length&&!foundYoffset;y++) {
			for(int x=0;x<board.length;x++) {
				if(board[x][y]!=' ') {
					yoffset=y;
					foundYoffset=true;
					break;
				}
			}	
		}
		for(int x=board.length-1;x>=0&&!foundXmax;x--) {
			for(int y=0;y<board.length;y++) {
				if(board[x][y]!=' ') {
					xmax=x;
					foundXmax=true;
					break;
				}
			}
		}
		for(int y=board.length-1;y>=0&&!foundYmax;y--) {
			for(int x=0;x<board.length;x++) {
				if(board[x][y]!=' ') {
					ymax=y;
					foundYmax=true;
					break;
				}
			}
		}
		String toReturn="";
		for(int x=xoffset;x<=xmax;x++) {
			for(int y=yoffset;y<=ymax;y++) {
				toReturn+=board[x][y]==' '?'#':board[x][y];
			}
			toReturn+="\n";
		}
		return toReturn;
	}
}
