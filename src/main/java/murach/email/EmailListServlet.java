package murach.email;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import murach.business.User;
import murach.data.UserDB;

public class EmailListServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String url = "/index.jsp";
        String action = req.getParameter("action");
        if (action == null) action = "join";

        if ("join".equals(action)) {
            url = "/index.jsp";
        } else if ("add".equals(action)) {
            String firstName = req.getParameter("firstName");
            String lastName  = req.getParameter("lastName");
            String email     = req.getParameter("email");

            User user = new User(firstName, lastName, email);

            String message;
            if (UserDB.emailExists(user.getEmail())) {
                message = "This email address already exists.<br>Please enter another email address.";
                url = "/index.jsp";
            } else {
                UserDB.insert(user);
                message = "";
                url = "/thanks.jsp";
            }
            req.setAttribute("user", user);
            req.setAttribute("message", message);
        }
        getServletContext().getRequestDispatcher(url).forward(req, resp);
    }
}
