package game_of_life;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GolFrame extends JFrame implements ActionListener
{
	int rows, cols;
	int scale = 70;
	int top_panel_height = 2;
		
	int min_scale = 20;
	int max_scale = 100;
	public int FPS = 6;

	boolean fullscreen = false;
	boolean resizing = false;
	boolean paused = true;
	
	GolLogic logic;
	GolPanel golPanel;
	JPanel top;
	
	TopButton size_minus_optn, size_plus_optn, speed_minus_optn, pause_optn, speed_plus_optn;
	
	public GolFrame(int rows, int cols, GolLogic logic)
	{
		super("Game of Life");
		
		this.rows  = rows;
		this.cols  = cols;
		this.logic = logic;
		
		setUndecorated(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
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
		getContentPane().removeAll();
		if(!fullscreen)
		{			
			setSize(scale * cols, scale * (top_panel_height + rows));
			setLocationRelativeTo(null);
		}
		else
			setExtendedState(MAXIMIZED_BOTH);
		
		FlowLayout frame_layout	= new FlowLayout(FlowLayout.CENTER, 0, 0);
		GridLayout top_grid		= new GridLayout(1, 9);
		
		setLayout(frame_layout);
		
		top = new JPanel();
		top.setPreferredSize(new Dimension(scale * cols, scale * top_panel_height));
		top.setLocation(0, 0);
		top.setLayout(top_grid);
		add(top);
			Font top_font = new Font("Arial", Font.BOLD, (1 * scale) / 2);
			
			size_minus_optn = new TopButton("-", 2, 1);
			size_minus_optn.addActionListener(this);
			size_minus_optn.setFont(top_font);
			top.add(size_minus_optn);
		
			JLabel size_label = new JLabel("Size", JLabel.CENTER);
			size_label.setBackground(Color.BLACK);
			size_label.setForeground(Color.GRAY);
			size_label.setBorder(BorderFactory.createMatteBorder(0, 1, 4, 1, Color.GRAY));
			size_label.setFont(top_font);
			size_label.setOpaque(true);
			top.add(size_label);
			
			size_plus_optn = new TopButton("+", 2, 2);
			size_plus_optn.addActionListener(this);
			size_plus_optn.setFont(top_font);
			top.add(size_plus_optn);
			
			JLabel label_game = new JLabel("Game", JLabel.RIGHT);
			label_game.setBackground(Color.BLACK);
			label_game.setForeground(Color.GRAY);
			label_game.setBorder(BorderFactory.createMatteBorder(0, 2, 4, 0, Color.GRAY));
			label_game.setFont(top_font);
			label_game.setOpaque(true);
			top.add(label_game);
			
			JLabel label_of = new JLabel("of", JLabel.CENTER);
			label_of.setBackground(Color.BLACK);
			label_of.setForeground(Color.GRAY);
			label_of.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, Color.GRAY));
			label_of.setFont(top_font);
			label_of.setOpaque(true);
			top.add(label_of);
			
			JLabel label_life = new JLabel("Life", JLabel.LEFT);
			label_life.setBackground(Color.BLACK);
			label_life.setForeground(Color.GRAY);
			label_life.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 1, Color.GRAY));
			label_life.setFont(top_font);
			label_life.setOpaque(true);
			top.add(label_life);
		
			speed_minus_optn = new TopButton("<<", 2, 1);
			speed_minus_optn.addActionListener(this);
			speed_minus_optn.setFont(top_font);
			top.add(speed_minus_optn);

			String pause_optn_str = null;
			if(paused)
				pause_optn_str = "I>";
			else if(!paused)
				pause_optn_str = "II";
			pause_optn = new TopButton(pause_optn_str, 1, 1);
			pause_optn.addActionListener(this);
			pause_optn.setFont(top_font);
			top.add(pause_optn);
			
			speed_plus_optn = new TopButton(">>", 1, 2);
			speed_plus_optn.addActionListener(this);
			speed_plus_optn.setFont(top_font);
			top.add(speed_plus_optn);

		if(!isVisible())
			setVisible(true);
		
		
		if(golPanel == null)
			golPanel = new GolPanel(this, rows, cols, top_panel_height, scale, logic);			
		else
		{
			golPanel.setScale(scale);
			golPanel.setPreferredSize(new Dimension(scale * cols, scale * rows));
			golPanel.setLocation(0, scale * top_panel_height);
			
		}
		add(golPanel);
		resizing = false;
	}
	
	public void decreaseScale()
	{
		if(scale > min_scale)
		{
			scale -= 1;
			fullscreen = false;
			updateFrame(logic);
		}
	}
	public void increaseScale()
	{
		if(scale < max_scale)
		{
			scale += 1;
			if(scale == max_scale)
				fullscreen = true;
			updateFrame(logic);
		}
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

		if(src == size_minus_optn)
			decreaseScale();
		else if(src == size_plus_optn)
			increaseScale();
		else if(src == speed_minus_optn && FPS > 1)
			FPS--;
		else if(src == pause_optn)
			togglePause();
		else if(src == speed_plus_optn)
			FPS++;
	}
}