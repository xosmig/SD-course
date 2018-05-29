package ru.spbau.mit.sd.hw11.messenger;

import io.grpc.stub.StreamObserver;
import org.junit.Test;
import ru.spbau.mit.sd.hw11.Messenger;

import static org.junit.Assert.assertEquals;

public class MessengerServiceTest {
    @Test
    public void send() throws Exception {
        StreamObserver<Messenger.Void> emptyObserver = new StreamObserver<Messenger.Void>() {
            @Override
            public void onNext(Messenger.Void value) {}

            @Override
            public void onError(Throwable t) {}

            @Override
            public void onCompleted() {}
        };

        new TestMessengerService("test1", "test1")
                .send(Messenger.Message.newBuilder().setMessage("test1").setSender("test1").build(),
                        emptyObserver);
        new TestMessengerService("", "test1")
                .send(Messenger.Message.newBuilder().setMessage("test1").setSender("").build(),
                        emptyObserver);
        new TestMessengerService("test1", "")
                .send(Messenger.Message.newBuilder().setMessage("").setSender("test1").build(),
                        emptyObserver);
    }

    private class TestMessengerService extends MessengerService {
        private final String targetSender;
        private final String targetMessage;

        private TestMessengerService(String targetSender, String targetMessage) {
            this.targetSender = targetSender;
            this.targetMessage = targetMessage;
        }

        @Override
        public void receiveMessage(long timestampMillis, String sender, String message) {
            assertEquals(targetSender, sender);
            assertEquals(targetMessage, message);
        }
    }
}