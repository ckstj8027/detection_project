package com.example.admin.service;

import com.example.grpc.ProfanityCheckRequest;
import com.example.grpc.ProfanityCheckResponse;
import com.example.grpc.ProfanityServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class ProfanityClientService {

    @GrpcClient("profanity-service")
    private ProfanityServiceGrpc.ProfanityServiceBlockingStub profanityServiceStub;

    public boolean checkProfanity(Long postId, String text) {
        ProfanityCheckRequest request = ProfanityCheckRequest.newBuilder()
                .setPostId(postId)
                .setText(text)
                .build();

        ProfanityCheckResponse response = profanityServiceStub.checkProfanity(request);
        return response.getIsProfane();
    }
}
