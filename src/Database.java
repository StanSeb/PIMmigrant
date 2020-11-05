import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    private Connection conn;

    public Database(){

        try{
            conn = DriverManager.getConnection("jdbc:sqlite:PIMmigrant_DB.db");
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    public void createNotes(Note notes){
        try{
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO notes (content) VALUES (?)");
            stmt.setString(1, notes.getContent());

            stmt.executeUpdate();

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }



}
