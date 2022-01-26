import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Calculator {
    private final ArrayList<String> term;
    private String tempNumber;
    private final ArrayList<String> symbols;
    private final ArrayList<String> symbols2;
    private int brackets;
    private Object DivideByNullException;
    private String answer = "0";
    private boolean academic = false;

    public Calculator() {
        term = new ArrayList<>();
        tempNumber = "";
        String[] arr = {"+", "*", "/", "-", "mod"};
        symbols = new ArrayList<>(List.of(arr));
        String[] arr2 = {"|", "√", "ln"};
        symbols2 = new ArrayList<>(List.of(arr2));
        brackets = 0;
    }

    public Calculator(boolean academic) {
        term = new ArrayList<>();
        tempNumber = "";
        String[] arr = {"+", "*", "/", "-"};
        symbols = new ArrayList<>(List.of(arr));
        String[] arr2 = {"|", "√", "ln"};
        symbols2 = new ArrayList<>(List.of());
        brackets = 0;
        this.academic = academic;
    }

    public String calculate() {
        term.add(tempNumber);
        tempNumber = "";
        for(String i : term) {
            System.out.println(i);
        }
        return analyse(term);
    }

    private String analyse(ArrayList<String> arr) {
        int counter = 0;
        ArrayList<Integer> arrIndex = new ArrayList<>();
        ArrayList<String> arr2 = new ArrayList<>();
        int length = arr.size();
        int i = 0;
        while(i < length) {
            if(Objects.equals(arr.get(i), ")")) {
                counter -= 1;
                if(counter == 0) {
                    int index = arr.indexOf("(");
                    for(int j = 0; j < arr2.size(); j++) {
                        arr.remove(index+1);
                    }

                    if(arr.size() == index + 1) {
                        arr.remove(index);
                    }
                    else {
                        arr.remove(index + 1);
                        arr.remove(index);
                    }
                    int lengthArr = arr2.size();
                    arr.add(index, analyse(arr2));
                    arr2.clear();
                    length -= lengthArr + 1;
                    i -= lengthArr + 1;
                }
            }
            if(counter > 0) {
                arr2.add(arr.get(i));
                arrIndex.add(i);
            }
            if(Objects.equals(arr.get(i), "(")) {
                counter += 1;
            }

            i += 1;
        }
        return compute(arr);
    }

    private String compute(ArrayList<String> arr) {
        try {
            int length = arr.size();
            int j = 0;
            while(j < length) {
                System.out.println(j);
                System.out.println(length);
                if(Objects.equals(arr.get(j), "%")) {
                    double result = Double.parseDouble(arr.get(j-1)) / 100;
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.add(j - 1, Double.toString(result));
                    length -= 1;
                    j -= 1;
                }
                else if(Objects.equals(arr.get(j), "²")) {
                    double result = Math.pow(Double.parseDouble(arr.get(j-1)), 2);
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.add(j - 1, Double.toString(result));
                    length -= 1;
                    j -= 1;
                }
                else if(Objects.equals(arr.get(j), "^")) {
                    double result = Math.pow(Double.parseDouble(arr.get(j - 1)), Double.parseDouble(arr.get(j + 1)));
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.add(j - 1, Double.toString(result));
                    length -= 2;
                    j -= 1;
                }
                else if(Objects.equals(arr.get(j), "!")) {
                    double result = Math.fak(Integer.parseInt(arr.get(j-1)));
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.add(j - 1, Double.toString(result));
                    length -= 1;
                    j -= 1;
                }
                else if(Objects.equals(arr.get(j), "exp")) {
                    double result = Math.exp(Double.parseDouble(arr.get(j+1)));
                    arr.remove(j + 1);
                    arr.remove(j);
                    arr.add(j, Double.toString(result));
                    length -= 1;
                    j -= 1;
                }
                else if(Objects.equals(arr.get(j), "log")) {
                    double result = Math.log(Double.parseDouble(arr.get(j+1)), Double.parseDouble(arr.get(j+2)));
                    arr.remove(j);
                    arr.remove(j);
                    arr.remove(j);
                    arr.add(j, Double.toString(result));
                    length -= 2;
                }
                else if(Objects.equals(arr.get(j), "ln")) {
                    double result = Math.ln(Double.parseDouble(arr.get(j+1)), 65);
                    arr.remove(j);
                    arr.remove(j);
                    arr.add(j, Double.toString(result));
                    length -= 1;
                }
                else if(Objects.equals(arr.get(j), "√")) {
                    double result = Math.pow(Double.parseDouble(arr.get(j + 1)), 1 / Double.parseDouble(arr.get(j - 1)));
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.add(j - 1, Double.toString(result));
                    length -= 2;
                    j -= 1;
                }
                else if(Objects.equals(arr.get(j), "|")) {
                    double result = Math.abs(Double.parseDouble(arr.get(j+1)));
                    arr.remove(j);
                    arr.remove(j);
                    arr.add(j, Double.toString(result));
                    length -= 2;
                }


                j += 1;
            }

            length = arr.size();
            j = 0;
            while(j < length) {
                if(Objects.equals(arr.get(j), "*")) {
                    double result = Double.parseDouble(arr.get(j - 1)) * Double.parseDouble(arr.get(j + 1));
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.add(j - 1, Double.toString(result));
                    length -= 2;
                    j -= 1;
                }
                else if(Objects.equals(arr.get(j), "/")) {
                    double result = Double.parseDouble(arr.get(j - 1)) / Double.parseDouble(arr.get(j + 1));
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.add(j - 1, Double.toString(result));
                    length -= 2;
                    j -= 1;
                }
                else if(Objects.equals(arr.get(j), "mod")) {
                    double result = Double.parseDouble(arr.get(j - 1)) % Double.parseDouble(arr.get(j + 1));
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.add(j - 1, Double.toString(result));
                    length -= 2;
                    j -= 1;
                }

                j += 1;
            }

            length = arr.size();
            j = 0;
            while(j < length) {
                if(Objects.equals(arr.get(j), "+")) {
                    double result = Double.parseDouble(arr.get(j - 1)) + Double.parseDouble(arr.get(j + 1));
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.add(j - 1, Double.toString(result));
                    length -= 2;
                    j -= 1;
                }
                else if(Objects.equals(arr.get(j), "-")) {
                    double result = Double.parseDouble(arr.get(j - 1)) - Double.parseDouble(arr.get(j + 1));
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.remove(j - 1);
                    arr.add(j - 1, Double.toString(result));
                    length -= 2;
                    j -= 1;
                }

                j += 1;
            }

            if(Objects.equals(arr.get(0), "Infinity")) {
                throw (Exception) DivideByNullException;
            }

            return arr.get(0);
        } catch(Exception err) {
            System.out.println(err);
            return "Syntax Error";
        }
    }

    public void addTerm(String str) {
        switch(str) {
            case "x" -> {
                term.add(tempNumber);
                tempNumber = "";

                if (Objects.equals(term.get(term.size() - 1), "")) {
                    term.remove(term.size()-1);
                }

                if(symbols.contains(term.get(term.size() - 1)))  {
                    term.remove(term.size()-1);
                }

                term.add("*");
            }
            case "÷" -> {
                term.add(tempNumber);
                tempNumber = "";

                if (Objects.equals(term.get(term.size() - 1), "")) {
                    term.remove(term.size()-1);
                }

                if(symbols.contains(term.get(term.size() - 1)))  {
                    term.remove(term.size()-1);
                }

                term.add("/");
            }
            case "-" -> {
                term.add(tempNumber);
                tempNumber = "";

                if (Objects.equals(term.get(term.size() - 1), "")) {
                    term.remove(term.size()-1);
                }

                if(term.size() == 0) {
                    tempNumber += "-";
                }
                else {
                    System.out.println(symbols2.contains(term.get(term.size() - 1)));
                    if(symbols.contains(term.get(term.size() - 1)) || symbols2.contains(term.get(term.size() - 1))) {
                        tempNumber += "-";
                    }
                    else if(term.size() > 1 && Objects.equals(term.get(term.size() - 2), "log")) {
                        tempNumber += "-";
                    }
                    else if(Objects.equals(term.get(term.size() - 1), "(")) {
                        term.add("0");
                        term.add(str);
                    }
                    else {
                        term.add(str);
                    }
                }
            }
            case "+" -> {
                term.add(tempNumber);
                tempNumber = "";

                if (Objects.equals(term.get(term.size() - 1), "")) {
                    term.remove(term.size()-1);
                }

                if(term.size() == 0 || Objects.equals(term.get(term.size() - 1), "(")) {
                    term.add("0");
                }

                if(symbols.contains(term.get(term.size() - 1)))  {
                    term.remove(term.size()-1);
                }

                term.add(str);
            }
            case "(" -> {
                term.add(tempNumber);
                tempNumber = "";
                brackets += 1;

                if (Objects.equals(term.get(term.size() - 1), "")) {
                    term.remove(term.size()-1);
                }

                if(term.size() > 0) {
                    if (!symbols.contains(term.get(term.size() - 1))) {
                        term.add("*");
                    }
                }

                term.add(str);
            }
            case ")" -> {
                if(brackets > 0) {
                    term.add(tempNumber);
                    tempNumber = "";

                    if (Objects.equals(term.get(term.size() - 1), "")) {
                        term.remove(term.size()-1);
                    }

                    if(symbols.contains(term.get(term.size()-1))) {
                        term.add("0");
                    }

                    term.add(str);
                }
            }
            case "²", "%" -> {
                term.add(tempNumber);
                tempNumber = "";

                if (Objects.equals(term.get(term.size() - 1), "")) {
                    term.remove(term.size()-1);
                }

                term.add(str);
            }
            case "π" -> {
                term.add(Double.toString(Math.PI));
            }
            case "ANS" -> {
                term.add(answer);
            }
            case "," -> {
                tempNumber += ".";
            }
            case "³" -> {
                term.add(tempNumber);
                tempNumber = "";

                if (Objects.equals(term.get(term.size() - 1), "")) {
                    term.remove(term.size()-1);
                }

                term.add("^");
                term.add("3");
            }
            case "^" -> {
                term.add(tempNumber);
                tempNumber = "";

                if (Objects.equals(term.get(term.size() - 1), "")) {
                    term.remove(term.size()-1);
                }

                term.add("^");
            }
            case "√" -> {
                if(Objects.equals(tempNumber, "")) {
                    term.add("2");
                }
                else {
                    term.add(tempNumber);
                    tempNumber = "";
                }

                term.add("√");
            }
            case "eᵡ" -> {
                term.add("exp");
            }
            case "log" -> {
                term.add("log");
                term.add("10");
            }
            case "ln" -> {
                term.add("ln");
            }
            case "|x|" -> {
                term.add("|");
            }
            case "mod" -> {
                term.add(tempNumber);
                tempNumber = "";

                if (Objects.equals(term.get(term.size() - 1), "")) {
                    term.remove(term.size()-1);
                }

                if(symbols.contains(term.get(term.size() - 1)))  {
                    term.remove(term.size()-1);
                }

                term.add("mod");
            }
            case "!" -> {
                term.add(tempNumber);
                tempNumber = "";

                if (Objects.equals(term.get(term.size() - 1), "")) {
                    term.remove(term.size()-1);
                }

                term.add("!");
            }
            case "e" -> {
                term.add(Double.toString(Math.E));
            }
            default -> {
                tempNumber += str;
            }
        }

    }



    public void clearTerm() {
        term.clear();
        tempNumber = "";
    }

    public ArrayList<String> getTerm() {
        return term;
    }

    public void setAnswer(String answer1) {
        this.answer = answer1;
    }
}
