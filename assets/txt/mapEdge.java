/**
 * @author B McDougall
 */
package roadgraph;

import geography.GeographicPoint;

/**
 * @author B McDougall
 * 
 * A class which represents edges for a graph of geographic 
 * locations that are represented as vertices
 */
class mapEdge {
	// member variables for initial, physical data
	private GeographicPoint start;
	private GeographicPoint end;
	private String streetname;
	private String roadType;
	private double distance;

	/** Constructor for an edge
	 * @param start The location of the edge's start
	 * @param end The location of the edge's end
	 * @param streetname The name of the street
	 * @param roadType The category of street
	 * @param distance The length of edge in km
	 */
	mapEdge(GeographicPoint start, GeographicPoint end,
			String streetname, String roadType, double distance) {
		this.start = start;
		this.end =  end;
		this.streetname =  streetname;
		this.roadType =  roadType;
		this.distance =  distance;
	}

	/** Method for setting start location of edge
	 * @param start The location of the edge's start
	 */
	void setStart(GeographicPoint start){
		this.start=start;
		return;
	}

	/** Method for getting start location of edge
	 * @return start The location of the edge's start
	 */
	GeographicPoint getStart(){
		return this.start;
	}

	/** Method for setting start location of edge
	 * @return void
	 */
	void setEnd(GeographicPoint end){
		this.end=end;
		return;
	}

	/** Method for getting start location of edge
	 * @return end The location of the edge's start
	 */
	GeographicPoint getEnd(){
		return this.end;
	}

	/** Method for setting streetname of edge
	 * @return void
	 */
	void setStreetname(String streetname){
		this.streetname=streetname;
		return;
	}

	/** Method for getting street name of edge
	 * @return streetname The name of the street
	 */
	String getStreetname(){
		return this.streetname;
	}

	/** Method for setting category of road for edge
	 * @return void
	 */
	void setRoadType(String roadType){
		this.roadType=roadType;
		return;
	}

	/** Method for getting category of street represented
	 * by edge
	 * @return roadType The category of street
	 */
	String getRoadType(){
		return this.roadType;
	}

	/** Method for setting length of edge with error catching
	 * @return void
	 */
	void setDistance(double distance){
		if (distance < 0)
		{System.err.println("Error; attempted to set neg distance");
		} else {
			this.distance=distance;
			return;
		}
	}

	/** Method for getting length of edge in km
	 * @return distance The length of edge in km
	 */
	double getDistance(){
		return this.distance;
	}

	/** Method for printing informative information of edge
	 * @return String
	 */
	public String toString()
	{
		String toReturn = "[EDGE between ";
		toReturn += "\n\t" + this.getStart();
		toReturn += "\n\t" + this.getEnd();
		toReturn += "\nStreet name: " + streetname + " Road type: " + roadType +
				" Segment length: " + String.format("%.3g", distance) + "km";
		
		return toReturn;
	}
	
}