package test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;

public class MySenderVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        HttpClient client = vertx.createHttpClient(new HttpClientOptions().setDefaultHost("localhost").setDefaultPort(
                HTTPServerReproducer.PORT));

        vertx.setPeriodic(1000, l -> {
            client.get("/").handler(response -> {
               response.bodyHandler(buf -> {
                   System.out.println("Received: " + response.statusCode() + " - " + buf.toString());
               });
            }).end();
        });

    }



}
