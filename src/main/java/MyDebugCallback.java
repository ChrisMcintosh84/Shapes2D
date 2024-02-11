import org.lwjgl.opengl.GLDebugMessageCallbackI;
import static org.lwjgl.system.MemoryUtil.memUTF8;

public class MyDebugCallback implements GLDebugMessageCallbackI {
    @Override
    public void invoke(int source, int type, int id, int severity, int length, long message, long userParam) {
        System.err.println("OpenGL Debug Message:");
        System.err.println(" Source: " + source);
        System.err.println("Type: " + type);
        System.err.println("ID: " + id);
        System.err.println("Severity: " + severity);
        System.err.println("Message: " + memUTF8(message));
    }
}
