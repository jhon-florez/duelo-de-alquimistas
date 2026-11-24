package duelo.estructuras;

/** Pila dinámica para mazos y descartes. */
public final class Pila<T> {
    private Object[] elementos = new Object[16];
    private int cantidad;

    public void push(T elemento) {
        // Si el arreglo se llena, se duplica antes de agregar la carta.
        if (cantidad == elementos.length) crecer();
        elementos[cantidad++] = elemento;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) return null;
        // Se retira el último elemento agregado: así funciona LIFO.
        int indice = --cantidad;
        T elemento = (T) elementos[indice];
        elementos[indice] = null;
        return elemento;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        return isEmpty() ? null : (T) elementos[cantidad - 1];
    }

    public boolean isEmpty() { return cantidad == 0; }
    public int size() { return cantidad; }

    private void crecer() {
        // Copiar en el mismo orden conserva la cima de la pila.
        Object[] nuevo = new Object[elementos.length * 2];
        System.arraycopy(elementos, 0, nuevo, 0, cantidad);
        elementos = nuevo;
    }
}
