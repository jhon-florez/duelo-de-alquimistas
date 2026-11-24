package duelo.modelo;

import duelo.estructuras.Cola;
import duelo.estructuras.ListaEnlazada;
import duelo.ia.CerebroAlquimista;

/** Coordina reglas, turno activo, registro de partida y decisiones del rival. */
public final class MotorPartida {
    public enum Turno { JUGADOR, RIVAL }
    private final CatalogoCartas catalogo = new CatalogoCartas();
    private final CerebroAlquimista cerebro = new CerebroAlquimista();
    private final Cola<Turno> turnos = new Cola<>();
    private final ListaEnlazada<String> historial = new ListaEnlazada<>();
    private Jugador jugador;
    private Jugador rival;
    private Turno turnoActual = Turno.JUGADOR;
    private boolean terminada;
    private String resultado = "";
    private String explicacionIA = "El rival analizará la partida cuando termine tu turno.";
    private Runnable alCambiar = () -> { };

    public void setAlCambiar(Runnable accion) { alCambiar = accion == null ? () -> { } : accion; }

    public void iniciar() {
        jugador = new Jugador("Tú", catalogo, new java.util.Random());
        rival = new Jugador("Maestro Ámbar", catalogo, new java.util.Random());
        turnoActual = Turno.JUGADOR; terminada = false; resultado = "";
        turnos.clear(); historial.clear();
        turnos.offer(Turno.JUGADOR); turnos.offer(Turno.RIVAL);
        jugador.robarInicial(4); rival.robarInicial(4); jugador.iniciarPrimeraMano();
        registrar("Sistema | El duelo comienza. Tú tienes la iniciativa.");
        alCambiar.run();
    }

    public boolean jugarCarta(int indice) {
        if (terminada || turnoActual != Turno.JUGADOR) return false;
        Carta carta = jugador.jugar(indice);
        if (carta == null) return false;
        aplicar(carta, jugador, rival);
        revisarFinal(); alCambiar.run();
        return true;
    }

    public void terminarTurno() {
        if (terminada || turnoActual != Turno.JUGADOR) return;
        avanzarTurno();
        registrar("Sistema | Maestro Ámbar analiza sus opciones.");
        alCambiar.run();
    }

    public void ejecutarTurnoRival() {
        if (terminada || turnoActual != Turno.RIVAL) return;
        int jugadas = 0;
        while (!terminada && jugadas < 3) {
            int indice = cerebro.elegirCarta(rival, jugador);
            explicacionIA = cerebro.getExplicacion();
            if (indice < 0) { registrar("Sistema | " + explicacionIA); break; }
            Carta carta = rival.jugar(indice);
            if (carta == null) break;
            aplicar(carta, rival, jugador);
            registrar("Sistema | Decisión del Maestro Ámbar: " + explicacionIA);
            jugadas++;
            revisarFinal();
            alCambiar.run();
        }
        if (!terminada) avanzarTurno();
        alCambiar.run();
    }

    private void avanzarTurno() {
        // El turno atendido pasa al final de la cola para alternar jugador y rival.
        Turno terminado = turnos.poll();
        if (terminado != null) turnos.offer(terminado);
        turnoActual = turnos.isEmpty() ? Turno.JUGADOR : turnos.peek();
        Jugador activo = turnoActual == Turno.JUGADOR ? jugador : rival;
        activo.iniciarTurno();
        String emisor = turnoActual == Turno.JUGADOR ? "Tú" : "Maestro Ámbar";
        if (activo.getCantidadCartas() > 0) registrar(emisor + " | roba una carta y recupera 3 de energía.");
        else registrar(emisor + " | recupera 3 de energía; no quedan cartas para robar.");
    }

    private void aplicar(Carta carta, Jugador origen, Jugador objetivo) {
        switch (carta.getTipo()) {
            case ATAQUE:
                objetivo.recibirDanio(carta.getPoder());
                registrar(emisor(origen) + " | " + verbo(origen, "lanza", "lanzas") + " "
                        + carta.getNombre() + " que " + minusculaInicial(carta.getDescripcion()) + " a "
                        + (objetivo == jugador ? "ti." : "Maestro Ámbar."));
                break;
            case CURACION:
                registrar(emisor(origen) + " | usa " + carta.getNombre() + ": recupera "
                        + origen.curar(carta.getPoder()) + " de vida.");
                break;
            case ESCUDO:
                origen.ganarEscudo(carta.getPoder());
                registrar(emisor(origen) + " | usa " + carta.getNombre() + ": obtiene "
                        + carta.getPoder() + " de escudo.");
                break;
            default:
                registrar(emisor(origen) + " | usa " + carta.getNombre() + ".");
                break;
        }
    }

    private String emisor(Jugador jugador) {
        return jugador == rival ? "Maestro Ámbar" : "Tú";
    }

    private String verbo(Jugador jugador, String terceraPersona, String segundaPersona) {
        return jugador == rival ? terceraPersona : segundaPersona;
    }

    private String minusculaInicial(String texto) {
        if (texto == null || texto.isEmpty()) return "";
        return Character.toLowerCase(texto.charAt(0)) + texto.substring(1);
    }

    private void revisarFinal() {
        if (jugador.getVida() > 0 && rival.getVida() > 0) return;
        terminada = true;
        resultado = jugador.getVida() > 0 ? "¡Victoria! Has vencido al Maestro Ámbar." : "El Maestro Ámbar ganó esta vez. Inténtalo de nuevo.";
        registrar("Sistema | " + resultado);
    }

    private void registrar(String mensaje) {
        historial.add(mensaje);
        if (historial.size() > 40) historial.removeAt(0);
    }

    public String getHistorial(int indice) { return historial.get(indice); }
    public int getHistorialSize() { return historial.size(); }
    public Jugador getJugador() { return jugador; }
    public Jugador getRival() { return rival; }
    public Turno getTurnoActual() { return turnoActual; }
    public boolean isTerminada() { return terminada; }
    public String getResultado() { return resultado; }
    public String getExplicacionIA() { return explicacionIA; }
}
