# Duelo de alquimistas

Juego de cartas por turnos hecho en Java y Swing. El jugador se enfrenta a un alquimista rival que elige sus cartas mediante un árbol de decisiones propio.

## Requisitos

- JDK 17 o superior.
- Windows: doble clic en `run.bat`, o ejecutarlo desde la terminal.
- VS Code: abrir esta carpeta e instalar **Extension Pack for Java**. Ejecuta `duelo.Main` con F5, o usa `run.bat`.

El proyecto no necesita librerías externas ni conexión a internet.

## Cómo jugar

1. Pulsa **Nueva partida**.
2. En tu turno, juega las cartas de tu mano haciendo clic en ellas. Cada carta consume energía.
3. Pulsa **Terminar turno** para que actúe el rival.
4. Gana quien reduzca los puntos de vida del otro alquimista a cero.

Las cartas rojas atacan, las verdes recuperan vida y las azules añaden escudo. El escudo absorbe daño antes que la vida. Cada turno se recuperan 3 puntos de energía y se roba una carta. Si el mazo se acaba, el descarte se mezcla y se reutiliza.

## Estructuras y algoritmos propios

No se usan las colecciones `Stack`, `Queue`, `List`, `Tree` ni `Hashtable` de Java. Las estructuras usadas por el juego se implementan en `duelo.estructuras`:

- `Pila<T>`: mazo y descarte; operaciones push, pop y peek.
- `Cola<T>`: orden circular de turnos de jugador y rival.
- `ListaEnlazada<T>`: mano de cartas e historial visible.
- `ArbolDecision`: decisiones tácticas del rival.
- `TablaHash<K,V>`: catálogo de cartas por identificador, con encadenamiento para colisiones.

Algoritmos a explicar en la exposición:

1. **Barajado Fisher–Yates**: recorre el arreglo desde el final e intercambia cada carta con una posición aleatoria válida.
2. **Cola circular**: avanza los índices de inicio y final con módulo para alternar turnos sin desplazar elementos.
3. **Tabla hash con encadenamiento**: transforma la clave en un índice y guarda claves que colisionan en nodos enlazados.
4. **Árbol de decisiones**: evalúa condiciones sobre vida, energía y mano; llega a una acción y selecciona una carta utilizable.
5. **Reciclado de descarte**: cuando el mazo queda vacío, mueve el descarte, lo baraja y lo vuelve a apilar.

## Organización

```text
src/main/java/duelo/
  Main.java
  modelo/       Carta, jugador y reglas de la partida
  estructuras/  estructuras de datos implementadas desde cero
  ia/           árbol de decisión del rival
  ui/           ventana y componentes Swing
```

## Controles y reinicio

Las cartas se juegan con clic. **Terminar turno** cede la iniciativa al rival. **Nueva partida** reinicia vidas, mazos, manos, energía, escudos e historial.

## Guion sugerido para el video (7–8 minutos)

El guion completo, con tiempos y demostración, está en [`VIDEO.md`](VIDEO.md). Graba la pantalla mientras ejecutas el juego y explica cada estructura mostrando su clase fuente. El enlace de YouTube se agrega a la entrega después de subir el video.

## Entrega

Repositorio público: [jhon-florez/duelo-de-alquimistas](https://github.com/jhon-florez/duelo-de-alquimistas). El código fuente, la configuración de VS Code y este README están publicados allí. El video se sube por separado a YouTube; agrega su URL a la entrega después de grabarlo.

