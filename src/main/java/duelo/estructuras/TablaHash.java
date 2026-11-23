package duelo.estructuras;

/** Tabla hash propia con encadenamiento separado para resolver colisiones. */
public final class TablaHash<K, V> {
    private final Nodo<K, V>[] cubetas;

    @SuppressWarnings("unchecked")
    public TablaHash(int capacidad) { cubetas = (Nodo<K, V>[]) new Nodo[Math.max(5, capacidad)]; }

    public void put(K clave, V valor) {
        int indice = indice(clave);
        for (Nodo<K, V> nodo = cubetas[indice]; nodo != null; nodo = nodo.siguiente) {
            if (nodo.clave.equals(clave)) { nodo.valor = valor; return; }
        }
        Nodo<K, V> nuevo = new Nodo<>(clave, valor);
        nuevo.siguiente = cubetas[indice];
        cubetas[indice] = nuevo;
    }

    public V get(K clave) {
        for (Nodo<K, V> nodo = cubetas[indice(clave)]; nodo != null; nodo = nodo.siguiente)
            if (nodo.clave.equals(clave)) return nodo.valor;
        return null;
    }

    private int indice(K clave) { return (clave.hashCode() & 0x7fffffff) % cubetas.length; }

    private static final class Nodo<K, V> {
        private final K clave;
        private V valor;
        private Nodo<K, V> siguiente;
        private Nodo(K clave, V valor) { this.clave = clave; this.valor = valor; }
    }
}
