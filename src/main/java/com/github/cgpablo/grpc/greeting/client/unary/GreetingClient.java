package com.github.cgpablo.grpc.greeting.client.unary;

import com.github.cgpablo.grpc.calculator.client.unary.CalculatorClient;
import com.proto.greet.*;
import io.grpc.*;

import java.util.concurrent.TimeUnit;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC client");
        GreetingClient greetingClient = new GreetingClient();
        greetingClient.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                // deactivate ssl
                .usePlaintext()
                .build();

        //doGreet(channel);
        doGreetWithDeadline(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();
    }

    private void doGreet(ManagedChannel channel) {
        System.out.println("Creating stub");
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        Greeting greeting = Greeting.newBuilder()
                .setFirstName("FirstName")
                .setLastName("LastName")
                .build();

        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        GreetResponse greetResponse = greetClient.greet(greetRequest);
        System.out.println(greetResponse.getResult());
    }

    private void doGreetWithDeadline(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub blockingStub = GreetServiceGrpc.newBlockingStub(channel);
        // First call (3000ms deadline)
        try {
            System.out.println("Sending a request with a deadline of 3000ms");
            GreetWithDeadlineResponse response = blockingStub.withDeadline(Deadline.after(3000, TimeUnit.MILLISECONDS))
                    .greetWithDeadline(GreetWithDeadlineRequest.newBuilder()
                            .setGreeting(Greeting.newBuilder()
                                    .setFirstName("FirstName")
                                    .getDefaultInstanceForType())
                            .build());
            System.out.println(response.getResult());
        } catch (StatusRuntimeException e) {
            if(e.getStatus() == Status.DEADLINE_EXCEEDED) {
                System.out.println("Deadline has been exceeded");
            } else {
                e.printStackTrace();
            }
        }
        // Second call (100ms deadline)
        try {
            System.out.println("Sending a request with a deadline of 100ms");
            GreetWithDeadlineResponse response = blockingStub.withDeadline(Deadline.after(100, TimeUnit.MILLISECONDS))
                    .greetWithDeadline(GreetWithDeadlineRequest.newBuilder()
                            .setGreeting(Greeting.newBuilder()
                                    .setFirstName("FirstName")
                                    .getDefaultInstanceForType())
                            .build());
            System.out.println(response.getResult());
        } catch (StatusRuntimeException e) {
            if(e.getStatus() == Status.DEADLINE_EXCEEDED) {
                System.out.println("Deadline has been exceeded");
            } else {
                e.printStackTrace();
            }
        }
    }
}
