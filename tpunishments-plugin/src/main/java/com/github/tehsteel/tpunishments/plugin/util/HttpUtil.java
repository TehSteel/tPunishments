package com.github.tehsteel.tpunishments.plugin.util;

import com.github.tehsteel.tpunishments.core.punishment.Punishment;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentId;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentType;
import com.github.tehsteel.tpunishments.core.report.Report;
import com.github.tehsteel.tpunishments.core.report.ReportId;
import com.github.tehsteel.tpunishments.plugin.Constants;
import com.google.common.annotations.Beta;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
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
	public static Optional<Punishment> getActivePunishment(final UUID uuid, final PunishmentType punishmentType) throws Exception {
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
			return Optional.empty();
		}

		return Optional.of(new Gson().fromJson(response.body(), Punishment.class));
	}

	/**
	 * Retrieves a punishment by its id from the remote server.
	 *
	 * @param punishmentId The id of the punishment.
	 * @return A punishment object, or null if none is found.
	 */
	public static Optional<Punishment> getPunishmentById(final PunishmentId punishmentId) throws Exception {
		final HttpClient client = HttpClient.newHttpClient();
		final HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(String.format("%s/api/v1/punishments/get?punishmentId=%s", Constants.WEBSITE, punishmentId)))
				.GET()
				.header("X-API-KEY", Constants.API_KEY)
				.timeout(Duration.ofSeconds(10))
				.build();


		final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		final String body = response.body();

		if (body == null || body.isBlank() || body.isEmpty()) {
			return Optional.empty();
		}


		return Optional.of(new Gson().fromJson(response.body(), Punishment.class));
	}

	@ApiStatus.Experimental
	@Beta
	public static Optional<List> getPunishmentsByUuid(final UUID uuid) throws Exception {
		final HttpClient client = HttpClient.newHttpClient();
		final HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(String.format("%s/api/v1/punishments/get?punishmentId=%s", Constants.WEBSITE, uuid)))
				.GET()
				.header("X-API-KEY", Constants.API_KEY)
				.timeout(Duration.ofSeconds(10))
				.build();

		final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		final String body = response.body();

		if (body == null || body.isBlank() || body.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(new Gson().fromJson(response.body(), List.class));
	}

	/**
	 * Updates an existing punishment on the remote server.
	 *
	 * @param punishment The punishment object to be updated.
	 * @throws URISyntaxException   If the URI syntax is incorrect.
	 * @throws IOException          If an IO error occurs.
	 * @throws InterruptedException If the operation is interrupted.
	 */
	public static void updatePunishment(final Punishment punishment) throws Exception {
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
	public static Optional<Punishment> createPunishment(final Punishment punishment) throws Exception {
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
			return Optional.empty();
		}

		return Optional.of(new Gson().fromJson(response.body(), Punishment.class));
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
	public static Optional<Report> createReport(final Report report) throws Exception {
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
			return Optional.empty();
		}

		return Optional.of(new Gson().fromJson(response.body(), Report.class));
	}

	/**
	 * Updates an existing report on the remote server.
	 *
	 * @param report The report object to be updated.
	 * @throws URISyntaxException   If the URI syntax is incorrect.
	 * @throws IOException          If an IO error occurs.
	 * @throws InterruptedException If the operation is interrupted.
	 */
	public static void updateReport(final Report report) throws Exception {
		final HttpClient client = HttpClient.newHttpClient();
		final HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(String.format("%s/api/v1/reports/update", Constants.WEBSITE)))
				.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(report)))
				.header("Content-Type", "application/json")
				.header("X-API-KEY", Constants.API_KEY)
				.timeout(Duration.ofSeconds(30))
				.build();

		client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	/**
	 * Retrieves a report by its id from the remote server.
	 *
	 * @param reportId The id of the report.
	 * @return A report object, or null if none is found.
	 * @throws URISyntaxException   If the URI syntax is incorrect.
	 * @throws IOException          If an IO error occurs.
	 * @throws InterruptedException If the operation is interrupted.
	 */
	public static Optional<Report> getReportById(final ReportId reportId) throws Exception {
		final HttpClient client = HttpClient.newHttpClient();
		final HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(String.format("%s/api/v1/reports/get?reportId=%s", Constants.WEBSITE, reportId)))
				.GET()
				.header("X-API-KEY", Constants.API_KEY)
				.timeout(Duration.ofSeconds(10))
				.build();


		final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		final String body = response.body();

		if (body == null || body.isBlank() || body.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(new Gson().fromJson(response.body(), Report.class));
	}

	/**
	 * Retrieves a report by its id from the remote server.
	 *
	 * @param uuid The uuid of the report.
	 * @return A list of report objects, or null if none is found.
	 * @throws URISyntaxException   If the URI syntax is incorrect.
	 * @throws IOException          If an IO error occurs.
	 * @throws InterruptedException If the operation is interrupted.
	 */
	public static Optional<List<Report>> getReportsByReporter(final UUID uuid) throws Exception {
		final HttpClient client = HttpClient.newHttpClient();
		final HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(String.format("%s/api/v1/reports/getReportsByReporter?uuid=%s", Constants.WEBSITE, uuid)))
				.GET()
				.header("X-API-KEY", Constants.API_KEY)
				.timeout(Duration.ofSeconds(10))
				.build();


		final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		final String body = response.body();

		if (body == null || body.isBlank() || body.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(new Gson().fromJson(response.body(), new TypeToken<List<Report>>() {
		}.getType()));
	}
}
