package com.example.Payment_System.Compliance;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ComplianceClient {

    private final ComplianceServiceGrpc.ComplianceServiceBlockingStub stub;

    public ComplianceClient() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        this.stub = ComplianceServiceGrpc.newBlockingStub(channel);
    }

    public boolean isApproved(String accountNumber) {

        CheckRequest request = CheckRequest.newBuilder()
                .setAccountNumber(accountNumber)
                .build();

        CheckResponse response = stub.check(request);

        return response.getApproved();
    }
}

