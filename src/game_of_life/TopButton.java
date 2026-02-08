package game_of_life;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

public class TopButton extends JButton implements MouseListener
{
	int border_top = 0, border_down = 4;
	int border_left, border_right;
	
	int border_top_filled = 0, border_down_filled = 8;
	int border_left_filled, border_right_filled;
	
	Border std_border, active_border;
	
	Color black	= Color.BLACK;
	Color gray	= Color.GRAY;
	Color white	= Color.WHITE;
	
	public TopButton(String title, int border_left, int border_right)
	{
		super(title);
		
		this.border_left = border_left;
			border_left_filled = 4 * border_left;
		this.border_right = border_right;
			border_right_filled = 4 * border_right;
		
		std_border		= BorderFactory.createMatteBorder(border_top, border_left, border_down, border_right, gray);
		active_border	= BorderFactory.createMatteBorder(border_top_filled, border_left_filled, border_down_filled, border_right_filled, white);
		
		setContentAreaFilled(false);
		setBackground(black);
		setForeground(gray);
		setBorder(std_border);
		addMouseListener(this);
		setOpaque(true);
		setFocusable(false);
	}

	@Override
	public void mouseEntered(MouseEvent enter)
	{
		// Init. hover effect
		setBorder(active_border);
	}
	@Override
	public void mouseExited(MouseEvent exit)
	{
		// End hover effect
		setBorder(std_border);		
	}
	@Override
	public void mousePressed(MouseEvent press)
	{
		setForeground(black);
		setBackground(white);
	}
	@Override
	public void mouseReleased(MouseEvent release)
	{
		setBackground(black);
		setForeground(gray);
		setFocusable(false);
	}
	@Override
	public void mouseClicked(MouseEvent e){}
}