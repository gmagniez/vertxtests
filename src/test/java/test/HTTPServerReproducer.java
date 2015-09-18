package test;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class HTTPServerReproducer {

    public static final int PORT = 8090;

    public static final int SERVER_INSTANCES = 1;

    public static final int CLIENT_INSTANCES = 1;

    public static final int EVENT_LOOPS_SIZE = VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE;

    public static void main(String[] args) {
        DeploymentOptions options = new DeploymentOptions()
                .setInstances(SERVER_INSTANCES);
        DeploymentOptions options2 = new DeploymentOptions().setInstances(CLIENT_INSTANCES);

        VertxOptions vertxOptions  = new VertxOptions().setEventLoopPoolSize(EVENT_LOOPS_SIZE);

        Vertx vertx = Vertx.vertx(vertxOptions);

        vertx.deployVerticle(MyReceiverVerticle.class.getCanonicalName(), options, ar -> {

            if (ar.succeeded()) {
                vertx.deployVerticle(MySenderVerticle.class.getCanonicalName(), options2, ar2 -> {
                    vertx.setTimer(2000, l -> {
                        vertx.deployVerticle(MyReceiverVerticle.class.getCanonicalName(), options, ar3 -> {
                            vertx.setTimer(5000, l2 -> {
                                vertx.undeploy(ar.result(), ar4 -> {
                                    if(ar4.succeeded()) {
                                        System.out.println("First instances undeployed");
                                    }
                                    else {
                                        ar4.cause().printStackTrace();
                                    }

                                });
                            });

                        });
                    });

                });

            } else {
                ar.cause().printStackTrace();
            }
        });
    }

}
