
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    private final static List<String> dictionary = new ArrayList<>();
    private final static List<Character> usedLetters = new ArrayList<>();
    private final static List<Character> hiddenWord = new ArrayList<>();
    private final static List<Character> displayWord = new ArrayList<>();

    private final static String START = "1";
    private final static String EXIT = "0";
    private final static int MAX_MISTAKES = 7;

    private static int numOfErrors = MAX_MISTAKES;

    private final static String[][] hangmanPictures = {{"┌───┐",
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


    public static void main(String[] args) {
        controlGame();
    }

    public static void controlGame() {
        while (getStartGame()) {
            if (tryCreateDictionary()) {
                startGameRound();
            } else {
                scanner.close();
                break;
            }
        }
    }

    public static void startGameRound() {
        hiddenWord.clear();
        displayWord.clear();
        usedLetters.clear();
        numOfErrors = MAX_MISTAKES;

        splitWordAsLetters(chooseHiddenWord());

        for (char i : hiddenWord) {
            displayWord.add('*');
        }
        printWord(displayWord);
        startGameLoop();
    }

    public static void startGameLoop() {
        while (!isGameOver()) {
            char letter = inputLetter();
            addToUsedLetters(letter);
            printUsedLetters();

            if (hiddenWord.contains(letter)) {
                System.out.println("Буква присутствует");
                revealGuessedLetters(letter);
                printWord(displayWord);
            } else {
                numOfErrors--;
                System.out.println("Данная буква в слове отсутствует...");
                System.out.printf("Осталось ошибок: %s \n", numOfErrors);
                drawHangman();
            }

            if(isWin()) {
                System.out.println("Вы выиграли!!! Поздравляю!");
                System.out.println();
            } else if (isLose()) {
                System.out.println("Вы проиграли...");
                printWord(hiddenWord);
                System.out.println();
            }
        }
    }

    //==================================================================================================================
    public static boolean getStartGame() {
        while (true) {
            System.out.println("Введите <1> для начала новой игры, введите <0> для выхода");
            String inputWord = scanner.nextLine().toUpperCase();

            if (inputWord.equals(START)) {
                return true;
            }
            if (inputWord.equals(EXIT)) {
                return false;
            }
        }
    }

    public static void splitWordAsLetters(String word) {
        char[] hiddenWordArray = word.toCharArray();
        for (char letter : hiddenWordArray) {
            hiddenWord.add(letter);
        }
    }

    public static boolean tryCreateDictionary() {
        String filePath = "src/dictionary.txt";

        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String line;

            while ((line = reader.readLine()) != null) {
                dictionary.add(line.toUpperCase());
            }
            return true;

        } catch (IOException e) {
            System.err.println("Не удалось закрыть файл словаря. Программа будет завершена.");
            return false;
        }
    }

    public static String chooseHiddenWord() {
        Random random = new Random();
        int randomNumberWord = random.nextInt(0, dictionary.size() - 1);
        return dictionary.get(randomNumberWord);
    }

    public static void printWord(List<Character> printWord) {
        System.out.print("Загаданное слово: ");
        for (char line : printWord) {
            System.out.print(line);
        }
        System.out.println();
    }

    public static char inputLetter() {
        while (true) {
            System.out.println("");
            System.out.println("Введите букву");

            String input = scanner.nextLine();

            if (input.length() != 1) {
                System.out.println("Необходимо ввести 1 символ.");
                continue;
            }

            boolean isRussianLetter = input.matches("[а-яА-ЯёЁ]+");

            if (!isRussianLetter) {
                System.out.printf("Вы ввели: %s. Необходимо ввести русскую букву. \n", input);
            } else {
                char upperCaseLetter = input.toUpperCase().charAt(0);
                boolean isLetterUsed = usedLetters.contains(upperCaseLetter);

                if (isLetterUsed) {
                    System.out.println("Данная буква уже использовалась! Введите другую.");
                } else {
                    return upperCaseLetter;
                }
            }
        }
    }

    private static void addToUsedLetters(char letter) {
        usedLetters.add(letter);
    }

    public static void printUsedLetters() {
        System.out.print("Вы ввели: ");
        for (char letter : usedLetters) {
            System.out.print(letter + " ");
        }
        System.out.println();
        System.out.println();
    }

    public static void revealGuessedLetters(char letter) {
        for (int index = 0; index < hiddenWord.size(); index++) {
            if (hiddenWord.get(index) == letter) {
                displayWord.set(index, letter);
            }
        }
    }

    public static boolean isWin() {
        return !(displayWord.contains('*'));
    }

    public static boolean isLose() {
        return numOfErrors == 0;
    }

    private static boolean isGameOver() {
        return isWin() || isLose();
    }

    public static void drawHangman() {
        int numOfDraw = (MAX_MISTAKES - numOfErrors) - 1;
        if (numOfDraw >= 0) {
            for (String state : hangmanPictures[numOfDraw]) {
                System.out.println(state);
            }
        }
    }

}