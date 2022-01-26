import javax.swing.border.EmptyBorder;
import java.awt.*;

public class KeypadNormal extends Keypad {
    private final int GRID_SIZE_HORIZONTAL = 4;
    private final int GRID_SIZE_VERTICAL = 6;

    private final OutputLabel label;
    private final Calculator calc;

    private final Color color1 = new Color(75, 75, 84);
    private final Color color2 = new Color(36, 36, 39);
    private final Color color3 = new Color(109, 109, 119);
    private final Color color4 = new Color(244, 93, 255);


    public KeypadNormal(OutputLabel l) {
        super();
        setLayout(new GridLayout(GRID_SIZE_VERTICAL, GRID_SIZE_HORIZONTAL, 2, 2));
        setBorder(new EmptyBorder(34, 0, 0, 0));

        label = l;
        calc = new Calculator();

        String[] arr = {"%", "π", "CE", "⌫", "(", ")", "x²", "÷", "7", "8", "9", "x", "4", "5", "6", "-", "1", "2", "3", "+", "ANS", "0", ",", "="};
        Color[] colors = {color1, color1, color1, color1, color1, color1, color1, color1, color2, color2, color2, color1, color2, color2, color2, color1, color2, color2, color2, color1, color2, color2, color2, color4};

        for(int i = 0; i < 24; i++) {
            if(i == 23) {
                this.add(new KeypadButton(arr[i], colors[i], new Color(227, 143, 233), label, calc, 88, 54));
            }
            else {
                this.add(new KeypadButton(arr[i], colors[i], color3, label, calc, 88, 54));
            }
        }


    }
}
