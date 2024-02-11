import static org.lwjgl.opengl.GL33.*;

public class VAO {
    private int id;

    public VAO() {
        id = glGenVertexArrays();
        glBindVertexArray(id);
    }

    public void bind() {
        glBindVertexArray(id);
    }

    public void unBind() {
        glBindVertexArray(0);
    }

    public void cleanUp() {
        glDeleteVertexArrays(id);
    }
}
