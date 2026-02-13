package game_of_life;

public class GolLogic
{
	int rows, cols;
	int toggled_row = -1,
		toggled_col = -1;
	
	public int[][] GRID;
	boolean updating = false;
	
	public GolLogic(int rows, int cols)
	{
//		rows += 2 * buffer;
//		cols += 2 * buffer;
		
		this.rows = rows;
		this.cols = cols;
		GRID = new int[rows][cols];
		for(int i = 0; i < rows ; i++)
		{
			for(int j = 0; j < cols; j++)
				GRID[i][j] = 0;
		}
	}
	
	/*
	 * Game of Life rules:
	    1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
		2. Any live cell with two or three live neighbours lives on to the next generation.
		3. Any live cell with more than three live neighbours dies, as if by overpopulation.
		4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
	 */
	public void update()
	{
		updating = true;
		int[][] GRID = new int[rows][cols];
		for(int i = 0; i < rows; i++)
			System.arraycopy(this.GRID[i], 0, GRID[i], 0, cols);
		
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{
				int n = getNeighbours(i, j);
				
				if(this.GRID[i][j] == 1)
				{
					if(n < 2)
						GRID[i][j] = 0;
					else if(n > 3)
						GRID[i][j] = 0;
				}
				else if(this.GRID[i][j] == 0 && n == 3)
					GRID[i][j] = 1;
			}
		}
		this.GRID = GRID;
	}
	
	private int getNeighbours(int current_row, int current_col)
	{
		int alive_neighbours = 0;
		int[] neighbour_indices = {-1, 0, 1};
		for(int i : neighbour_indices)
		{
			for(int j : neighbour_indices)
			{
				int row = current_row + i;
				int col = current_col + j;
				
				if((0 <= row && row < rows) && (0 <= col && col < cols))
				{
					if(i == 0 && j == 0)
						continue;
					if(GRID[row][col] == 1)
						alive_neighbours++;
				}
			}
		}
		
		return alive_neighbours;
	}
	/*
	public void clearBufferzone()
	{
		for(int col = buffer; col < cols - buffer; col++)
		{
			if(GRID[0][col] == 1)
				GRID[rows - (2 * buffer)][col] = 1;
			if(GRID[rows - buffer][col] == 1)
				GRID[buffer][col] = 1;
		}
		for(int row = buffer; row < rows - buffer; row++)
		{
			if(GRID[row][0] == 1)
				GRID[row][cols - (2 * buffer)] = 1;
			if(GRID[row][cols - (2 * buffer)] == 1)
				GRID[row][0] = 1;
		}
	}
	 */
	public synchronized boolean isUpdating()
	{
		return updating;
	}
	public void setUpdating(boolean updating)
	{
		this.updating = updating;
	}
	
	public void toggleCell(int row, int col, String mouse_btn)
	{
		if(mouse_btn.equals("rmb"))
			GRID[row][col] = 0;
		else if(mouse_btn.equals("lmb"))
			GRID[row][col] = 1;
	}
}
