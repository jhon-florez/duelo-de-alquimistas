package duelo.ia;

/** Árbol binario cuyas hojas indican la prioridad táctica del rival. */
public final class ArbolDecision {
    public enum Condicion { VIDA_BAJA, JUGADOR_DEBIL, ENERGIA_ALTA, SIEMPRE }
    public enum Accion { CURAR, ATAQUE_POTENTE, ATAQUE, ESCUDO }
    private final Nodo raiz;

    public ArbolDecision() {
        Nodo escudo = hoja(Accion.ESCUDO);
        Nodo ataque = hoja(Accion.ATAQUE);
        Nodo ataquePotente = hoja(Accion.ATAQUE_POTENTE);
        Nodo curar = hoja(Accion.CURAR);
        raiz = pregunta(Condicion.VIDA_BAJA, curar,
                pregunta(Condicion.JUGADOR_DEBIL, ataque,
                        pregunta(Condicion.ENERGIA_ALTA, ataquePotente, escudo)));
    }

    public Accion decidir(Contexto contexto) {
        Nodo actual = raiz;
        while (!actual.esHoja()) actual = evaluar(actual.condicion, contexto) ? actual.si : actual.no;
        return actual.accion;
    }

    private boolean evaluar(Condicion condicion, Contexto c) {
        switch (condicion) {
            case VIDA_BAJA: return c.vidaRival <= 15 && c.tieneCuracion;
            case JUGADOR_DEBIL: return c.vidaJugador <= 10;
            case ENERGIA_ALTA: return c.energia >= 3;
            default: return true;
        }
    }

    private Nodo pregunta(Condicion c, Nodo si, Nodo no) { return new Nodo(c, si, no, null); }
    private Nodo hoja(Accion a) { return new Nodo(null, null, null, a); }

    public static final class Contexto {
        public final int vidaRival, vidaJugador, energia;
        public final boolean tieneCuracion;
        public Contexto(int vidaRival, int vidaJugador, int energia, boolean tieneCuracion) {
            this.vidaRival = vidaRival; this.vidaJugador = vidaJugador;
            this.energia = energia; this.tieneCuracion = tieneCuracion;
        }
    }

    private static final class Nodo {
        private final Condicion condicion;
        private final Nodo si, no;
        private final Accion accion;
        private Nodo(Condicion condicion, Nodo si, Nodo no, Accion accion) {
            this.condicion = condicion; this.si = si; this.no = no; this.accion = accion;
        }
        private boolean esHoja() { return accion != null; }
    }
}
