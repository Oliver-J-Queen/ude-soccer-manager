package org.clientconnection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.registration.LoginController;
import org.tournament.Tournament;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
public class TournamentHelper {
    private static int port = 8080;

    public static String joinTournament(String userID, String tournamentID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() + ":" + port + "/tournament/" + "join/" + userID + "/" + tournamentID + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String createTournament(String userID, String tournamentName, String mode, String entryFee, String maxPlayers) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() + ":" + port + "/tournament/" + "create/" + userID + "/" + tournamentName + "/" + mode + "/" + entryFee + "/" + maxPlayers + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static ArrayList<Tournament> getTournaments() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() + ":" + port + "/tournament/" + "getAllTournaments/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<Tournament>>() {
        }.getType());
    }

    public static int[] getPlacements(String tournamentID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() + ":" + port + "/tournament/" + "start/" + tournamentID + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), int[].class);
    }
    public static String createTournament(String userID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() + ":" + port + "/tournament/" + "test/" + userID + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
