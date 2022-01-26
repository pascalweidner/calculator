import javax.swing.*;
import java.awt.*;

public class AcademicCalculator extends JPanel {
    private final OutputLabel output;
    private final Keypad keypad;

    public AcademicCalculator() {
        super();
        setBackground(new Color(60, 60, 64));

        output = new OutputLabel();
        keypad = new KeypadAcademic(output);

        this.add(output, BorderLayout.NORTH);
        this.add(keypad, BorderLayout.SOUTH);

        setVisible(true);
    }
}
