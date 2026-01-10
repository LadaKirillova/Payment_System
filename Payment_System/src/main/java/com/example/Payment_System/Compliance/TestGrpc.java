package com.example.Payment_System.Compliance;

public class TestGrpc {

    public static void main(String[] args) {
        ComplianceClient client = new ComplianceClient();
        boolean approved = client.isApproved("ACC-456789");
        System.out.println("Approved = " + approved);
    }
}