# Guion para el video de exposición (7–8 minutos)

Graba la pantalla con el juego abierto y cambia al editor para mostrar las clases indicadas. Habla con tus propias palabras y muestra una partida real.

| Tiempo | Demostración |
|---|---|
| 0:00–0:40 | Presenta el objetivo: vencer al alquimista rival con cartas de ataque, escudo y curación. Menciona que se desarrolló en Java con Swing y que las estructuras se implementaron desde cero. |
| 0:40–1:40 | Pulsa **Nueva partida**. Explica la mano, vida, escudo, energía, mazo y descarte. Juega una carta de ataque y describe su coste y efecto. |
| 1:40–2:35 | Muestra `Pila.java` y explica push/pop/peek. Relaciona el mazo con la pila y muestra que al agotarse se mezcla el descarte. |
| 2:35–3:20 | Muestra `Cola.java` y `MotorPartida.java`. Explica los índices circulares y cómo se alternan los turnos. Termina el turno en pantalla y deja actuar al rival. |
| 3:20–4:05 | Muestra `ListaEnlazada.java`. Explica cómo se agregan y eliminan cartas de la mano y cómo se conservan los mensajes recientes del historial. |
| 4:05–5:20 | Muestra `ArbolDecision.java` y `CerebroAlquimista.java`. Explica una condición real (vida baja, energía suficiente o carta de ataque) y sigue sus ramas hasta la jugada. Compara la explicación en el panel de IA con el código. |
| 5:20–6:05 | Muestra `TablaHash.java` y `CatalogoCartas.java`. Explica el índice hash, las colisiones por encadenamiento y la búsqueda de una carta por ID. |
| 6:05–6:45 | Explica Fisher–Yates en `Barajador.java`: selección aleatoria e intercambio desde el final del arreglo. Si es posible reinicia la partida para enseñar el orden distinto. |
| 6:45–7:30 | Demuestra curación o escudo, pasa otro turno y explica el registro de acciones. Termina indicando cómo ejecutar con `run.bat` o VS Code y dónde está el README. |

Antes de entregar: comprueba que la grabación dure al menos 7 minutos, súbela a YouTube con la visibilidad requerida por el docente y copia el enlace en la plataforma de entrega.
