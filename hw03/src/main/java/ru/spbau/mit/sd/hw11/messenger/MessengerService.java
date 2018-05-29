package ru.spbau.mit.sd.hw11.messenger;

import io.grpc.stub.StreamObserver;
import ru.spbau.mit.sd.hw11.Logging;
import ru.spbau.mit.sd.hw11.Messenger;
import ru.spbau.mit.sd.hw11.MessengerServiceGrpc;

import java.util.logging.Logger;

/**
 * Service for receiving and sending messages. Message contains nickname of sender and sanded text
 */
public abstract class MessengerService extends MessengerServiceGrpc.MessengerServiceImplBase{
    private final static Logger LOGGER = Logging.logger();

    /**
     * Main MessengerService method that receives (in this receiving implementation) message and gives it to receiveMessage method
     */
    @Override
    public final void send(Messenger.Message request, StreamObserver<Messenger.Void> responseObserver) {
        LOGGER.info("Receive message from " + request.getSender());
        responseObserver.onNext(Messenger.Void.getDefaultInstance());
        responseObserver.onCompleted();
        receiveMessage(System.currentTimeMillis(), request.getSender(), request.getMessage());
    }

    /**
     * Should deliver message to user
     */
    protected abstract void receiveMessage(long timestampMillis, String sender, String message);
}
