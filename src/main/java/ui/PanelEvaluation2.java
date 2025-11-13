package ui;

import domain.ANAresult;
import domain.Symptom;
import domain.SymptomType;
import engine.Scorecard;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import java.util.function.BiConsumer;


import javax.swing.*;
import java.awt.*;
import java.util.*;

public class PanelEvaluation2 extends JPanel {

    private final Map<JCheckBox, SymptomType> symptomChecks = new LinkedHashMap<>(); // boolean symptoms
    private final Map<JTextField, SymptomType> symptomFields = new LinkedHashMap<>(); // numeric symptoms


    public PanelEvaluation2(LupusApp app) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        // ---------- Panel Central Translucido ----------
        JPanel frame = new JPanel();
        frame.setBackground(new Color(255, 255, 255, 180));
        frame.setBorder(BorderFactory.createEmptyBorder(40, 80, 20, 80));
        frame.setLayout(new BorderLayout(30, 25));
        frame.setPreferredSize(new Dimension(950, 850));

        // ---------- Titulo ----------
        JLabel subtitle = new JLabel("Section 2 · Clinical Evaluation", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        subtitle.setForeground(new Color(30, 60, 120));
        subtitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        frame.add(subtitle, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // ----------Texto de instrucciones ----------
        JLabel info = new JLabel("<html><div style='width:1150; text-align:justify;'>"
                + "Select the observed <b>clinical symptoms</b> and <b>immunological findings</b> in the patient, "
                + "according to the <b>EULAR/ACR 2019</b> classification criteria for the diagnosis of "
                + "<b>Systemic Lupus Erythematosus</b>."
                + "</div></html>");
        info.setFont(new Font("SansSerif", Font.PLAIN, 18));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        info.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        mainPanel.add(info);

        // ---------- Panel de síntomas ----------
        JPanel gridPanel = new JPanel();
        gridPanel.setOpaque(false);
        gridPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbcCat = new GridBagConstraints();
        gbcCat.gridx = 0;
        gbcCat.gridy = 0;
        gbcCat.anchor = GridBagConstraints.NORTHWEST;
        gbcCat.fill = GridBagConstraints.HORIZONTAL;
        gbcCat.weightx = 0.5;
        gbcCat.insets = new Insets(10, 15, 10, 15);

// función auxiliar para añadir par de categorías (izq + der) sin forzar alturas
        BiConsumer<JPanel, JPanel> addCategoryPair = (left, right) -> {
            gbcCat.gridx = 0;
            gridPanel.add(left, gbcCat);
            gbcCat.gridx = 1;
            gridPanel.add(right, gbcCat);
            gbcCat.gridy++;
        };

// -------------- Filas de categorías ---------------
        addCategoryPair.accept(
                createCatPanelMixed("General manifestations", new Object[][]{
                        {"Fever (°C)", SymptomType.FEVER, "num", "Use dot for decimals (e.g. 38.5)"},
                }),
                createCatPanelBoolean("Mucocutaneous", new Object[][]{
                        {"Non-scarring alopecia", SymptomType.ALOPECIA},
                        {"Oral or nasal ulcers", SymptomType.ORAL_ULCERS},
                        {"Subacute/discoid cutaneous lupus", SymptomType.SUBACUTE_DISCOID_LESION},
                        {"Acute cutaneous lupus (malar or butterfly rash)", SymptomType.ACUTE_RASH}
                })
        );

        addCategoryPair.accept(
                createCatPanelBoolean("Musculoskeletal", new Object[][]{
                        {"Arthritis (≥2 joints with pain or stiffness)", SymptomType.ARTHRITIS}
                }),
                createCatPanelBoolean("Serosal", new Object[][]{
                        {"Pleural or pericardial effusion", SymptomType.SEROUS_EFFUSION},
                        {"Acute pericarditis", SymptomType.ACUTE_PERICARDITIS}
                })
        );

        addCategoryPair.accept(
                createCatPanelMixed("Renal", new Object[][]{
                        {"Proteinuria (g/24h)", SymptomType.PROTEINURIA, "num", "Enter numeric value (e.g. 0.5)"},
                        {"Class III / IV nephritis (biopsy)", SymptomType.NEPHRITIS_CLASS_III_IV, "bool"},
                        {"Class V nephritis (biopsy)", SymptomType.NEPHRITIS_CLASS_V, "bool"}
                }),
                createCatPanelMixed("Hematologic", new Object[][]{
                        {"Leukopenia (cells/mm³)", SymptomType.LEUKOPENIA, "num", "Enter numeric value (e.g. 4000)"},
                        {"Thrombocytopenia (cells/mm³)", SymptomType.THROMBOCYTOPENIA, "num", "Enter numeric value (e.g. 90000)"},
                        {"Autoimmune hemolysis", SymptomType.AUTOIMMUNE_HEMOLYSIS, "bool"}
                })
        );

        addCategoryPair.accept(
                createCatPanelBoolean("Neuropsychiatric", new Object[][]{
                        {"Delirium attributable to SLE", SymptomType.DELIRIUM},
                        {"Psychosis attributable to SLE", SymptomType.PSYCHOSIS},
                        {"Seizures without other cause", SymptomType.SEIZURES}
                }),
                createCatPanelBoolean("Immunologic", new Object[][]{
                        {"Anti-dsDNA or Anti-Sm positive", SymptomType.ANTI_DSDNA_OR_ANTI_SM},
                        {"Antiphospholipid antibodies (aPL) positive", SymptomType.APL},
                        {"Low complement (C3 or C4)", SymptomType.LOW_C3_OR_C4},
                        {"Markedly low complement (C3 and C4)", SymptomType.LOW_C3_AND_C4},
                        {"Documented thrombosis (arterial or venous)", SymptomType.THROMBOSIS}
                })
        );

        addCategoryPair.accept(
                createCatPanelBoolean("Additional symptoms (not in EULAR/ACR)", new Object[][]{
                        {"Raynaud’s phenomenon", SymptomType.RAYNAUD},
                        {"Skin thickening or sclerodactyly", SymptomType.SKIN_THICKENING},
                        {"Proximal muscle weakness", SymptomType.MUSCLE_WEAKNESS},
                        {"Elevated CK or myositis", SymptomType.ELEVATED_CK},
                        {"Persistent fatigue or malaise", SymptomType.FATIGUE}
                }),
                new JPanel() // vacío a la derecha
        );


        JScrollPane scrollPane = new JScrollPane(gridPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(950, 480));
        scrollPane.setMinimumSize(new Dimension(950, 200));

        mainPanel.add(scrollPane);
        frame.add(mainPanel, BorderLayout.CENTER);

        // ---------- Boton inferior ----------
        JButton continuarBtn = new JButton("Continue → Results");
        continuarBtn.setFont(new Font("SansSerif", Font.BOLD, 21));
        continuarBtn.setForeground(Color.BLACK);
        continuarBtn.setBackground(Color.WHITE);
        continuarBtn.setPreferredSize(new Dimension(420, 35));
        continuarBtn.addActionListener(e -> correrReglasYMostrar(app));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(continuarBtn);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(40, 0, 160, 0);
        wrapper.add(frame, gbc);

        add(wrapper);
    }private JPanel createCatPanelMixed(String title, Object[][] items) {
        JPanel panel = baseCatPanel(title);

        for (Object[] it : items) {
            String text = (String) it[0];
            SymptomType type = (SymptomType) it[1];
            String mode = (String) it[2];
            if ("num".equals(mode))
                addNumericRow(panel, text, type, (String) it[3]);
            else
                addCheckboxRow(panel, text, type);
        }

        return panel;
    }

    private JPanel createCatPanelBoolean(String title, Object[][] items) {
        JPanel panel = baseCatPanel(title);
        for (Object[] it : items)
            addCheckboxRow(panel, (String) it[0], (SymptomType) it[1]);
        return panel;
    }

    private JPanel baseCatPanel(String title) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setForeground(new Color(30, 60, 120));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        p.add(lbl);
        return p;
    }

    private void addNumericRow(JPanel parent, String label, SymptomType type, String unitsHint) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 2));
        row.setOpaque(false);

        JLabel lbl = new JLabel("•    " + label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lbl.setPreferredSize(new Dimension(260, 24));

        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(70, 24));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setToolTipText(unitsHint);

        row.add(lbl);
        row.add(field);
        parent.add(row);
        parent.add(Box.createRigidArea(new Dimension(0, 3)));

        symptomFields.put(field, type);
    }

    private void addCheckboxRow(JPanel parent, String text, SymptomType type) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 2));
        row.setOpaque(false);

        JLabel bullet = new JLabel("•");
        bullet.setFont(new Font("SansSerif", Font.PLAIN, 16));
        bullet.setForeground(Color.BLACK);
        bullet.setPreferredSize(new Dimension(10, 24));

        JCheckBox cb = new JCheckBox(text);
        cb.setOpaque(false);
        cb.setFont(new Font("SansSerif", Font.PLAIN, 16));

        row.add(bullet);
        row.add(cb);
        parent.add(row);
        parent.add(Box.createRigidArea(new Dimension(0, 3)));
        symptomChecks.put(cb, type);
    }



    private void correrReglasYMostrar(LupusApp app) {
        // Construir los facts Symptom presentes
        ArrayList<Symptom> facts = new ArrayList<>();

        // Booleans
        for (Map.Entry<JCheckBox, SymptomType> e : symptomChecks.entrySet()) {
            if (e.getKey().isSelected()) {
                facts.add(new Symptom(e.getValue(), true));
            }
        }

        // Numerics
        for (Map.Entry<JTextField, SymptomType> e : symptomFields.entrySet()) {
            String text = e.getKey().getText().trim();
            if (!text.isEmpty()) {
                try {
                    // Reemplazar coma por punto
                    text = text.replace(",", ".");
                    double value = Double.parseDouble(text);

                    if (value < 0) {
                        JOptionPane.showMessageDialog(this,
                                "Invalid value for " + e.getValue().name() + ": must be positive.",
                                "Invalid input", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Symptom s = new Symptom(e.getValue(), true);
                    s.setNumericValue(value);
                    facts.add(s);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid numeric value for " + e.getValue().name()
                                    + ". Please use digits and '.' or ',' as decimal separator.",
                            "Input error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

            }
        }


        // Validacion minima
        if (facts.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select or enter at least one symptom.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }


        try {
            Scorecard score = app.getScorecard(); // Reutilizamos el Scorecard global
            KieServices ks = KieServices.Factory.get();
            KieContainer kc = ks.getKieClasspathContainer();

            // --- 1. Sesion principal: CLASIFICACION EULAR/ACR ---
            KieSession ksession = kc.newKieSession("classificationKieSession");
            ksession.setGlobal("score", score);

            // Insertar ANA result (obtenido en el panel 1)
            ANAresult ana = app.getAnaResult();
            if (ana != null) ksession.insert(ana);

            for (Symptom s : facts) ksession.insert(s);

            ksession.fireAllRules();
            ksession.dispose();

            int total = score.total();
            boolean elegible = score.isEntryEligible();

            // --- 2. Sesion diferencial: SI NO ES LES O POCOS PUNTOS ---
            KieSession ksessionDiff = kc.newKieSession("differentialKieSession");
            ksessionDiff.setGlobal("score", score);

            // Insertar ANA result (obtenido en el panel 1)
            if (ana != null) ksessionDiff.insert(ana);
            for (Symptom s : facts) ksessionDiff.insert(s);

            ksessionDiff.fireAllRules();
            ksessionDiff.dispose();

            // --- 3. Sesion secundaria: TRATAMIENTO ---
            KieSession ksessionTreatment = kc.newKieSession("treatmentKieSession");
            ksessionTreatment.setGlobal("score", score);

            // Insertar ANA result (obtenido en el panel 1)
            if (ana != null) ksessionTreatment.insert(ana);
            for (Symptom s : facts) ksessionTreatment.insert(s);

            ksessionTreatment.fireAllRules();
            ksessionTreatment.dispose();

            // --- 4. Decision final y mostrado de resultados ---
            if (!elegible) {
                // ANA negativo o <1:80
                if (!score.getDifferentials().isEmpty()) {
                    app.getPanelResults().mostrarDiagnostico(
                            "NOT eligible for LUPUS (ANA negative or <1:80).",
                            total,
                            score
                    );
                } else {
                    app.getPanelResults().mostrarDiagnostico(
                            "NOT eligible for LUPUS and no relevant differential diagnosis.",
                            total,
                            score
                    );
                }
            } else if (total >= 10) {
                app.getPanelResults().mostrarDiagnostico(
                        "LUPUS confirmed by EULAR/ACR 2019 criteria",
                        total,
                        score
                );
            } else {
                if (!score.getDifferentials().isEmpty()) {
                    app.getPanelResults().mostrarDiagnostico(
                            "Does NOT meet LUPUS criteria. Possible differential diagnoses:",
                            total,
                            score
                    );
                } else {
                    app.getPanelResults().mostrarDiagnostico(
                            "Does NOT meet LUPUS criteria or any clear alternative diagnosis.",
                            total,
                            score
                    );
                }
            }

            // --- 5. MOSTRAR PANTALLA DE RESULTADOS ---
            app.showScreen("RESULTS");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error executing rules: " + ex.getMessage(),
                    "Drools error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void resetAll() {
        // Clear checkboxes
        for (JCheckBox cb : symptomChecks.keySet()) {
            cb.setSelected(false);
        }
        // Clean numeric fields
        for (JTextField field : symptomFields.keySet()) {
            field.setText("");
        }
    }



}
