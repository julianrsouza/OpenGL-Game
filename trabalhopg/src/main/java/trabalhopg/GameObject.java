package trabalhopg;

import static org.lwjgl.opengl.GL11.*;

import trabalhopg.io.Input;
import trabalhopg.render.Model;
import trabalhopg.render.Texture;

import static org.lwjgl.glfw.GLFW.*;

public class GameObject {
    private Model model;
    private Texture texture;
    private float x, y;
    private float width, height;

    public GameObject(Model model, Texture texture, float x, float y, float width, float height) {
        this.model = model;
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update(Input input) {
        if (input.isKeyDown(GLFW_KEY_W) && y + height / 2 < 1) {
            y += 0.1f;
        }
        if (input.isKeyDown(GLFW_KEY_S) && y - height / 2 > -1) {
            y -= 0.1f;
        }

    }

    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }
    
    public void moveAutomatically(float dx) {
    	x += dx;
    }

    public void render() {
        texture.bind();
        glPushMatrix();
        glTranslatef(x, y, 0);
        model.render();
        glPopMatrix();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
