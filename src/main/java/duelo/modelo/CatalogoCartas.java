package duelo.modelo;

import duelo.estructuras.TablaHash;

public final class CatalogoCartas {
    private final TablaHash<String, Carta> cartas = new TablaHash<>(11);

    public CatalogoCartas() {
        registrar(new Carta("FUEGO", "Bola de fuego", "Inflige 5 de daño", TipoCarta.ATAQUE, 1, 5));
        registrar(new Carta("ACIDO", "Frasco ácido", "Inflige 8 de daño", TipoCarta.ATAQUE, 2, 8));
        registrar(new Carta("RAYO", "Rayo solar", "Inflige 12 de daño", TipoCarta.ATAQUE, 3, 12));
        registrar(new Carta("CURAR", "Poción vital", "Recupera 7 de vida", TipoCarta.CURACION, 2, 7));
        registrar(new Carta("ESCUDO", "Piel de cristal", "Añade 6 de escudo", TipoCarta.ESCUDO, 1, 6));
        registrar(new Carta("BARRERA", "Barrera rúnica", "Añade 10 de escudo", TipoCarta.ESCUDO, 2, 10));
    }

    public Carta buscar(String id) { return cartas.get(id); }
    private void registrar(Carta carta) { cartas.put(carta.getId(), carta); }

    public Carta[] crearMazo() {
        String[] ids = {"FUEGO", "FUEGO", "FUEGO", "FUEGO", "ACIDO", "ACIDO", "RAYO", "CURAR", "CURAR", "CURAR",
                "ESCUDO", "ESCUDO", "ESCUDO", "BARRERA", "FUEGO", "ACIDO", "CURAR", "ESCUDO", "BARRERA", "RAYO"};
        Carta[] mazo = new Carta[ids.length];
        for (int i = 0; i < ids.length; i++) mazo[i] = buscar(ids[i]);
        return mazo;
    }
}
