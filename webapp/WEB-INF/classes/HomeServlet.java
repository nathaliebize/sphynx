import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns={"/"})
public class HomeServlet extends HttpServlet 
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException 
    {
        response.setContentType("text/html"); 
        PrintWriter out = response.getWriter();
        out.println(
            "<html><head><title>Hello Client!</title></head><body>"
            + "<h1>Hello Sphynx!</h1>"
            + "</body></html>" );
    }
}
