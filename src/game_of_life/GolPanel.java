package game_of_life;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class GolPanel extends JPanel implements Runnable, MouseMotionListener
{
	GolFrame gol_frame;
	GolLogic logic;
	int[][] GRID;
	int rows, cols;
	int scale;
	public void setScale(int scale)
	{
		this.scale = scale;
	}
	public Thread thread;
	String mouse_btn = "";
	
	public GolPanel(GolFrame golFrame, int rows, int cols, int top_panel_height, int scale, GolLogic logic)
	{
		super();
		
		setPreferredSize(new Dimension(scale * cols, scale * rows));
		setLocation(0, scale * top_panel_height);
		addMouseMotionListener(this);

		this.gol_frame = golFrame;
		
		if(logic == null)
			logic = new GolLogic(rows, cols);
		this.logic = logic;
		
		this.rows = rows;
		this.cols = cols;
		this.scale = scale;
		
		addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent press)
			{
				if(press.getButton() == 1)
					mouse_btn = "lmb";
				else if(press.getButton() == 3)
					mouse_btn = "rmb";
				else
					mouse_btn = "";
			}
		});
		
		golFrame.setLogic(logic);
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run()
	{
		while(thread != null)
		{
			double delta = 0;
			long last_time = System.nanoTime();
			long current_time;
			
			while(!gol_frame.isResizing() && !gol_frame.isPaused())
			{
				double draw_interval = Math.pow(10, 9);
				draw_interval /= gol_frame.FPS;
				current_time = System.nanoTime();
				delta += (current_time - last_time) / draw_interval;
				last_time = current_time;
				if(delta >= 1)
				{
					// If previous update is finished
					if(!logic.isUpdating())
					{
						logic.update();
						repaint();
						logic.setUpdating(false);
					}
					
					delta = 0;
				}
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g_1d)
	{
		super.paintComponent(g_1d);
		Graphics2D g_2d = (Graphics2D) g_1d;
		
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{				
				switch(logic.GRID[i][j])
				{
					case 0:
						g_2d.setColor(Color.BLACK);
						break;
					case 1:
						g_2d.setColor(Color.WHITE);
						break;
				}
				int x = scale * (j - 0);
				int y = scale * (i - 0);
				g_2d.fillRect(x, y, scale, scale);
			}
		}
		
		g_2d.dispose();
	}

	@Override
	public void mouseDragged(MouseEvent dragged)
	{
		if(gol_frame.isPaused())
		{
			int col = dragged.getX() / scale;
			int row = dragged.getY() / scale;
			if((0 <= col && col < cols) &&
					(0 <= row && row < rows))
			{
				logic.toggleCell(row, col, mouse_btn);
				repaint();
			}
		}
	}
	@Override
	public void mouseMoved(MouseEvent e){}

	public void randomCells()
	{
		logic.setUpdating(true);
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{
				int r =(int) (Math.random() * 3);
				if(r == 0)
					logic.GRID[i][j] = 1;
				else
					logic.GRID[i][j] = 0;					
			}
		}
		logic.setUpdating(false);
		repaint();
	}
}