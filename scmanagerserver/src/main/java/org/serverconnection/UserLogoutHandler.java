package org.serverconnection;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UserLogoutHandler extends AbstractHandler {

    /**
     * @author Yelle Lieder (handle method)
     * had some inspiration from https://git.uni-due.de/sktrkley/self-study-exercises-for-programming.git, nothing directly copied
     */
    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user")&&splitTarget[2].equals("logout")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            logoutUser(response.getWriter(), splitTarget[3]);
            originalRequest.setHandled(true);
        }
    }

    public void logoutUser(PrintWriter input, String username){
        System.out.println("logout User");
        UserLoginHandler tp = new UserLoginHandler();
        tp.setStatus(username, "Offline");
        input.print("Logout succesful");
    }
}
