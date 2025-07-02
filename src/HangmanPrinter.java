public class HangmanPrinter {
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

    public static void drawHangman(int maxMistakes, int numOfErrors) {
        int numOfDraw = (maxMistakes - numOfErrors) - 1;
        if (numOfDraw >= 0) {
            for (String state : hangmanPictures[numOfDraw]) {
                System.out.println(state);
            }
        }
    }
}
