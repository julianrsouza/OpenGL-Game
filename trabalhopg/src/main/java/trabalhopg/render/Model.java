package trabalhopg.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.lwjgl.BufferUtils;

public class Model {
	private int draw_count;
	private int vbo_id;
	private int texture_id;
	private int indice_id;

	public Model(float[] vertices, float[] texture_coords, int[] indices) {
		draw_count = indices.length;

		vbo_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
		glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

		texture_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, texture_id);
		glBufferData(GL_ARRAY_BUFFER, createBuffer(texture_coords), GL_STATIC_DRAW);

		indice_id = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indice_id);

		IntBuffer indiceBuffer = BufferUtils.createIntBuffer(indices.length);
		indiceBuffer.put(indices);
		indiceBuffer.flip();
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indiceBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void render() {
		glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, texture_id); 
        glTexCoordPointer(2, GL_FLOAT, 0, 0);       

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indice_id);
        glDrawElements(GL_TRIANGLES, draw_count, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
	}

	private FloatBuffer createBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
