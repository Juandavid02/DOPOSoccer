package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Tarjeta seleccionable que muestra un título, una descripción y,
 * opcionalmente, una imagen. La usa la pantalla de selección del perfil de
 * la IA.
 *
 * @param <T> valor que representa la tarjeta
 */
public class TarjetaOpcion<T> extends JToggleButton {

    private final T valor;

    /**
     * Crea una tarjeta con una imagen ya cargada.
     *
     * @param valor valor que representa la tarjeta
     * @param titulo título de la tarjeta
     * @param descripcion texto corto de la tarjeta
     * @param imagen imagen superior o null si no tiene
     */
    public TarjetaOpcion(T valor, String titulo, String descripcion,
                         BufferedImage imagen) {
        this.valor = valor;

        if (imagen != null) {
            Image escalada = imagen.getScaledInstance(
                    120, 85, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(escalada));
        }
        setText("<html><div style='text-align:center; width:190px'>"
                + "<b>" + titulo + "</b><br>" + descripcion + "</div></html>");
        setFont(new Font("SansSerif", Font.PLAIN, 12));
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setOpaque(true);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setRolloverEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(260, imagen == null ? 150 : 200));
        addItemListener(e -> actualizarEstilo());
        actualizarEstilo();
    }

    /**
     * Devuelve el valor que representa la tarjeta.
     *
     * @return valor de la tarjeta
     */
    public T getValor() {
        return valor;
    }

    /**
     * Aplica los colores según si la tarjeta está seleccionada.
     */
    private void actualizarEstilo() {
        if (isSelected()) {
            setBackground(new Color(72, 48, 138));
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.WHITE, 3, true));
        } else {
            setBackground(new Color(228, 240, 248));
            setForeground(new Color(22, 48, 68));
            setBorder(BorderFactory.createLineBorder(
                    new Color(120, 160, 185), 2, true));
        }
    }

    /**
     * Dibuja el fondo con el color del estado para que el tema del sistema
     * no lo reemplace.
     *
     * @param g contexto gráfico del componente
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        g2.dispose();
        super.paintComponent(g);
    }
}
