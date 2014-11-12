
public class G 
{
	public static boolean silent = false;  // verbose output
	public static int MAXDEGREE = 25;      // maximum node degree
	public static boolean generateRandomH = false;  // if false, the same H matrix will be generated in every run
	public static int RAND_SEED = 13; // even when H deterministically generated, you can change the seed to get different H
	public static int MAX_DEPTH = 10;  // minimum girth - for large N
	public static boolean COL_EXPORT_MODE = true;
	public static int MAX_ITERATIONS = 20;

	
	public static void trace (String str)
	{
		System.out.print(str);
	}
	public static void traceln (String str)
	{
		System.out.println(str);
	}
	public static void traceln ()
	{
		System.out.println();
	}
}
