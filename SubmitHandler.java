import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class SubmitHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if ("/submit".equalsIgnoreCase(target)) {
            String favoriteGenre = request.getParameter("genre");

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();

            out.println("<html>");
            out.println("<head><title>Music App - Result</title></head>");
            out.println("<body>");
            out.println("<h1>Your Favorite Genre</h1>");
            out.println("<p>Your favorite genre is: " + favoriteGenre + "</p>");
            out.println("</body>");
            out.println("</html>");

            baseRequest.setHandled(true);
        }
    }
}
