package ui;

import domain.ANAresult;
import domain.Symptom;
import domain.SymptomType;
import engine.Scorecard;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class PanelEvaluacion2 extends JPanel {

    private final Map<JCheckBox, SymptomType> symptomChecks = new LinkedHashMap<>();


    public PanelEvaluacion2(LupusApp app) {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));

        // ---------- Panel Central Translucido ----------
        JPanel frame = new JPanel(new BorderLayout(30, 25));
        frame.setBackground(new Color(255, 255, 255, 220));
        frame.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        frame.setPreferredSize(new Dimension(1300, 850));

        // ---------- Titulo ----------
        JLabel subtitle = new JLabel("Section 2 · Clinical Evaluation", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        frame.add(subtitle, BorderLayout.NORTH);

        // ----------Texto de instrucciones ----------
        JLabel info = new JLabel("<html><div style='width:1150px; text-align:justify;'>"
                + "Select the observed <b>clinical symptoms</b> and <b>immunological findings</b> in the patient, "
                + "according to the <b>EULAR/ACR 2019</b> classification criteria for the diagnosis of "
                + "<b>Systemic Lupus Erythematosus</b>."
                + "</div></html>");
        info.setFont(new Font("SansSerif", Font.PLAIN, 18));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        info.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        frame.add(info, BorderLayout.BEFORE_FIRST_LINE);

        // ---------- Panel de sintomas ----------
        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 40, 20)); // 2 columnas
        gridPanel.setOpaque(false);

        // -------------------- CONSTITUTIONAL --------------------
        addCat(gridPanel, "General manifestations", new Object[][]{
                {"Fever (>38ºC) without infectious cause", SymptomType.FIEBRE}
        });

        // -------------------- MUCOSOCUTANEOUS --------------------
        addCat(gridPanel, "Mucocutaneous", new Object[][]{
                {"Non-scarring alopecia", SymptomType.ALOPECIA},
                {"Oral or nasal ulcers", SymptomType.ULCERAS_ORALES},
                {"Subacute/discoid cutaneous lupus", SymptomType.LESION_SUBAGUDA_DISCOIDE},
                {"Acute cutaneous lupus (malar or butterfly rash)", SymptomType.ERITEMA_AGUDO}
        });

        // -------------------- MUSCULOSKELETAL --------------------
        addCat(gridPanel, "Musculoskeletal", new Object[][]{
                {"Arthritis (≥2 joints with pain or stiffness)", SymptomType.ARTRITIS}
        });

        // -------------------- SEROSAL --------------------
        addCat(gridPanel, "Serosal", new Object[][]{
                {"Pleural or pericardial effusion", SymptomType.DERRAME_SEROSO},
                {"Acute pericarditis", SymptomType.PERICARDITIS_AGUDA}
        });

        // -------------------- RENAL --------------------
        addCat(gridPanel, "Renal", new Object[][]{
                {"Proteinuria >0.5 g/24h", SymptomType.PROTEINURIA},
                {"Class III / IV nephritis (biopsy)", SymptomType.NEFRITIS_CLASE_III_IV},
                {"Class V nephritis (biopsy)", SymptomType.NEFRITIS_CLASE_V}
        });

        // -------------------- HEMATOLOGIC --------------------
        addCat(gridPanel, "Hematologic", new Object[][]{
                {"Leukopenia <4,000/mm³", SymptomType.LEUCOPENIA},
                {"Thrombocytopenia <100,000/mm³", SymptomType.TROMBOCITOPENIA},
                {"Autoimmune hemolysis", SymptomType.HEMOLISIS_AUTOIMMUNE}
        });

        // -------------------- NEURO --------------------
        addCat(gridPanel, "Neuropsychiatric", new Object[][]{
                {"Delirium attributable to SLE", SymptomType.DELIRIO},
                {"Psychosis attributable to SLE", SymptomType.PSICOSIS},
                {"Seizures without other cause", SymptomType.CONVULSIONES}
        });

        // -------------------- IMMUNOLOGIC --------------------
        addCat(gridPanel, "Immunologic", new Object[][]{
                {"Anti-dsDNA or Anti-Sm positive", SymptomType.ANTI_DSDNA_O_ANTI_SM},
                {"Antiphospholipid antibodies (aPL) positive", SymptomType.APL},
                {"Low complement (C3 or C4)", SymptomType.C3_O_C4_BAJO},
                {"Markedly low complement (C3 and C4)", SymptomType.C3_Y_C4_BAJOS}
        });

        // -------------------- ADDITIONAL --------------------
        addCat(gridPanel, "Additional symptoms to consider (not included in EULAR/ACR criteria)", new Object[][]{
                {"Raynaud’s phenomenon", SymptomType.RAYNAUD},
                {"Skin thickening or sclerodactyly", SymptomType.ENGROSAMIENTO_CUTANEO},
                {"Proximal muscle weakness", SymptomType.DEBILIDAD_MUSCULAR},
                {"Elevated CK or myositis", SymptomType.CK_ELEVADA},
                {"Persistent fatigue or malaise", SymptomType.FATIGA}
        });

        JScrollPane scrollPane = new JScrollPane(gridPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(1100, 550));

        frame.add(scrollPane, BorderLayout.CENTER);

        // ---------- Boton inferior ----------
        JButton continuarBtn = new JButton("Continue → Results");
        continuarBtn.setFont(new Font("SansSerif", Font.BOLD, 22));
        continuarBtn.setForeground(Color.BLACK);
        continuarBtn.setBackground(Color.WHITE);
        continuarBtn.setPreferredSize(new Dimension(420, 50));
        continuarBtn.addActionListener(e -> correrReglasYMostrar(app));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(continuarBtn);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        add(frame, BorderLayout.CENTER);
    }

     private void addCat(JPanel parent, String title, Object[][] items) {
            JPanel p = new JPanel();
            p.setOpaque(false);
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            JLabel lbl = new JLabel(title);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
            lbl.setForeground(new Color(30, 60, 120));
            lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
            p.add(lbl);

            for (Object[] it : items) {
                String texto = (String) it[0];
                SymptomType type = (SymptomType) it[1];
                JCheckBox cb = new JCheckBox("  " + texto);
                cb.setOpaque(false);
                cb.setFont(new Font("SansSerif", Font.PLAIN, 17));
                symptomChecks.put(cb, type);
                p.add(cb);
            }
            parent.add(p);
     }

    private void correrReglasYMostrar(LupusApp app) {
        // Construir los facts Symptom presentes
        ArrayList<Symptom> facts = new ArrayList<>();
        for (Map.Entry<JCheckBox, SymptomType> e : symptomChecks.entrySet()) {
            if (e.getKey().isSelected()) facts.add(new Symptom(e.getValue(), true));
        }

        // Validacion minima
        if (facts.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select at least one symptom.", "Warning",
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

            System.out.println("Differential diagnosis rules executed successfully.");
            if (!score.getDiferenciales().isEmpty()) {
                System.out.println("Suggested differential diagnosis:");
                for (String dx : score.getDiferenciales()) {
                    System.out.println(" - " + dx);
                }
            }

            // --- 3. Sesion secundaria: TRATAMIENTO ---
            KieSession ksessionTreatment = kc.newKieSession("treatmentKieSession");
            ksessionTreatment.setGlobal("score", score);

            // Insertar ANA result (obtenido en el panel 1)
            if (ana != null) ksessionTreatment.insert(ana);
            for (Symptom s : facts) ksessionTreatment.insert(s);

            ksessionTreatment.fireAllRules();
            ksessionTreatment.dispose();

            System.out.println("Treatment rules executed successfully.");
            System.out.println("Stored treatments:");
            for (String t : score.getTratamientos()) {
                System.out.println(" - " + t);
            }

            // --- 4. Decision final y mostrado de resultados ---
            if (!elegible) {
                // ANA negativo o <1:80
                if (!score.getDiferenciales().isEmpty()) {
                    app.getPanelResultados().mostrarDiagnostico(
                            "Not eligible for SLE (ANA negative or <1:80).",
                            total,
                            score
                    );
                } else {
                    app.getPanelResultados().mostrarDiagnostico(
                            "Not eligible for SLE and no relevant differential diagnosis.",
                            total,
                            score
                    );
                }
            } else if (total >= 10) {
                app.getPanelResultados().mostrarDiagnostico(
                        "SLE confirmed by EULAR/ACR 2019 criteria",
                        total,
                        score
                );
            } else {
                if (!score.getDiferenciales().isEmpty()) {
                    app.getPanelResultados().mostrarDiagnostico(
                            "Does not meet SLE criteria. Possible differential diagnoses:",
                            total,
                            score
                    );
                } else {
                    app.getPanelResultados().mostrarDiagnostico(
                            "Does not meet SLE criteria or any clear alternative diagnosis.",
                            total,
                            score
                    );
                }
            }

            // --- 5. MOSTRAR PANTALLA DE RESULTADOS ---
            app.showScreen("RESULTADOS");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error executing rules: " + ex.getMessage(),
                    "Drools error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
