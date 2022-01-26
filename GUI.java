import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    public GUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(60, 60, 64));
        setSize(new Dimension(380, 550));
        setIconImage(null);
        setTitle("Calculator");

        StandardCalculator standardCalculator = new StandardCalculator();
        AcademicCalculator academicCalculator = new AcademicCalculator();

        //getContentPane().add(new JLabel("test"), BorderLayout.NORTH);
        getContentPane().add(academicCalculator, BorderLayout.CENTER);
        setVisible(true);

    }
}
