package duelo.modelo;

public final class Carta {
    private final String id;
    private final String nombre;
    private final String descripcion;
    private final TipoCarta tipo;
    private final int coste;
    private final int poder;

    public Carta(String id, String nombre, String descripcion, TipoCarta tipo, int coste, int poder) {
        this.id = id; this.nombre = nombre; this.descripcion = descripcion;
        this.tipo = tipo; this.coste = coste; this.poder = poder;
    }
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public TipoCarta getTipo() { return tipo; }
    public int getCoste() { return coste; }
    public int getPoder() { return poder; }
}
