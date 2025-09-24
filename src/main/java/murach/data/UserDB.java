package murach.data;

import java.sql.*;

import murach.business.User;
import murach.sql.ConnectionPool;

public class UserDB {
    public static int insert(User user){
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement preparedStatement = null;

        String query = "INSERT INTO User (Email, FirstName, LastName) " + "VALUES (?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(preparedStatement);
            pool.freeConnection(connection);
        }
    }

    public static int update(User user) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement preparedStatement = null;

        String query = "UPDATE User SET " + "FirstName = ?, " + "LastName = ?, " + "WHERE Email = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());

            return preparedStatement.executeUpdate();
        } catch (SQLException e ) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(preparedStatement);
            pool.freeConnection(connection);
        }
    }

    public static int delete(User user) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement preparedStatement = null;

        String query = "DELETE FROM Users " + "WHERE Email = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getEmail());

            return preparedStatement.executeUpdate();
        } catch (SQLException e ) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(preparedStatement);
            pool.freeConnection(connection);
        }
    }

    public static boolean emailExists(String email) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        String query = "SELECT Email FROM Users " + "WHERE Email = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            rs = preparedStatement.executeQuery();

            return rs.next();
        } catch (SQLException e ) {
            System.out.println(e);
            return false;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(preparedStatement);
            pool.freeConnection(connection);
        }
    }

    public static User selectUser(String email) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Users " + "WHERE Email = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            rs = preparedStatement.executeQuery();
            User user = null;
            if (rs.next()) {
                user = new User();
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setEmail(rs.getString("Email"));
            }
            return user;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(preparedStatement);
            pool.freeConnection(connection);
        }
    }
}
