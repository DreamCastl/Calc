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

    
    
     public List<Map<String, String>> ProcessIncomingEmails() {
        List<Map<String, String>> rez = new ArrayList<>();

        if (!DriverNMFO.isAutorizationConncet()) {
            logger.warn("НМФО не авторизован");
            DriverNMFO.authorization(DriverConnect.getDriver());
            if (!DriverNMFO.isAutorizationConncet()) {
                logger.warn("Попытка авторизации провалилась, попробуйте позднее");
                return null;
            }
        }
        // 4. Получаемся письма - заявки
        //  Message[] messages = EmailReader.ReadMessage("INBOX");//"письма")
        Message[] messages = EmailReader.ReadMessage("INBOX/Newsletters");//"письма")

        for (Message Message : messages
        ) {
            try {
                logger.info("Начинаем разбирать письма");
                Map<String, String> CurrentLine = getLineBaseInfo();
                String Content = null;
                try {
                    Content = EmailReader.GetContentMail(Message);
                } catch (MessagingException | IOException e) {
                    e.printStackTrace();
                    logger.warn("Не удалось получить контент из письма");
                } finally {
                    EmailReader.SetFlagSeen(Message, false);
                }
                if (Content.equals("")) {
                    System.out.println("Не прочитал письмо");
                    //  CurrentLine.add("Ошибка,заявка не прочитана");
                    continue;

                }
                ParserData ParserData = new ParserData();
                CurrentLine.put("Number", ParserData.NumberApplicationFromContext(Content));
                String NumberProgramm = ParserData.NumberProgrammFromContext(Content);
                //CurrentLine.add(NumberApplication);

                // 5. Получаем данные с НМФО
                logger.info("------------>" + CurrentLine.get("Number") + "<------------"); //todo выделить начало обрабатываемой заявки,чтобы в логе выделить её начало

                if (CurrentLine.get("Number").contains("NMOV")) {
                    DriverNMFO.voPageDesktopPreparation(DriverConnect.getDriver());
                } else {
                    DriverNMFO.spoPageDesktopPreparation(DriverConnect.getDriver());
                }

                List<String> clientInfo = new ArrayList<>();
                List<String> DataClientInfo = DriverConnect.getSpoAndVoPage().searchForApplication(CurrentLine.get("Number"));
                if (DataClientInfo.size() != 0) {
                    clientInfo = DriverConnect.getSpoAndVoPage().getClientInfo();
                }
                // 6. Записываем строку в google Sheet
                //todo исправить НОРМАЛЬНО конструкцию на if-else
                if (clientInfo.size() == 0) {
                    logger.warn("Не нашел контент по заявке " + CurrentLine.get("Number"));
                    EmailReader.SetFlagSeen(Message, false);
                    logger.info("------------> END <------------");
                    continue;
                } else {
                    clientInfo.add(0, DataClientInfo.get(0));
                    clientInfo.add(1, DataClientInfo.get(1));
                    clientInfo.add(2, ParserData.getNameProgramm(Content));

                    CurrentLine.put("Payer", clientInfo.get(18 + 3));
                    CurrentLine.put("Email", clientInfo.get(16 + 3));

                }
                // 7. Подтверждаем заявку на НМФО
                //todo
                DriverConnect.getSpoAndVoPage().setConfirmationCheckBox();
                DriverConnect.getSpoAndVoPage().closeWindowsAndReturnCyclePc();
                // 8. Отправляем письмо на почту.

                AddPlayerAndAttach(CurrentLine);

                mailSender.sendMessageWithAttachment(CurrentLine);
                SheetsService.AppendRow("Заявки", clientInfo);
                EmailReader.SetFlagSeen(Message, true);
                rez.add(CurrentLine);

                logger.info("------------> END <------------"); //todo выделить конец обрабатываемой заявки,чтобы в логе выделить что по этой заявке все ок при быстром просмотре лога

            } catch (Exception e) {
                DriverConnect.getSpoAndVoPage().closeWindowsAndReturnCyclePc();
                EmailReader.SetFlagSeen(Message, false);
                logger.warn("Fucking ERROR " + e.getMessage());
            }
        }
        return rez;

    }
    
}

