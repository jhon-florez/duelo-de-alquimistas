package duelo;

import duelo.interfaz.VentanaJuego;
import javax.swing.SwingUtilities;

public final class Main {
    private Main() { }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaJuego().setVisible(true));
    }
}
