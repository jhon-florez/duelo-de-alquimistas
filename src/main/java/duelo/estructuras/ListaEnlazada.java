package duelo.estructuras;

/** Lista simple enlazada para la mano y el historial. */
public final class ListaEnlazada<T> {
    private Nodo<T> primero;
    private int cantidad;

    public void add(T valor) {
        Nodo<T> nuevo = new Nodo<>(valor);
        if (primero == null) primero = nuevo;
        else {
            Nodo<T> actual = primero;
            while (actual.siguiente != null) actual = actual.siguiente;
            actual.siguiente = nuevo;
        }
        cantidad++;
    }

    public T get(int indice) {
        if (indice < 0 || indice >= cantidad) return null;
        Nodo<T> actual = primero;
        for (int i = 0; i < indice; i++) actual = actual.siguiente;
        return actual.valor;
    }

    public T removeAt(int indice) {
        if (indice < 0 || indice >= cantidad) return null;
        Nodo<T> anterior = null;
        Nodo<T> actual = primero;
        for (int i = 0; i < indice; i++) { anterior = actual; actual = actual.siguiente; }
        if (anterior == null) primero = actual.siguiente;
        else anterior.siguiente = actual.siguiente;
        cantidad--;
        return actual.valor;
    }

    public int size() { return cantidad; }
    public boolean isEmpty() { return cantidad == 0; }
    public void clear() { primero = null; cantidad = 0; }

    private static final class Nodo<E> {
        private final E valor;
        private Nodo<E> siguiente;
        private Nodo(E valor) { this.valor = valor; }
    }
}
