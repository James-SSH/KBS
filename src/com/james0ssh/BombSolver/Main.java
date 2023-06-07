package com.james0ssh.BombSolver;
import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        Bomb bomb = new Bomb();
        bomb.addModule(new SimpleWires());
    }
}

final class Bomb {
    byte strikes = 0;
    byte batteryCount = 0;
    boolean hasBatteries = false;
    boolean hasEven;
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
            do {
                System.out.print("How many batteries does it have?\n____:");
                input = scan.nextLine();
            } while (Integer.parseInt(input) > 0);
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
        hasEven = s % 2 == 1;
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

    abstract String solve();

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

    public SimpleWire() {
        System.out.println("What colour wire");
        String input = new Scanner(System.in).nextLine();
        assert input != null;
        wc = wireColour.valueOf(input.toUpperCase());
    }

    public SimpleWire(wireColour wireColour) {
        this.wc = wireColour;
    }
}

final class ComplexWire{
    wireColour wc0;
    wireColour wc1;
}

final class SimpleWires extends Module {
    SimpleWire[] wires = new SimpleWire[6];

    private String drawWireModule() {
        StringBuilder module = new StringBuilder("[------------------]\n");
        for(byte i = 0; i <= 5; i++) {
            module.append(String.format("| %d. ", i + 1));
            if (wires[i].wc != null) {
                module.append(wires[i].wc.name());
            } else {
                module.append("no wire");
            }
            module.append("~".repeat(Math.max(0, 13 - module.length() + 3))).append("|\n");
        }
        module.append("[------------------]");
        return module.toString();
    }

    //TODO: Implement
    @Override
    String solve() {
        byte wireCount = 0;
        for (SimpleWire sw : wires){
            wireCount += (sw.wc != null) ? 1 : 0;
        }
        switch(wireCount) {
            case 3:
                break;
            default:
                break;
        }
        return null;
    }
    //TODO: Implement
    public SimpleWires() {

    }
}