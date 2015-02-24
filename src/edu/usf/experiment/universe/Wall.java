package edu.usf.experiment.universe;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

public class Wall {

	LineSegment segment;
	
	public Wall(float x1, float y1, float x2, float y2){
		segment = new LineSegment(new Coordinate(x1, y1), new Coordinate(x2, y2));
	}
}
