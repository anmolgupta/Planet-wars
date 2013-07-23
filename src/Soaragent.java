import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import sml.*;

public class Soaragent 
{ 	
 
	private static Kernel kernel;
 	private static Agent agent;
 	private static Identifier inputLink;
	private static Identifier planetwarsWME;
 	private static StringElement isInit;
 	private static Identifier planet;
 	private static Identifier score;
 	private static Identifier fleet;
 	private static Identifier [] candidates_s;
	private static Identifier [] candidates_d;
 	private static IntElement numships_us;
 	private static IntElement numships_opponent;
 	private static IntElement productions_us;
 	private static IntElement productions_opponent;
 	private static IntElement number_fleets;
 	private static IntElement planet_id;
	private static IntElement myfleetsize;
	private static FloatElement value;
 	private static StringElement attack_mode;
 
 	private static PrintWriter csv;
 	
 	public static void DoTurn(PlanetWars pw, Agent a) throws FileNotFoundException 
    {
 		int numFleets = 1;
 		boolean attackMode = false;
 		/*int num_ships_us = pw.NumShips(1);
		int num_ships_opponent = pw.NumShips(2);
		int prod_us = pw.Production(1);
		int prod_opponent = pw.Production(2);
		int numFleets = 1;
		int myfleets = pw.MyFleets().size();
		boolean attackMode = false;
		
		a.Update(numships_us, num_ships_us);
		a.Update(numships_opponent, num_ships_opponent);
		a.Update(productions_us, prod_us);
		a.Update(productions_opponent, prod_opponent);
		a.Update(number_fleets, numFleets);
		a.Update(attack_mode, "false");
		a.Update(myfleetsize, myfleets);
		String attackmode_1="false";
		a.Commit();
		a.RunSelfTilOutput();*/
 		if(pw.NumShips(1) > pw.NumShips(2)) 
		{
			if (pw.Production(1) > pw.Production(2)) //This logic is being implemented by second.soar
			{	
				numFleets = 1;
				attackMode=true;
			}
			else
				numFleets = 3;
		} 
		else 
		{
		    if (pw.Production(1) > pw.Production(2)) 
		    {
			numFleets = 1;  //This logic is being implemented by third.soar
		    } else 
		    {
			numFleets = 5;  //This logic is being implemented by fourth.soar
		    }	    
		}
 		if (pw.MyFleets().size() >= numFleets) 
		{
		    return;
		}
		List<Planet> candidates_source = pw.NotMyPlanets();
		candidates_s = new Identifier[candidates_source.size()];
		csv.write("\nCAndidates size"+candidates_source.size());
		for (int i = 0; i < candidates_source.size(); i++)
		{
			//candidates_s[i] = a.CreateIdWME(planet, "candidate_s");
			//value = a.CreateFloatWME(candidates_s[i], "value",(candidates_source.get(i).NumShips()) / (1 + candidates_source.get(i).GrowthRate()));
			//score = a.CreateIdWME(candidates_s[i], "score");
			//value=a.CreateFloatWME(score,"value",(candidates_source.get(i).NumShips()) / (1 + candidates_source.get(i).GrowthRate()));
			//planet_id=a.CreateIntWME(score, "id", candidates_source.get(i).PlanetID());
		}
		/*
		for (int i = 0; i < candidates_source.size(); i++)
		{
			candidates_s[i] = a.CreateIdWME(planet, "candidate_s");
			score = a.CreateIdWME(candidates_s[i], "score");
			value=a.CreateFloatWME(score,"value",(candidates_source.get(i).NumShips()) / (1 + candidates_source.get(i).GrowthRate()));
			planet_id=a.CreateIntWME(score, "id", candidates_source.get(i).PlanetID());
		}*/
		a.Commit();
		a.RunSelfTilOutput();

		csv.write("\nNumber commands"+a.GetNumberCommands());
		int id=0;
		int maxval;
		int sc;
		for (int i = 0; i < a.GetNumberCommands(); ++i) 
	    {
			Identifier commandWME = a.GetCommand(i);
			String commandName = commandWME.GetAttribute();
			if (commandName.equals("sixth"))
			{
				csv.write("\nSixth Prod");
				//maxval=Integer.parseInt(commandWME.GetParameterValue("max-val"));
				//csv.write("\nMax Val"+maxval+"\n");
				break;
				//commandWME.AddStatusComplete();
			}
	    }
		Planet source= pw.GetPlanet(id);
		/*	if (commandName.equals("first"))
			{
				csv.write("\nFirst Prod");
				attackmode_1=commandWME.GetParameterValue("attack_mode");
				numFleets=Integer.parseInt(commandWME.GetParameterValue("number_fleets"));
				csv.write("\nattackmode_1"+attackmode_1);
				csv.write("\nnumFleets"+numFleets+"\n");
				break;
				//commandWME.AddStatusComplete();
			}
			else
			if (commandName.equals("second"))
			{
				csv.write("\nSecond Prod");
				numFleets=Integer.parseInt(commandWME.GetParameterValue("number_fleets"));
				csv.write("\nnumFleets"+numFleets+"\n");		
				break;
				//commandWME.AddStatusComplete();
			}
			else
			if (commandName.equals("third"))
			{
				csv.write("\nThird Prod");
				numFleets=Integer.parseInt(commandWME.GetParameterValue("number_fleets"));
				csv.write("\nnumFleets"+numFleets+"\n");
					
				break;
				//commandWME.AddStatusComplete();
			}
			else
			if (commandName.equals("fourth"))
			{
				csv.write("\nFourth Prod");
				numFleets=Integer.parseInt(commandWME.GetParameterValue("number_fleets"));
				csv.write("\nnumFleets"+numFleets+"\n");
					
				break;
				//commandWME.AddStatusComplete();
			}*/
			/*if (commandName.equals("fifth"))
			{
				csv.write("\nFifth Prod\n");
				return;
			}*/
	   	



		/*if(pw.NumShips(1) > pw.NumShips(2)) 
		{
			if (pw.Production(1) < pw.Production(2)) //This logic is being implemented by second.soar
				numFleets = 3;
			else
				numFleets = 1;
		} 
		else 
		{
		    if (pw.Production(1) > pw.Production(2)) 
		    {
			numFleets = 1;  //This logic is being implemented by third.soar
		    } else 
		    {
			numFleets = 5;  //This logic is being implemented by fourth.soar
		    }	    
		}*/
		//if(attackmode_1=="true")
			//attackMode=true;

		
		
		
		//  If we current have more tha numFleets fleets in flight, just do
		// nothing until at least one of the fleets arrives.
	/*	if (pw.MyFleets().size() >= numFleets) 
		{
		    return;
		}*/
		
		/*for (Planet p : pw.MyPlanets()) 
		{
		    double score = (double)p.NumShips() / (1 + p.GrowthRate());
		    if (score > sourceScore) 
		    {
			sourceScore = score;
			source = p;
		    }
		}
		
		*/
		
		
		/*
		 List<Planet> candidates = pw.NotMyPlanets();
	
	
			if (attackMode) 
			{
			    candidates = pw.EnemyPlanets();
			}
			for (Planet p : candidates) 
			{
			    double score = (double)(1 + p.GrowthRate()) / p.NumShips();
			    if (score > destScore) {
				destScore = score;
				dest = p;
			    }
			}
		 
		 */
	/*	List<Planet> candidates_source = pw.MyPlanets();
		candidates_s = new Identifier[candidates_source.size()];
		for (int i = 0; i < candidates_source.size(); i++)
		{
			candidates_s[i] = agent.CreateIdWME(planet, "candidate_s");
			score = agent.CreateFloatWME(candidates_s[i], "score", (candidates_source.get(i).NumShips()) / (1 + candidates_source.get(i).GrowthRate()));
			planet_id=agent.CreateIntWME(candidates_s[i], "id", candidates_source.get(i).PlanetID());
		}
		
		List<Planet> candidates_dest = pw.NotMyPlanets();
		if (attackMode) 
		{
		    candidates_dest = pw.EnemyPlanets();
		}
		candidates_d = new Identifier[candidates_dest.size()];
		for (int i = 0; i < candidates_dest.size(); i++)
		{
			candidates_d[i] = agent.CreateIdWME(planet, "candidate_d");
			score = agent.CreateFloatWME(candidates_d[i], "score", (1 + candidates_dest.get(i).GrowthRate())/(candidates_dest.get(i).NumShips()));
			planet_id=agent.CreateIntWME(candidates_d[i], "id", candidates_source.get(i).PlanetID());
		}*/
		
	/*	Planet source = null;		
		Planet dest = null;
	
		double destScore = Double.MIN_VALUE;
		
		List<Planet> candidates = pw.NotMyPlanets();
		if (attackMode) 
		{
		    candidates = pw.EnemyPlanets();
		}
	
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

		}*/
		Planet dest = null;
		
		double destScore = Double.MIN_VALUE;
		List<Planet> candidates = pw.NotMyPlanets();
		
		
		if (attackMode) 
		{
		    candidates = pw.EnemyPlanets();
		}
		for (Planet p : candidates) 
		{
		    double score = (double)(1 + p.GrowthRate()) / p.NumShips();
		    if (score > destScore) {
			destScore = score;
			dest = p;
		    }
		}
		if (source != null && dest != null) 
		{
		    int numShips = source.NumShips()/2;
		  //  for (int i = 0; i < a.GetNumberCommands(); ++i) 
		    //{
				//Identifier commandWME = a.GetCommand(i);
				//String commandName = commandWME.GetAttribute();
				//if (commandName.equals("stop")) 
				//{
					pw.IssueOrder(source, dest, numShips);
				//}
		//	}
		}	
    }


    public static void main(String[] args) throws FileNotFoundException 
    {
    	csv = new PrintWriter("output");
		
    	kernel = kernel.CreateRemoteConnection(true,null,12121);
    	String version = kernel.GetSoarKernelVersion();
    	System.out.println("soar version : "+version);
    	agent = kernel.GetAgent("soar1");	//soar1 is the name of the agent in soar
    	System.out.println(agent.GetAgentName());
        if (kernel.HadError())
                throw new IllegalStateException("Error creating agent: " + kernel.GetLastErrorDescription()) ;
    	
        
        //My own WMEs start here
        
        inputLink = agent.GetInputLink();
        isInit = agent.CreateStringWME(inputLink, "init","no");
		planetwarsWME = agent.CreateIdWME(inputLink, "planetwars");
		planet = agent.CreateIdWME(planetwarsWME, "planet");
		fleet = agent.CreateIdWME(planetwarsWME, "fleet");
		numships_us=agent.CreateIntWME(planet,"numships_us",0);
		numships_opponent=agent.CreateIntWME(planet,"numships_opponent",0);
		productions_us=agent.CreateIntWME(planet,"productions_us",0);
		productions_opponent=agent.CreateIntWME(planet,"productions_opponent",0);
		number_fleets=agent.CreateIntWME(fleet,"number_fleets",0);
		myfleetsize = agent.CreateIntWME(fleet,"myfleetsize",0);
		attack_mode = agent.CreateStringWME(planetwarsWME, "attack_mode","false");
		
		//My own WMEs end here

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
		csv.close();
    }
}
