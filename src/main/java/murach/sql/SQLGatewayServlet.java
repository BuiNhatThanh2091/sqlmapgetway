package murach.sql;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(name = "SQLGatewayServlet", urlPatterns = {"/sqlgateway"})
public class SQLGatewayServlet extends HttpServlet {

    // ✅ Thêm allowPublicKeyRetrieval & useUnicode, tắt SSL cho dev
	private static final String URL =
		    "jdbc:mysql://127.0.0.1:3306/sqlgateway"
		  + "?useUnicode=true&characterEncoding=UTF-8"
		  + "&allowPublicKeyRetrieval=true"
		  + "&useSSL=false"
		  + "&serverTimezone=UTC";

    private static final String USER = "sqlgateway_user";
    private static final String PASS = "23162091"; // đổi mật khẩu thật

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Lần đầu vào trang
        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String sqlStatement = req.getParameter("sqlStatement");
        String sqlResult;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
                 Statement statement = connection.createStatement()) {

                sqlStatement = (sqlStatement == null) ? "" : sqlStatement.trim();

                if (sqlStatement.length() >= 6 && sqlStatement.substring(0, 6).equalsIgnoreCase("SELECT")) {
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
            }
        } catch (ClassNotFoundException e) {
            sqlResult = "<p>Error loading MySQL driver:<br>" + e.getMessage() + "</p>";
        } catch (SQLException e) {
            sqlResult = "<p>Error executing SQL:<br>" + e.getMessage() + "</p>";
        }

        HttpSession session = req.getSession();
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);

        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
