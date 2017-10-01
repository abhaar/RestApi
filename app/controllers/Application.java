package controllers;

import play.Logger;
import play.libs.concurrent.HttpExecution;
import play.mvc.*;

import services.FileService;
import views.html.*;

import javax.inject.Inject;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

public class Application extends Controller {
    private MyExecutionContext myExecutionContext;
    private FileService fileService;

    @Inject
    public Application(MyExecutionContext myExecutionContext, FileService fileService) {
        this.myExecutionContext = myExecutionContext;
        this.fileService = fileService;
        Logger.info("cwd = " + Paths.get("").toAbsolutePath().toString());
    }

    public Result index() {
        return ok(index.render("Welcome!"));
    }

    public CompletionStage<Result> line(long index) {
        return getLine(index);
    }

    private CompletionStage<Result> getLine(long index) {
        Executor myEc = HttpExecution.fromThread((Executor) myExecutionContext);
        return CompletableFuture.supplyAsync(() -> fileService.getLine(index), myEc)
                .thenApplyAsync(i -> i.map(Results::ok)
                                    .orElseGet(() -> status(413, "index out of bounds")), myEc);

    }
}
