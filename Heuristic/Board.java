import java.util.ArrayList;
public class Board {
	public static int rows=5;
	public static int columns=3;
	private Board parent = null; /* only initial board's parent is null */
	private int costToCurrState;
	public int[][] tiles;

	public Board(int[][] cells)                 //Populate states
	{
		tiles = new int[rows][columns];
		for (int i = 0 ;i<rows; i++)
			for (int j = 0; j<columns; j++)
			{
				tiles[i][j] = cells[i][j];
			}
		this.costToCurrState = 0;
	}
	public boolean equals(Object x)         //Can be used for repeated state checking
	{
		Board another = (Board)x;
		if (columns != another.columns || rows != another.rows) return false;
		for (int i = 0; i<rows; i++)
			for (int j = 0; j<columns; j++)
				if (tiles[i][j] != another.tiles[i][j])
					return false;
		return true;
	}
	public int compare(Object x){
		int result = 0;
		Board another = (Board)x;
		for (int i = 0; i<rows; i++)
			for (int j = 0; j<columns; j++)
				if (this.tiles[i][j] != another.tiles[i][j])
					result++;
		return result;
	}
	public int manhattan(Object x){
		int result = 0;
		Board another = (Board)x;
		for (int i = 0; i<rows; i++){
			for (int j = 0; j<columns; j++){
				int curr = tiles[i][j];
				int k = 0;
				int l = 0;
				LOOP:		for(k = 0; k<rows; k++){
					for(l = 0; l<columns; l++){
						if(another.tiles[k][l]==curr)
							break LOOP;
					}
				}
				result += (Math.abs(k-i) + Math.abs(l-j));
			}
		}
		return result;
	}

	public ArrayList<Board> getSuccessors()     //Use cyclic ordering for expanding nodes - Right, Down, Left, Up
	{
		/* TODO */
		ArrayList<Board> result = new ArrayList<Board>();
		int i = 0;
		int j = 0;
		LOOP:		for(i = 0; i<rows; i++){
			for(j = 0; j<columns; j++){
				if(tiles[i][j]==0)
					break LOOP;
			}
		}
		if(j + 1 <= columns -1 && tiles[i][j+1]!= -1 ){
			int childTiles[][] = arrcopy(tiles);
			childTiles[i][j] = childTiles[i][j + 1];
			childTiles[i][j + 1] = 0;
			Board childBoard = new Board(childTiles);
			childBoard.setCostToCurrState(this.getCostToCurrState()+1);
			childBoard.setParent(this);
			result.add(childBoard);
			//	childBoard.print();
		}
		if(i + 1 <= rows - 1 && tiles[i+1][j]!= -1){
			int childTiles[][] = arrcopy(tiles);
			childTiles[i][j] = childTiles[i + 1][j];
			childTiles[i + 1][j] = 0;
			Board childBoard = new Board(childTiles);
			childBoard.setCostToCurrState(this.getCostToCurrState()+1);
			childBoard.setParent(this);
			result.add(childBoard);
			//childBoard.print();
		}
		if(j - 1 >= 0 && tiles[i][j-1]!= -1){
			int childTiles[][] = arrcopy(tiles);
			childTiles[i][j] = childTiles[i][j - 1];
			childTiles[i][j - 1] = 0;
			Board childBoard = new Board(childTiles);
			childBoard.setCostToCurrState(this.getCostToCurrState()+1);
			childBoard.setParent(this);
			result.add(childBoard);
			//childBoard.print();
		}
		if(i - 1 >= 0 && tiles[i-1][j]!= -1){
			int childTiles[][] = arrcopy(tiles);
			childTiles[i][j] = childTiles[i - 1][j];
			childTiles[i - 1][j] = 0;
			Board childBoard = new Board(childTiles);
			childBoard.setCostToCurrState(this.getCostToCurrState()+1);
			childBoard.setParent(this);
			result.add(childBoard);
			//childBoard.print();
		}
		return result;
	}

	public void print()
	{
		for (int i = 0; i<rows; i++)
		{
			for (int j = 0 ;j<columns; j++)
			{
				if (j > 0) System.out.print("\t");
				System.out.print(tiles[i][j]);
			}
			System.out.println();
		}
	}

	public void setParent(Board parentBoard)
	{
		parent = parentBoard;
	}

	public Board getParent()
	{
		return parent;
	}
	public void setCostToCurrState(int cost){
		this.costToCurrState = cost;
	}
	public int getCostToCurrState(){
		return this.costToCurrState;
	}
	public int[][] arrcopy(int[][] matrix){
		int [][] myInt = new int[matrix.length][];
		for(int i = 0; i < matrix.length; i++)
		{
			int[] aMatrix = matrix[i];
			int   aLength = aMatrix.length;
			myInt[i] = new int[aLength];
			System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
		}
		return myInt;
	}

}
