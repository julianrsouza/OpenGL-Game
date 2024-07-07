package trabalhopg;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import trabalhopg.render.Texture;

public class ParallaxLayer {
    private int vboId;
    private int textureId;
    private int drawCount;
    private float speed;
    private float positionX;
    private Texture texture;

    public ParallaxLayer(float speed, String texturePath) {
        this.speed = speed;
        this.positionX = 0;

        float[] vertices = {
            -1.5f, -1.0f, 0.0f,   // bottom-left
             1.5f, -1.0f, 0.0f,   // bottom-right
             1.5f,  1.0f, 0.0f,   // top-right
            -1.5f,  1.0f, 0.0f    // top-left
        };

        float[] textureCoords = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f
        };

        int[] indices = {
            0, 1, 2,
            2, 3, 0
        };

        drawCount = indices.length;

        // Cria o VBO (Vertex Buffer Object) para os vértices
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        // Cria o VBO para as coordenadas de textura
        textureId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, textureId);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(textureCoords), GL_STATIC_DRAW);

        // Cria o VBO para os índices
        int indiceId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indiceId);
        IntBuffer indiceBuffer = BufferUtils.createIntBuffer(indices.length);
        indiceBuffer.put(indices);
        indiceBuffer.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indiceBuffer, GL_STATIC_DRAW);

        // Carrega a textura
        texture = new Texture(texturePath);

        // Limpa os bindings
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void update(float deltaTime) {
        positionX -= speed * deltaTime;
    }

    public void render( float screenWidth) {
        // Habilita o uso dos VBOs
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        // Liga o VBO dos vértices
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        // Liga o VBO das coordenadas de textura
        glBindBuffer(GL_ARRAY_BUFFER, textureId);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);

        // Desloca a camada de parallax na horizontal
        glPushMatrix();
        glTranslatef(positionX, 0, 0);
        
        if (positionX <= -screenWidth) {
            // Move a camada de volta para a direita da tela
            positionX += screenWidth;
        }
        // Aplica a textura
        texture.bind();

        // Desenha a camada de parallax
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, drawCount);
        glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);
        
        if (positionX < 0) {
            glTranslatef(screenWidth, 0, 0);
            glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);
        }
        // Restaura a matriz de transformação
        glPopMatrix();

        // Desabilita os VBOs e limpa os bindings
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private FloatBuffer createBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}