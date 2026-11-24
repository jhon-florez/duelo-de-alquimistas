package duelo.ia;

import duelo.modelo.Carta;
import duelo.modelo.Jugador;
import duelo.modelo.TipoCarta;

/** Interpreta la hoja del árbol y elige la mejor carta disponible para esa acción. */
public final class CerebroAlquimista {
    private final ArbolDecision arbol = new ArbolDecision();
    private String explicacion = "Esperando su turno.";

    public int elegirCarta(Jugador rival, Jugador jugador) {
        boolean tieneCuracion = false;
        // El árbol necesita saber si el rival tiene una carta para curarse.
        for (int i = 0; i < rival.getCantidadCartas(); i++)
            if (rival.getCarta(i).getTipo() == TipoCarta.CURACION) tieneCuracion = true;
        ArbolDecision.Contexto contexto = new ArbolDecision.Contexto(
                rival.getVida(), jugador.getVida(), rival.getEnergia(), tieneCuracion);
        ArbolDecision.Accion accion = arbol.decidir(contexto);
        int elegida = mejorCarta(rival, accion);
        // Si no hay carta para la prioridad, prueba las alternativas en orden.
        if (elegida < 0) elegida = mejorCarta(rival, ArbolDecision.Accion.ATAQUE);
        if (elegida < 0) elegida = mejorCarta(rival, ArbolDecision.Accion.ESCUDO);
        if (elegida < 0) elegida = mejorCarta(rival, ArbolDecision.Accion.CURAR);
        Carta carta = elegida < 0 ? null : rival.getCarta(elegida);
        explicacion = describir(accion, rival, jugador, carta);
        return elegida;
    }

    private int mejorCarta(Jugador jugador, ArbolDecision.Accion accion) {
        int elegida = -1, puntuacion = -1;
        for (int i = 0; i < jugador.getCantidadCartas(); i++) {
            Carta carta = jugador.getCarta(i);
            if (carta.getCoste() > jugador.getEnergia() || !coincide(carta, accion)) continue;
            // Entre cartas del mismo tipo favorece más poder por menor coste.
            int valor = carta.getPoder() - carta.getCoste();
            if (valor > puntuacion) { puntuacion = valor; elegida = i; }
        }
        return elegida;
    }

    private boolean coincide(Carta carta, ArbolDecision.Accion accion) {
        switch (accion) {
            case CURAR: return carta.getTipo() == TipoCarta.CURACION;
            case ESCUDO: return carta.getTipo() == TipoCarta.ESCUDO;
            case ATAQUE_POTENTE: return carta.getTipo() == TipoCarta.ATAQUE && carta.getPoder() >= 8;
            default: return carta.getTipo() == TipoCarta.ATAQUE;
        }
    }

    private String describir(ArbolDecision.Accion accion, Jugador rival, Jugador jugador, Carta carta) {
        if (carta == null) return "No tiene una carta utilizable; conserva energía.";
        switch (accion) {
            case CURAR: return "Su vida está en " + rival.getVida() + "; eligió curarse con " + carta.getNombre() + ".";
            case ATAQUE: return "Tu vida está en " + jugador.getVida() + "; intenta cerrar el duelo con " + carta.getNombre() + ".";
            case ATAQUE_POTENTE: return "Tiene energía suficiente; eligió un ataque potente: " + carta.getNombre() + ".";
            default: return "Eligió reforzar su defensa con " + carta.getNombre() + ".";
        }
    }

    public String getExplicacion() { return explicacion; }
}
