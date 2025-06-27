
import java.util.*;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    static List<Character> usedLettersList = new ArrayList<>();
    static List<Character> hiddenWordList = new ArrayList<>();
    static List<Character> displayWordList = new ArrayList<>();

    static String[][] lostState = {{"┌───┐",
                                    "│",
                                    "│",
                                    "│",
                                    "╘═════"},
                                    {"┌───┐",
                                     "│   О",
                                     "│",
                                     "│",
                                     "╘═════"},
                                    {"┌───┐",
                                     "│   О",
                                     "│   |",
                                     "│",
                                     "╘═════"},
                                    {"┌───┐",
                                     "│   О",
                                     "│  /|",
                                     "│",
                                     "╘═════"},
                                    {"┌───┐",
                                     "│   О",
                                     "│  /|\\",
                                     "│",
                                     "╘═════"},
                                    {"┌───┐",
                                     "│   О",
                                     "│  /|\\",
                                     "│  / ",
                                     "╘═════"},
                                    {"┌───┐",
                                     "│   О",
                                     "│  /|\\",
                                     "│  / \\",
                                     "╘═════"}
    };

    private static int numOfErrors = 7;

    public static void main(String[] args) {
        controlGame();
    }

    public static void controlGame() {
        if (getStartGame()) {
            startGameRound();
        } else {
            scanner.close();
        }
    }

    public static void startGameRound() {
        hiddenWordList.clear();
        displayWordList.clear();
        usedLettersList.clear();
        numOfErrors = 7;

        splitWordAsLetters(chooseHiddenWord());

        for (char i : hiddenWordList){
            displayWordList.add('*');
        }
        printHiddenWord(displayWordList);
        startGameLoop();
    }

    public static void startGameLoop() {
        while(true) {
            char inputLetter = getPlayerLetter();
            boolean haveLetter = checkLetterInWord(hiddenWordList, inputLetter);
            boolean gameOver = false;
            printHiddenWord(displayWordList);
            if (haveLetter) {
                gameOver = checkWinInGame(displayWordList);
            } else {
                numOfErrors--;
                gameOver = checkLostInGame(numOfErrors);
                drawHangman(numOfErrors);
            }
            if (gameOver) {
                controlGame();
                break;
            }
        }
    }

    //==================================================================================================================
    public static boolean getStartGame() {
        while (true) {
            System.out.println("Введите <start> для начала новой игры, введите <exit> для выхода");

            String inputWord = scanner.nextLine().toUpperCase();

            if (inputWord.equals("START")) {
                return true;
            } else if (inputWord.equals("EXIT")){
                return false;
            }
        }
    }

    public static void splitWordAsLetters(String hiddenWord) {
        char[] hiddenWordArray = hiddenWord.toCharArray();
        for (char letter : hiddenWordArray){
            hiddenWordList.add(letter);
        }
    }

    public static String chooseHiddenWord(){
        //тут алгоритм выбора слова из словаря в файле
        //необходимо создать файл
        return "ЛАМПА";
    }

    public static void printHiddenWord (List<Character> displayWordList) {
        System.out.print("Загаданное слово: ");
        for (char letter : displayWordList){
            System.out.print(letter);
        }
        System.out.println();
    }

    //==================================================================================================================
    public static char getPlayerLetter() {
        usedLettersList.add(getUserLetter());
        printUsedLetters(usedLettersList);
        return usedLettersList.get(usedLettersList.size() - 1);
    }

    public static char getUserLetter() {
        while (true) {
            System.out.println("");
            System.out.println("Введите букву");

            char inputLetter = scanner.nextLine().charAt(0);

            if (Character.isLetter(inputLetter)) {
                char upCaseInputLetter = Character.toUpperCase(inputLetter);
                boolean isLetterUsed = checkInputLetterUsed(upCaseInputLetter);

                if (isLetterUsed) {
                    System.out.println("Данная буква уже использовалась! Введите другую.");
                } else {
                    return upCaseInputLetter;
                }
            } else {
                System.out.println("Вы ввели: " + inputLetter + ". Необходимо ввести букву.");
            }
        }
    }

    public static boolean checkInputLetterUsed(char inputLetter) {
        for(char usedLetter : usedLettersList) {
            if (inputLetter == usedLetter) {
                return true;
            }
        }
        return false;
    }

    public static void printUsedLetters(List<Character> usedLettersList) {
        System.out.print("Вы ввели: ");
        for (char letter : usedLettersList) {
            System.out.print(letter + " ");
        }
        System.out.println();
        System.out.println();
    }

    public static boolean checkLetterInWord(List<Character> hideWordList, char inLetter) {

        if (hideWordList.contains(inLetter)) {
            System.out.println("Буква присутствует");
            for (int index = 0; index < hideWordList.size(); index++) {
                if (hideWordList.get(index) == inLetter){
                    displayWordList.set(index, inLetter);
                }
            }
            return true;
        } else {
            System.out.println("Данная буква в слове отсутствует...");
            return false;
        }
    }

    public static boolean checkWinInGame (List<Character> displayWordList) {
        if (!(displayWordList.contains('*'))) {
            System.out.println("Вы виграли!!! Поздравляю!");
            System.out.println();
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkLostInGame(int numOfErrors) {

        System.out.println("Осталось " + numOfErrors + " ошибок.");
        if (numOfErrors == 0) {
            System.out.println("Вы проиграли...");
            System.out.println();
            return true;
        } else {
            return false;
        }
    }

    public static void drawHangman(int numOfErrors) {
        int numOfDraw = (7 - numOfErrors) - 1;
        if (numOfDraw >= 0) {
            for (String state : lostState[numOfDraw]){
                System.out.println(state);
            }
        }
    }
}