package com.badlogic.androidgames.framework.math;

public class OverlapTester {
    public static boolean overlapCircles(Circle c1, Circle c2) {
        float distance = c1.center.dist2(c2.center);
        float radiusSum = c1.radius + c2.radius;
        return distance <= radiusSum * radiusSum;
    }
    
    public static boolean overlap(Rectangle r1, Rectangle r2) {
        if(r1.lowerLeft.x < r2.lowerLeft.x + r2.width &&
           r1.lowerLeft.x + r1.width > r2.lowerLeft.x &&
           r1.lowerLeft.y < r2.lowerLeft.y + r2.height &&
           r1.lowerLeft.y + r1.height > r2.lowerLeft.y)
            return true;
        else
            return false;
    }
    
    public static boolean overlap(Circle c, Rectangle r) {
        float closestX = c.center.x;
        float closestY = c.center.y;
        
        if(c.center.x < r.lowerLeft.x) {
            closestX = r.lowerLeft.x; 
        } 
        else if(c.center.x > r.lowerLeft.x + r.width) {
            closestX = r.lowerLeft.x + r.width;
        }
          
        if(c.center.y < r.lowerLeft.y) {
            closestY = r.lowerLeft.y;
        } 
        else if(c.center.y > r.lowerLeft.y + r.height) {
            closestY = r.lowerLeft.y + r.height;
        }
        
        return c.center.dist2(closestX, closestY) < c.radius * c.radius;           
    }
    
	public static boolean overlap(Sphere s1, Sphere s2) {
		  float distance = s1.center.dist2(s2.center);
	      float radiusSum = s1.radius + s2.radius;
	      return distance <= radiusSum * radiusSum;
	}
	
	public static boolean pointInSphere(Sphere c, Vector3 p) {
	    return c.center.dist2(p) < c.radius * c.radius;
	}
	
	public static boolean pointInSphere(Sphere c, float x, float y, float z) {
	    return c.center.dist2(x, y, z) < c.radius * c.radius;
	}
    
    public static boolean pointInCircle(Circle c, Vector2 p) {
        return c.center.dist2(p) < c.radius * c.radius;
    }
    
    public static boolean pointInCircle(Circle c, float x, float y) {
        return c.center.dist2(x, y) < c.radius * c.radius;
    }
    
    public static boolean pointInRectangle(Rectangle r, Vector2 p) {
        return r.lowerLeft.x <= p.x && r.lowerLeft.x + r.width >= p.x &&
               r.lowerLeft.y <= p.y && r.lowerLeft.y + r.height >= p.y;
    }
    
    public static boolean pointInRectangle(Rectangle r, float x, float y) {
        return r.lowerLeft.x <= x && r.lowerLeft.x + r.width >= x &&
               r.lowerLeft.y <= y && r.lowerLeft.y + r.height >= y;
    }
}