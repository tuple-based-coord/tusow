package it.unibo.coordination.tusow.routes;

import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import it.unibo.coordination.tusow.api.UsersApi;
import it.unibo.coordination.tusow.exceptions.BadContentError;
import it.unibo.coordination.tusow.exceptions.HttpError;
import it.unibo.coordination.tusow.presentation.Link;
import it.unibo.coordination.tusow.presentation.User;
import it.unibo.presentation.Deserializer;
import it.unibo.presentation.MIMETypes;
import it.unibo.presentation.Serializer;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static it.unibo.presentation.MIMETypes.*;

public class UsersPath extends Path {


    public UsersPath() {
        super("/users");
    }

	@Override
	protected void setupRoutes() {
        addRoute(HttpMethod.POST, this::post)
                .consumes(APPLICATION_JSON.toString())
                .consumes(APPLICATION_XML.toString())
                .consumes(APPLICATION_YAML.toString())
                .produces(APPLICATION_JSON.toString())
                .produces(APPLICATION_XML.toString())
                .produces(APPLICATION_YAML.toString().toString());

        addRoute(HttpMethod.GET, this::get)
                .produces(APPLICATION_JSON.toString())
                .produces(APPLICATION_XML.toString())
                .produces(APPLICATION_YAML.toString());

        addRoute(HttpMethod.GET, "/:identifier", this::getUser)
                .produces(APPLICATION_JSON.toString())
                .produces(APPLICATION_XML.toString())
                .produces(APPLICATION_YAML.toString());

        addRoute(HttpMethod.PUT, "/:identifier", this::putUser)
                .consumes(APPLICATION_JSON.toString())
                .consumes(APPLICATION_XML.toString())
                .consumes(APPLICATION_YAML.toString())
                .produces(APPLICATION_JSON.toString())
                .produces(APPLICATION_XML.toString())
                .produces(APPLICATION_YAML.toString());
	}

    private Serializer<User> getUsersMarshaller(MIMETypes mimeType) {
        return getPresentation().serializerOf(User.class, mimeType);
    }

    private Deserializer<User> getUsersUnmarshaller(MIMETypes mimeType) {
        return getPresentation().deserializerOf(User.class, mimeType);
    }

    private Serializer<Link> getLinkMarshaller(MIMETypes mimeType) {
        return getPresentation().serializerOf(Link.class, mimeType);
    }

    private Deserializer<Link> getLinkUnmarshaller(MIMETypes mimeType) {
        return getPresentation().deserializerOf(Link.class, mimeType);
    }

    private void post(RoutingContext routingContext) {
		final UsersApi api = UsersApi.get(routingContext);
        final Promise<Link> result = Promise.promise();
        final MIMETypes mimeType = MIMETypes.parse(routingContext.parsedHeaders().contentType().value());
        result.future().setHandler(responseHandler(routingContext, this::getLinkMarshaller));

		try {
			final User user = getUsersUnmarshaller(mimeType).fromString(routingContext.getBodyAsString());
            validateUserForPost(user);
			api.createUser(user, result);
		} catch(HttpError e) {
            result.fail(e);
        } catch (/*IOException | */IllegalArgumentException e) {
			result.fail(new BadContentError(e));
		}
	}

    private void validateUserForPost(User user) {
        requireNoneIsNull(user.getEmail(), user.getUsername(), user.getPassword());
        requireAllAreNull(user.getId(), user.getLink());

        user.setId(UUID.randomUUID());
        user.setLinkUrl(getSubPath(user.getUsername()));
    }

    private void get(RoutingContext routingContext) {
        final UsersApi api = UsersApi.get(routingContext);
        final Promise<Collection<? extends User>> result = Promise.promise();

        result.future().setHandler(responseHandlerWithManyContents(routingContext, this::getUsersMarshaller, x -> cleanUsers(x)));

        try {
            final Optional<Integer> skip = Optional.ofNullable(routingContext.queryParams().get("skip")).map(Integer::parseInt);
            final Optional<Integer> limit = Optional.ofNullable(routingContext.queryParams().get("limit")).map(Integer::parseInt);
            final Optional<String> filter = Optional.ofNullable(routingContext.queryParams().get("filter"));

            api.readAllUsers(skip.orElse(0), limit.orElse(10), filter.orElse(""), result);
        } catch(HttpError e) {
            result.fail(e);
        } catch (IllegalArgumentException e) {
            result.fail(new BadContentError(e));
        }
	}

    private Collection<? extends User> cleanUsers(Collection<? extends User> list) {
        return list.stream().map(this::cleanUser).collect(Collectors.toList());
    }


    private void getUser(RoutingContext routingContext) {
        final UsersApi api = UsersApi.get(routingContext);
        final Promise<User> result = Promise.promise();
        result.future().setHandler(responseHandler(routingContext, this::getUsersMarshaller, this::cleanUser));

        try {
            final String identifier = routingContext.pathParam("identifier");
            api.readUser(identifier, result);
        } catch(HttpError e) {
            result.fail(e);
        } catch (IllegalArgumentException e) {
            result.fail(new BadContentError(e));
        }
    }

    private void putUser(RoutingContext routingContext) {
        final UsersApi api = UsersApi.get(routingContext);
        final MIMETypes mimeType = MIMETypes.parse(routingContext.parsedHeaders().contentType().value());
        final Promise<User> result = Promise.promise();
        result.future().setHandler(responseHandler(routingContext, this::getUsersMarshaller, this::cleanUser));

        try {
            final User user = getUsersUnmarshaller(mimeType).fromString(routingContext.getBodyAsString()); // = User.parse(routingContext.parsedHeaders().contentType().value(), routingContext.getBodyAsString());
            validateUserForPutUser(user);

            api.updateUser(routingContext.pathParam("identifier"), user, result);
        } catch(HttpError e) {
            result.fail(e);
        } catch (IllegalArgumentException /*| IOException */ e) {
            result.fail(new BadContentError(e));
        }
    }

    private User cleanUser(User u) {
        return new User(u).setPassword(null);
    }

    private void validateUserForPutUser(User user) {
        requireAllAreNull(user.getId(), user.getLink(), user.getRole());
    }

}
