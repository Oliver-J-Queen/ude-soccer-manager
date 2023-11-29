open module scmanagerserver.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires javax.servlet.api;
    requires ormlitebuild;
    requires jsoup;
    requires org.eclipse.jetty.server;
    requires org.junit.jupiter.api;

    exports org.serverlauncher;
    exports org.serverconnection;
}