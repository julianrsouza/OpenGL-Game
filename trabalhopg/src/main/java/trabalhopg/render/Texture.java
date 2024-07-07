package trabalhopg.render;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {
    private int textureId;
    private int width;
    private int height;

    public Texture(String path) {
        BufferedImage image = loadImage(path);
        if (image == null) {
            throw new IllegalArgumentException("Texture not found: " + path);
        }
        width = image.getWidth();
        height = image.getHeight();

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4); // 4 for RGBA

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green component
                buffer.put((byte) (pixel & 0xFF));         // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component
            }
        }
        buffer.flip();

        // Create OpenGL texture
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Setup wrap mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // Setup texture scaling filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Send texel data to OpenGL
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        // Generate mipmaps
        glGenerateMipmap(GL_TEXTURE_2D);
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void bind(int sampler) {
        if (sampler >= 0 && sampler <= 31) {
            glActiveTexture(GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, textureId);
        } else {
            throw new IllegalArgumentException("Invalid texture sampler index! Should be between 0 and 31.");
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public int getTextureId() {
        return textureId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}