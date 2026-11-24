package duelo.modelo;

import java.util.Random;

/** Barajado Fisher–Yates sobre el arreglo de cartas. */
public final class Barajador {
    private Barajador() { }

    public static void barajar(Carta[] cartas, Random aleatorio) {
        for (int i = cartas.length - 1; i > 0; i--) {
            // Fisher–Yates elige una posición entre 0 e i para intercambiarla con la actual.
            int j = aleatorio.nextInt(i + 1);
            Carta temporal = cartas[i];
            cartas[i] = cartas[j];
            cartas[j] = temporal;
        }
    }
}
