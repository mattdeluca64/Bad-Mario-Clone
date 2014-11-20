package org.util.math;
/*
 * useful for counting "islands" (1's connected in 8 directions)
 * 10011
 * 00011
 * 10000
 * 01100
 * there are three islands
 *
 */

public class DFS{
	private static int ROW;
	private static int COL;
	private static int[][] M;

	public DFS(int[][] M,int rows,int cols){
		this.ROW = rows;
		this.COL = cols;
		this.M = M;
	}

	public int getConnected(){
		//---------------------------------------------
		//create 5x5 grid of falses visited
		boolean visited[][] = new boolean[ROW][COL];
		for(int i=0;i<ROW;i++){
			for(int j=0;j<COL;j++){
				visited[i][j] = false;
			}
		}
		//---------------------------------------------
		//init count as 0 and traverse through all the cells of given matrix
		//return count
		//everytime count increments, an island is found 
		//---------------------------------------------
		int count = 0;
		for(int i=0;i<ROW;++i){
			for(int j=0;j<COL;++j){
				if(this.M[i][j]==1 && !visited[i][j]){
					DFS(this.M,i,j,visited);
					++count;
					//System.out.println("row/col of notvisited and 1 found-- "+i+" "+j);
				}
			}
		}
		return count;
	}
	private void DFS(int M[][],int row,int col,boolean visited[][]){
		//---------------------------------------------
		//used to get row/col numbers of 8 neighbors
		int rowNbr[] = {-1,-1,-1,0,0,1,1,1};
		int colNbr[] = {-1, 0, 1,-1,1,-1,0,1};
		//---------------------------------------------
		//mark as visited
		visited[row][col] = true;
		for(int i=0;i<8;++i){
			if(isSafe(this.M,row+rowNbr[i],col+colNbr[i],visited)){
				//System.out.println("inside dfs! - row: "+row+" col: "+col);
				DFS(this.M,row+rowNbr[i],col+colNbr[i],visited);
			}
		}
	}
	public boolean isSafe(int M[][],int row,int col,boolean visited[][]){
		//checks if its a good to go index
		if((row>=0) && (row < ROW) && (col >= 0) && (col < COL) && (this.M[row][col]==1) && (!visited[row][col])){
			return true;
		} else{
			return false;
		}
	}
}
					
