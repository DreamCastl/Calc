import java.io.*;

public class Main {

    public Main() {
    }

    public static void main(String[] args) throws IOException {
        boolean reEnter = false;
        do {
            System.out.println("Enter to expression:");

            InputStream inputStream = System.in;
            Reader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String expression = bufferedReader.readLine(); //читаем строку с клавиатуры

            int Action = CheckAction(expression);
            if (Action == 4) {
                System.out.println("Не задан операнд");
            } else {
                Calculate calculate = new Calculate(expression, Action);
            }

            System.out.println("Введите 1 чтобы продолжить");
            String reEnterUser = bufferedReader.readLine(); //читаем строку с клавиатуры
            if (reEnterUser.contains("1")) reEnter = true;
            else reEnter = false;

        } while (reEnter);
    }

    static int CheckAction(String expression) {
        if (expression.indexOf("+") != -1) {
            return 0;
        } else if (expression.indexOf("-") != -1) {
            return 1;
        } else if (expression.indexOf("*") != -1) {
            return 2;
        } else if (expression.indexOf("/") != -1) {
            return 3;
        } else {
            return 4;
        }
    }

}

