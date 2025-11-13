package ui;

import javax.swing.*;
import java.awt.*;
public class PanelInitial extends JPanel {

    public PanelInitial(LupusApp app) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel frame = new JPanel();
        frame.setBackground(new Color(255, 255, 255, 180));
        frame.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));

        JLabel text = new JLabel(
                "<html><div style='text-align: justify; width: 700px;'>"
                        + "This system applies the <b>EULAR/ACR 2019</b> classification criteria "
                        + "for the diagnosis of <b>Systemic Lupus Erythematosus (SLE)</b>.<br><br>"
                        + "It allows the clinician to enter the patient's clinical and immunological data, "
                        + "process them through logical inference, and automatically obtain a report "
                        + "with the corresponding diagnostic score."
                        + "</div></html>");
        text.setFont(new Font("SansSerif", Font.PLAIN, 18));
        text.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton start = new JButton("Start Evaluation");
        start.setFont(new Font("SansSerif", Font.BOLD, 21));
        start.setForeground(Color.BLACK);
        start.setBackground(Color.WHITE);
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.setMaximumSize(new Dimension(420, 75));
        start.addActionListener(e -> app.showScreen("EVALUATION"));

        frame.add(text);
        frame.add(Box.createVerticalStrut(40));
        frame.add(start);

        add(frame);
    }
}
