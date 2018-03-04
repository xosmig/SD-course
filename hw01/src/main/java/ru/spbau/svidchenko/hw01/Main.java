package ru.spbau.svidchenko.hw01;

import ru.spbau.svidchenko.hw01.common.IOUtils;
import ru.spbau.svidchenko.hw01.common.ThreadingService;
import ru.spbau.svidchenko.hw01.exceptions.CliException;
import ru.spbau.svidchenko.hw01.parser.Parser;
import ru.spbau.svidchenko.hw01.parser.ParserImpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Simple CLI
 * @author ArgentumWalker
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Parser parser = new ParserImpl();
        while (true) {
            try {
                String line = scanner.nextLine();
                if (parser.parseStatement(line).execute(System.in, System.out)) {
                    ThreadingService.stop();
                    break;
                }
            } catch (CliException e) {
                System.err.println(e.getMessage());
            } catch (NoSuchElementException e) {
                scanner = new Scanner(System.in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
