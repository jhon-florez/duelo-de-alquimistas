package duelo.interfaz;

import duelo.modelo.Carta;
import duelo.modelo.MotorPartida;
import duelo.modelo.TipoCarta;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/** Ventana principal y presentación visual de la partida. */
public final class VentanaJuego extends JFrame {
    private static final Color TEXTO = new Color(236, 239, 247);
    private final MotorPartida motor = new MotorPartida();
    private final JLabel estado = new JLabel("Pulsa Nueva partida para comenzar", SwingConstants.CENTER);
    private final JLabel panelJugador = etiquetaGrande();
    private final JLabel panelRival = etiquetaGrande();
    private final JLabel estadoMazos = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel pensamientoIA = new JLabel("Las decisiones del Maestro Ámbar se explicarán aquí.");
    private final JTextPane registro = new JTextPane();
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
        JPanel raiz = new FondoAlquimia(new BorderLayout(16, 16));
        raiz.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        setContentPane(raiz);

        JPanel cabecera = new JPanel(new BorderLayout(10, 8));
        cabecera.setOpaque(false);
        JPanel identidad = new JPanel(new GridLayout(2, 1, 0, 2));
        identidad.setOpaque(false);
        JLabel titulo = new JLabel("DUELO DE ALQUIMISTAS");
        titulo.setForeground(new Color(247, 190, 91));
        titulo.setFont(new Font("SansSerif", Font.BOLD, 23));
        JLabel subtitulo = new JLabel("Estrategia, cartas y alquimia");
        subtitulo.setForeground(new Color(204, 216, 229));
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        identidad.add(titulo);
        identidad.add(subtitulo);
        estado.setForeground(TEXTO);
        estado.setFont(new Font("SansSerif", Font.BOLD, 15));
        estado.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel acciones = new JPanel(); acciones.setOpaque(false);
        estilizarBoton(nueva, new Color(135, 95, 45));
        estilizarBoton(pasar, new Color(40, 130, 105));
        acciones.add(nueva); acciones.add(pasar);
        cabecera.add(identidad, BorderLayout.WEST);
        cabecera.add(estado, BorderLayout.CENTER);
        cabecera.add(acciones, BorderLayout.EAST);
        raiz.add(cabecera, BorderLayout.NORTH);

        JPanel mesa = new JPanel(new BorderLayout(12, 12)); mesa.setOpaque(false);
        JPanel contendientes = new JPanel(new GridLayout(1, 2, 12, 0)); contendientes.setOpaque(false);
        contendientes.add(caja("CONTRINCANTE · MAESTRO ÁMBAR", panelRival, new Color(247, 190, 91)));
        contendientes.add(caja("TÚ · TU PERSONAJE", panelJugador, new Color(99, 210, 190)));
        JPanel lateral = new PanelTarjeta(new BorderLayout(8, 8), new Color(15, 24, 40, 228));
        lateral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(247, 190, 91, 105), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        lateral.setPreferredSize(new Dimension(330, 300));
        JLabel historialTitulo = new JLabel("ÚLTIMAS JUGADAS");
        historialTitulo.setForeground(new Color(247, 190, 91));
        historialTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        registro.setEditable(false);
        registro.setForeground(TEXTO);
        registro.setFont(new Font("SansSerif", Font.PLAIN, 13));
        registro.setMargin(new Insets(10, 10, 10, 10));
        registro.setOpaque(false);
        lateral.add(historialTitulo, BorderLayout.NORTH);
        JScrollPane panelHistorial = new JScrollPane(registro);
        panelHistorial.setBorder(BorderFactory.createEmptyBorder());
        panelHistorial.setOpaque(false);
        panelHistorial.getViewport().setOpaque(false);
        lateral.add(panelHistorial, BorderLayout.CENTER);
        mesa.add(contendientes, BorderLayout.CENTER);
        mesa.add(lateral, BorderLayout.EAST);
        raiz.add(mesa, BorderLayout.CENTER);

        JPanel zonaInferior = new JPanel(new BorderLayout(8, 8)); zonaInferior.setOpaque(false);
        pensamientoIA.setForeground(new Color(194, 180, 255));
        pensamientoIA.setFont(new Font("SansSerif", Font.ITALIC, 13));
        estadoMazos.setForeground(new Color(173, 184, 204));
        JPanel textos = new PanelTarjeta(new GridLayout(3, 1, 0, 4), new Color(15, 24, 40, 218));
        textos.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        JLabel ayuda = new JLabel("CÓMO JUGAR: elige una carta → revisa su coste → pulsa Terminar turno");
        ayuda.setForeground(new Color(255, 226, 165));
        ayuda.setFont(new Font("SansSerif", Font.BOLD, 12));
        textos.add(ayuda);
        textos.add(pensamientoIA); textos.add(estadoMazos);
        mano.setOpaque(false);
        zonaInferior.add(textos, BorderLayout.NORTH);
        JScrollPane panelCartas = new JScrollPane(mano);
        panelCartas.setBorder(BorderFactory.createEmptyBorder());
        panelCartas.setOpaque(false);
        panelCartas.getViewport().setOpaque(false);
        zonaInferior.add(panelCartas, BorderLayout.CENTER);
        raiz.add(zonaInferior, BorderLayout.SOUTH);
    }

    private JPanel caja(String titulo, JLabel contenido, Color acento) {
        JPanel panel = new PanelTarjeta(new BorderLayout(8, 12), new Color(15, 24, 40, 226));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(acento.getRed(), acento.getGreen(), acento.getBlue(), 115), 1),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        JLabel encabezado = new JLabel(titulo);
        encabezado.setForeground(acento);
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
        panelRival.setText(resumenEstado(motor.getRival().getVida(), motor.getRival().getEscudo(), motor.getRival().getEnergia()));
        panelJugador.setText(resumenEstado(motor.getJugador().getVida(), motor.getJugador().getEscudo(), motor.getJugador().getEnergia()));
        boolean tuTurno = !motor.isTerminada() && motor.getTurnoActual() == MotorPartida.Turno.JUGADOR;
        estado.setText(motor.isTerminada() ? motor.getResultado() : tuTurno ? "Tu turno · juega tus cartas" : "Turno del Maestro Ámbar…");
        pasar.setEnabled(tuTurno);
        pensamientoIA.setText("Maestro Ámbar: " + motor.getExplicacionIA());
        estadoMazos.setText("Tus cartas: mazo " + motor.getJugador().getCartasEnMazo() + " · descarte "
                + motor.getJugador().getCartasEnDescarte() + "     |     Maestro Ámbar: mazo "
                + motor.getRival().getCartasEnMazo() + " · descarte " + motor.getRival().getCartasEnDescarte());
        mano.removeAll();
        for (int i = 0; i < motor.getJugador().getCantidadCartas(); i++) {
            final int indice = i;
            Carta carta = motor.getJugador().getCarta(i);
            String tipo = carta.getTipo().toString();
            JButton boton = new JButton("<html><div style='text-align:center;width:130px'>"
                    + "<span style='font-size:9px;color:#F5D28B'>" + tipo + "</span><br>"
                    + "<b style='font-size:14px'>" + carta.getNombre() + "</b><br>"
                    + "<span style='font-size:11px'>" + carta.getDescripcion() + "</span><br><br>"
                    + "<b>Cuesta " + carta.getCoste() + " energía</b></div></html>");
            boton.setPreferredSize(new Dimension(140, 124));
            boton.setFocusPainted(false); boton.setForeground(TEXTO);
            boton.setBackground(colorCarta(carta.getTipo()));
            boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 90), 1),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)));
            boton.setToolTipText("Pulsa para jugar " + carta.getNombre() + " (cuesta " + carta.getCoste() + " energía)");
            boton.setEnabled(tuTurno && carta.getCoste() <= motor.getJugador().getEnergia());
            boton.addActionListener(evento -> {
                motor.jugarCarta(indice);
                if (motor.isTerminada()) mostrarResultado();
            });
            mano.add(boton);
        }
        mostrarHistorial();
        mano.revalidate(); mano.repaint();
    }

    private void mostrarHistorial() {
        StyledDocument documento = registro.getStyledDocument();
        try {
            documento.remove(0, documento.getLength());
            for (int i = 0; i < motor.getHistorialSize(); i++) {
                String entrada = motor.getHistorial(i);
                int separador = entrada.indexOf(" | ");
                String emisor = separador < 0 ? "SISTEMA" : entrada.substring(0, separador);
                String accion = separador < 0 ? entrada : entrada.substring(separador + 3);

                SimpleAttributeSet estiloEmisor = new SimpleAttributeSet();
                StyleConstants.setFontFamily(estiloEmisor, "SansSerif");
                StyleConstants.setFontSize(estiloEmisor, 11);
                StyleConstants.setBold(estiloEmisor, true);
                StyleConstants.setForeground(estiloEmisor, colorEmisor(emisor));

                SimpleAttributeSet estiloAccion = new SimpleAttributeSet();
                StyleConstants.setFontFamily(estiloAccion, "SansSerif");
                StyleConstants.setFontSize(estiloAccion, 13);
                StyleConstants.setForeground(estiloAccion, TEXTO);

                documento.insertString(documento.getLength(), emisor.toUpperCase() + "\n", estiloEmisor);
                int inicioAccion = documento.getLength();
                documento.insertString(inicioAccion, accion + "\n\n", estiloAccion);
                SimpleAttributeSet espacio = new SimpleAttributeSet();
                StyleConstants.setLeftIndent(espacio, 8);
                StyleConstants.setSpaceBelow(espacio, 8);
                documento.setParagraphAttributes(inicioAccion, accion.length() + 1, espacio, false);
            }
            registro.setCaretPosition(documento.getLength());
        } catch (BadLocationException excepcion) {
            throw new IllegalStateException("No se pudo actualizar el historial.", excepcion);
        }
    }

    private Color colorEmisor(String emisor) {
        if ("Maestro Ámbar".equals(emisor)) return new Color(247, 190, 91);
        if ("Tú".equals(emisor)) return new Color(99, 210, 190);
        return new Color(159, 177, 208);
    }

    private String resumenEstado(int vida, int escudo, int energia) {
        return "<html><span style='font-size:15px;color:#FF8787'>♥ Vida: " + vida + "</span>"
                + "&nbsp;<span style='font-size:15px;color:#C5D6FF'>◈ Escudo: " + escudo + "</span>"
                + "&nbsp;<span style='font-size:15px;color:#FFD166'>✦ Energía: " + energia + "</span></html>";
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

    // Atenúa la ilustración para que las cartas y los indicadores se lean con facilidad.
    private static final class FondoAlquimia extends JPanel {
        private final BufferedImage imagen = cargarFondo();

        private FondoAlquimia(java.awt.LayoutManager diseño) { super(diseño); }

        @Override
        protected void paintComponent(Graphics grafico) {
            super.paintComponent(grafico);
            Graphics2D lienzo = (Graphics2D) grafico.create();
            lienzo.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            if (imagen != null) lienzo.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            lienzo.setColor(new Color(5, 12, 24, 90));
            lienzo.fillRect(0, 0, getWidth(), getHeight());
            lienzo.dispose();
        }

        private static BufferedImage cargarFondo() {
            try (java.io.InputStream archivo = VentanaJuego.class.getResourceAsStream("/fondo-alquimia.jpg")) {
                return archivo == null ? null : ImageIO.read(archivo);
            } catch (IOException excepcion) {
                return null;
            }
        }
    }

    private static final class PanelTarjeta extends JPanel {
        private final Color color;

        private PanelTarjeta(java.awt.LayoutManager diseño, Color color) {
            super(diseño);
            this.color = color;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics grafico) {
            Graphics2D lienzo = (Graphics2D) grafico.create();
            lienzo.setColor(color);
            lienzo.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            lienzo.dispose();
            super.paintComponent(grafico);
        }
    }
}
