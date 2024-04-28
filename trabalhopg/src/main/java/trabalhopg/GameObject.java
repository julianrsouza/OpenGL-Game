package trabalhopg;

import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;


public class GameObject {
	private String objectName;
    private float[] vertices;
	private float positionX, positionY;
	private float speedPositionX, speedPositionY;
	
	public GameObject( String objectName, float[] vertices, float positionX, float positionY ) {
		this.objectName = objectName;
		this.vertices = vertices;
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	public GameObject() {}

	public void render() {
		glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		glBegin(GL_QUADS);
		for (int i = 0; i < vertices.length; i += 2) {
			GL11.glVertex2f(vertices[i] + positionX, vertices[i + 1] + positionY);
    	}
        glEnd();
    }
	
	public void move() {
		positionX += speedPositionX;
		positionY += speedPositionY;
	}
	
	public boolean checkCollision( GameObject otherObject ) {
		float thisLeft = this.positionX;
	    float thisRight = this.positionX + this.getWidth();
	    float thisTop = this.positionY + this.getHeight();
	    float thisBottom = this.positionY;

	    float otherLeft = otherObject.getPositionX();
	    float otherRight = otherObject.getPositionX() + otherObject.getWidth();
	    float otherTop = otherObject.getPositionY() + otherObject.getHeight();
	    float otherBottom = otherObject.getPositionY();

	    if (thisRight >= otherLeft && thisLeft <= otherRight &&
	        thisTop >= otherBottom && thisBottom <= otherTop) {
	        return true; 
	    }

	    return false;
	}
	
	public float getWidth() {
	    float minX = Float.MAX_VALUE;
	    float maxX = Float.MIN_VALUE;
	    for (int i = 0; i < vertices.length; i += 2) {
	        float x = vertices[i] + positionX;
	        if (x < minX) minX = x;
	        if (x > maxX) maxX = x;
	    }
	    return maxX - minX;
	}

	public float getHeight() {
	    float minY = Float.MAX_VALUE;
	    float maxY = Float.MIN_VALUE;
	    for (int i = 1; i < vertices.length; i += 2) {
	        float y = vertices[i] + positionY;
	        if (y < minY) minY = y;
	        if (y > maxY) maxY = y;
	    }
	    return maxY - minY;
	}
	
	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public float[] getVertices() {
		return vertices;
	}

	public void setVertices(float[] vertices) {
		this.vertices = vertices;
	}

	public float getPositionX() {
		return positionX;
	}

	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}

	public float getPositionY() {
		return positionY;
	}

	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}

	public float getSpeedPositionX() {
		return speedPositionX;
	}

	public void setSpeedPositionX(float speedPositionX) {
		this.speedPositionX = speedPositionX;
	}

	public float getSpeedPositionY() {
		return speedPositionY;
	}

	public void setSpeedPositionY(float speedPositionY) {
		this.speedPositionY = speedPositionY;
	}
}
