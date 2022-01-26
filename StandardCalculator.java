import javax.swing.*;
import java.awt.*;

public class StandardCalculator extends JPanel {
    private OutputLabel output;
    private Keypad keypad;

    public StandardCalculator() {
        super();
        setBackground(new Color(60, 60, 64));

        output = new OutputLabel();
        keypad = new KeypadNormal(output);

        this.add(output, BorderLayout.NORTH);
        this.add(keypad, BorderLayout.SOUTH);

        setVisible(true);
    }
}
