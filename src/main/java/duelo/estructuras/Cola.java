package duelo.estructuras;

/** Cola circular dinámica para alternar los turnos. */
public final class Cola<T> {
    private Object[] elementos = new Object[8];
    private int inicio;
    private int cantidad;

    public void offer(T elemento) {
        if (cantidad == elementos.length) crecer();
        // El módulo permite volver al inicio al llegar al final del arreglo.
        elementos[(inicio + cantidad) % elementos.length] = elemento;
        cantidad++;
    }

    @SuppressWarnings("unchecked")
    public T poll() {
        if (isEmpty()) return null;
        // Se atiende primero el turno más antiguo: la cola sigue FIFO.
        T elemento = (T) elementos[inicio];
        elementos[inicio] = null;
        inicio = (inicio + 1) % elementos.length;
        cantidad--;
        return elemento;
    }

    public boolean isEmpty() { return cantidad == 0; }
    public int size() { return cantidad; }
    public void clear() { elementos = new Object[8]; inicio = 0; cantidad = 0; }

    @SuppressWarnings("unchecked")
    public T peek() { return isEmpty() ? null : (T) elementos[inicio]; }

    private void crecer() {
        Object[] nuevo = new Object[elementos.length * 2];
        // Copiar desde el inicio lógico deja los turnos en orden y sin saltos.
        for (int i = 0; i < cantidad; i++) nuevo[i] = elementos[(inicio + i) % elementos.length];
        elementos = nuevo;
        inicio = 0;
    }
}
