package edu.usf.experiment.universe;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

public class Wall {

	public LineSegment s;
	
	public Wall(float x1, float y1, float x2, float y2){
		s = new LineSegment(new Coordinate(x1, y1), new Coordinate(x2, y2));
	}
	
	public float getX1(){
		return (float) s.p0.x;
	}
	
	public float getY1(){
		return (float) s.p0.y;
	}
	
	public float getX2(){
		return (float) s.p1.x;
	}
	
	public float getY2(){
		return (float) s.p1.y;
	}

}
