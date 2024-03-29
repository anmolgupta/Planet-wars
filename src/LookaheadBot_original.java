import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public class LookaheadBot_original 
{

	public static void DoTurn(PlanetWars pw) 
	{
		
		double score = Double.MIN_VALUE;		
	
		Planet source = null;
		Planet dest = null;
		int numFleets = 1;
		 if (pw.NumShips(1) > pw.NumShips(2)) 
		 {
		    if (pw.Production(1) > pw.Production(2)) 
		    {
		    	numFleets = 1;
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
		
		if(pw.MyFleets().size()>=numFleets)
			return;
		// We try to simulate each possible action and its outcome after two turns
		// considering each of my planets as a possible source 
		// and each enemy planet as a possible destination
		
		for (Planet myPlanet: pw.MyPlanets()){
			
			//avoid planets with only one ship
			if (myPlanet.NumShips() <= 1)
				continue;		
			
			for (Planet notMyPlanet: pw.NotMyPlanets()){

				// Create simulation environment - need to create one for each simulation
				//simpw will hold all the planets of pw after this line
				SimulatedPlanetWars simpw = createSimulation(pw);
				
				// (1) simulate my turn with the current couple of source and destination
				simpw.simulateAttack(myPlanet, notMyPlanet);
				// (2) simulate the growth of ships that happens in each turn
				simpw.simulateGrowth();

				// (3) simulate the opponent's turn, assuming that the opponent is the BullyBot		
				//     here you can add other opponents
				simpw.simulateBullyBotAttack();
				// (4) simulate the growth of ships that happens in each turn
				simpw.simulateGrowth();
				
				// (5) evaluate how the current simulated state is
				//     here you can change how a state is evaluated as good
				double scoreMax = evaluateState(simpw);
				
				// (6) find the planet with the maximum evaluated score
				//     this is the most promising future state
				if (scoreMax > score) {					
					score = scoreMax;
					source = myPlanet;
					dest = notMyPlanet;
					
				}
				
			}
		}
		
		
			
		// Attack using the source and destinations that lead to the most promising state in the simulation
		if (source != null && dest != null) {
			pw.IssueOrder(source, dest,source.NumShips()/2);
		}
		

	}
	
	
	/**
	 * This function evaluates how promising a simulated state is.
	 * You can change it to anything that makes sense, using combinations 
	 * of number of planets, ships or growth rate.
	 * @param SimulatedPlanetWars pw
	 * @return score of the final state of the simulation
	 */
	 
	public static double evaluateState(SimulatedPlanetWars pw){
		
		// CHANGE HERE
		
				
		/*double destScore = Double.MIN_VALUE;
		for (Planet p : pw.NotMyPlanets()) 
		{
			for (Planet x : pw.MyPlanets()) 
			{
			    double dist=distance(p.X(),p.Y(),x.X(),x.Y());
			    double score = (double)(1 + p.GrowthRate())*(x.NumShips()) / ((1 + x.GrowthRate())*(p.NumShips()*dist));
			    if (score > destScore) 
			    {
					destScore = score;
			    }
			}
		    
		}
		return destScore;*/
		double enemyShips = 1.0;
		double myShips = 1.0;
		//int growthrate_enemy = 0;
		//int growthrate_my = 0;
		for (Planet planet: pw.EnemyPlanets()){
			//growthrate_enemy += planet.GrowthRate();
			enemyShips += planet.NumShips();
		}
		
		for (Planet planet: pw.MyPlanets()){
			//growthrate_my += planet.GrowthRate();
			myShips += planet.NumShips()*planet.GrowthRate();
		}
		
		return (myShips)/(enemyShips);
	}
	


	// don't change this
	public static void main(String[] args) {
		
		String line = "";
		String message = "";
		int c;
		try {
			while ((c = System.in.read()) >= 0) {
				switch (c) {
				case '\n':
					if (line.equals("go")) {
						PlanetWars pw = new PlanetWars(message);
						DoTurn(pw);
						pw.FinishTurn();
						message = "";
					} else {
						message += line + "\n";
					}
					line = "";
					break;
				default:
					line += (char) c;
					break;
				}
			}
		} catch (Exception e) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			String stackTrace = writer.toString();
			System.err.println(stackTrace);
			System.exit(1); //just stop now. we've got a problem
		}
	}
	
	/**
	 * Create the simulation environment. Returns a SimulatedPlanetWars instance.
	 * Call every time you want a new simulation environment.
	 * @param The original PlanetWars object
	 * @return SimulatedPlanetWars instance on which to simulate your attacks. Create a new one everytime you want to try alternative simulations.
	 */
	public static SimulatedPlanetWars createSimulation(PlanetWars pw){
		return dummyBot.new SimulatedPlanetWars(pw);
	}
	
	
	/**
	 * Static LookaheadBot_original, used only to access SimulatedPlanetWars (DON'T CHANGE)
	 */
	static LookaheadBot_original dummyBot = new LookaheadBot_original();
	
	/**
	 * Class which provide the simulation environment, has same interface as PlanetWars 
	 * (except for Fleets, that are not used).
	 *
	 */
	public class SimulatedPlanetWars{

		List<Planet> planets = new ArrayList<Planet>();
		List<Fleet> fleets =new ArrayList<Fleet>();
		
		public SimulatedPlanetWars(PlanetWars pw) {

			for (Planet planet: pw.Planets()){
				planets.add(planet);
			}
		}
		
		public void simulateGrowth() {
			for (Planet p: planets){
				
				if(p.Owner() == 0)
					continue;
				
				Planet newp = new Planet(p.PlanetID(), p.Owner(), p.NumShips()+p.GrowthRate() , 
						p.GrowthRate(), p.X(), p.Y());
				
				planets.set(p.PlanetID(), newp);
			}
		}

		
		
		 public int Production(int playerID) {
			int prod = 0;
			for (Planet p : planets) {
			    if (p.Owner() == playerID) {
				prod += p.GrowthRate();
			    }
			}
			return prod;
		    }
		
		public void simulateAttack( int player, Planet source, Planet dest)
		{
			
			if (source.Owner() != player){
				return;
			}
			
			
			// Simulate attack
			if (source != null && dest != null) {
						
				Planet newSource = new Planet(source.PlanetID(), source.Owner(), source.NumShips()/2 , 
						source.GrowthRate(), source.X(), source.Y());
				Planet newDest = new Planet(dest.PlanetID(), dest.Owner(), Math.abs(dest.NumShips()-source.NumShips()/2 ), 
						dest.GrowthRate(), dest.X(), dest.Y());
				
				if(dest.NumShips()< source.NumShips()/2){
					//change owner
					newDest.Owner(player);
				}
				
				planets.set(source.PlanetID(), newSource);
				planets.set(dest.PlanetID(), newDest);
			}


		}
		
		public void simulateAttack( Planet source, Planet dest){
			simulateAttack(1, source, dest);
		}
		
		
		public void simulateBullyBotAttack(){
			int numFleets = 1;
			 boolean attackMode = false;
				if (NumShips(2) > NumShips(1)) 
				{
				    if (Production(2) > Production(1)) 
				    {
						attackMode = true;
						numFleets = 1;
				    } 
				    else 
				    {
				    	numFleets = 3;
				    }
				} 
				else 
				{
				    if (Production(2) > Production(1)) 
				    {
				    	numFleets = 1;
				    } else 
				    {
				    	numFleets = 5;
				    }	    
				}
				if (EnemyFleets().size() >= numFleets) 
				{
				    return;
				}
			/*	
			
		for (Planet myPlanet: pw.MyPlanets()){
			
			//avoid planets with only one ship
			if (myPlanet.NumShips() <= 1)
				continue;		
			
			for (Planet notMyPlanet: pw.NotMyPlanets()){

				// Create simulation environment - need to create one for each simulation
				//simpw will hold all the planets of pw after this line
				SimulatedPlanetWars simpw = createSimulation(pw);
				
				// (1) simulate my turn with the current couple of source and destination
				simpw.simulateAttack(myPlanet, notMyPlanet);
				// (2) simulate the growth of ships that happens in each turn
				simpw.simulateGrowth();

				// (3) simulate the opponent's turn, assuming that the opponent is the BullyBot		
				//     here you can add other opponents
				simpw.simulateBullyBotAttack();
				// (4) simulate the growth of ships that happens in each turn
				simpw.simulateGrowth();
				
				// (5) evaluate how the current simulated state is
				//     here you can change how a state is evaluated as good
				double scoreMax = evaluateState(simpw);
				
				// (6) find the planet with the maximum evaluated score
				//     this is the most promising future state
				if (scoreMax > score) {					
					score = scoreMax;
					source = myPlanet;
					dest = notMyPlanet;
					
				}
				
			}
		}
		
		
			
		// Attack using the source and destinations that lead to the most promising state in the simulation
		if (source != null && dest != null) {
			pw.IssueOrder(source, dest,source.NumShips()/2);
		}
		

			*/
			// (2) Find my strongest planet.
				Planet source = null;
				double sourceScore = Double.MIN_VALUE;
				for (Planet p : EnemyPlanets()) //Myplanets tha pehle yahan.
				{
				    double score = (double)p.NumShips() / (1 + p.GrowthRate());
				    if (score > sourceScore) 
				    {
					sourceScore = score;
					source = p;
				    }
				}
				// (3) Find the weakest enemy or neutral planet.
				Planet dest = null;
				double destScore = Double.MIN_VALUE;
				
				List<Planet> candidates = MyPlanets();//Notmyplantes tha pehle yahan
				for (Planet p : planets) {
				    if (p.Owner() == 0) {
					candidates.add(p);
				    }
				}
				
				if (attackMode) 
				{
				    candidates = MyPlanets(); //enemyplanets tha yahan
				}
				for (Planet p : candidates) 
				{
				    double score = (double)(1 + p.GrowthRate()) / p.NumShips();
				    if (score > destScore) {
				    destScore = score;
					dest = p;
				    }
				}
				// (4) Send half the ships from my strongest planet to the weakest
				// planet that I do not own.
				if (source != null && dest != null) 
				{
				    simulateAttack(2, source, dest);
				} 

	
		}
		public List<Planet> Planets(){
			return planets;
		}
		public List<Fleet> EnemyFleets() {
			List<Fleet> r = new ArrayList<Fleet>();
			for (Fleet f : fleets) {
			    if (f.Owner() == 2) {
				r.add(f);
			    }
			}
			return r;
		    }
		
	    // Returns the number of planets. Planets are numbered starting with 0.
	    public int NumPlanets() {
	    	return planets.size();
	    }
		
	    // Returns the planet with the given planet_id. There are NumPlanets()
	    // planets. They are numbered starting at 0.
	    public Planet GetPlanet(int planetID) {
	    	return planets.get(planetID);
	    }
	    
	    // Return a list of all the planets owned by the current player. By
	    // convention, the current player is always player number 1.
	    public List<Planet> MyPlanets() {
			List<Planet> r = new ArrayList<Planet>();
			for (Planet p : planets) {
			    if (p.Owner() == 1) {
				r.add(p);
			    }
			}
			return r;
	    }
	    
	    // Return a list of all neutral planets.
	    public List<Planet> NeutralPlanets() {
		List<Planet> r = new ArrayList<Planet>();
		for (Planet p : planets) {
		    if (p.Owner() == 0) {
			r.add(p);
		    }
		}
		return r;
	    }

	    // Return a list of all the planets owned by rival players. This excludes
	    // planets owned by the current player, as well as neutral planets.
	    public List<Planet> EnemyPlanets() {
		List<Planet> r = new ArrayList<Planet>();
		for (Planet p : planets) {
		    if (p.Owner() >= 2) {
			r.add(p);
		    }
		}
		return r;
	    }

	    // Return a list of all the planets that are not owned by the current
	    // player. This includes all enemy planets and neutral planets.
	    public List<Planet> NotMyPlanets() {
		List<Planet> r = new ArrayList<Planet>();
		for (Planet p : planets) {
		    if (p.Owner() != 1) {
			r.add(p);
		    }
		}
		return r;
	    }
	    
	    // Returns the distance between two planets, rounded up to the next highest
	    // integer. This is the number of discrete time steps it takes to get
	    // between the two planets.
		public int Distance(int sourcePlanet, int destinationPlanet) {
			Planet source = planets.get(sourcePlanet);
			Planet destination = planets.get(destinationPlanet);
			double dx = source.X() - destination.X();
			double dy = source.Y() - destination.Y();
			return (int) Math.ceil(Math.sqrt(dx * dx + dy * dy));
		}
	    
	    // If the game is not yet over (ie: at least two players have planets or
	    // fleets remaining), returns -1. If the game is over (ie: only one player
	    // is left) then that player's number is returned. If there are no
	    // remaining players, then the game is a draw and 0 is returned.
		public int Winner() {
			Set<Integer> remainingPlayers = new TreeSet<Integer>();
			for (Planet p : planets) {
				remainingPlayers.add(p.Owner());
			}
			switch (remainingPlayers.size()) {
			case 0:
				return 0;
			case 1:
				return ((Integer) remainingPlayers.toArray()[0]).intValue();
			default:
				return -1;
			}
		}

	    // Returns the number of ships that the current player has, either located
	    // on planets or in flight.
	    public int NumShips(int playerID) {
		int numShips = 0;
		for (Planet p : planets) {
		    if (p.Owner() == playerID) {
			numShips += p.NumShips();
		    }
		}
		return numShips;
	    }

	    

	    public void IssueOrder(Planet source, Planet dest) {
	    	simulateAttack(source,dest);
	    }
	    
	
	}
}
