package trabalhopg;

public class Collision {
	public static boolean checkCollision(GameObject obj1, GameObject obj2) {
	    float obj1Left = obj1.getX() - obj1.getWidth() / 2;
	    float obj1Right = obj1.getX() + obj1.getWidth() / 2;
	    float obj1Top = obj1.getY() + obj1.getHeight() / 2;
	    float obj1Bottom = obj1.getY() - obj1.getHeight() / 2;

	    float obj2Left = obj2.getX() - obj2.getWidth() / 2;
	    float obj2Right = obj2.getX() + obj2.getWidth() / 2;
	    float obj2Top = obj2.getY() + obj2.getHeight() / 2;
	    float obj2Bottom = obj2.getY() - obj2.getHeight() / 2;

	    return obj1Right > obj2Left &&
	           obj1Left < obj2Right &&
	           obj1Top > obj2Bottom &&
	           obj1Bottom < obj2Top;
	}
    public static boolean isWithinBounds(GameObject obj, float screenWidth, float screenHeight) {
        return obj.getX() - obj.getWidth() / 2 >= -screenWidth / 2 &&
               obj.getX() + obj.getWidth() / 2 <= screenWidth / 2 &&
               obj.getY() - obj.getHeight() / 2 >= -screenHeight / 2 &&
               obj.getY() + obj.getHeight() / 2 <= screenHeight / 2;
    }
}