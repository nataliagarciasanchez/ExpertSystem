package ui;

import javax.swing.*;
import java.awt.*;

import domain.ANAresult;
import engine.Scorecard;

public class PanelEvaluation extends JPanel {
    private JTextField tfAna;
    private JCheckBox cbAnaPositive;
    public PanelEvaluation(LupusApp app) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        // ---------- Panel central gris translucido ----------
        JPanel frame = new JPanel();
        frame.setBackground(new Color(255, 255, 255, 180));
        frame.setPreferredSize(new Dimension(1100, 800));
        frame.setLayout(new BorderLayout(30, 25));
        frame.setBorder(BorderFactory.createEmptyBorder(40, 100, 20, 100));

        // ---------- Titulo principal ----------
        JLabel subtitle = new JLabel("Section 1 · ANA Evaluation", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 28)); // un poco más pequeño que el título principal
        subtitle.setForeground(new Color(30, 60, 120)); // mismo color que "Evaluation Results"
        subtitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0)); // margen arriba y abajo
        frame.add(subtitle, BorderLayout.NORTH);


        // ---------- Panel principal  ----------
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // ---------- Explicacion ----------
        JLabel anaExplanation = new JLabel("<html><div style='width:880px; text-align:justify;'>"
                + "This section verifies whether the patient meets the mandatory entry criterion "
                + "according to the <b>EULAR/ACR 2019</b> classification criteria.<br><br>"
                + "The <b>ANA (Antinuclear Antibody)</b> test must be positive at a titer equal to or higher than <b>1:80</b> "
                + "measured by indirect immunofluorescence on HEp-2 cells. "
                + "A negative result excludes the diagnosis of <b>Systemic Lupus Erythematosus</b>.<br><br>"
                + "Please enter the ANA titer (for example <b>80</b>, <b>160</b>, <b>320</b>...) "
                + "and check the box if the result was positive."
                + "</div></html>");
        anaExplanation.setFont(new Font("SansSerif", Font.PLAIN, 18));
        anaExplanation.setAlignmentX(Component.CENTER_ALIGNMENT);
        anaExplanation.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(anaExplanation);

        // ---------- Campos de entrada  ----------
        JPanel anaInputs = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        anaInputs.setOpaque(false);

        JLabel lblAna = new JLabel("ANA titer (denominator):");
        lblAna.setFont(new Font("SansSerif", Font.PLAIN, 19));

        tfAna = new JTextField("");
        tfAna.setFont(new Font("SansSerif", Font.PLAIN, 19));
        tfAna.setPreferredSize(new Dimension(150, 42));
        tfAna.setBackground(Color.WHITE);
        tfAna.setForeground(Color.BLACK);

        cbAnaPositive = new JCheckBox("ANA positive");
        cbAnaPositive.setFont(new Font("SansSerif", Font.PLAIN, 19));
        cbAnaPositive.setOpaque(false);

        anaInputs.add(lblAna);
        anaInputs.add(tfAna);
        anaInputs.add(cbAnaPositive);
        mainPanel.add(anaInputs);

        frame.add(mainPanel, BorderLayout.CENTER);

        // ---------- Boton inferior ----------
        JButton evaluarBtn = new JButton("Continue → Section 2");
        evaluarBtn.setFont(new Font("SansSerif", Font.BOLD, 21));
        evaluarBtn.setForeground(Color.BLACK);
        evaluarBtn.setBackground(Color.WHITE);
        evaluarBtn.setPreferredSize(new Dimension(420, 35));
        evaluarBtn.addActionListener(e -> {
            try {
                boolean isPositive = this.cbAnaPositive.isSelected();
                int denominator = Integer.parseInt(this.tfAna.getText().trim());

                // Hechos de dominio
                ANAresult ana = new ANAresult(isPositive, denominator);
                Scorecard score = app.getScorecard();
                app.setAnaResult(ana);

                var ks = org.kie.api.KieServices.Factory.get();
                var kc = ks.getKieClasspathContainer();
                var ksession = kc.newKieSession("classificationKieSession");


                ksession.setGlobal("score", score);
                ksession.insert(ana);
                ksession.fireAllRules();
                ksession.dispose();

                if (score.isEntryEligible()) {
                    app.showScreen("EVALUATION2");
                } else {
                    app.getPanelResults().mostrarDiagnostico(
                            "Not eligible (ANA negative or <1:80)",
                            0,
                            score
                    );
                    app.showScreen("RESULTS");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this, "Please enter a valid number for the ANA titer.", "Invalid input",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this, "Error running rules: " + ex.getMessage(), "Drools error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(evaluarBtn);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(40, 0, 160, 0);
        wrapper.add(frame, gbc);

        add(wrapper);

    }
    public void resetAll() {
        this.tfAna.setText("");
        this.cbAnaPositive.setSelected(false);
    }
}
