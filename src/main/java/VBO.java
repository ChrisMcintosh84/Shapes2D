import static org.lwjgl.opengl.GL33.*;

public class VBO {
    private int id;

    public VBO(float[] data) {
        id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, id);
    }

    public void unBind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void cleanUp() {
        glDeleteBuffers(id);
    }
}
