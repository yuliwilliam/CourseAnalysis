import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;

public class MySQLdb {

    private final static Logger logger = LogManager.getLogger(MySQLdb.class.getName());

    private Connection connection;

    private final String USERNAME;
    private final String PASSWORD;
    private final String HOST;

    public MySQLdb(String host,String username, String password){
        this.USERNAME = username;
        this.PASSWORD = password;
        this.HOST = host;
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(HOST, USERNAME, PASSWORD);
        if (!connection.isClosed()) {
            logger.info("connected to database");
        } else {
            logger.error("unable to connect to database");
            throw new RuntimeException("failed to connect to database with host: " + HOST + " username: " + USERNAME + " password: " + PASSWORD);
        }
    }

    private void disconnect() throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
        }
        if (connection.isClosed()) {
            logger.info("disconnected to database");
        } else {
            throw new RuntimeException("failed to disconnect from database with host: " + HOST + " username: " + USERNAME + " password: " + PASSWORD);
        }
    }

    public JSONArray runQuery(String query) throws SQLException {
        logger.info("Query = " + query);
        try {
            connect();
            PreparedStatement statement = connection.prepareStatement(query);
            return convert(statement.executeQuery());
        } finally {
            disconnect();
        }
    }

    public void runUpdate(String query) throws SQLException {
        logger.info("Query = " + query);
        try {
            connect();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } finally {
            disconnect();
        }
    }

    public  JSONArray convert(ResultSet rs) throws SQLException, JSONException {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();

        while (rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 1; i < numColumns + 1; i++) {
                String column_name = rsmd.getColumnName(i);

                if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
                    obj.put(column_name, rs.getArray(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
                    obj.put(column_name, rs.getBoolean(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
                    obj.put(column_name, rs.getBlob(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
                    obj.put(column_name, rs.getDouble(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
                    obj.put(column_name, rs.getFloat(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
                    obj.put(column_name, rs.getNString(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
                    obj.put(column_name, rs.getString(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
                    obj.put(column_name, rs.getDate(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
                    obj.put(column_name, rs.getTimestamp(column_name));
                } else {
                    obj.put(column_name, rs.getObject(column_name));
                }
            }

            json.put(obj);
        }

        return json;
    }


}
