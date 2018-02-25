package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
//import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.io.Serializable;
import java.util.LinkedList;


public class Game implements Serializable {
    protected static TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 85;
    public static final int HEIGHT = 50;
    private static final Font GENERIC = new Font("Times New Roman", Font.PLAIN, 30);
    protected static TETile[][] world;
    protected static Map map;
    protected static Input player;
    protected static LinkedList<Character> a = new LinkedList<>();

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        // initialize main menu
        Menu menu = new Menu(WIDTH, HEIGHT);
        menu.run();

        // initialize the map from the given seed
        ter.initialize(WIDTH, HEIGHT, 0, 0);
        ter.renderFrame(world);

        // add interactive stuff

        // run the player
        player.run();

        // continually move the world, add win condition later
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        String actions = readInput(input);
        ter.initialize(WIDTH, HEIGHT, 0, -1);
        if (!actions.equals("")) {
            Map test = new Map(Long.parseLong(actions), WIDTH, HEIGHT);
            test.generate();
            world = test.getWorld();
            player = new Input();
            ter.renderFrame(world);
            player.run(actions);
        } else {
            ter.renderFrame(world);
            player.run(actions);
        }
        return world;
    }

    // temporary for phase 1

    /**
     * have to handle inputs other than n # s
     * also cannot move after loading data
     * fix edge cases of not getting last letter
     */
    private String readInput(String input) {
        int l = input.length();
        String actions = "";
        for (int i = 0; i < l - 1; i++) {
            if (input.substring(0, 1).equalsIgnoreCase("n")) {
                if (Character.isDigit(input.charAt(i))) {
                    actions += input.charAt(i);
                }
                if (input.substring(i, i + 1).equalsIgnoreCase("s")) {
                    for (int j = i; j < l; j++) {
                        a.addLast(input.charAt(j));
                    }
                    break;
                }
            } else if (input.substring(0, 1).equalsIgnoreCase("l")) {
                Game.ter = Data.load("proj2/byog/SaveFiles/ter");
                Game.map = Data.load("proj2/byog/SaveFiles/map");
                Game.world = Data.load("proj2/byog/SaveFiles/world");
                Game.player = Data.load("proj2/byog/SaveFiles/input");
                a.addLast(input.charAt(i));
            } else {
                System.exit(0);
            }
        }
        return actions;
    }
}
