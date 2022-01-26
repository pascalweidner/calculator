import javax.swing.border.EmptyBorder;
import java.awt.*;

public class KeypadAcademic extends Keypad{
    private final int GRID_SIZE_HORIZONTAL = 5;
    private final int GRID_SIZE_VERTICAL = 7;

    private final OutputLabel label;
    private final Calculator calc;

    private final Color color1 = new Color(75, 75, 84);
    private final Color color2 = new Color(36, 36, 39);
    private final Color color3 = new Color(109, 109, 119);
    private final Color color4 = new Color(244, 93, 255);

    public KeypadAcademic(OutputLabel l) {
        super();
        setLayout(new GridLayout(GRID_SIZE_VERTICAL, GRID_SIZE_HORIZONTAL, 2, 2));
        setBorder(new EmptyBorder(34, 0, 0, 0));

        label = l;
        calc = new Calculator();
        String[] arr = {"x³", "e", "π", "CE", "⌫", "xⁿ", "%", "mod", "|x|", "n!", "√x", "(", ")", "x²", "÷", "ⁿ√x", "7", "8", "9", "x", "eᵡ", "4", "5", "6", "-", "log", "1", "2", "3", "+", "ln", "ANS", "0", ",", "="};
        Color[] colors = {color1, color1, color1, color1, color1, color1, color1, color1, color1, color1, color1, color1, color1, color1, color1, color1, color2, color2, color2, color1, color1, color2, color2, color2, color1, color1, color2, color2, color2, color1, color1, color2, color2, color2, color4};
        for(int i = 0; i < 35; i++) {
            if(i == 34) {
                this.add(new KeypadButton(arr[i], colors[i], new Color(227, 143, 233), label, calc, 68, 42));
            }
            else {
                this.add(new KeypadButton(arr[i], colors[i], color3, label, calc, 68, 42));
            }
        }
    }
}
