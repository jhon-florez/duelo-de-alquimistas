package duelo.modelo;

import duelo.estructuras.ListaEnlazada;
import duelo.estructuras.Pila;
import java.util.Random;

public final class Jugador {
    private final String nombre;
    private final ListaEnlazada<Carta> mano = new ListaEnlazada<>();
    private final Pila<Carta> mazo = new Pila<>();
    private final Pila<Carta> descarte = new Pila<>();
    private final Random aleatorio;
    private int vida = 30;
    private int escudo;
    private int energia;

    public Jugador(String nombre, CatalogoCartas catalogo, Random aleatorio) {
        this.nombre = nombre; this.aleatorio = aleatorio;
        Carta[] cartas = catalogo.crearMazo();
        Barajador.barajar(cartas, aleatorio);
        for (int i = cartas.length - 1; i >= 0; i--) mazo.push(cartas[i]);
    }

    public void iniciarTurno() { energia = 3; robar(); }
    public void iniciarPrimeraMano() { energia = 3; }
    public void robarInicial(int cantidad) { for (int i = 0; i < cantidad; i++) robar(); }

    public boolean robar() {
        if (mazo.isEmpty()) reciclarDescarte();
        Carta carta = mazo.pop();
        if (carta == null) return false;
        mano.add(carta);
        return true;
    }

    private void reciclarDescarte() {
        if (descarte.isEmpty()) return;
        // El descarte vuelve al mazo, pero se baraja para cambiar el orden anterior.
        Carta[] recicladas = new Carta[descarte.size()];
        for (int i = 0; i < recicladas.length; i++) recicladas[i] = descarte.pop();
        Barajador.barajar(recicladas, aleatorio);
        for (int i = recicladas.length - 1; i >= 0; i--) mazo.push(recicladas[i]);
    }

    public Carta jugar(int indice) {
        Carta carta = mano.get(indice);
        if (carta == null || carta.getCoste() > energia) return null;
        mano.removeAt(indice);
        energia -= carta.getCoste();
        descarte.push(carta);
        return carta;
    }

    public void recibirDanio(int cantidad) {
        // El escudo absorbe primero; solo el daño restante reduce la vida.
        int absorbido = Math.min(escudo, cantidad);
        escudo -= absorbido;
        vida = Math.max(0, vida - (cantidad - absorbido));
    }
    public int curar(int cantidad) { int antes = vida; vida = Math.min(30, vida + cantidad); return vida - antes; }
    public void ganarEscudo(int cantidad) { escudo += cantidad; }
    public boolean gastarEnergia(int cantidad) { if (cantidad > energia) return false; energia -= cantidad; return true; }

    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getEscudo() { return escudo; }
    public int getEnergia() { return energia; }
    public int getCantidadCartas() { return mano.size(); }
    public Carta getCarta(int indice) { return mano.get(indice); }
    public int getCartasEnMazo() { return mazo.size(); }
    public int getCartasEnDescarte() { return descarte.size(); }
}
