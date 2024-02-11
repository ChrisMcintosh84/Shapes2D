import static org.lwjgl.opengl.GL33.*;

public class ShaderProgram {
    private int id;

    public ShaderProgram() {
        id = glCreateProgram();
    }

    public void attachShaders(Shader vertexShader, Shader fragmentShader) {
        glAttachShader(id, vertexShader.getId());
        glAttachShader(id, fragmentShader.getId());
    }

    public void link() {
        glLinkProgram(id);

        checkStatus();
    }

    public void use() {
        glUseProgram(id);
    }

    public void unUse() {
        glUseProgram(0);
    }

    public int getId() {
        return id;
    }

    public void checkStatus() {
        int status = glGetProgrami(id, GL_LINK_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetProgramInfoLog(id));
        }
    }
}
