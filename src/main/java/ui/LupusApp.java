package ui;

import engine.Scorecard;
import domain.ANAresult;

import javax.swing.*;
import java.awt.*;

public class LupusApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private PanelInicio panelInicio;
    private PanelEvaluacion panelEvaluacion;

    private PanelEvaluacion2 panelEvaluacion2;
    private PanelResultados panelResultados;
    private PanelTreatment panelTreatment;

    private Image backgroundImage;

    private final Scorecard scorecard = new Scorecard();
    private ANAresult anaResult;

    public void setAnaResult(ANAresult result) {
        this.anaResult = result;
    }

    public ANAresult getAnaResult() {
        return this.anaResult;
    }

    public Scorecard getScorecard() { return scorecard; }

    public LupusApp() {
        setTitle("Expert System for Lupus Diagnosis");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // pantalla completa

        backgroundImage = new ImageIcon(getClass().getResource("/images/medical_background.jpg")).getImage();

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // ---------- Titulo fijo ----------
        JLabel titleLabel = new JLabel("Expert System for Lupus Diagnosis", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(140, 0, 40, 0));
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

        // ---------- Contenedor central ----------
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        // ---------- Paneles del sistema ----------
        panelInicio = new PanelInicio(this);
        panelEvaluacion = new PanelEvaluacion(this);
        panelEvaluacion2 = new PanelEvaluacion2(this);
        panelResultados = new PanelResultados(this);
        panelTreatment = new PanelTreatment(this);

        mainPanel.add(panelInicio, "INICIO");
        mainPanel.add(panelEvaluacion, "EVALUACION");
        mainPanel.add(panelEvaluacion2, "EVALUACION2");
        mainPanel.add(panelResultados, "RESULTADOS");
        mainPanel.add(panelTreatment, "TRATAMIENTO");


        showScreen("INICIO");
    }

    public void showScreen(String name) {
        cardLayout.show(mainPanel, name);
    }

    public PanelResultados getPanelResultados() {
        return panelResultados;
    }
    public PanelTreatment getPanelTreatment() { return panelTreatment; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LupusApp().setVisible(true));
    }
}
