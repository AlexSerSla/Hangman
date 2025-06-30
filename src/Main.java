
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
        if (getStartGame()) {
            if (tryCreateDictionary()) {
                startGameRound();
            }
        } else {
            scanner.close();
        }
    }

    public static void startGameRound() {
        hiddenWord.clear();
        displayWord.clear();
        usedLetters.clear();
        numOfErrors = 7;

        splitWordAsLetters(chooseHiddenWord());

        for (char i : hiddenWord){
            displayWord.add('*');
        }
        printWord(displayWord);
        startGameLoop();
    }

    public static void startGameLoop() {
        while(true) {
            char letter = getPlayerLetter();
            boolean hasLetter = checkLetterInWord(letter);
            boolean gameOver = false;
            printWord(displayWord);
            if (hasLetter) {
                gameOver = isWin();
            } else {
                numOfErrors--;
                gameOver = hasPlayerLost();
                drawHangman();
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

    public static void splitWordAsLetters(String hiddenWord) {
        char[] hiddenWordArray = hiddenWord.toCharArray();
        for (char letter : hiddenWordArray){
            Main.hiddenWord.add(letter);
        }
    }

    public static boolean tryCreateDictionary() {
        BufferedReader reader = null;
        boolean isDictionaryRead = false;
        try {
            reader = new BufferedReader(new FileReader("src/dictionaryr.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line.toUpperCase());
            }
            isDictionaryRead = true;
        } catch (IOException e) {
            System.err.println("Не удалось открыть файл словаря. Программа будет завершена.");
            isDictionaryRead = false;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println("Не удалось закрыть файл словаря. Программа будет завершена.");
                isDictionaryRead = false;
            } finally {
                return isDictionaryRead;
            }
        }
    }

    public static String chooseHiddenWord(){
        Random random = new Random();
        int randomNumberWord = random.nextInt(0, dictionary.size() - 1);
        return dictionary.get(randomNumberWord);
    }

    public static void printWord(List<Character> printWord) {
        System.out.print("Загаданное слово: ");
        for (char line : printWord){
            System.out.print(line);
        }
        System.out.println();
    }

    //==================================================================================================================
    public static char getPlayerLetter() {
        usedLetters.add(inputLetter());
        printUsedLetters();
        return usedLetters.getLast();
    }

    public static char inputLetter() {
        while (true) {
            System.out.println("");
            System.out.println("Введите букву");

            char letter = scanner.nextLine().charAt(0);

            if (Character.isLetter(letter)) {
                char upCaseLetter = Character.toUpperCase(letter);
                boolean isLetterUsed = isUsedLetter(upCaseLetter);

                if (isLetterUsed) {
                    System.out.println("Данная буква уже использовалась! Введите другую.");
                } else {
                    return upCaseLetter;
                }
            } else {
                System.out.printf("Вы ввели: %s. Необходимо ввести букву. \n", letter);
            }
        }
    }

    public static boolean isUsedLetter(char letter) {
        return usedLetters.contains(letter);
    }

    public static void printUsedLetters() {
        System.out.print("Вы ввели: ");
        for (char letter : usedLetters) {
            System.out.print(letter + " ");
        }
        System.out.println();
        System.out.println();
    }

    public static boolean checkLetterInWord(char letter) {

        if (hiddenWord.contains(letter)) {
            System.out.println("Буква присутствует");
            for (int index = 0; index < hiddenWord.size(); index++) {
                if (hiddenWord.get(index) == letter){
                    displayWord.set(index, letter);
                }
            }
            return true;
        }

        System.out.println("Данная буква в слове отсутствует...");
        return false;
    }

    public static boolean isWin() {
        if (!(displayWord.contains('*'))) {
            System.out.println("Вы виграли!!! Поздравляю!");
            System.out.println();
            return true;
        }

        return false;
    }

    public static boolean hasPlayerLost() {
        if (numOfErrors < MAX_MISTAKES && numOfErrors > 0) {
            System.out.printf("Осталось ошибок: %s \n", numOfErrors);
        }

        if (numOfErrors == 0) {
            System.out.println("Вы проиграли...");
            printWord(hiddenWord);
            System.out.println();
            return true;
        }

        return false;
    }

    public static void drawHangman() {
        int numOfDraw = (MAX_MISTAKES - numOfErrors) - 1;
        if (numOfDraw >= 0) {
            for (String state : hangmanPictures[numOfDraw]){
                System.out.println(state);
            }
        }
    }
}