package lab1;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
public class GlobalKeyListener implements NativeKeyListener {
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            System.exit(0);
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) { }

    public void nativeKeyTyped(NativeKeyEvent e) { }
}
