package com.example.profanity.service;

import com.example.grpc.ProfanityCheckRequest;
import com.example.grpc.ProfanityCheckResponse;
import com.example.grpc.ProfanityServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class ProfanityGrpcService extends ProfanityServiceGrpc.ProfanityServiceImplBase {

    private final ProfanityCheckService profanityCheckService;

    @Override
    public void checkProfanity(ProfanityCheckRequest request, StreamObserver<ProfanityCheckResponse> responseObserver) {
        boolean isProfane = profanityCheckService.isProfane(request.getText());
        
        ProfanityCheckResponse response = ProfanityCheckResponse.newBuilder()
                .setIsProfane(isProfane)
                .setReason(isProfane ? "Contains profanity" : "Clean")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
