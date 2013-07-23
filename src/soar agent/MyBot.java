import java.util.*;
import sml.*;
public class MyBot 
{
	/*
 	private static FloatElement planet_x;
 	private static FloatElement planet_y;
 	private static IntElement growthrate;
 	private static IntElement p_owner;
 	private static IntElement planetid;
 	private static IntElement planet_num_ship;
 	
 	
 	private static IntElement f_owner;
 	private static IntElement fleet_num_ship;
 	private static IntElement source_planet;
 	private static IntElement dest_planet;
 	private static IntElement total_trip;
 	private static IntElement turns_left;*/
 	
 	//My own start here
	private static Kernel kernel;
 	private static Agent agent;
 	
 	private static Identifier inputLink;
 	private static Identifier planetwarsWME;
 	private static StringElement isInit;

 	
 	private static Identifier planet;
 	private static Identifier fleet;
 	private static IntElement planet_num_ship_us;
 	private static IntElement planet_num_ship_opponent;
 	private static IntElement productions_us;
 	private static IntElement productions_opponent;
 	private static IntElement number_Fleets;
 	private static StringElement attack_mode;
 
 	//My own ends here
 	
 	

	public static void DoTurn(PlanetWars pw, Agent a) 
    {
		int num_ships_us = pw.NumShips(1);
		int num_ships_opponent = pw.NumShips(2);
		int prod_us = pw.Production(1);
		int prod_opponent = pw.Production(2);
		int numFleets = 1;
		boolean attackMode = false;
		int number_of_our_fleets_in_flight = pw.MyFleets().size();
		a.Update(planet_num_ship_us, num_ships_us);
		a.Update(planet_num_ship_opponent, num_ships_opponent);
		a.Update(productions_us, prod_us);
		a.Update(productions_opponent, prod_opponent);
		a.Update(number_Fleets, numFleets);
		a.Update(attack_mode, "false");
		
		a.Commit();
		a.RunSelfTilOutput();
		//int numFleets = 1;
		
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
		    } else 
		    {
			numFleets = 5;
		    }	    
		}
		// (1) If we current have more tha numFleets fleets in flight, just do
		// nothing until at least one of the fleets arrives.
		if (pw.MyFleets().size() >= numFleets) 
		{
		    return;
		}
		// (2) Find my strongest planet.
		Planet source = null;
		double sourceScore = Double.MIN_VALUE;
	/*	for (Planet p : pw.MyPlanets()) 
		{
		    double score = (double)p.NumShips() / (1 + p.GrowthRate());
		    if (score > sourceScore) 
		    {
				sourceScore = score;
				source = p;
		    }
		}*/
		// (3) Find the weakest enem8y or neutral planet.
		Planet dest = null;
		double destScore = Double.MIN_VALUE;
		List<Planet> candidates = pw.NotMyPlanets();
		if (attackMode) 
		{
		    candidates = pw.EnemyPlanets();
		}
	/*	for (Planet p : candidates) 
		{
		    double score = (double)(1 + p.GrowthRate()) / p.NumShips();
		    if (score > destScore) 
		    {
				destScore = score;
				dest = p;
		    }
		}*/
		for (Planet p : candidates) 
		{
			for (Planet x : pw.MyPlanets()) 
			{
			    int dist=pw.Distance(p,x);
			    double score = (double)(1 + p.GrowthRate())*(x.NumShips()) / ((1 + x.GrowthRate())*(p.NumShips()*dist));
			    if (score > destScore) 
			    {
					destScore = score;
					source = x;
					dest = p;
			    }
			}
		    
		}
		// (4) Send half the ships from my strongest planet to the weakest
		// planet that I do not own.
		if (source != null && dest != null) 
		{
		    int numShips = source.NumShips()/2;
		    for (int i = 0; i < a.GetNumberCommands(); ++i) 
		    {
				Identifier commandWME = a.GetCommand(i);
				String commandName = commandWME.GetAttribute();
			
				if (commandName.equals("stop")) 
				{
					pw.IssueOrder(source, dest, numShips);
				}
			}
		}	
    }


    public static void main(String[] args) 
    {
    	kernel = kernel.CreateRemoteConnection(true,null,12121);
    	String version = kernel.GetSoarKernelVersion();
    	System.out.println("soar version : "+version);
    	agent = kernel.GetAgent("soar1");	//soar1 is the name of the agent in saor
    	System.out.println(agent.GetAgentName());
        if (kernel.HadError())
                throw new IllegalStateException("Error creating agent: " + kernel.GetLastErrorDescription()) ;
    	
        inputLink = agent.GetInputLink();
		
		
	
		isInit = agent.CreateStringWME(inputLink, "init","no");
		planetwarsWME = agent.CreateIdWME(inputLink, "planetwars");
		planet = agent.CreateIdWME(planetwarsWME, "planet");
		fleet = agent.CreateIdWME(planetwarsWME, "fleet");
	
		//My own WMEs start here
		
		planet_num_ship_us=agent.CreateIntWME(planet,"planet_num_ship_us",0);
		planet_num_ship_opponent=agent.CreateIntWME(planet,"planet_num_ship_opponent",0);
		productions_us=agent.CreateIntWME(planet,"productions_us",0);
		productions_opponent=agent.CreateIntWME(planet,"productions_opponent",0);
		number_Fleets=agent.CreateIntWME(fleet,"number_Fleets",0);
		attack_mode = agent.CreateStringWME(planetwarsWME, "attack_mode","false");
	 	
		
		//My own WMEs end here
		
	/*	p_owner=agent.CreateIntWME(planet, "p_owner", 0);
		planetid=agent.CreateIntWME(planet, "planetid", 0);
		growthrate = agent.CreateIntWME(planet, "growthrate", 0);
		planet_x = agent.CreateFloatWME(planet, "x", 0.0);
		planet_y = agent.CreateFloatWME(planet, "y", 0.0);
		planet_num_ship=agent.CreateIntWME(planet,"planet_num_ship",0);
		
		
		fleet = agent.CreateIdWME(planetwarsWME, "fleet");
		fleet_num_ship = agent.CreateIntWME(fleet,"fleet_num_ships",0);
		f_owner = agent.CreateIntWME(fleet, "f_owner", 0);
		source_planet =agent.CreateIntWME(fleet, "source_planet", 0);
		dest_planet = agent.CreateIntWME(fleet, "dest_planet", 0);
		total_trip = agent.CreateIntWME(fleet, "total_trip", 0);
		turns_left = agent.CreateIntWME(fleet, "turns_left", 0); */
		
        kernel.SetAutoCommit(false);
        
        
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
							DoTurn(pw,agent);
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
		{
	    // Owned.
		}
		
    }
}