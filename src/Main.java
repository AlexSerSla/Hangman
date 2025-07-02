
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final List<String> dictionary = new ArrayList<>();
    private static final List<Character> usedLetters = new ArrayList<>();
    private static final List<Character> hiddenWord = new ArrayList<>();
    private static final List<Character> displayWord = new ArrayList<>();

    private static final String START = "1";
    private static final String EXIT = "0";

    private static final int MAX_MISTAKES = 7;
    private static int numOfErrors = MAX_MISTAKES;

    public static void main(String[] args) {
        controlGame();
    }

    private static void controlGame() {
        while (getStartGame()) {
            if (tryCreateDictionary()) {
                startGameRound();
            } else {
                scanner.close();
                break;
            }
        }
    }

    private static void startGameRound() {
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

    private static void startGameLoop() {
        while (!isGameOver()) {
            char letter = inputUpperCaseLetter();
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

                HangmanPrinter.drawHangman(MAX_MISTAKES, numOfErrors);
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

    private static boolean getStartGame() {
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

    private static void splitWordAsLetters(String word) {
        char[] hiddenWordArray = word.toCharArray();
        for (char letter : hiddenWordArray) {
            hiddenWord.add(letter);
        }
    }

    private static boolean tryCreateDictionary() {
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

    private static String chooseHiddenWord() {
        Random random = new Random();
        int randomNumberWord = random.nextInt(0, dictionary.size() - 1);
        return dictionary.get(randomNumberWord);
    }

    private static void printWord(List<Character> printWord) {
        System.out.print("Загаданное слово: ");
        for (char line : printWord) {
            System.out.print(line);
        }
        System.out.println();
    }

    private static char inputUpperCaseLetter() {
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

    private static void printUsedLetters() {
        System.out.print("Вы ввели: ");
        for (char letter : usedLetters) {
            System.out.print(letter + " ");
        }
        System.out.println();
        System.out.println();
    }

    private static void revealGuessedLetters(char letter) {
        for (int index = 0; index < hiddenWord.size(); index++) {
            if (hiddenWord.get(index) == letter) {
                displayWord.set(index, letter);
            }
        }
    }

    private static boolean isWin() {
        return !(displayWord.contains('*'));
    }

    private static boolean isLose() {
        return numOfErrors == 0;
    }

    private static boolean isGameOver() {
        return isWin() || isLose();
    }
}