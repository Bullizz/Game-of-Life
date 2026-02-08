package game_of_life;

public class Main
{
	public static void main(String[] args)
	{
		int rows = 10;
		int cols = 20;
		GolLogic logic = null;
		new GolFrame(rows, cols, logic);
	}
}