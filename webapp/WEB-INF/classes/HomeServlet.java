import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* HomeServlet is a simple servlet that greet any user.
*
* @author Nathalie Bize
*/
@WebServlet(urlPatterns={"/"})
public class HomeServlet extends HttpServlet 
{
    /**
    * This method handles a get method.
    *
    * @param request the request object
    * @param response the response object
    * @throws ServletException
    * @throws IOException
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException 
    {
        response.setContentType("text/html"); 
        PrintWriter out = response.getWriter();
        out.println(
            "<html>" +
            "<head><title>Hello Client!</title></head>" +
            "<body><h1>Hello Sphynx!</h1></body>" +
            "</html>" 
            );
    }
}
