package com.aszender.inventory.grpc;

import com.aszender.contracts.inventory.v1.InventoryServiceGrpc;
import com.aszender.contracts.inventory.v1.ReserveStockRequest;
import com.aszender.contracts.inventory.v1.ReserveStockResponse;
import com.aszender.inventory.service.InventoryReservationOrchestrator;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InventoryGrpcService extends InventoryServiceGrpc.InventoryServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(InventoryGrpcService.class);

    private final InventoryReservationOrchestrator orchestrator;

    public InventoryGrpcService(InventoryReservationOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @Override
    public void reserveStock(ReserveStockRequest request, StreamObserver<ReserveStockResponse> responseObserver) {
        log.info("gRPC reserveStock called: orderId={}, itemsCount={}", request.getOrderId(), request.getItemsCount());
        try {
            if (request.getOrderId() <= 0) {
                log.warn("Invalid orderId: {}", request.getOrderId());
                responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("order_id must be > 0").asRuntimeException());
                return;
            }
            if (request.getItemsCount() <= 0) {
                log.warn("Empty items list");
                responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("items must not be empty").asRuntimeException());
                return;
            }

            log.debug("Calling orchestrator.reserve for orderId={}", request.getOrderId());
            InventoryReservationOrchestrator.ReserveResult result = orchestrator.reserve(
                    request.getOrderId(),
                    request.getItemsList().stream()
                            .map(i -> new InventoryReservationOrchestrator.ReserveLine(i.getProductId(), i.getQuantity()))
                            .toList()
            );
            log.info("Reserve result for orderId={}: reserved={}, reason={}", request.getOrderId(), result.reserved(), result.reason());

            ReserveStockResponse response = ReserveStockResponse.newBuilder()
                    .setReserved(result.reserved())
                    .setReason(result.reason() == null ? "" : result.reason())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            log.debug("gRPC reserveStock completed successfully for orderId={}", request.getOrderId());
        } catch (IllegalArgumentException ex) {
            log.warn("IllegalArgumentException in reserveStock: {}", ex.getMessage());
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asRuntimeException());
        } catch (Exception ex) {
            log.error("Exception in reserveStock for orderId={}", request.getOrderId(), ex);
            responseObserver.onError(Status.INTERNAL.withDescription("reserveStock failed").withCause(ex).asRuntimeException());
        }
    }
}
