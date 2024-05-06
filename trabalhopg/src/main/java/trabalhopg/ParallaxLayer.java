package trabalhopg;

public class ParallaxLayer extends GameObject {
	private float speed;
	
	public ParallaxLayer(String id, float[] vertices, float positionX, float positionY,float speed) {
		super(id, vertices, positionX, positionY);
		this.speed = speed;
	}
	
	public void move(GameObject player) {
		if(player.getSpeedPositionX() > 0) {
			this.setSpeedPositionX(speed);
		} 
		if (player.getSpeedPositionX() < 0) {
			this.setSpeedPositionX(-speed);
		}
	
		
	}
}
