import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class KeypadButton extends JButton {
    public KeypadButton(String text, Color color, Color colorHover, OutputLabel label, Calculator calc, int width, int height) {
        setText(text);
        setBackground(color);
        setForeground(Color.WHITE);
        setBorder(null);
        setPreferredSize(new Dimension(width, height));
        setFont(new Font("Segoe UI", Font.BOLD, 28));
        setFocusPainted(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(colorHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(color);
            }
        });

        addActionListener(e -> {
            if(text == "x²") {
                label.addText("²");
                calc.addTerm("²");
            }
            else if(Objects.equals(text, "⌫")) {
                label.removeLastChar();
            }
            else if(text.equals("CE")) {
                label.clearText();
                calc.clearTerm();
            }
            else if(text.equals("ANS")) {
                label.addText("ANS");
                calc.addTerm("ANS");
            }
            else if(Objects.equals(text, "=")) {
                label.clearText();
                String result = calc.calculate();
                label.addText(result);
                calc.setAnswer(result);

            }
            else if(Objects.equals(text, "n!")) {
                label.addText("!");
                calc.addTerm("!");
            }
            else if(Objects.equals(text, "|x|")) {
                label.addText("|");
                calc.addTerm("|");
            }
            else if(Objects.equals(text, "eᵡ")) {
                label.addText("e");
                label.addText("^");
                calc.addTerm("eᵡ");
            }
            else if(Objects.equals(text, "x³")) {
                label.addText("³");
                calc.addTerm("³");
            }
            else if(Objects.equals(text, "xⁿ")) {
                label.addText("^");
                calc.addTerm("^");
            }
            else if(Objects.equals(text, "√x")) {
                label.addText("√");
                calc.addTerm("√");
            }
            else if(Objects.equals(text, "ⁿ√x")) {
                label.addText("√");
                calc.addTerm("√");
            }
            else {
                label.addText(text);
                calc.addTerm(text);
            }
        });
        setVisible(true);
    }
}
