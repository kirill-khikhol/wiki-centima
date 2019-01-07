package main.java;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

/*	In this application was decided to use Vert.x framework because it 
 * has a great possibility for scaling and extension of functionality without changing 
 * the existing code during future development of the application.
 * 
 * 	Despite now this application has very small functionality, Angular 6 framework
 * allows to us safely extend it by adding new components and services.
 * 
 */
public class WikiVerticle extends AbstractVerticle {

	private Map<Integer, WikiMessage> messages = new HashMap<>();

	public static void main(String[] args) {
		Vertx.vertx().deployVerticle(new WikiVerticle());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void start(Future<Void> future) {
		populateStorage();
		Router router = Router.router(vertx);

		router.route().handler(StaticHandler.create());

		router.get("/messages").handler(this::getAllMessages);
		router.route("/message*").handler(BodyHandler.create());
		router.post("/message").handler(this::addMessage);
		router.delete("/messages/:id").handler(this::removeMessage);

		vertx.createHttpServer().requestHandler(router::accept).listen(8080, result -> {
			if (result.succeeded()) {
				future.complete();
			} else {
				future.fail(result.cause());
			}
		});
	}

	private void getAllMessages(RoutingContext routingContext) {
		routingContext.response().putHeader("Content-Type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(messages.values()));
	}

	private void addMessage(RoutingContext routingContext) {
		WikiMessage msg = Json.decodeValue(routingContext.getBodyAsString(), WikiMessage.class);
		messages.put(msg.getId(), msg);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(msg));
	}

	private void removeMessage(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		if (id == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			messages.remove(Integer.valueOf(id));
		}
		routingContext.response().setStatusCode(204).end();
	}

	private void populateStorage() {
		WikiMessage m1 = new WikiMessage("first message");
		messages.put(m1.getId(), m1);
		WikiMessage m2 = new WikiMessage("second message");
		messages.put(m2.getId(), m2);
		WikiMessage m3 = new WikiMessage("third message");
		messages.put(m3.getId(), m3);
	}

}
