package com.aszender.inventory.grpc;

import com.aszender.contracts.inventory.v1.InventoryServiceGrpc;
import com.aszender.contracts.inventory.v1.ReserveStockRequest;
import com.aszender.contracts.inventory.v1.ReserveStockResponse;
import com.aszender.inventory.service.InventoryReservationOrchestrator;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;

@Component
public class InventoryGrpcService extends InventoryServiceGrpc.InventoryServiceImplBase {

    private final InventoryReservationOrchestrator orchestrator;

    public InventoryGrpcService(InventoryReservationOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @Override
    public void reserveStock(ReserveStockRequest request, StreamObserver<ReserveStockResponse> responseObserver) {
        try {
            if (request.getOrderId() <= 0) {
                responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("order_id must be > 0").asRuntimeException());
                return;
            }
            if (request.getItemsCount() <= 0) {
                responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("items must not be empty").asRuntimeException());
                return;
            }

            InventoryReservationOrchestrator.ReserveResult result = orchestrator.reserve(
                    request.getOrderId(),
                    request.getItemsList().stream()
                            .map(i -> new InventoryReservationOrchestrator.ReserveLine(i.getProductId(), i.getQuantity()))
                            .toList()
            );

            ReserveStockResponse response = ReserveStockResponse.newBuilder()
                    .setReserved(result.reserved())
                    .setReason(result.reason() == null ? "" : result.reason())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException ex) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asRuntimeException());
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription("reserveStock failed").withCause(ex).asRuntimeException());
        }
    }
}
