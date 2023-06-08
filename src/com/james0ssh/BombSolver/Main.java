package com.james0ssh.BombSolver;
import java.util.*;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        Bomb bomb = new Bomb();
        bomb.addModule(new Keypad());
        for (Module m : bomb.modules) {
            System.out.println(m.solve(bomb));
        }
    }
}

final class Bomb {
    byte strikes = 0;
    byte batteryCount = 0;
    boolean hasBatteries = false;
    boolean bombParity;
    boolean hasVowel;
    List<String> labels = new ArrayList<>();
    List<Module> modules = new ArrayList<>();

    public Bomb() {
        String input;
        System.out.print("Does it have Batteries? N|Y\n___:");
        Scanner scan = new Scanner(System.in);
        do {
            input = scan.nextLine();
        } while (input != null && (!input.equalsIgnoreCase("N") && !input.equalsIgnoreCase("Y")));
        assert input != null;
        if (input.equalsIgnoreCase("Y")) {
            hasBatteries = true;
            boolean isNumber = false;
            do {
                System.out.print("How many batteries does it have?\n____:");
                input = scan.nextLine();
                try {
                    Byte.parseByte(input);
                    isNumber = true;
                } catch (NumberFormatException e){
                    System.out.println("Not a number");
                }
            } while (!isNumber);
            batteryCount = (byte) Short.parseShort(input);
        }
        clearScreen();
        System.out.println("Does the Bomb have any Labels N|Y\n___:");
        do {
            input = scan.nextLine();
        } while (input != null && (!input.equalsIgnoreCase("N") && !input.equalsIgnoreCase("Y")));
        assert input != null;
        if (input.equalsIgnoreCase("Y")) {
            System.out.println("Enter Labels \n\t0 to exit");
            String s;
            do {
                do {
                    s = scan.nextLine();
                } while (s.length() != 3 && !s.equals("0"));
                labels.add(s.toUpperCase());
            } while (!s.equals("0"));
            Predicate<? super String> f = str -> str.equals("0");
            labels.removeIf(f);
        }
        clearScreen();
        System.out.print("What is the Bomb's Serial Number\n___:");
        input = scan.nextLine();
        hasVowel = input.matches(".*[aeiouAEIOU].*");
        char[] c = input.toCharArray();
        Byte s = Byte.parseByte(String.valueOf(c[input.length() - 1]));
        c = null;
        bombParity = s % 2 == 1;
        s = null;
        input = null;
        scan = null;
        System.gc();
    }

    public void addModule(Module mod) {
        modules.add(mod);
    }

    private void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Handle exceptions
        }
    }

}

abstract class Module {
    boolean diffused;

    public Module() {
        this.diffused = false;
    }

    abstract String solve(Bomb bomb);

}

final class Key {
    public Keys keyvalue;
    enum Keys {
        COPYRIGHT,
        FILLEDSTAR,
        HOLLOWSTAR,
        SMILEYFACE,
        DOUBLEK,
        OMEGA,
        SQUIDKNIFE,
        PUMPKIN,
        HOOKN,
        TEEPEE,
        SIX,
        SQUIGGLYN,
        AT,
        AE,
        MELTEDTHREE,
        EURO,
        CIRCLE,
        NWITHHAT,
        DRAGON,
        QUESTIONMARK,
        PARAGRAPH,
        RIGHTC,
        LEFTC,
        PITCHFORK,
        TRIPOD,
        CURSIVE,
        TRACKS,
        BALLOON,
        WEIRDNOSE,
        UPSIDEDOWNY,
        BT
    }
    char[] symbols = new char[] {
            '©',
            '★',
            '☆',
            'ټ',
            'Җ',
            'Ω',
            'Ѭ',
            'Ѽ',
            'ϗ',
            '\0',
            'б',
            'Ϟ',
            'Ѧ',
            'æ',
            'Ԇ',
            'Ӭ',
            '\0',
            'Ҋ',
            'Ѯ',
            '¿',
            '¶',
            'Ͼ',
            'Ͽ',
            'Ψ',
            '\0',
            'Ҩ',
            '҂',
            'Ϙ',
            '\0',
            'ƛ',
            'Ѣ'
    };
    HashMap<Keys, Character> symbolMap = new HashMap<>();

    public Key() {
        for(byte i = 0; i < symbols.length; i++) symbolMap.put(Keys.values()[i], symbols[i]);
    }
}

enum wireColour {
    RED,
    WHITE,
    BLACK,
    YELLOW,
    BLUE
}

final class SimpleWire {
    public wireColour wc;

    /*public SimpleWire() {
        System.out.println("What colour wire");
        String input = new Scanner(System.in).nextLine();
        assert input != null;
        wc = wireColour.valueOf(input.toUpperCase());
    }*/

    public SimpleWire(wireColour wireColour) {
        this.wc = wireColour;
    }
}

final class ComplexWire{
    wireColour wc0;
    wireColour wc1;
    boolean hasSymbol;
    boolean hasLED;
}

final class SimpleWires extends Module {
    private final SimpleWire[] wires = new SimpleWire[6];
    private String drawWireModule() {
        StringBuilder module = new StringBuilder("[------------------]\n");
        for(byte i = 0; i <= 5; i++) {
            StringBuilder wire = new StringBuilder();
            wire.append(String.format("| %d. ", i + 1));
            if (wires[i] != null) {
                wire.append(wires[i].wc.name());
            } else {
                wire.append("no wire");
            }
            wire.append("~".repeat(Math.max(0, 16 - wire.length() + 3))).append("|\n");
            module.append(wire);
        }
        module.append("[------------------]");
        return module.toString();
    }

    @Override
    String solve(Bomb bomb) {
        byte wireCount = 0;
        List<wireColour> currentWires = new ArrayList<>();
        for (SimpleWire sw : wires){
            wireCount += (sw != null) ? 1 : 0;
            if (sw != null) currentWires.add(sw.wc);
        }
        this.diffused = true;
        switch (wireCount) {
            case 3 -> {
                if (currentWires.stream().noneMatch(wc -> wc == wireColour.RED)) return "cut second";
                else if (currentWires.lastIndexOf(wireColour.WHITE) == 2) return "cut last";
                else if (currentWires.stream().filter(e -> e == wireColour.BLUE).count() > 1) return "cut last blue";
                else return "cut last";
            }
            case 4 -> {
                if (currentWires.stream().filter(wc -> wc == wireColour.RED).count() > 1 && !bomb.bombParity) return "cut last red";
                else if (currentWires.get(3) == wireColour.YELLOW && currentWires.stream().noneMatch(wc -> wc == wireColour.RED)) return "cut first";
                else if (currentWires.stream().filter(wc -> wc == wireColour.BLUE).count() == 1) return "cut first";
                else if (currentWires.stream().filter(wc -> wc == wireColour.YELLOW).count() > 1) return "cut last";
                else return "cut second";
            }
            case 5 -> {
                if (currentWires.get(4) == wireColour.BLACK && !bomb.bombParity) return "cut fourth";
                else if (currentWires.stream().filter(wc -> wc == wireColour.RED).count() == 1 && currentWires.stream().filter(wc -> wc == wireColour.YELLOW).count() > 2) return "cut first";
                else if (currentWires.stream().noneMatch(wc -> wc == wireColour.BLACK)) return "cut second";
                else return "cut first";
            }
            case 6 -> {
                if (currentWires.stream().noneMatch(wc -> wc == wireColour.YELLOW) && !bomb.bombParity) return "cut third";
                else if (currentWires.stream().filter(wc -> wc == wireColour.YELLOW).count() == 1 && currentWires.stream().filter(wc -> wc == wireColour.WHITE).count() > 1) return "cut fourth";
                else if (currentWires.stream().noneMatch(wc -> wc == wireColour.RED)) return "cut last";
                else return "cut fourth";
            }
            default -> {
                this.diffused = false;
                return "Something went wrong";
            }
        }
    }
    public SimpleWires() {
        System.out.println(drawWireModule());
        System.out.println("Which rows have wires attached 'Comma Separated'");
        String input = new Scanner(System.in).nextLine();
        String[] values = input.split(",");
        for (byte i = 0; i < values.length; i++){values[i] = values[i].trim();}
        Byte[] B_rows = Arrays.stream(values).map(Byte::parseByte).toArray(Byte[]::new);
        byte[] rows = new byte[B_rows.length];
        for(byte i = 0; i < B_rows.length; i++){rows[i] = B_rows[i];}
        B_rows = null;
        System.out.println("What are the colours of the wires? 'Comma Separated'");
        input = new Scanner(System.in).nextLine();
        String[] rowsColour = input.split(",");
        for(byte i = 0; i < rowsColour.length; i++){rowsColour[i] = rowsColour[i].trim().toUpperCase();}
        input = null;
        for(byte i = 0; i < rows.length; i++) {
            wires[rows[i] - 1] = new SimpleWire(wireColour.valueOf(rowsColour[i]));
        }
        System.out.println(drawWireModule());
    }
}

final class Button extends Module {
    enum buttonText {
        ABORT,
        DETONATE,
        HOLD,
        PRESS
    }
    enum buttonColour {
        BLUE,
        RED,
        WHITE,
        YELLOW,
        BLACK
    }
    private final buttonColour _buttonColour;
    private final buttonText _buttonText;
    Button() {
        System.out.println("What colour is the button?\n___:");
        _buttonColour = buttonColour.valueOf(new Scanner(System.in).nextLine().toUpperCase());
        System.out.println("What is the button text");
        _buttonText = buttonText.valueOf(new Scanner(System.in).nextLine().toUpperCase());
    }

    @Override
    String solve(Bomb bomb) {
        if ((_buttonColour.equals(buttonColour.RED) && _buttonText.equals(buttonText.HOLD)) || (bomb.batteryCount > 2 && _buttonText.equals(buttonText.DETONATE)) || (bomb.labels.stream().anyMatch(l -> Objects.equals(l, "FRK")) && bomb.batteryCount > 3)) return "Hold and quickly release";
        else return "Hold it.\n\tBlue 4\n\tYellow 5\n\telse 1";
    }
}

final class Keypad extends Module {
    Key[] keys = new Key[4];
    Key keyref = new Key();
    Key.Keys[][] solveColumns = new Key.Keys[][] {{Key.Keys.BALLOON, Key.Keys.AT, Key.Keys.UPSIDEDOWNY, Key.Keys.SQUIGGLYN, Key.Keys.SQUIDKNIFE, Key.Keys.LEFTC},
                                                    {Key.Keys.EURO, Key.Keys.BALLOON, Key.Keys.LEFTC, Key.Keys.CURSIVE, Key.Keys.HOLLOWSTAR, Key.Keys.HOOKN, Key.Keys.QUESTIONMARK},
                                                      {Key.Keys.COPYRIGHT, Key.Keys.PUMPKIN, Key.Keys.CURSIVE, Key.Keys.DOUBLEK, Key.Keys.MELTEDTHREE, Key.Keys.UPSIDEDOWNY, Key.Keys.HOLLOWSTAR},
                                                        {Key.Keys.SIX, Key.Keys.PARAGRAPH, Key.Keys.BT, Key.Keys.SQUIDKNIFE, Key.Keys.DOUBLEK, Key.Keys.QUESTIONMARK, Key.Keys.SMILEYFACE},
                                                          {Key.Keys.PITCHFORK, Key.Keys.SMILEYFACE, Key.Keys.BT, Key.Keys.RIGHTC, Key.Keys.PARAGRAPH, Key.Keys.DRAGON, Key.Keys.FILLEDSTAR},
                                                            {Key.Keys.SIX, Key.Keys.EURO, Key.Keys.TRACKS, Key.Keys.AE, Key.Keys.PITCHFORK, Key.Keys.NWITHHAT, Key.Keys.OMEGA}};

    Keypad() {
        for(HashMap.Entry<Key.Keys, Character> ent : keyref.symbolMap.entrySet()) System.out.println(ent.getKey().ordinal() + ". " + ent.getKey().name() + " [" + ent.getValue() + "]");
        System.out.print("Enter Symbols, (The number assoc, Comma sep)\n___:");
        String input = new Scanner(System.in).nextLine();
        String[] in = input.split(",");
        for (byte i = 0; i < 4; i++) {
            keys[i] = new Key();
            keys[i].keyvalue = Key.Keys.values()[Byte.parseByte(in[i])];
            keys[i].symbolMap = null;
            keys[i].symbols = null;
        }
    }
    //TODO: Implement
    @Override
    String solve(Bomb bomb) {
        return null;
    }
}