package org.clientconnection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.ChoiceBox;
import org.FriendRequests.FriendRequest;
import org.statistics.Statistic;
import org.overview.Player;
import org.registration.LoginController;
import org.registration.User;

import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

/**
 * @author Yelle Lieder
 * had some inspiration from https://git.uni-due.de/sktrkley/self-study-exercises-for-programming.git, nothing directly copied
 */

public class RequestHelper {
    private static int port = 8080;

    public static String login(String username, String password) throws IOException, InterruptedException, ConnectException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/login/" + username + "/" + password + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String logout(String username) throws IOException, InterruptedException, ConnectException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/logout/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String registration(String username, String email, String password) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":"+ port + "/user/registration/" + username + "/" + email + "/" + password + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String editUserPassword(String username, String newPassword) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/edit/password/" + username + "/" + newPassword + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String editUserName(String username, String newUsername) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/edit/username/" + username + "/" + newUsername + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String editUserEmail(String username, String newEmail) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/edit/email/" + username + "/" + newEmail + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String getPoints(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/points/get/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static ArrayList<Player> viewPlayer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/player/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<Player>>() {
        }.getType());
    }
    public static String getUserInfo(String username) throws IOException, InterruptedException, ConnectException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/getUserInfo/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String createTeam(String clubname, String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/team/create/" + clubname + "/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String changeTeamName(String clubname, String username) throws IOException, InterruptedException {
        clubname=clubname.replace(" ", "+");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/team/change/" + clubname + "/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static ArrayList<String> getTeam(String userID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/team/get/" + userID + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<String>>() {
        }.getType());
    }

    public static ArrayList<String> buy_small(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/shop/buy/small/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<String>>() {
        }.getType());
    }
    public static ArrayList<String> buy_medium(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/shop/buy/medium/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<String>>() {
        }.getType());
    }
    public static ArrayList<String> buy_large(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/shop/buy/large/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<String>>() {
        }.getType());
    }
    public static ArrayList<String> buy_special(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/shop/buy/special/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<String>>() {
        }.getType());
    }
    public static ArrayList<User> getFriendList(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/getFriendList/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<User>>() {
        }.getType());
    }
    public static String sendFriendRequest(String username, String friendsUsername) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/sendFriendRequest/" + username + "/" + friendsUsername + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String answerFriendRequest(String username, String friendsUsername, String answer) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/answerFriendRequest/" + username + "/" + friendsUsername + "/" + answer + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static ArrayList<User> getAllUser(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/getAllUser/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<User>>() {
        }.getType());
    }
    public static ArrayList<FriendRequest> getOpenRequests(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/getOpenRequests/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<FriendRequest>>() {
        }.getType());
    }
    public static String removeFriend(String username, String friendUsername) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/removeFriend/" + username + "/" + friendUsername + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String saveFormation(String formation, String userID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/formation/" + formation + "/" + userID + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String saveSetup(ArrayList<ChoiceBox> startingeleven, String userID) throws IOException, InterruptedException {
        String team = "";
        for (ChoiceBox b: startingeleven){
            team += b.getValue().toString()+"/";
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/saveteam/"+userID+"/" + team.replace(" ","_") + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String saveSetupReserve(ArrayList<ChoiceBox> reserveplayer, String userID) throws IOException, InterruptedException {
        String team="";
        for (ChoiceBox b: reserveplayer){
            team+=b.getValue().toString()+"/";
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/savebench/"+userID+"/" + team.replace(" ","_") + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static ArrayList<String> getStartingEleven(String userID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/getstartingeleven/"+userID+"/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<String>>() {
        }.getType());
    }

    public static ArrayList<String> getBench(String userID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/getbench/"+userID+"/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<String>>() {
        }.getType());
    }

    public static ArrayList<Statistic> getStatistic(String userID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/statistic/"+userID+"/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<Statistic>>() {
        }.getType());
    }

    public static String setDefaultStadium(String defaultstadiumname, String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/stadium/create/" + defaultstadiumname + "/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String changeStadiumName(String stadiumname, String username) throws IOException, InterruptedException {
        stadiumname=stadiumname.replace(" ", "+");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/stadium/stadiumname/" + stadiumname + "/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String checkStadium(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/stadium/check/" + "/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String upgradeParkingMedium(String username,String capacityNew, int price) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/upgrade/parking/toMedium/" + username + "/" + capacityNew + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String upgradeParkingBig(String username,String capacityNew, int price) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/upgrade/parking/toBig/" + username + "/" + capacityNew + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String upgradeParkingEnlarged(String username,String capacityNew, int price) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/upgrade/parking/enlarged/" + username + "/" + capacityNew + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String upgradeImbisMedium(String username,String capacityNew, int price) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/upgrade/imbis/toMedium/" + username + "/" + capacityNew + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String upgradeImbisBig(String username,String capacityNew, int price) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/upgrade/imbis/toBig/" + username + "/" + capacityNew + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String upgradeImbisEnlarged(String username,String capacityNew, int price) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/upgrade/imbis/enlarged/" + username + "/" + capacityNew + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String upgradeCapacity1(String username,String capacityNew, int price) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/upgrade/capacity/step1/" + username + "/" + capacityNew + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String upgradeCapacity2(String username,String capacityNew, int price) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/upgrade/capacity/step2/" + username + "/" + capacityNew + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String upgradeCapacity3(String username,String capacityNew, int price) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/upgrade/capacity/step3/" + username + "/" + capacityNew + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String getStadiumImbis(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/getStadium/imbis/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String getStadiumCapacity(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/getStadium/capacity/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String getStadiumParking(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/getStadium/parking/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String getStadiumName(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/getStadium/name/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static ArrayList<String> getAllStadium(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/getStadium/stadium/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<String>>() {
        }.getType());
    }

    public static String saveIcon(String Icon, String userID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/setIcon/" + Icon + "/" + userID + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String getIcon(String userID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/getIcon/" + userID + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String startMatch(String player1, String player2) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/startMatch/" + player1 + "/" + player2 + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String sendMatchRequest (String username, String friendsUsername) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/sendMatchRequest/" + username + "/" + friendsUsername + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String answerMatchRequest (String username, String opponentUsername, String answer) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/answerMatchRequest/" + username + "/" + opponentUsername + "/" + answer)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static ArrayList<User> getMatchRequest(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/user/getMatchRequest/"+username+"/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<User>>() {
        }.getType());
    }

    public static ArrayList<String> getPlayerAtPosition(String userID, String position) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/atPosition/" + userID + "/"+position+ "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<ArrayList<String>>() {
        }.getType());
    }

    public static String getTactics(String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create("http://"+ LoginController.getServerAddress() +":" + port + "/getTactics/" + username + "/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
