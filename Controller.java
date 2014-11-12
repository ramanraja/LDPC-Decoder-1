 /**
 * Generates LDPC codes by Gallager or PEG algorithm
 * Can detect and remove 4-cycles (not needed for PEG)
 * Simulates a BEC, and decodes the all zero code word.
 * Computes the colum rank of H corresponding to the stopping set.
 * The Matrix H can find the rank of itself or a subset of its columns.
 * @author Rajaraman
 */
import java.io.FileInputStream;
import java.util.Properties;
import java.util.BitSet;

public class Controller 
{
	public static void main (String[] args) throws Exception
	{
        Properties prop = new Properties();
        prop.load (new FileInputStream("settings.txt"));
        String str;

        str = prop.getProperty ("block_length", "18").trim(); 
        int n = Integer.parseInt(str);
        str = prop.getProperty ("check_length", "12").trim(); 
        int m = Integer.parseInt(str);
        
        str = prop.getProperty ("row_weight", "6").trim(); 
        int rowwt = Integer.parseInt(str);
        str = prop.getProperty ("column_weight", "3").trim(); 
        int colwt = Integer.parseInt(str);
        
        str = prop.getProperty ("silent_mode", "false").trim(); 
        G.silent = (str.equalsIgnoreCase("true") ? true : false);
        str = prop.getProperty ("generate_random", "true").trim(); 
        G.generateRandomH = (str.equalsIgnoreCase("true") ? true : false);
        str = prop.getProperty ("random_seed", "13").trim(); 
        G.RAND_SEED = Integer.parseInt(str);
		
//		Gallager g = new Gallager();
//		g.autoInit (n, colwt, rowwt);
//		g.generateRaman();
        
		Peg g = new Peg();
		g.init(m, n, colwt);
		g.generate();
// 		g.dump();
		
		if (g.detectCycles())
		{
			g.printCycles();
			g.removeCycles();
			//g.dump();
			g.detectCycles();
			g.printCycles();		
		}
		
		Matrix mat = new Matrix();
		mat.init(m, n, g.exportToBitSet());
		TannerGraph tg = new TannerGraph();
		tg.init(mat);
		Channel chl = new Channel();
		chl.init(n);
		chl.setErasureProbability(0.5f);
		
		BitSet data = new BitSet(n);  // all-zero code word
		tg.setData(data);
		tg.setErasures(chl.getErasures());
		boolean result = tg.decode();
		if (result) 
			G.traceln ("Decoded successfully");
		else
		{
			G.traceln ("Decoding failed");
			BitSet colmask = tg.getErasures();
			G.traceln ("Stopping set (cardinality=" +colmask.cardinality() +")");
			G.traceln (colmask.toString());
			mat.selectColumns(colmask); // the original H is DESTROYED
			int rank = mat.getRank();  // the original H is DESTROYED
			G.traceln ("Rank of stopping set colums=" +rank);
		}
		
		G.trace ("\nDone !");
	}
}
