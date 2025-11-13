package ui;

import engine.Scorecard;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class PanelResults extends JPanel {

    private JLabel resultadoLabel;
    private JLabel puntosLabel;
    private JLabel interpretacionLabel;
    private JTextArea desgloseArea;
    private JButton verTratamientoBtn;


    public PanelResults(LupusApp app) {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));


        // --- Panel Central ---
        JPanel frame = new JPanel(new BorderLayout(30, 25));
        frame.setBackground(new Color(255, 255, 255, 220));
        frame.setPreferredSize(new Dimension(1550, 1050));
        frame.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // --- Titulo ---
        JLabel title = new JLabel("Evaluation Results", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(new Color(30, 60, 120));
        frame.add(title, BorderLayout.NORTH);

        // --- Panel central ---
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        resultadoLabel = new JLabel("", SwingConstants.CENTER);
        resultadoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        resultadoLabel.setForeground(new Color(20, 30, 70));
        resultadoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        puntosLabel = new JLabel("", SwingConstants.CENTER);
        puntosLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        puntosLabel.setForeground(new Color(0, 80, 160));
        puntosLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        interpretacionLabel = new JLabel("", SwingConstants.CENTER);
        interpretacionLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        interpretacionLabel.setForeground(new Color(90, 90, 90));
        interpretacionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(resultadoLabel);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(puntosLabel);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(interpretacionLabel);

        // --- DESGLOSE DE DOMINIOS ---
        desgloseArea = new JTextArea();
        desgloseArea.setEditable(false);
        desgloseArea.setOpaque(false);
        desgloseArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        desgloseArea.setForeground(new Color(50, 50, 50));
        desgloseArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        desgloseArea.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 0));
        desgloseArea.setLineWrap(true);
        desgloseArea.setWrapStyleWord(true);


        JScrollPane scroll = new JScrollPane(desgloseArea);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(1200, 650));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(scroll);

        frame.add(centerPanel, BorderLayout.CENTER);

        // --- Boton inferior ---
        verTratamientoBtn = new JButton("View recommended treatment");
        verTratamientoBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        verTratamientoBtn.setBackground(Color.WHITE);
        verTratamientoBtn.addActionListener(e -> {
            app.getPanelTreatment().mostrarTreatment(app.getScorecard().getTreatments());
            app.showScreen("TREATMENT");
        });

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.add(verTratamientoBtn);
        frame.add(bottom, BorderLayout.SOUTH);

        add(frame);
    }

    // --- Mostrar diagnostico completo ---
    public void mostrarDiagnostico(String texto, int puntos, Scorecard score) {

        resultadoLabel.setText(texto);
        puntosLabel.setText("Total score: " + puntos + " points");

        // Color segun el texto
        String textoLower = texto.toLowerCase();
        if (textoLower.contains("confirmed")) {
            resultadoLabel.setForeground(new Color(180, 0, 0));
        } else if (textoLower.contains("not eligible") || textoLower.contains("does not meet")) {
            resultadoLabel.setForeground(new Color(0, 120, 0));
        } else {
            resultadoLabel.setForeground(new Color(20, 30, 70));
        }

        // Si NO cumple criterio ANA
        if (score == null || !score.isEntryEligible()) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append("CLINICAL INTERPRETATION:\n");
            sb.append("------------------------\n");
            sb.append("The patient does NOT meet the mandatory entry criterion (ANA positive ≥1:80).\n");
            sb.append("EULAR/ACR 2019 score and domain breakdown are not applicable.");
            desgloseArea.setText(sb.toString());
            return;
        }

        // Si SÍ cumple ANA
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("DETAILED SCORE BREAKDOWN:\n");
        sb.append("------------------------\n");
        for (Map.Entry<?, Integer> e : score.getPointsMap().entrySet()) {
            sb.append(String.format("• %-20s → %2d puntos%n", e.getKey(), e.getValue()));
        }

        if (!score.getMetCriteria().isEmpty()) {
            sb.append("\n");
            sb.append("FULFILLED CRITERIA\n");
            sb.append("------------------------\n");
            for (String linea : score.getMetCriteria()) {
                sb.append("   - ").append(linea).append("\n");
            }
        }

        sb.append("\n");
        sb.append("CLINICAL INTERPRETATION:\n");
        sb.append("------------------------\n");
        if (puntos >= 10) {
            sb.append("The patient meets the entry criterion (ANA positive ≥1:80) and exceeds the required 10 points.\n");
        } else {
            sb.append("The patient meets the entry criterion (ANA positive ≥1:80) but does not reach 10 points.\n");
        }

        if (score != null && !score.getDifferentials().isEmpty()) {
            sb.append("\n");
            sb.append("SUGGESTED DIFFERENTIAL DIAGNOSIS:\n");
            sb.append("------------------------\n");
            for (String d : score.getDifferentials()) {
                sb.append("• ").append(d).append("\n");
            }
        }

        desgloseArea.setText(sb.toString());
    }

    public void resetAll() {
        resultadoLabel.setText("");
        puntosLabel.setText("");
        interpretacionLabel.setText("");
        desgloseArea.setText("");
    }


}
