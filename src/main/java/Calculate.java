import java.util.InputMismatchException;

public class Calculate {


    public Calculate(String str, int action) {
        String[] words = str.split("");

        String FirstWord = "";
        String SecondWord = "";
        Boolean flagFW = true;

        // сплит ругается, поэтому разбиваем на слова перебром
        // зачем до этого выбирал action - непонятно
        for (int i = 0; i < words.length; i++) {
            if (flagFW) {

                switch (words[i]) {
                    case "*": {
                        flagFW = false;
                        continue;
                    }
                    case "/": {
                        flagFW = false;
                        continue;
                    }
                    case "-": {
                        flagFW = false;
                        continue;
                    }
                    case "+": {
                        flagFW = false;
                        continue;
                    }
                }

                FirstWord = FirstWord + words[i];
            } else if ("*".equals(words[i]) ||
                    "/".equals(words[i]) ||
                     "-".equals(words[i]) ||
                    "+".equals(words[i])) {
                System.out.println("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
                return;
            } else {
                SecondWord = SecondWord + words[i];
            }
        }
        // сносим пробелы, т.к. в ТЗ ничего о их наличии или отстувии небыло, хотя может быть и так сработало бы
        FirstWord = FirstWord.replace(" ", "");
        SecondWord = SecondWord.replace(" ", "");

        // определяем арабские или римские символы
        boolean ROME = false;
        int num1 = CheckNum(FirstWord);
        int num2 = CheckNum(SecondWord);

        if (num1 == 0 & num2 == 0) {
            num1 = CheckRome(FirstWord);
            num2 = CheckRome(SecondWord);

            if (num1 != 0 & num2 != 0) {
                ROME = true;
            } else {
                System.out.println("Необходимо здадать либо римские либо арабские цифры от 1 до 10");
                return;
            }
        } else if (num1 == 0 | num2 == 0) {
            System.out.println("Необходимо здадать либо римские либо арабские цифры от 1 до 10");
            return;
        }

        int Result = 0;

        switch (action) {
            case 0:
                Result = num1 + num2;
                break;
            case 1:
                Result = num1 - num2;
                break;
            case 2:
                Result = num1 * num2;
                break;
            case 3:
                try {
                    Result = num1 / num2;
                } catch (ArithmeticException a) {
                    System.out.println("Деление на 0");
                }
                break;
        }

        if (ROME) {
            if (Result >= 0) {
                System.out.println(convert(Result));
            } else System.out.println("в римской системе нет отрицательных чисел");

        } else {
            System.out.println(Result);
        }


    }
       /* switch (action) {
            case (0):
                words = str.split("+");
                System.out.println("+");
                break;
            case (1):
                words = str.split("-");
                break;
            case (2):
                char Sym = '+';
                words = str.split(Sym);
                break;
            case (3):
                words = str.split("/");
                break;
            default:
                System.out.println("Ошибка");
                break;
        }  TODO не понял почему */


    private int CheckNum(String word) {
        if (word.equals("1")) {
            return 1;
        } else if (word.equals("2")) {
            return 2;
        } else if (word.equals("3")) {
            return 3;
        } else if (word.equals("4")) {
            return 4;
        } else if (word.equals("5")) {
            return 5;
        } else if (word.equals("6")) {
            return 6;
        } else if (word.equals("7")) {
            return 7;
        } else if (word.equals("8")) {
            return 8;
        } else if (word.equals("9")) {
            return 9;
        } else if (word.equals("10")) {
            return 10;
        } else return 0;
    }

    private int CheckRome(String word) {

        if (word.equals("I")) {
            return 1;
        } else if (word.equals("II")) {
            return 2;
        } else if (word.equals("III")) {
            return 3;
        } else if (word.equals("IV")) {
            return 4;
        } else if (word.equals("V")) {
            return 5;
        } else if (word.equals("VI")) {
            return 6;
        } else if (word.equals("VII")) {
            return 7;
        } else if (word.equals("VIII")) {
            return 8;
        } else if (word.equals("IX")) {
            return 9;
        } else if (word.equals("X")) {
            return 10;
        } else
            return 0;


    }

    private static String convert(int num) {
        String[] Rome100 = {"O", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X",
                "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX",
                "XXI", "XXII", "XXIII", "XXIV", "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX",
                "XXXI", "XXXII", "XXXIII", "XXXIV", "XXXV", "XXXVI", "XXXVII", "XXXVIII", "XXXIX", "XL",
                "XLI", "XLII", "XLIII", "XLIV", "XLV", "XLVI", "XLVII", "XLVIII", "XLIX", "L",
                "LI", "LII", "LIII", "LIV", "LV", "LVI", "LVII", "LVIII", "LIX", "LX",
                "LXI", "LXII", "LXIII", "LXIV", "LXV", "LXVI", "LXVII", "LXVIII", "LXIX", "LXX",
                "LXXI", "LXXII", "LXXIII", "LXXIV", "LXXV", "LXXVI", "LXXVII", "LXXVIII", "LXXIX", "LXXX",
                "LXXXI", "LXXXII", "LXXXIII", "LXXXIV", "LXXXV", "LXXXVI", "LXXXVII", "LXXXVIII", "LXXXIX", "XC",
                "XCI", "XCII", "XCIII", "XCIV", "XCV", "XCVI", "XCVII", "XCVIII", "XCIX", "C"
        };
        String s = Rome100[num];
        return s;
    }
}
