package com.github.tehsteel.tpunishments.plugin.util;

import com.github.tehsteel.tpunishments.core.punishment.Punishment;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentId;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentType;
import com.github.tehsteel.tpunishments.core.report.Report;
import com.github.tehsteel.tpunishments.plugin.Constants;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * This utility class provides methods for interacting with a remote server's API.
 */
public final class HttpUtil {
	private HttpUtil() {
	}

	/**
	 * Retrieves an active punishment for a specific UUID and punishment type from the remote server.
	 *
	 * @param uuid           The UUID of the player.
	 * @param punishmentType The type of punishment to retrieve.
	 * @return An active punishment object, or null if none is found.
	 * @throws URISyntaxException   If the URI syntax is incorrect.
	 * @throws IOException          If an IO error occurs.
	 * @throws InterruptedException If the operation is interrupted.
	 */
	public static Punishment getActivePunishment(final UUID uuid, final PunishmentType punishmentType) throws URISyntaxException, IOException, InterruptedException {
		final HttpClient client = HttpClient.newHttpClient();
		final HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(String.format("%s/api/v1/punishments/getActivePunishmentByUuid?uuid=%s&punishmentType=%s", Constants.WEBSITE, uuid, punishmentType.getName().toUpperCase())))
				.GET()
				.header("X-API-KEY", Constants.API_KEY)
				.timeout(Duration.ofSeconds(10))
				.build();


		final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		final String body = response.body();

		if (body == null || body.isBlank() || body.isEmpty()) {
			return null;
		}

		return new Gson().fromJson(response.body(), Punishment.class);
	}

	/**
	 * Retrieves a punishment by its id from the remote server.
	 *
	 * @param punishmentId The id of the punishment.
	 * @return A punishment object, or null if none is found.
	 * @throws URISyntaxException   If the URI syntax is incorrect.
	 * @throws IOException          If an IO error occurs.
	 * @throws InterruptedException If the operation is interrupted.
	 */
	public static Punishment getPunishmentById(final PunishmentId punishmentId) throws URISyntaxException, IOException, InterruptedException {
		final HttpClient client = HttpClient.newHttpClient();
		final HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(String.format("%s/api/v1/punishments/get?punishmentId=%s", Constants.WEBSITE, punishmentId.id())))
				.GET()
				.header("X-API-KEY", Constants.API_KEY)
				.timeout(Duration.ofSeconds(10))
				.build();


		final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		final String body = response.body();

		if (body == null || body.isBlank() || body.isEmpty()) {
			return null;
		}


		return new Gson().fromJson(response.body(), Punishment.class);
	}

	public static List getPunishmentsByUuid(final UUID uuid) throws URISyntaxException, IOException, InterruptedException {
		final HttpClient client = HttpClient.newHttpClient();
		final HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(String.format("%s/api/v1/punishments/get?punishmentId=%s", Constants.WEBSITE, uuid)))
				.GET()
				.header("X-API-KEY", Constants.API_KEY)
				.timeout(Duration.ofSeconds(10))
				.build();


		System.out.println(Constants.WEBSITE);


		final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		final String body = response.body();

		if (body == null || body.isBlank() || body.isEmpty()) {
			return null;
		}


		return new Gson().fromJson(response.body(), List.class);
	}

	/**
	 * Updates an existing punishment on the remote server.
	 *
	 * @param punishment The punishment object to be updated.
	 * @throws URISyntaxException   If the URI syntax is incorrect.
	 * @throws IOException          If an IO error occurs.
	 * @throws InterruptedException If the operation is interrupted.
	 */
	public static void updatePunishment(final Punishment punishment) throws URISyntaxException, IOException, InterruptedException {
		final HttpClient client = HttpClient.newHttpClient();
		final HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(String.format("%s/api/v1/punishments/update", Constants.WEBSITE)))
				.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(punishment)))
				.header("Content-Type", "application/json")
				.header("X-API-KEY", Constants.API_KEY)
				.timeout(Duration.ofSeconds(30))
				.build();

		client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	/**
	 * Creates a new punishment on the remote server.
	 *
	 * @param punishment The punishment object to be created.
	 * @return The created punishment object, or null if the operation fails.
	 * @throws URISyntaxException   If the URI syntax is incorrect.
	 * @throws IOException          If an IO error occurs.
	 * @throws InterruptedException If the operation is interrupted.
	 */
	public static Punishment createPunishment(final Punishment punishment) throws URISyntaxException, IOException, InterruptedException {
		final HttpClient client = HttpClient.newHttpClient();
		final HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(String.format("%s/api/v1/punishments/create", Constants.WEBSITE)))
				.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(punishment)))
				.header("Content-Type", "application/json")
				.header("X-API-KEY", Constants.API_KEY)
				.timeout(Duration.ofSeconds(30))
				.build();

		final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		final String body = response.body();

		if (body == null || body.isBlank() || body.isEmpty()) {
			return null;
		}

		return new Gson().fromJson(response.body(), Punishment.class);
	}

	/**
	 * Creates a new report on the remote server.
	 *
	 * @param report The report object to be created.
	 * @return The created report object, or null if the operation fails.
	 * @throws URISyntaxException   If the URI syntax is incorrect.
	 * @throws IOException          If an IO error occurs.
	 * @throws InterruptedException If the operation is interrupted.
	 */
	public static Report createReport(final Report report) throws URISyntaxException, IOException, InterruptedException {
		final HttpClient client = HttpClient.newHttpClient();
		final HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(String.format("%s/api/v1/reports/create", Constants.WEBSITE)))
				.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(report)))
				.header("Content-Type", "application/json")
				.header("X-API-KEY", Constants.API_KEY)
				.timeout(Duration.ofSeconds(30))
				.build();

		final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		final String body = response.body();

		if (body == null || body.isBlank() || body.isEmpty()) {
			return null;
		}

		return new Gson().fromJson(response.body(), Report.class);
	}
}