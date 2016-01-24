/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;
//import week2example.MazeNode;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections 
 *
 */
public class MapGraph {
	//TODO: Add your member variables here in WEEK 2
	/*		
	  NOTE: detailed debugging System.out.println(String) were
	        removed from this class because the msgs reduced
	        the read-ability of the code. Some remain
	        
	  NOTE: for auto-graders, please comment out any
	        System.err.println() and Exception(Strings).
	        See lines near: 122, 177, 234, and 240

	  User need not know vertex, edge count, etc., so these
      member variables are private
	 */	
	// HashMap: member variable that store GeographicPoints as (location, nodes)
	// in mapGraph (adjacency list structure since degree << numVertices)
	private HashMap<GeographicPoint, mapVertex> mapVertices;
	//	HashSet: member variable that stores visited nodes for BFS
	private HashSet<GeographicPoint> setVerticesVisited;
	// HashMap: member variable that is used to track route linkage of nodes in BFS
	private HashMap<GeographicPoint, GeographicPoint> parentMap;

	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		// TODO: Implement in this constructor in WEEK 2
		// Maps contain locations & vertices (nodes) with
		// its weighted edges. NOTE:  weight = distance
		// Structure is adjacency list b/c degree << numVertices
		mapVertices = new HashMap<GeographicPoint, mapVertex>();
	}

	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		//TODO: Implement this method in WEEK 2
		// number of vertices are tracked in addVertex()
		//		return numVertices;
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
		// number of edges are tracked in addEdge() and
		// is the total count of values inside the mapGraph HashMap
		int edgeListCount = 0;
		for (GeographicPoint pt : this.getVertices()) {
			edgeListCount += this.mapVertices.get(pt).getMapEdge().size();
		}
		return edgeListCount;
	}



	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
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
				this.getVertices().contains(location) ){
			return false;
		}
		else {
			// Boundary conditions satisfied.
			// addVertex method called
			implementAddVertex(location);
			// System.out.println("Successfully added vertex...NEXT!");
		}
		return true;
	}

	public void implementAddVertex(GeographicPoint location) {
		//TODO complete this method  which is added by myself
		// purpose is treating nodes as an object that contains
		// a list of edges that connect to other nodes in mapGraph
		mapVertex mapVertex = new mapVertex();
		mapVertex.setLocation(location);
		mapVertex.setMapEdge(); // instantiate an empty list for containing edges
		//add vertex to HashMap mapGraph
		this.mapVertices.put(location, mapVertex);
		return;
	}

	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
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
		// in this method, we check whether conditions for adding an edge
		// are satisfied. Conditions satisfied -> implementAddEdge() else
		// throw exception for violating boundary conditions

		if (from != null && to != null && roadName != null && 
				roadType != null && length >= 0 &&
				this.getVertices().contains(from) && this.getVertices().contains(to) )
		{
			// call method to add edge
			implementAddEdge(from , to, roadName, roadType, length);
		}
		else {
			throw new IndexOutOfBoundsException("Specifications for edge are invalid."
					+ "Edge not added to mapGraph");
		}

	}

	/**
	 * Method executes addition of new
	 * edge to vertex.
	 */
	public void implementAddEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) {
		//TODO complete this method added by myself
		// get the list for the HashMap key "from" and add the GP "to" to the valueList;
		// by implementation, GPs are already in keySet of mapGraph.
		// instantiate a mapEdge
		mapEdge mapEdge = new mapEdge();
		mapEdge.setStart(from);
		mapEdge.setEnd(to);
		mapEdge.setStreetname(roadName);
		mapEdge.setDistance(length);

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
		// TODO: Implement this method in WEEK 2

		// Hook for visualization.  See write-up.
		// nodeSearched.accept(next.getLocation());

		// execute BFS after conditional testing
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
				// Initialize variables for BFS
				GeographicPoint nextGP;
				Queue<GeographicPoint> bfsQueue = new LinkedList<GeographicPoint>();
				setVerticesVisited = new HashSet<GeographicPoint>(); 
				parentMap = new HashMap<GeographicPoint, GeographicPoint>();

				nextGP = null; // interesting decision: assign null or start
				bfsQueue.add(start);
				//	nodeSearched.accept(nextGP);
				//	setVerticesVisited.add(nextGP);

				//	debug variable that tracks while-loop count in BFS
				int counterDebug=0;
				// by implementation, this whileLoop finds a route
				while (!bfsQueue.isEmpty()) {
					nextGP = bfsQueue.poll();
					nodeSearched.accept(nextGP);

					if (nextGP.equals(goal)) break;

					for (mapEdge edge : this.mapVertices.get(nextGP).getMapEdge()){
						// logic that excludes visited sites from queue
						if ( !setVerticesVisited.contains(edge.getEnd()) ){
							bfsQueue.add(edge.getEnd());
							parentMap.put(edge.getEnd(), nextGP);
							//System.out.println("At whileLoop #" + counterDebug + 
							//		"parentMap key,value pair: (" + nextGP + ", " + edge.getEnd() + ")");
							setVerticesVisited.add(nextGP);
						}
					}
					counterDebug++;
					// System.out.println("BFS: Dequeue (" + nextGP.getX() + ", " + nextGP.getY() + ")");
				}
				// logic test that confirms whether BFS is successful
				if (!nextGP.equals(goal)) {
					System.out.println("Damn! No path from " + start + "to" + goal);
					return null;
				}
				return buildRouteList(parentMap, start, goal);
			}
	}

	private List<GeographicPoint> buildRouteList(HashMap<GeographicPoint, GeographicPoint> parentMap,
			GeographicPoint start, GeographicPoint goal)
	{
		// routeList is used as return and is a list of nodes for the route
		LinkedList<GeographicPoint> routeList = new LinkedList<GeographicPoint>();
		GeographicPoint current = goal;
		/*
		System.out.println("\nEntering buildRoute(). \nHere is parentMap for building routeList: \n" +
				"Start node=" + start + "Goal Node = " + goal +
				"\n\nBefore while-loop, routeList = " + routeList +
				"\n\nparentMap:" );
		printMapGraph(parentMap);
		System.out.println("\nCurrent GP (key) = " + current);
		System.out.println("\nSince start = " + start + ", so start.equals(current) = " + current.equals(start));
		System.out.println("\n and Next GP (value) going into routeList:  " + parentMap.get(current));
		 */
		while (!current.equals(start)) {
			routeList.addFirst(current);
			current = parentMap.get(current);
		}
		// add start GeographicPoint to head of routeList
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

		return null;
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

		return null;
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
			for (mapEdge edge : map.mapVertices.get(pt).edgeList){
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

	/** Method for generating random points outside graph for debug testing
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 * @return The Geographic Point constructed from (x, y).
	 */
	public static GeographicPoint genPoint(double x, double y){
		GeographicPoint GP = new GeographicPoint(x, y);
		return GP;
	}


	public static void main(String[] args)
	{
/*		
		System.out.print("Making a new map...");
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", theMap);
		System.out.println("DONE.");

		System.out.println("numVertices: " + theMap.getNumVertices());
		System.out.println("numEdges: " + theMap.getNumEdges());
		System.out.println("\n\nmapGraph:\n" + theMap.getVertices() +"\n\n");
		printMapGraph(theMap);

		//		Generate access to GeographicPoints inside theMap for BFS testing
		GeographicPoint[]  listGPs =  theMap.getVertices().
				toArray(new GeographicPoint[theMap.getVertices().size()]);
		theMap.bfs(listGPs[0], new GeographicPoint(10, 10));

		MapGraph graph = new MapGraph();
		GraphLoader.loadRoadMap("data/graders/mod2/map2.txt", graph);
		printMapGraph(graph);
		graph.bfs(genPoint(0, 0), genPoint(6, 6));
*/


		// You can use this method for testing.
		// Use this code in Week 3 End of Week Quiz
		/*
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);


		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		 */
	}

	class mapVertex{
		GeographicPoint location;
		List<mapEdge> edgeList;	

		private void setLocation(GeographicPoint location){
			this.location=location;
			return;
		}

		private GeographicPoint getLocation(){
			return this.location;
		}

		private void setMapEdge(){
			this.edgeList = new ArrayList<mapEdge>();
			return;
		}

		private void setMapEdge(mapEdge mapEdge){
			this.edgeList.add(mapEdge);
			return;
		}

		private List<mapEdge> getMapEdge(){
			return this.edgeList;
		}
	}

	class mapEdge{
		GeographicPoint start;
		GeographicPoint end;
		String streetname;
		String roadType;
		double distance;

		private void setStart(GeographicPoint start){
			this.start=start;
			return;
		}

		private GeographicPoint getStart(){
			return this.start;
		}

		private void setEnd(GeographicPoint end){
			this.end=end;
			return;
		}

		private GeographicPoint getEnd(){
			return this.end;
		}

		private void setStreetname(String streetname){
			this.streetname=streetname;
			return;
		}

		private String getStreetname(){
			return this.streetname;
		}

		private void setRoadType(String roadType){
			this.roadType=roadType;
			return;
		}

		private String getoadType(){
			return this.roadType;
		}

		private void setDistance(double distance){
			this.distance=distance;
			return;
		}

		private double getDistance(){
			return this.distance;
		}


	}

}
