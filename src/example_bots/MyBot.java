import java.util.*;

public class MyBot 
{
    public static double distance(double x,double y,double x1,double y1)
    {
    	double t1=x-x1;
    	double t2=y-y1;
    	double t3=(t1*t1)+(t2*t2);
    	double t4=Math.sqrt(t3);
    	return t4;
    }
	public static void DoTurn(PlanetWars pw) 
   {
     int numFleets = 1;
	boolean attackMode = false;
	if (pw.NumShips(1) > pw.NumShips(2)) 
	{
	    if (pw.Production(1) > pw.Production(2)) 
	    {
	    	numFleets = 1;
	    	attackMode = true;
	    } 
	    else 
	    {
	    	numFleets = 3;
	    }
	} 
	else 
	{
	    if (pw.Production(1) > pw.Production(2)) 
	    {
	    	numFleets = 1;
	    } 
	    else 
	    {
	    	numFleets = 5;
	    }	    
	}
	

	if (pw.MyFleets().size() >= numFleets)
	    return;
	
	// Find my strongest planet.
	Planet source = null;
//	double sourceScore = Double.MIN_VALUE;
	

	
	// Find the weakest enemy or neutral planet.
	Planet dest = null;
	double destScore = Double.MIN_VALUE;
	List<Planet> candidates = pw.NotMyPlanets();
	if (attackMode) 
	{
	    candidates = pw.EnemyPlanets();
	}
	
	for (Planet p : candidates) 
	{
	    
		for (Planet s_option : pw.MyPlanets()) 
		{
		    double dist=distance(s_option.X(),s_option.Y(),p.X(),p.Y() );		
			double score = (double)(p.NumShips()*dist)/(1+p.GrowthRate());
			if (score > destScore) 
			{
				destScore = score;
				dest = p;
				source=s_option;
			}
		}
	}
	// (4) Send half the ships from my strongest planet to the weakest
	// planet that I do not own.
	if (source != null && dest != null) 
	{
	    int numShips = source.NumShips() / 2;
	    pw.IssueOrder(source, dest, numShips);
	}
   }

    public static void main(String[] args) 
    {
    	String line = "";
    	String message = "";
    	int c;
    	try 
    	{
    		while ((c = System.in.read()) >= 0) 
    		{
    			switch (c) 
    			{
    				case '\n':
    					if (line.equals("go")) 
    					{
    						PlanetWars pw = new PlanetWars(message);
    						DoTurn(pw);
    						pw.FinishTurn();
    						message = "";
    					} 
    					else 
    					{
    						message += line + "\n";
    					}
    					line = "";
    					break;
    				default:
    					line += (char)c;
    					break;
    			}
    		}
    	} 
    	catch (Exception e) 
    	{}
    }
}

