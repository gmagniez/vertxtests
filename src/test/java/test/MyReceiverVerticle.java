package test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;

public class MyReceiverVerticle extends AbstractVerticle {

    public static int ID = 0;

    private int id = ID++;

    private HttpServer server;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        HttpServerOptions options = new HttpServerOptions().setPort(HTTPServerReproducer.PORT);
        server = vertx.createHttpServer(options).requestHandler(request -> {
            System.out.println("Received request " + id);
            request.response().end("HI FROM : " + id);
        }).listen(ar -> {
            if (ar.succeeded()) {
                System.out.println("LISTENING " + id);
                startFuture.complete();
            } else {
                startFuture.fail(ar.cause());
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        System.out.println("STOPPING " + id);
        server.close(ar -> {
            if (ar.succeeded()) {
                System.out.println("CLOSED: " + id);
                stopFuture.complete();
            } else {
                stopFuture.fail(ar.cause());
            }
        });
    }

}
