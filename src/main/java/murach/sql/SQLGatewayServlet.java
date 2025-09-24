package murach.sql;

import java.io.IOException;
import java.sql.*;
import javax.naming.InitialContext;       // vẫn là javax.naming
import javax.naming.NamingException;
import javax.sql.DataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(name = "SQLGatewayServlet", urlPatterns = {"/sqlgateway"})
public class SQLGatewayServlet extends HttpServlet {

    private DataSource ds;

    @Override
    public void init() throws ServletException {
        try {
            InitialContext ic = new InitialContext();
            ds = (DataSource) ic.lookup("java:comp/env/jdbc/SQLGatewayDS");
        } catch (NamingException e) {
            throw new ServletException("JNDI lookup failed: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String sqlStatement = req.getParameter("sqlStatement");
        String sqlResult;

        try (Connection connection = ds.getConnection();
             Statement statement = connection.createStatement()) {

            sqlStatement = (sqlStatement == null) ? "" : sqlStatement.trim();

            if (sqlStatement.length() >= 6
                    && sqlStatement.substring(0, 6).equalsIgnoreCase("SELECT")) {

                try (ResultSet rs = statement.executeQuery(sqlStatement)) {
                    sqlResult = SQLUtil.getHtmlTable(rs);
                }
            } else if (!sqlStatement.isEmpty()) {
                int i = statement.executeUpdate(sqlStatement);
                sqlResult = (i == 0)
                        ? "<p>The statement executed successfully!</p>"
                        : "<p>The statement executed successfully!<br>" + i + " row(s) affected.</p>";
            } else {
                sqlResult = "<p>Please enter a valid SQL statement.</p>";
            }

        } catch (SQLException e) {
            sqlResult = "<p>Error executing SQL:<br>" + e.getMessage() + "</p>";
        }

        HttpSession session = req.getSession();
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);

        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
