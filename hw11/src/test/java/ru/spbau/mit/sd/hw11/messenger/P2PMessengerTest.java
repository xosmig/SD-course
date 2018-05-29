package ru.spbau.mit.sd.hw11.messenger;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertArrayEquals;


public class P2PMessengerTest {
    private int FIRST_PORT_NO = 4567;
    private int SECOND_PORT_NO = 4568;

    @Test
    public void send() throws Exception {
        StringBuilder longMessage = new StringBuilder("K");
        for (int i = 0; i < 10000; i++) {
            longMessage.append("ek");
        }
        List<String> firstMessages = Arrays.asList("Rofl", "MegaRofl", "", "R");
        List<String> secondMessages = Arrays.asList("Kek", "Kekek", longMessage.toString(), "");
        TestMessengerService firstService = new TestMessengerService();
        TestMessengerService secondService = new TestMessengerService();
        Thread firstThread = new Thread(
                () -> sendAll("Rofler", firstMessages, firstService, FIRST_PORT_NO, SECOND_PORT_NO)
        );
        Thread secondThread = new Thread(
                () -> sendAll("Keker", secondMessages, secondService, SECOND_PORT_NO, FIRST_PORT_NO)
        );
        firstThread.start();
        secondThread.start();
        firstThread.join(TimeUnit.SECONDS.toMillis(15));
        secondThread.join(TimeUnit.SECONDS.toMillis(15));
        assertArrayEquals(firstMessages.toArray(), secondService.receivedMessages.toArray());
        assertArrayEquals(secondMessages.toArray(), firstService.receivedMessages.toArray());
    }

    private void sendAll(
            String nick,
            List<String> messages,
            MessengerService service,
            int port,
            int targetPort
    ) {
        try {
            P2PMessenger messenger = new P2PMessenger(targetPort, "localhost", port, nick, service);
            for (String message : messages) {
                messenger.send(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class TestMessengerService extends MessengerService {
        public final List<String> receivedMessages = new ArrayList<>();

        @Override
        public void receiveMessage(long timestampMillis, String sender, String message) {
            receivedMessages.add(message);
        }
    }
}