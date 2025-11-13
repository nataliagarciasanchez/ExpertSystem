package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import engine.Scorecard;

public class PanelTreatment extends JPanel {
    private JTextArea treatmentArea;
    private JButton volverInicioBtn;
    private LupusApp app;

    public PanelTreatment(LupusApp app) {
        this.app = app;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel frame = new JPanel();
        frame.setBackground(new Color(255, 255, 255, 230));
        frame.setPreferredSize(new Dimension(900, 500));
        frame.setLayout(new BorderLayout(20, 30));
        frame.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel("Treatment Recommendations", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(new Color(30, 60, 120));
        frame.add(title, BorderLayout.NORTH);

        treatmentArea = new JTextArea();
        treatmentArea.setEditable(false);
        treatmentArea.setOpaque(false);
        treatmentArea.setFont(new Font("SansSerif", Font.PLAIN, 17));
        treatmentArea.setForeground(new Color(20, 40, 80));
        treatmentArea.setLineWrap(true);
        treatmentArea.setWrapStyleWord(true);
        treatmentArea.setMargin(new Insets(20, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(treatmentArea);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(800, 300));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        frame.add(scroll, BorderLayout.CENTER);

        volverInicioBtn = new JButton("← Back to Home");
        volverInicioBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        volverInicioBtn.setBackground(Color.WHITE);
        volverInicioBtn.addActionListener(e -> {
            app.resetAll();// clean Scorecard + symptoms
            app.showScreen("INITIAL");
        });

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.add(volverInicioBtn);
        frame.add(bottom, BorderLayout.SOUTH);

        add(frame);
    }

    public void mostrarTreatment(List<String> recomendaciones) {
        if (recomendaciones == null || recomendaciones.isEmpty()) {
            treatmentArea.setText("No treatment recommendations have been generated.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Treatment recommendations for this patient:\n\n");
        for (String rec : recomendaciones) {
            sb.append("• ").append(rec).append("\n\n");
        }
        treatmentArea.setText(sb.toString());
        treatmentArea.setCaretPosition(0); // vuelve al inicio del texto
    }
}
