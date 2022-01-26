import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Objects;

public class OutputLabel extends JLabel {
    private String text = "0";

    public OutputLabel() {
        super();
        setFont(new Font("Segoe UI", Font.BOLD, 40));
        setHorizontalAlignment(SwingConstants.RIGHT);
        setForeground(Color.WHITE);
        setText(text);
        setPreferredSize(new Dimension(370, 84));

        EmptyBorder padding = new EmptyBorder(0, 7, 0, 7);
        LineBorder border = new LineBorder(Color.WHITE, 1);
        setBorder(BorderFactory.createCompoundBorder(padding, border));

        setVisible(true);
    }

    public void addText(String text1) {
        if (Objects.equals(this.text, "0")) {
            this.text = text1;
        }
        else {
            this.text += text1;
        }

        if(this.text.length() > 19) {
            return;
        }
        else if(this.text.length() > 18) {
            setFont(new Font("Segoe UI", Font.BOLD, 29));
        }
        else if(this.text.length() > 17) {
            setFont(new Font("Segoe UI", Font.BOLD, 31));
        }
        else if(this.text.length() > 16) {
            setFont(new Font("Segoe UI", Font.BOLD, 33));
        }
        else if(this.text.length() > 15) {
            setFont(new Font("Segoe UI", Font.BOLD, 35));
        }
        else if(this.text.length() > 14) {
            setFont(new Font("Segoe UI", Font.BOLD, 37));
        }
        setText(this.text);
    }

    public void clearText() {
        this.text = "0";
        setText(this.text);
    }

    public void removeLastChar(){
        if(this.text.length() == 1) {
            this.text = "0";
        }
        else {
            this.text = removeLastChars(this.text);
        }
        setText(this.text);
    }

    private static String removeLastChars(String str) {
        return str.substring(0, str.length() - 1);
    }

    @Override
    public String getText() {
        return text;
    }
}
