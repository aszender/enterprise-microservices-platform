package com.aszender.inventory.grpc;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.context.SmartLifecycle;

import java.io.IOException;

public class GrpcServerLifecycle implements SmartLifecycle {

    private final int port;
    private final BindableService service;

    private volatile Server server;
    private volatile boolean running;

    public GrpcServerLifecycle(int port, BindableService service) {
        this.port = port;
        this.service = service;
    }

    @Override
    public void start() {
        if (running) return;
        try {
            this.server = ServerBuilder.forPort(port)
                    .addService(service)
                    .build()
                    .start();
            this.running = true;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to start gRPC server on port " + port, e);
        }
    }

    @Override
    public void stop() {
        if (!running) return;
        if (server != null) {
            server.shutdown();
        }
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public int getPhase() {
        // Start relatively early.
        return Integer.MIN_VALUE;
    }
}
