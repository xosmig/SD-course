package ru.spbau.mit.sd.hw11;

import ru.spbau.mit.sd.hw11.messenger.MessengerService;
import ru.spbau.mit.sd.hw11.messenger.P2PMessenger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class of Console version of Messenger.
 * Reads from stdin, writes to stdout.
 * Closes by "\exit" command.
 * Arguments: nickname, port, recipient host, recipient port.
 */
public class ConsoleMessenger extends P2PMessenger {
    private final static Logger LOGGER = Logging.logger();

    public ConsoleMessenger(
            int receiverPort,
            String receiverHost,
            int senderPort,
            String nickname
    ) throws IOException {
        super(receiverPort, receiverHost, senderPort, nickname, ConsoleMessengerService.getInstance());
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: messenger <nickname> <port> <recipient host> <recipient port>");
        }
        try (ConsoleMessenger messenger = new ConsoleMessenger(Integer.valueOf(args[3]), args[2],
                Integer.valueOf(args[1]), args[0])) {
            String msg;
            Scanner inputScanner = new Scanner(System.in);
            while (inputScanner.hasNextLine()) {
                msg = inputScanner.nextLine();
                if (msg.equals("\\exit")) {
                    break;
                }
                messenger.send(msg);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Messenger not launched", e);
        }
    }

    private static class ConsoleMessengerService extends MessengerService {
        private static final SimpleDateFormat timeFormat = new SimpleDateFormat("<HH:mm:ss>");
        private static final ConsoleMessengerService INSTANCE = new ConsoleMessengerService();

        @Override
        public void receiveMessage(long timestampMillis, String sender, String message) {
            System.out.println(timeFormat.format(new Date(timestampMillis)) + " " + sender + ": " + message);
        }

        public static ConsoleMessengerService getInstance() {
            return INSTANCE;
        }
    }
}
