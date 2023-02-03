package client.cli;

public class HangmanGraphics {
    public static String FOUR_ATTEMPTS = "--------------";
    public static String THREE_ATTEMPTS = "\t|----\n" + "\t|\n" + "\t|\n" + "\t|\n" + "\t|\n" + "\t|\n" + FOUR_ATTEMPTS;
    public static String TWO_ATTEMPTS = "\t|----|\n" + "\t|    0\n" + "\t|\n" + "\t|\n" + "\t|\n" + "\t|\n" + FOUR_ATTEMPTS;
    public static String ONE_ATTEMPTS = "\t|----|\n" + "\t|    0\n" + "\t|   /|\\\n" + "\t|\n" + "\t|\n" + "\t|\n" + FOUR_ATTEMPTS;
    public static String ZERO_ATTEMPTS = "\t|----|\n" + "\t|    0\n" + "\t|   /|\\\n" + "\t|   / \\\n" + "\t|\n" + "\t|\n" + FOUR_ATTEMPTS;
}
