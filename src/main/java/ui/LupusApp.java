package ui;

import engine.Scorecard;
import domain.ANAresult;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class LupusApp extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    private final PanelInitial panelInitial;
    private final PanelEvaluation panelEvaluation;

    private final PanelEvaluation2 panelEvaluation2;
    private final PanelResults panelResults;
    private final PanelTreatment panelTreatment;

    private final Image backgroundImage;

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
        setTitle("Clinical Decision Support System for Lupus Diagnosis");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen

        URL imageUrl = getClass().getResource("/images/medical_background.jpg");
        if (imageUrl != null) {
            backgroundImage = new ImageIcon(imageUrl).getImage();
        } else {
            System.err.println("⚠️ Warning: medical_background.jpg not found in resources!");
            backgroundImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // ---------- Fixed title ----------
        JLabel titleLabel = new JLabel("Clinical Decision Support System for Lupus Diagnosis", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(140, 0, 40, 0));
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

        // ---------- Central container ----------
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        // ---------- System panels ----------
        panelInitial = new PanelInitial(this);
        panelEvaluation = new PanelEvaluation(this);
        panelEvaluation2 = new PanelEvaluation2(this);
        panelResults = new PanelResults(this);
        panelTreatment = new PanelTreatment(this);

        mainPanel.add(panelInitial, "INITIAL");
        mainPanel.add(panelEvaluation, "EVALUATION");
        mainPanel.add(panelEvaluation2, "EVALUATION2");
        mainPanel.add(panelResults, "RESULTS");
        mainPanel.add(panelTreatment, "TREATMENT");


        showScreen("INITIAL");
    }

    public void showScreen(String name) {
        cardLayout.show(mainPanel, name);
    }

    public PanelResults getPanelResults() {
        return panelResults;
    }
    public PanelTreatment getPanelTreatment() { return panelTreatment; }

    public void resetAll() {
        // Clean scorecard and ANA result
        scorecard.reset();
        anaResult = null;

        // Clean symptom fields
        panelEvaluation.resetAll();
        panelEvaluation2.resetAll();
        panelResults.resetAll();
        panelTreatment.mostrarTreatment(null);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LupusApp().setVisible(true));
    }
}
