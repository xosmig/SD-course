package ru.spbau.mit.sd.hw11.messenger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.spbau.mit.sd.hw11.Logging;
import ru.spbau.mit.sd.hw11.Messenger;
import ru.spbau.mit.sd.hw11.MessengerServiceGrpc;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Logger;

public class P2PMessenger implements Closeable {
    private final static Logger LOGGER = Logging.logger();

    private final String nickname;
    private final Server server;
    private final ManagedChannel channel;
    private final MessengerServiceGrpc.MessengerServiceBlockingStub stub;

    public P2PMessenger(
            int receiverPort,
            String receiverHost,
            int senderPort,
            String nickname,
            MessengerService service
    ) throws IOException {
        this.nickname = nickname;

        server = ServerBuilder.forPort(senderPort).addService(service).build().start();
        channel = ManagedChannelBuilder.forAddress(receiverHost, receiverPort).usePlaintext(true).build();
        stub = MessengerServiceGrpc.newBlockingStub(channel);
    }

    public void send(String message) {
        LOGGER.info("Sending message");
        stub.send(Messenger.Message.newBuilder().setSender(nickname).setMessage(message).build());
    }

    @Override
    public void close() throws IOException {
        server.shutdown();
        channel.shutdown();
    }
}
