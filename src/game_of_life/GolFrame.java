package game_of_life;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GolFrame implements ActionListener
{
	JFrame frame;
	
	int rows, cols;
	int scale = 7;
	int top_panel_height = 20;
		
	int min_scale = 3;
	int max_scale = 9;
	public int FPS = 18;

	boolean resizing = false;
	boolean paused = true;
	
	GolLogic logic;
	GolPanel golPanel;
	JPanel top;
	
	TopButton rng_btn, size_minus_optn, size_plus_optn, speed_minus_optn, pause_optn, speed_plus_optn;
	
	public GolFrame(int rows, int cols, GolLogic logic)
	{
		this.rows  = rows;
		this.cols  = cols;
		this.logic = logic;
		
		frame = new JFrame();
		frame.setTitle("Game of Life");
		frame.setUndecorated(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		updateFrame(logic);
	}
	
	public void setLogic(GolLogic logic)
	{
		this.logic = logic;
	}

	// Add all GUI-comps.
	public void updateFrame(GolLogic logic)
	{
		resizing = true;
		frame.getContentPane().removeAll();
		
		GridLayout top_grid	= new GridLayout(1, 9);
		
		Container container = frame.getContentPane();
		container.setLayout(new BorderLayout());
		
		top = new JPanel();
		top.setPreferredSize(new Dimension(scale * cols, scale * top_panel_height));
		top.setLocation(0, 0);
		top.setLayout(top_grid);
		container.add(top, BorderLayout.NORTH);		
			Font font1 = new Font("Arial", Font.BOLD, scale / 2);
			Font font2 = new Font("Arial", Font.PLAIN, scale );
			
				String rng_btn_label = getAsciiAsString("rng");
			rng_btn = new TopButton(rng_btn_label, 2, 1);
			rng_btn.addActionListener(this);
			rng_btn.setFont(font2);
			top.add(rng_btn);
			
				String size_minus_optn_label = getAsciiAsString("monitor_decrease");
			size_minus_optn = new TopButton(size_minus_optn_label, 1, 1);
			size_minus_optn.addActionListener(this);
			size_minus_optn.setFont(font1);
			size_minus_optn.setForeground(Color.WHITE);
			top.add(size_minus_optn);
			
				String size_plus_optn_label = getAsciiAsString("monitor_increase");
			size_plus_optn = new TopButton(size_plus_optn_label, 2, 2);
			size_plus_optn.addActionListener(this);
			size_plus_optn.setFont(font1);
			top.add(size_plus_optn);
			
				String label_game_label = getAsciiAsString("Game");
			JLabel label_game = new JLabel(label_game_label, JLabel.RIGHT);
			label_game.setBackground(Color.BLACK);
			label_game.setForeground(Color.GRAY);
			label_game.setBorder(BorderFactory.createMatteBorder(0, 2, 4, 0, Color.GRAY));
			label_game.setFont(font2);
			label_game.setForeground(Color.WHITE);
			label_game.setOpaque(true);
			top.add(label_game);
			
				String label_of_label = getAsciiAsString("of");
			JLabel label_of = new JLabel(label_of_label, JLabel.CENTER);
			label_of.setBackground(Color.BLACK);
			label_of.setForeground(Color.GRAY);
			label_of.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, Color.GRAY));
			label_of.setFont(font2);
			label_of.setForeground(Color.WHITE);
			label_of.setOpaque(true);
			top.add(label_of);
			
				String label_life_label = getAsciiAsString("Life");
			JLabel label_life = new JLabel(label_life_label, JLabel.LEFT);
			label_life.setBackground(Color.BLACK);
			label_life.setForeground(Color.GRAY);
			label_life.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 1, Color.GRAY));
			label_life.setFont(font2);
			label_life.setForeground(Color.WHITE);
			label_life.setOpaque(true);
			top.add(label_life);
		
				String speed_minus_optn_label = getAsciiAsString("speed_decrease");
			speed_minus_optn = new TopButton(speed_minus_optn_label, 2, 1);
			speed_minus_optn.addActionListener(this);
			speed_minus_optn.setFont(font2);
			top.add(speed_minus_optn);

			String pause_optn_str = null;
			if(paused)
				pause_optn_str = getAsciiAsString("play");
			else if(!paused)
				pause_optn_str = getAsciiAsString("pause");
			pause_optn = new TopButton(pause_optn_str, 1, 1);
			pause_optn.addActionListener(this);
			pause_optn.setFont(font2);
			top.add(pause_optn);
			
				String speed_plus_optn_label = getAsciiAsString("speed_increase");
			speed_plus_optn = new TopButton(speed_plus_optn_label, 1, 2);
			speed_plus_optn.addActionListener(this);
			speed_plus_optn.setFont(font2);
			top.add(speed_plus_optn);

		if(!frame.isVisible())
			frame.setVisible(true);
		
		if(golPanel == null)
			golPanel = new GolPanel(this, rows, cols, top_panel_height, scale, logic);			
		else
		{
			golPanel.setScale(scale);
			golPanel.setPreferredSize(new Dimension(scale * cols, scale * rows));
			golPanel.setLocation(0, scale * top_panel_height);
			
		}
		container.add(golPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		resizing = false;
	}
	
	private String getAsciiAsString(String filename)
	{
		StringBuffer sb = new StringBuffer("<html><pre>");
		String path = "ascii\\";
		path += filename;
		path += ".txt";
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(path));
			Scanner file_reader = new Scanner(reader);
			
			while(file_reader.hasNextLine())
			{
				// Attach ASCII row by row
				String curr_row = file_reader.nextLine();
				sb.append(curr_row);
				sb.append("<br/>");
			}
			sb.append("</pre></html>");
			file_reader.close();			
			reader.close();
		} catch(Exception e)
		{
			return "Couldn't load ascii file";
		}
		
		return sb.toString();
	}
	
	private void togglePause()
	{
		if(paused)
		{
			paused = false;
			pause_optn.setText("||");
		}
		else if(!paused)
		{
			paused = true;
			pause_optn.setText("I>");
		}
		updateFrame(logic);
	}

	public synchronized boolean isResizing()
	{
		return resizing;
	}
	
	public synchronized boolean isPaused()
	{
		return paused;
	}

	// Which button pressed
	@Override
	public void actionPerformed(ActionEvent action)
	{
		Object src = action.getSource();

		if(src == rng_btn)
			golPanel.randomCells();
		else if(src == size_minus_optn && scale > min_scale)
		{
			scale--;
			updateFrame(logic);
		}
		else if(src == size_plus_optn && scale < max_scale)
		{
			scale++;
			updateFrame(logic);
		}
		else if(src == speed_minus_optn && FPS > 1)
			FPS--;
		else if(src == pause_optn)
			togglePause();
		else if(src == speed_plus_optn)
			FPS++;
	}
}