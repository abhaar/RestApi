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

    /**
     *
     * @param myExecutionContext Play is asynchronous from top to bottom. However, reading a file line by line
     *                           is a blocking operation. Therefore, we do not want to do that operation in the
     *                           main context of the application. Instead we create a custom MyExecutionContext
     *                           and pass the reading of file to that context. The number of threads and parallelism
     *                           for that context can be configured in application.conf
     *                           This allows us to keep the number of threads to a minimum (since threads themselves
     *                           have an overhead).
     * @param fileService The service that is responsible for fetching line from the given file.
     */
    @Inject
    public Application(MyExecutionContext myExecutionContext, FileService fileService) {
        this.myExecutionContext = myExecutionContext;
        this.fileService = fileService;
    }

    // Welcome page
    public Result index() {
        return ok(index.render("Welcome!"));
    }

    public CompletionStage<Result> line(long index) {
        return getLine(index);
    }

    /**
     *
     * @param index The line number to get
     * @return HTTP 200 with the line if found, otherwise HTTP 413
     */
    private CompletionStage<Result> getLine(long index) {
        Executor myEc = HttpExecution.fromThread((Executor) myExecutionContext);
        return CompletableFuture.supplyAsync(() -> fileService.getLine(index), myEc)
                .thenApplyAsync(i -> i.map(Results::ok)
                                    .orElseGet(() -> status(413, "index out of bounds")), myEc);

    }
}
