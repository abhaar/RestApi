package controllers;

import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;
import services.FileService;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class ApplicationTest {

    @Inject
    private MyExecutionContext myExecutionContext;
    private FileService fileService;

    private Application application;

    @Before
    public void setUp() {
        fileService = mock(FileService.class);
        application = new Application(myExecutionContext, fileService);
    }

    @Test
    public void testInvalidLineIndexes() throws ExecutionException, InterruptedException {
        when(fileService.getLine(any(Long.class))).thenReturn(Optional.empty());
        CompletionStage<Result> result = application.line(-1);
        assertEquals(REQUEST_ENTITY_TOO_LARGE, result.toCompletableFuture().get().status());
    }

    @Test
    public void testLine() throws ExecutionException, InterruptedException {
        when(fileService.getLine(any(Long.class))).thenReturn(Optional.of("dummy line"));
        CompletionStage<Result> result = application.line(1);
        assertEquals(OK, result.toCompletableFuture().get().status());
    }
}