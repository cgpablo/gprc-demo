package com.github.cgpablo.grpc.calculator.server;

import com.github.cgpablo.grpc.calculator.service.CalculatorServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CalculatorServer {

    private static final int PORT = 50052;
    private Server server;

    public void start() throws IOException {
        server = ServerBuilder.forPort(PORT)
                .addService(new CalculatorServiceImpl())
                .build()
                .start();
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server == null) {
            return;
        }
        server.awaitTermination();
    }

    public static void main(String[] args)
            throws InterruptedException, IOException {
        CalculatorServer server = new CalculatorServer();
        server.start();
        server.blockUntilShutdown();
    }
}
