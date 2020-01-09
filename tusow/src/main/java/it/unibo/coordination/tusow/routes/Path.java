package it.unibo.coordination.tusow.routes;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import it.unibo.coordination.tusow.exceptions.BadContentError;
import it.unibo.coordination.tusow.exceptions.HttpError;
import it.unibo.presentation.MIMETypes;
import it.unibo.presentation.Presentation;
import it.unibo.presentation.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class Path {

    private static final Logger LOGGER = LoggerFactory.getLogger(Path.class);

    private String path;
    private Router router;
    private String parentPath = "";

    public Path(String subPath) {
        this.path = Objects.requireNonNull(subPath);

    }

    protected abstract void setupRoutes();

    protected Route addRoute(HttpMethod method, String path, Handler<RoutingContext> handler) {
        LOGGER.info("Add route: {} {}", method, getPath() + path);
        return router.route(method, getPath() + path)
//	        .handler(LoggerHandler.create())
                .handler(this::log)
                .handler(BodyHandler.create())
                .handler(handler)
                .handler(ErrorHandler.create());
    }

    private void log(RoutingContext routingContext) {
        final HttpServerRequest req = routingContext.request();
        LOGGER.info("{} {}", req.method(), req.absoluteURI());
        routingContext.next();
    }

    protected Route addRoute(HttpMethod method, Handler<RoutingContext> handler) {
        return addRoute(method, "", handler);
    }

    protected String getPath() {
        return parentPath + path;
    }

    protected String getSubPath(String subResource) {
        return getPath() + "/" + subResource;
    }

    protected <X> Handler<X> successfulResponseHandler(RoutingContext routingContext, Function<MIMETypes, Serializer<X>> marshaller) {
        return successfulResponseHandler(routingContext, marshaller, Function.identity());
    }

    protected <X> Handler<AsyncResult<X>> responseHandler(RoutingContext routingContext, Function<MIMETypes, Serializer<X>> marshaller) {
        return responseHandler(routingContext, marshaller, Function.identity());
    }

    protected <X> Handler<Collection<? extends X>> successfulResponseHandlerWithManyContents(RoutingContext routingContext, Function<MIMETypes, Serializer<X>> marshaller) {
        return successfulResponseHandlerWithManyContents(routingContext, marshaller, Function.identity());
    }

    protected <X> Handler<AsyncResult<Collection<? extends X>>> responseHandlerWithManyContents(RoutingContext routingContext, Function<MIMETypes, Serializer<X>> marshaller) {
        return responseHandlerWithManyContents(routingContext, marshaller, Function.identity());
    }

    protected <X> Handler<X> successfulResponseHandler(RoutingContext routingContext, Function<MIMETypes, Serializer<X>> marshaller, Function<X, X> cleaner) {
        return x -> handleContent(routingContext, marshaller, cleaner, x);
    }

    protected <X> Handler<AsyncResult<X>> responseHandler(RoutingContext routingContext, Function<MIMETypes, Serializer<X>> marshaller, Function<X, X> cleaner) {
        return x -> {
            if (!handleException(routingContext, x)) {
                handleContent(routingContext, marshaller, cleaner, x.result());
            }
        };
    }

    protected <X> void handleContent(RoutingContext routingContext, Function<MIMETypes, Serializer<X>> marshaller, Function<X, X> cleaner, X content) {
        try {
            final X cleanResult = cleaner.apply(content);
            final MIMETypes mimeType = MIMETypes.parse(routingContext.getAcceptableContentType());
            final String result;

            if (cleanResult instanceof Collection) {
                throw new UnsupportedOperationException();
            }

            result = marshaller.apply(mimeType).toString(cleanResult);

            routingContext.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE, mimeType.toString())
                    .setStatusCode(200)
                    .end(result);
        } catch (Throwable e) {
            handleException(routingContext, e);
        }
    }

    private <X> boolean handleException(RoutingContext routingContext, AsyncResult<X> x) {
        if (x.failed()) {
            handleException(routingContext, x.cause());
            return true;
        }
        return false;
    }

    private void handleException(RoutingContext routingContext, Throwable e) {
        LOGGER.warn(e.getMessage(), e);
        if (e instanceof HttpError) {
            final HttpError exception = (HttpError) e;
            routingContext.response()
                    .setStatusCode(exception.getStatusCode())
                    .end(exception.getMessage());
        } else {
            routingContext.response()
                    .setStatusCode(500)
                    .end("Internal Server Error");

        }
    }

    private <X> void handleManyContents(RoutingContext routingContext, Function<MIMETypes, Serializer<X>> marshaller, Function<Collection<? extends X>, Collection<? extends X>> cleaner, Collection<? extends X> contents) {
        try {
            final Collection<? extends X> cleanResult = cleaner.apply(contents);
            final MIMETypes mimeType = MIMETypes.parse(routingContext.getAcceptableContentType());
            final String result;

            result = marshaller.apply(mimeType).toString(cleanResult);

            routingContext.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE, mimeType.toString())
                    .setStatusCode(cleanResult.isEmpty() ? 204 : 200)
                    .end(result);
        } catch (Throwable e) {
            handleException(routingContext, e);
        }
    }

    protected <X> Handler<Collection<? extends X>> successfulResponseHandlerWithManyContents(RoutingContext routingContext, Function<MIMETypes, Serializer<X>> marshaller, Function<Collection<? extends X>, Collection<? extends X>> cleaner) {
        return x -> handleManyContents(routingContext, marshaller, cleaner, x);
    }

    protected <X> Handler<AsyncResult<Collection<? extends X>>> responseHandlerWithManyContents(RoutingContext routingContext, Function<MIMETypes, Serializer<X>> marshaller, Function<Collection<? extends X>, Collection<? extends X>> cleaner) {
        return x -> {
            if (!handleException(routingContext, x)) {
                handleManyContents(routingContext, marshaller, cleaner, x.result());
            }
        };
    }

    protected <X> Handler<X> responseHandlerWithNoContent(RoutingContext routingContext) {
        return x -> routingContext.response()
                .setStatusCode(204)
                .end();
    }

    protected Handler<Throwable> responseHandlerFallback(RoutingContext routingContext) {
        return t -> handleException(routingContext, t);
    }

    protected <X extends Number> void handleNumericContent(RoutingContext routingContext, String header, X content) {
        try {
            routingContext.response()
                    .putHeader(header, content.toString())
                    .setStatusCode(200)
                    .end();
        } catch (Throwable e) {
            handleException(routingContext, e);
        }
    }

    protected <X extends Number> Handler<AsyncResult<X>> responseHandlerWithNumericContent(RoutingContext routingContext, String header) {
        return x -> {
            if (!handleException(routingContext, x)) {
				handleNumericContent(routingContext, header, x.result());
			}
        };
    }

    protected <X extends Number> Handler<X> successfulResponseHandlerWithNumericContent(RoutingContext routingContext, String header) {
        return x -> handleNumericContent(routingContext, header, x);
    }

    protected Presentation getPresentation() {
        return Presentation.Companion.getDefault();
    }

    public void attach(Router router) {
        this.router = Objects.requireNonNull(router);
        setupRoutes();
    }

    public Path append(Path route) {
        route.parentPath = path;
        route.attach(router);
        return route;
    }

    public Path append(String subPath, Path route) {
        route.parentPath = parentPath + path + subPath;
        route.attach(router);
        return route;
    }

    protected static void requireAllAreNull(Object x, Object... xs) {
        if (Stream.concat(Stream.of(x), Stream.of(xs)).anyMatch(Objects::nonNull)) {
            throw new BadContentError();
        }
    }

    protected static void requireAllAreNullOrEmpty(Collection<?> x, Collection<?>... xs) {
        if (Stream.concat(Stream.of(x), Stream.of(xs))
                .filter(Objects::nonNull).anyMatch(it -> it.size() > 0)) {
            throw new BadContentError();
        }
    }

    protected static void requireNoneIsNull(Object x, Object... xs) {
        if (Stream.concat(Stream.of(x), Stream.of(xs)).anyMatch(Objects::isNull)) {
            throw new BadContentError();
        }
    }

    protected static void requireSomeIsNonNull(Object x, Object... xs) {
        if (Stream.concat(Stream.of(x), Stream.of(xs)).noneMatch(Objects::nonNull)) {
            throw new BadContentError();
        }
    }


}
