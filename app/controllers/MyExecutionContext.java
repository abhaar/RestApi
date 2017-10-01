package controllers;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

public class MyExecutionContext extends CustomExecutionContext {

    @Inject
    public MyExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "my-context");
    }
}
