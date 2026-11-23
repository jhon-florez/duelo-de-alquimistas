package duelo.ui;

import duelo.modelo.Carta;
import duelo.modelo.MotorPartida;
import duelo.modelo.TipoCarta;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/** Ventana principal y presentación visual de la partida. */
public final class VentanaJuego extends JFrame {
    private static final Color FONDO = new Color(16, 22, 37);
    private static final Color TARJETA = new Color(28, 38, 59);
    private static final Color TEXTO = new Color(236, 239, 247);
    private final MotorPartida motor = new MotorPartida();
    private final JLabel estado = new JLabel("Pulsa Nueva partida para comenzar", SwingConstants.CENTER);
    private final JLabel panelJugador = etiquetaGrande();
    private final JLabel panelRival = etiquetaGrande();
    private final JLabel estadoMazos = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel pensamientoIA = new JLabel("La estrategia del rival aparecerá aquí.");
    private final JTextArea registro = new JTextArea();
    private final JPanel mano = new JPanel(new GridLayout(0, 5, 10, 10));
    private final JButton pasar = new JButton("Terminar turno");
    private final JButton nueva = new JButton("Nueva partida");
    private final Timer temporizadorRival;

    public VentanaJuego() {
        super("Duelo de alquimistas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 720));
        setSize(1180, 790);
        setLocationRelativeTo(null);
        getContentPane().setBackground(FONDO);
        temporizadorRival = new Timer(650, evento -> {
            ((Timer) evento.getSource()).stop();
            motor.ejecutarTurnoRival();
            if (motor.isTerminada()) mostrarResultado();
        });
        construirInterfaz();
        motor.setAlCambiar(this::actualizar);
        nueva.addActionListener(evento -> { temporizadorRival.stop(); motor.iniciar(); pasar.setEnabled(true); });
        pasar.addActionListener(evento -> {
            motor.terminarTurno(); pasar.setEnabled(false); temporizadorRival.restart();
        });
        actualizar();
    }

    private void construirInterfaz() {
        JPanel raiz = new JPanel(new BorderLayout(16, 16));
        raiz.setBackground(FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        setContentPane(raiz);

        JPanel cabecera = new JPanel(new BorderLayout(10, 8));
        cabecera.setOpaque(false);
        JLabel titulo = new JLabel("DUELO DE ALQUIMISTAS");
        titulo.setForeground(new Color(247, 190, 91));
        titulo.setFont(new Font("SansSerif", Font.BOLD, 25));
        estado.setForeground(TEXTO);
        estado.setFont(new Font("SansSerif", Font.PLAIN, 15));
        JPanel acciones = new JPanel(); acciones.setOpaque(false);
        estilizarBoton(nueva, new Color(115, 83, 190));
        estilizarBoton(pasar, new Color(53, 146, 118));
        acciones.add(nueva); acciones.add(pasar);
        cabecera.add(titulo, BorderLayout.WEST);
        cabecera.add(estado, BorderLayout.CENTER);
        cabecera.add(acciones, BorderLayout.EAST);
        raiz.add(cabecera, BorderLayout.NORTH);

        JPanel mesa = new JPanel(new BorderLayout(12, 12)); mesa.setOpaque(false);
        JPanel contendientes = new JPanel(new GridLayout(2, 1, 0, 12)); contendientes.setOpaque(false);
        contendientes.add(caja("MAESTRO ÁMBAR · RIVAL", panelRival));
        contendientes.add(caja("TU PERSONAJE", panelJugador));
        JPanel lateral = new JPanel(new BorderLayout(8, 8));
        lateral.setBackground(TARJETA); lateral.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        lateral.setPreferredSize(new Dimension(330, 300));
        JLabel historialTitulo = new JLabel("HISTORIAL DE LA PARTIDA");
        historialTitulo.setForeground(new Color(247, 190, 91));
        historialTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        registro.setEditable(false); registro.setLineWrap(true); registro.setWrapStyleWord(true);
        registro.setBackground(TARJETA); registro.setForeground(TEXTO);
        registro.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lateral.add(historialTitulo, BorderLayout.NORTH);
        lateral.add(new JScrollPane(registro), BorderLayout.CENTER);
        mesa.add(contendientes, BorderLayout.CENTER);
        mesa.add(lateral, BorderLayout.EAST);
        raiz.add(mesa, BorderLayout.CENTER);

        JPanel zonaInferior = new JPanel(new BorderLayout(8, 8)); zonaInferior.setOpaque(false);
        pensamientoIA.setForeground(new Color(194, 180, 255));
        pensamientoIA.setFont(new Font("SansSerif", Font.ITALIC, 13));
        estadoMazos.setForeground(new Color(173, 184, 204));
        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 3)); textos.setOpaque(false);
        textos.add(pensamientoIA); textos.add(estadoMazos);
        mano.setOpaque(false);
        zonaInferior.add(textos, BorderLayout.NORTH);
        zonaInferior.add(new JScrollPane(mano), BorderLayout.CENTER);
        raiz.add(zonaInferior, BorderLayout.SOUTH);
    }

    private JPanel caja(String titulo, JLabel contenido) {
        JPanel panel = new JPanel(new BorderLayout(8, 12));
        panel.setBackground(TARJETA); panel.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        JLabel encabezado = new JLabel(titulo);
        encabezado.setForeground(new Color(159, 177, 208));
        encabezado.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(encabezado, BorderLayout.NORTH);
        contenido.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private static JLabel etiquetaGrande() {
        JLabel etiqueta = new JLabel(" "); etiqueta.setForeground(TEXTO);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 19)); return etiqueta;
    }

    private void actualizar() {
        if (motor.getJugador() == null) return;
        panelRival.setText("♥ " + motor.getRival().getVida() + " vida     ◈ " + motor.getRival().getEscudo()
                + " escudo     ✦ " + motor.getRival().getEnergia() + " energía");
        panelJugador.setText("♥ " + motor.getJugador().getVida() + " vida     ◈ " + motor.getJugador().getEscudo()
                + " escudo     ✦ " + motor.getJugador().getEnergia() + " energía");
        boolean tuTurno = !motor.isTerminada() && motor.getTurnoActual() == MotorPartida.Turno.JUGADOR;
        estado.setText(motor.isTerminada() ? motor.getResultado() : tuTurno ? "Tu turno · juega tus cartas" : "Turno del Maestro Ámbar…");
        pasar.setEnabled(tuTurno);
        pensamientoIA.setText("IA: " + motor.getExplicacionIA());
        estadoMazos.setText("Tu mazo " + motor.getJugador().getCartasEnMazo() + " · descarte "
                + motor.getJugador().getCartasEnDescarte() + "          Rival mazo "
                + motor.getRival().getCartasEnMazo() + " · descarte " + motor.getRival().getCartasEnDescarte());
        mano.removeAll();
        for (int i = 0; i < motor.getJugador().getCantidadCartas(); i++) {
            final int indice = i;
            Carta carta = motor.getJugador().getCarta(i);
            JButton boton = new JButton("<html><center><b>" + carta.getNombre() + "</b><br>" + carta.getDescripcion()
                    + "<br><br>Coste " + carta.getCoste() + " energía</center></html>");
            boton.setPreferredSize(new Dimension(150, 104));
            boton.setFocusPainted(false); boton.setForeground(TEXTO);
            boton.setBackground(colorCarta(carta.getTipo()));
            boton.setEnabled(tuTurno && carta.getCoste() <= motor.getJugador().getEnergia());
            boton.addActionListener(evento -> {
                motor.jugarCarta(indice);
                if (motor.isTerminada()) mostrarResultado();
            });
            mano.add(boton);
        }
        registro.setText("");
        for (int i = 0; i < motor.getHistorialSize(); i++) registro.append("• " + motor.getHistorial(i) + "\n");
        registro.setCaretPosition(registro.getDocument().getLength());
        mano.revalidate(); mano.repaint();
    }

    private Color colorCarta(TipoCarta tipo) {
        switch (tipo) {
            case ATAQUE: return new Color(139, 61, 67);
            case CURACION: return new Color(46, 119, 86);
            default: return new Color(53, 94, 151);
        }
    }

    private void estilizarBoton(JButton boton, Color color) {
        boton.setBackground(color); boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false); boton.setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    private void mostrarResultado() {
        if (motor.isTerminada()) JOptionPane.showMessageDialog(this, motor.getResultado(), "Fin del duelo", JOptionPane.INFORMATION_MESSAGE);
    }
}
