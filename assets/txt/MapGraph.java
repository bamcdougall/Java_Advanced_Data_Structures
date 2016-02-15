/**
 * @author UCSD MOOC development team and B McDougall
 *
 */
package roadgraph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;
//import week2example.MazeNode;

/**
 * @author UCSD MOOC development team and B McDougall
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections 
 *
 */
public class MapGraph {
	//TODO: Add your member variables here in WEEK 2
	/*		
	  REMINDER: for auto-graders, please comment out any
	        System.err.println() and Exception(Strings).
	        See lines near: 122, 177, 234, and 240
	 HashMap: member variable that store GeographicPoints as (location, nodes)
	 in mapGraph (adjacency list structure since degree << numVertices)
	 */	
	private HashMap<GeographicPoint, mapVertex> mapVertices;

	/** 
	 * Empty constructor: create a new empty MapGraph 
	 */
	public MapGraph()
	{
		// TODO: Implement in this constructor in WEEK 2
		// Maps contain vertices (nodes) with
		// its weighted edges. NOTE:  weight contains distance, etc
		// Structure follows adjacency list b/c degree << numVertices
		mapVertices = new HashMap<GeographicPoint, mapVertex>();
	}

	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		//TODO: Implement this method in WEEK 2
		return this.mapVertices.keySet().size();
	}

	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		//TODO: Implement this method in WEEK 2
		// the vertices in this MapGraph are contained
		// in the key set of the hash map.
		return mapVertices.keySet();
	}

	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		//TODO: Implement this method in WEEK 2
		// number of edges is the total count of values
		// of mapGraph's HashMap
		int edgeListCount = 0;
		for (GeographicPoint pt : this.getVertices()) {
			edgeListCount += this.mapVertices.get(pt).getMapEdge().size();
		}
		return edgeListCount;
	}

	/** Confirm that a node corresponding to an intersection is a
	 * valid Geographic Point.
	 * If the location is already in the graph or null, this method does 
	 * not change the graph. If a valid location, then calls method to
	 * add location to graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		// TODO: Implement this method in WEEK 2
		// Conditional testing determines whether boundary
		// conditions are satisfied
		if (location == null ) {
			System.err.println("Proposed vertex is null; not added to mapGraph");
			return false;		
		} else if(
				// already in map, so do nothing
				this.getVertices().contains(location) ){
			return false;
		}
		else {
			// Boundary conditions satisfied.
			implementAddVertex(location);
			return true;
		}
	}

	/** This method is called by addVertex(GeographicPoint location).
	 * The method adds the location to the graph. Flow
	 * control returned to calling method.
	 * @param location  The location of the intersection.
	 * @return void.
	 */
	private void implementAddVertex(GeographicPoint location) {
		//TODO complete this method that is added by author
		// purpose: nodes are objects that contain
		// list of edges that connect to other nodes in mapGraph
		mapVertex mapVertex = new mapVertex(location);
		mapVertex.setMapEdge(); // instantiate an empty list for containing edges
		this.mapVertices.put(location, mapVertex);
		return;
	}

	/**
	 * Confirms validity of proposed edge before adding to graph. If valid
	 * then calls methods that adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {

		//TODO: Implement this method in WEEK 2
		// Check whether conditions for adding an edge are satisfied. 
		// Conditions satisfied -> implementAddEdge() else
		// throw exception for violating boundary conditions

		if (from != null && to != null && roadName != null && 
				roadType != null && length >= 0 &&
				this.getVertices().contains(from) && this.getVertices().contains(to) )
		{
			// boundary conditions satisfied, so
			implementAddEdge(from , to, roadName, roadType, length);
		}
		else {
			throw new IndexOutOfBoundsException("Specifications of edge are invalid."
					+ "Edge not added to mapGraph");
		}

	}

	/**
	 * Method executes addition of new
	 * edge to vertex and returns flow control to calling method
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @return void
	 */
	private void implementAddEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) {
		//TODO complete this method added by myself
		// get the edgeList for the HashMap key "from"; add the GP "to" to the valueList;
		// by implementation, GPs are already in keySet of mapGraph.
		// instantiate a mapEdge
		mapEdge mapEdge = new mapEdge(from, to, roadName, roadType, length);
		this.mapVertices.get(mapEdge.getStart()).setMapEdge(mapEdge);
		return;
	}

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		Consumer<GeographicPoint> temp = (x) -> {};
		return bfs(start, goal, temp);
	}

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		/*		
        TODO: Implement this method in WEEK 2

		Hook for visualization.  See write-up.
		nodeSearched.accept(next.getLocation());

		Determine whether boundary conditions for BFS are satisfied
		 */
		if (start == null || goal == null) {
			System.err.println("Start or goal node is null!  No path exists.");
			return null;
		} else 
			if (!this.mapVertices.containsKey(start) || 
					!this.mapVertices.containsKey(goal)) 
			{
				System.err.println("Start or goal node is valid GeographicPoint, "
						+ "but not in MapGraph!  No path exists.");
				return null;
			}
			else
			{
				// Initialize method variables for BFS
				GeographicPoint nextGP;
				HashSet<GeographicPoint> setVerticesVisited;
				HashMap<GeographicPoint, GeographicPoint> parentMap;
				Queue<GeographicPoint> bfsQueue = new LinkedList<GeographicPoint>();
				setVerticesVisited = new HashSet<GeographicPoint>(); 
				parentMap = new HashMap<GeographicPoint, GeographicPoint>();

				nextGP = null; // interesting decision: assign null or start
				bfsQueue.add(start);

				// this whileLoop finds a route using BFS
				while (!bfsQueue.isEmpty()) {
					nextGP = bfsQueue.poll();
					nodeSearched.accept(nextGP);

					if (nextGP.equals(goal)) break;

					for (mapEdge edge : this.mapVertices.get(nextGP).getMapEdge()){
						// logic that excludes visited sites from queue
						if ( !setVerticesVisited.contains(edge.getEnd()) ){
							bfsQueue.add(edge.getEnd());
							parentMap.put(edge.getEnd(), nextGP);
							setVerticesVisited.add(nextGP);
						}
					}
				}
				// logic test that confirms whether BFS is successful
				if (!nextGP.equals(goal)) {
					System.out.println("Snap! No path from " + start + "to" + goal);
					return null;
				}
				return buildRouteList(parentMap, start, goal);
			}
	}

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param parentMap The route determined by geographic search algorithm as optimal
	 * path between start and goal.
	 * @return The list of intersections that form the shortest (dependent on algorithm)
	 *   path from start to goal (including both start and goal).
	 */
	private List<GeographicPoint> buildRouteList(HashMap<GeographicPoint, GeographicPoint> parentMap,
			GeographicPoint start, GeographicPoint goal)
	{
		// routeList is returned list of nodes for final route
		LinkedList<GeographicPoint> routeList = new LinkedList<GeographicPoint>();
		GeographicPoint current = goal;

		while (!current.equals(start)) {
			routeList.addFirst(current);
			current = parentMap.get(current);
		}
		routeList.addFirst(start);
		return routeList;
	}


	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
		Consumer<GeographicPoint> temp = (x) -> {};
		return dijkstra(start, goal, temp);
	}

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
			GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 3

		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());

		// method informed by bfs() of this class
		if (start == null || goal == null) {
			System.err.println("Start or goal node is null!  No path exists.");
			return null;
		} else 
			if (!this.mapVertices.containsKey(start) || 
					!this.mapVertices.containsKey(goal)) 
			{
				System.err.println("Start or goal node is valid GeographicPoint, "
						+ "but not in MapGraph!  No path exists.");
				return null;
			}
			else
			{
				// Initialize variables for DIJKSTRA
				int counterDijkstra=0;
				mapVertex nextVertex;
				HashSet<GeographicPoint> setVerticesVisited;
				// parentMap: member variable that is used to track route linkage of nodes
				HashMap<GeographicPoint, GeographicPoint> parentMap;
				PriorityQueue<mapVertex> dijkstraQueue = new PriorityQueue<mapVertex>();

				nextVertex = null; // interesting decision: assign null or start
				setVerticesVisited = new HashSet<GeographicPoint>(); 
				parentMap = new HashMap<GeographicPoint, GeographicPoint>();

				// TODO initialize cumulative edgeDistances from Start to POSITIVE_INFINITY,
				//                 routeStart to start for all edges of this graph.
				for (mapVertex vertice : this.mapVertices.values()){
					vertice.setDistanceEdgeCumFromStart();
					vertice.setStartRoute(start);
				}

				// Enqueue(Start,0)
				mapVertices.get(start).setDistanceEdgeCumFromStart(0);
				dijkstraQueue.add(mapVertices.get(start));

				// by implementation, this whileLoop finds a route
				while (!dijkstraQueue.isEmpty()) {
					nextVertex = dijkstraQueue.poll();
					counterDijkstra++;
					if ( !setVerticesVisited.contains(nextVertex.getLocation()))	{
						setVerticesVisited.add(nextVertex.getLocation());
						nodeSearched.accept(nextVertex.getLocation());
					}

					// exit point of successful Dijkstra search
					if (nextVertex.getLocation().equals(goal)) {
						break;
					}

					// Nearest neighbors of nextVertex are the ends of edges that are HashMap values
					for (mapEdge edge : this.mapVertices.get(nextVertex.getLocation()).getMapEdge()){
						// logic that excludes visited sites from queue
						if ( !setVerticesVisited.contains(edge.getEnd()) ){
							double cumEdgeDistances = nextVertex.getDistanceEdgeCumFromStart() + edge.getDistance();
							if (cumEdgeDistances < this.mapVertices.get(edge.getEnd()).getDistanceEdgeCumFromStart())
							{
								this.mapVertices.get(edge.getEnd()).setDistanceEdgeCumFromStart(cumEdgeDistances);
								parentMap.put(edge.getEnd(), nextVertex.getLocation());
								dijkstraQueue.add(this.mapVertices.get(edge.getEnd()));
							}
						}
					}
				}
				// logic test: exit from unsuccessful search OR print route of successful search
				if (!nextVertex.getLocation().equals(goal)) {
					System.out.println("Snap! No path from " + start + "to" + goal);
					return null;
				} else
				{
					//					System.out.println("Count of nodes visited via Dijkstra: " +
					//							counterDijkstra);
					return buildRouteList(parentMap, start, goal);
				}
			}
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		Consumer<GeographicPoint> temp = (x) -> {};
		return aStarSearch(start, goal, temp);
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
			GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 3

		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());

		// method informed by dijkstra() of this class
		if (start == null || goal == null) {
			System.err.println("Start or goal node is null!  No path exists.");
			return null;
		} else 
			if (!this.mapVertices.containsKey(start) || 
					!this.mapVertices.containsKey(goal)) 
			{
				System.err.println("Start or goal node is valid GeographicPoint, "
						+ "but not in MapGraph!  No path exists.");
				return null;
			}
			else
			{
				// Initialize variables for aStarSearch
				mapVertex nextVertex;
				HashSet<GeographicPoint> setVerticesVisited;
				// parentMap: member variable that is used to track route linkage of nodes
				HashMap<GeographicPoint, GeographicPoint> parentMap;
				PriorityQueue<mapVertex> aStartQueue = new PriorityQueue<mapVertex>();

				nextVertex = null; // interesting decision: assign null or start
				setVerticesVisited = new HashSet<GeographicPoint>(); 
				parentMap = new HashMap<GeographicPoint, GeographicPoint>();

				// TODO initialize cumulative edgeDistances from Start to POSITIVE_INFINITY,
				//                 routeStart to start for all edges of this graph.
				for (mapVertex vertice : this.mapVertices.values()){
					vertice.setDistanceEdgeCumFromStart();
					vertice.setStartRoute(start);
				}

				// Enqueue(Start,0)
				mapVertices.get(start).setDistanceEdgeCumFromStart(0);
				aStartQueue.add(mapVertices.get(start));
				//	debug variable that tracks while-loop count in aStartSearch
				int counterAStarSearch=0;
				// Initialization variables for aStarSearch completed

				// by implementation, this whileLoop finds a route
				while (!aStartQueue.isEmpty()) {
					nextVertex = aStartQueue.poll();
					counterAStarSearch++;
					if ( !setVerticesVisited.contains(nextVertex.getLocation()))	{
						setVerticesVisited.add(nextVertex.getLocation());
						nodeSearched.accept(nextVertex.getLocation());
					}
					// TODO exit point of successful aStarSearch search
					if (nextVertex.getLocation().equals(goal)) {
						break;
					}

					// Nearest neighbors of nextVertex are the ends of edges that are HashMap values
					for (mapEdge edge : this.mapVertices.get(nextVertex.getLocation()).getMapEdge()){
						// logic that excludes visited sites from queue
						if ( !setVerticesVisited.contains(edge.getEnd()) ){
							double cumEdgeDistances = nextVertex.getDistanceEdgeCumFromStart() + 
									edge.getDistance() + edge.getEnd().distance(goal);
							if (cumEdgeDistances < this.mapVertices.get(edge.getEnd()).getDistanceEdgeCumFromStart()) {
								this.mapVertices.get(edge.getEnd()).setDistanceEdgeCumFromStart(cumEdgeDistances);
								parentMap.put(edge.getEnd(), nextVertex.getLocation());
								aStartQueue.add(this.mapVertices.get(edge.getEnd()));
							}
						}
					}
				}
				// logic test: exit from unsuccessful search OR print route of successful search
				if (!nextVertex.getLocation().equals(goal)) {
					System.out.println("Snap! No path from " + start + "to" + goal);
					return null;
				} else
				{
					//					System.out.println("Count of nodes visited via aStarSearch: " +
					//							counterAStarSearch);
					return buildRouteList(parentMap, start, goal);
				}
			}
	}

	/** Use greedy algorithm for routing a list
	 * of selected nodes within a graph. Search algortim
	 * implemented is aStarSearch. Separate method used
	 * for measuring traversing distance, so uninfluenced
	 * by aStarSearch underestimate of distance between nodes
	 * 
	 * @param List of GPs of map to visit where node0 is
	 * starting and ending site and each node visited once
	 * 
	 * @return List<GeographicPoint> of route to visit List
	 * of GPs.
	 */
	public List<GeographicPoint> greedyCycle(List<GeographicPoint> visitList) {
		List<GeographicPoint> listRoutes = new LinkedList<GeographicPoint>();
		for (int i=0; i < visitList.size()-1; i++) {
			listRoutes.addAll(aStarSearch(visitList.get(i), visitList.get(i+1)));
		}
		return listRoutes;
	}

	/** Method for determining total distance of a route
	 * determined using Greedy Algorithm.
	 * 
	 * @param List of GPs of map to visit where node0 is
	 * starting and ending site and each node visited once
	 * 
	 * @return double Distance traveled by completing a cycle.
	 */
	public double distanceCycleRoute(List<GeographicPoint> listRoutes){
		double routeDistance = 0;
		for (int i=0; i < listRoutes.size()-1; i++) {
			for (mapEdge edge : mapVertices.get(listRoutes.get(i)).getMapEdge()) {
				if (edge.getStart().equals(listRoutes.get(i)) && 
						edge.getEnd().equals(listRoutes.get(i+1))) {
					routeDistance += edge.getDistance();
				}
			}
		}
		return routeDistance;
	}

	/** Method for simple model of traveling sales person 
	 * decision-based improvements using greedy algorithm
	 * & Two-Opt swapping
	 * 
	 * @param List of GPs of map to visit where node0 is
	 * starting and ending site and each node visited once
	 * 
	 * @return double Distance traveled by completing a cycle.
	 */
	public List<GeographicPoint> twoOptSwap(List<GeographicPoint> visitList) {
		// initialize parameters for tracking best route visited and
		// for holding intermediate route of 2-opt permutations
		double distanceNewGreedy2OptRouting = Double.POSITIVE_INFINITY;
		double distanceBestGreedy2OptRouting = Double.POSITIVE_INFINITY;
		List<GeographicPoint> newVisitList = new LinkedList<GeographicPoint>();
		List<GeographicPoint> newGreedyRouting = new LinkedList<GeographicPoint>();
		List<GeographicPoint> bestVisitList = new LinkedList<GeographicPoint>();
		List<GeographicPoint> bestGreedy2OptRouting = new LinkedList<GeographicPoint>();

		bestGreedy2OptRouting = greedyCycle(visitList);
		distanceBestGreedy2OptRouting = distanceCycleRoute(bestGreedy2OptRouting);
		int allowableSwap = visitList.size() - 2;
		int visitListSize = visitList.size();

		// indices are specified such that 1st and last sites are not swapped
		for (int i = 1; i < allowableSwap - 1; i++) {
			for (int k = i + 1; k < allowableSwap; k++) {
				newVisitList = implementTwoOptSwap(visitList, i, k);
				// check whether distance of new two-opt path is better than
				// previously determined best distance. If yes, then save path
				newGreedyRouting = greedyCycle(newVisitList);
				distanceNewGreedy2OptRouting = distanceCycleRoute(newGreedyRouting);

				if (distanceNewGreedy2OptRouting < distanceBestGreedy2OptRouting){
					bestVisitList = newVisitList;
					bestGreedy2OptRouting = newGreedyRouting;
					distanceBestGreedy2OptRouting = distanceNewGreedy2OptRouting;
					System.out.println("\nA shorter distance is returned by new routing, which is " +
							"total distance [km] = " + String.format("%.1f%n",
							distanceBestGreedy2OptRouting));
					newVisitList.clear();
				} else {
					System.out.println("\nThe best distance is from an earlier routing, which is " +
							"total distance [km] = " + String.format("%.1f%n",
							distanceBestGreedy2OptRouting));
					System.out.println("\n\tThe distance from this run of TwoOpt Swap is [km] = " +
							 String.format("%.1f%n", distanceNewGreedy2OptRouting));
					newVisitList.clear();
				}				
			}
		}
		return bestGreedy2OptRouting;
	}


	/** Method for executing Two-Opt swapping of a route.
	 * See [Two-Opt Swap](https://en.wikipedia.org/wiki/2-opt)
	 * for details
	 * 
	 * @param List of GPs of map to visit where node0 is
	 * starting and ending site and each node visited once
	 * @param i Used for i-k of Two-Opt Swapping algorithm
	 * @param k Used for i-k of Two-Opt Swapping algorithm
	 * 
	 * @return double Distance traveled by completing a cycle.
	 */
	private List<GeographicPoint> implementTwoOptSwap(List<GeographicPoint> visitList,
			int i, int k) {
		List<GeographicPoint> newVisitList = new LinkedList<GeographicPoint>();
		// Generate first (i-1) elements as-is
		for (int iIndex = 0; iIndex < i; iIndex++) {
			newVisitList.add(visitList.get(iIndex));
		}
		// Reverse order of elements (i...k)
		for (int iIndex = k; iIndex >= i; iIndex--) {
			newVisitList.add(visitList.get(iIndex));
		}
		// Generate elements (k+1...n) as-is
		for (int iIndex = k+1; iIndex < visitList.size(); iIndex++) {
			newVisitList.add(visitList.get(iIndex));
		}
		return newVisitList;
	}



	/** Method for printing MapGraph for debugging
	 * 
	 * @param map MapGraph
	 * @return Line print of key followed by iteration
	 *         of its values.
	 */
	public static void printMapGraph(MapGraph map) {
		// TODO print method for debugging package
		for (GeographicPoint pt : map.getVertices()) {
			System.out.println("Vertex (" + pt.toString() +
					")");
			for (mapEdge edge : map.mapVertices.get(pt).getMapEdge()){
				System.out.println(edge.getStreetname());
			}
		}
	}

	/** Method (overloaded) for printing HashMap for debugging
	 * 
	 * @param map HashMap
	 * @return Line print of (key, value) pairs that represent
	 *         parent map resulting from BFS.
	 */
	public static void printMapGraph(HashMap<GeographicPoint,GeographicPoint> map) {
		for (GeographicPoint key : map.keySet()) {
			System.out.println("Key - " + key + ". Values are: " +
					map.get(key));
		}
	}

	/** Method (overloaded) for printing Route HashMap for debugging
	 * 
	 * @param route List
	 * @return Line print of GPs that are contained in a List.
	 */
	public static void printMapGraph(List<GeographicPoint> route) {
		for (GeographicPoint GP : route) {
			System.out.println(GP);
		}
	}

	public static void main(String[] args)
	{
		/*
		// Map and BFS Testing		
		System.out.print("Making a new map...");
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", theMap);
		System.out.println("DONE.");

		System.out.println("numVertices: " + theMap.getNumVertices());
		System.out.println("numEdges: " + theMap.getNumEdges());
		System.out.println("\n\nmapGraph:\n" + theMap.getVertices() +"\n\n");
		printMapGraph(theMap);

		// Generate access to GeographicPoints inside theMap for BFS testing
		System.out.println("\n\nTesting bfs() search");
		GeographicPoint[]  listGPs =  theMap.getVertices().
				toArray(new GeographicPoint[theMap.getVertices().size()]);
		System.out.println("\tThe route via bfs() is: ");
		printMapGraph(theMap.bfs(listGPs[0], listGPs[5]));

		// Additional Map and BFS Testing
		MapGraph graph = new MapGraph();
		GraphLoader.loadRoadMap("data/graders/mod2/map2.txt", graph);
		System.out.println("\n\nThe class of 'graph' is: " + graph.getClass()
		+ "\n\tContents of the graph are:");
		printMapGraph(graph);
		System.out.println("\nWith bfs(), the route from Start (" + new GeographicPoint(0, 0) + 
				") to Destination (" + new GeographicPoint(6, 6) + ") is :");
		printMapGraph(graph.bfs(new GeographicPoint(0, 0), new GeographicPoint(6, 6)));
		 */
		/*		
		// Dijkstra Testing

		MapGraph graph = new MapGraph();
		GraphLoader.loadRoadMap("data/graders/mod2/map1.txt", graph);

		System.out.println("\nWith bfs(), the route from Start (" + new GeographicPoint(0, 0) + 
				") to Destination (" + new GeographicPoint(6, 6) + ") is :");
		printMapGraph(graph.bfs(new GeographicPoint(0, 0), new GeographicPoint(6, 6)));

		System.out.println("\nWith dijkstra(), the route from Start (" + new GeographicPoint(0, 0) + 
				") to Destination (" + new GeographicPoint(6, 6) + ") is :");
		printMapGraph(graph.dijkstra(new GeographicPoint(0, 0), new GeographicPoint(6, 6)));

		MapGraph graph2 = new MapGraph();
		GraphLoader.loadRoadMap("data/graders/mod3/map2.txt", graph2);
		System.out.println("With graph2:");
		// printMapGraph(graph2);

		System.out.println("\nWith bfs(), the route from Start (" + new GeographicPoint(7, 3) + 
				") to Destination (" + new GeographicPoint(4, -1) + ") is :");
		printMapGraph(graph2.bfs(new GeographicPoint(7, 3), new GeographicPoint(4, -1)));

		System.out.println("\nWith dijkstra(), the route from Start (" + new GeographicPoint(7, 3) + 
				") to Destination (" + new GeographicPoint(4, -1) + ") is :");
		printMapGraph(graph2.dijkstra(new GeographicPoint(7, 3), new GeographicPoint(4, -1)));

		// aStar Testing
		System.out.println("\nWith aStarSearch(), the route from Start (" + new GeographicPoint(7, 3) + 
				") to Destination (" + new GeographicPoint(4, -1) + ") is :");
		printMapGraph(graph2.aStarSearch(new GeographicPoint(7, 3), new GeographicPoint(4, -1))); 
		 */

		/*		
		// You can use this method for testing.
		// Use this code in Week 3 End of Week Quiz

		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);

		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
		 */
		// Map and TSP Testing		
		System.out.print("Making a new map...");
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", theMap);
		System.out.println("DONE.");

		List<GeographicPoint> visitList = new LinkedList<GeographicPoint>();
		visitList.add(new GeographicPoint(4, 1));
		visitList.add(new GeographicPoint(7, 3));
		visitList.add(new GeographicPoint(8, -1));
		visitList.add(new GeographicPoint(6.5, 0));
		visitList.add(new GeographicPoint(5, 1));
		visitList.add(new GeographicPoint(4, 0));
		visitList.add(new GeographicPoint(4, 1));

		System.out.println("\n\tTotal distance [km] = " + String.format("%.1f%n",
				theMap.distanceCycleRoute(theMap.greedyCycle(visitList))));

		System.out.println("The optimal decision route is:");
		printMapGraph(theMap.twoOptSwap(visitList));
	}
}
