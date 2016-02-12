/**
 * @author UCSD MOOC development team and B McDougall
 *
 */
package roadgraph;

import java.util.ArrayList;
import java.util.List;
import geography.GeographicPoint;

/**
 * @author B McDougall
 * 
 * A class which represents vertices for a graph of geographic 
 * locations
 */class mapVertex implements Comparable{

//	private final MapGraph mapVertex;
	private GeographicPoint location;
	private List<mapEdge> edgeList;	
	
	// member variables added for advanced search algorithms
	private GeographicPoint startRoute;
	private double distanceEdgeCumFromStart;
	private double distanceGeoFromStart;

	/** Constructor for a vertice
	 * @param location The location of the vertice as
	 * geographical position system coordinates
	 */
	mapVertex(GeographicPoint location) {
		this.location = location;
		this.edgeList = null;
	}

	/** Method for defining a vertice
	 * @param location The location of the vertice as
	 * geographical position system coordinates
	 */
	void setLocation(GeographicPoint location){
		this.location=location;
		return;
	}

	/** Method for getting the the GPS location
	 * of this.vertice
	 * @return location, GPS coordinates
	 */
	GeographicPoint getLocation(){
		return this.location;
	}

	/** Constructor for defining a list an array list
	 * of edges that originates from this.vertice
	 * @return void
	 */
	void setMapEdge(){
		this.edgeList = new ArrayList<mapEdge>();
		return;
	}

	/** Method for adding an edge to array list
	 * of edges that originates from this.vertice
	 * @return void
	 */
	void setMapEdge(mapEdge mapEdge){
		this.edgeList.add(mapEdge);
		return;
	}

	/** Method for getting the array list
	 * of edges that originates from this.vertice
	 * @return edgeList, an array list of edges
	 */
	List<mapEdge> getMapEdge(){
		return this.edgeList;
	}
	
	// getters/setters for advanced search algorithms
	/** Method for defining the global start point for
	 * search algorithms of this package
	 * @return void
	 */
	void setStartRoute(GeographicPoint startRoute){
		this.startRoute=startRoute;
		return;
	}

	/** Method for getting the global start location
	 * of a route search
	 * @return location, GPS coordinates
	 */
	GeographicPoint getStartRoute(){
		return this.startRoute;
	}

	/** Empty method for initializing the priority of
	 * a vertice in a priority queue
	 * @return void
	 */
	void setDistanceEdgeCumFromStart(){
		this.distanceEdgeCumFromStart = Double.POSITIVE_INFINITY;
	}

	/** Method for setting the distance (priority) of the current
	 * vertice from the global start vertice
	 * @return void
	 */
	void setDistanceEdgeCumFromStart(double distance){
		this.distanceEdgeCumFromStart = distance; 
	}

	/** Method for getting the cumulative edge distance
	 * of a vertice from the global start location
	 * @return distance, (km) via edge traversing
	 */
	double getDistanceEdgeCumFromStart(){
		return this.distanceEdgeCumFromStart;
	}

	/** Method for setting the geographical distance of this.vertice
	 * from the global start point for
	 * search algorithms of this package
	 * @return void
	 */
	void setDistanceGeoFromStart(){
		if (this.getStartRoute() != null){
			this.distanceGeoFromStart=this.getLocation().distance(this.getStartRoute());
		} else
		{
			System.err.println("Line #744: RouteStart for this edge is null");;
			this.distanceGeoFromStart=Double.POSITIVE_INFINITY;
		}
		return;
	}

	/** Method for getting the geographical distance of this.vertice
	 * from the global start point for
	 * search algorithms of this package
	 * @return double (km)
	 */
	double getDistanceGeoFromStart(){
		this.setDistanceGeoFromStart();
		return this.distanceGeoFromStart;
	}
	
	// Code to implement Comparable
	/** Method for comparing the priority of vertices within a priority queue
	 * @return int
	 */
	public int compareTo(Object object) {
		// convert to map node, may throw exception
		mapVertex vertex = (mapVertex) object; 
		return ((Double)this.getDistanceEdgeCumFromStart()).compareTo((Double) vertex.getDistanceEdgeCumFromStart());
	}

}