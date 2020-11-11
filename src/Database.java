import com.fasterxml.jackson.core.JsonProcessingException;
import express.utils.Utils;
import org.apache.commons.fileupload.FileItem;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.sql.*;
import java.time.Instant;
import java.util.List;

public class Database {

    private Connection conn;

    public Database() {

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:PIMmigrant_DB.db");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createNotes(Note note) {
        try {

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO notes (title, content, timestamp, imgUrl) VALUES (?,?,?,?)");
            stmt.setString(1,note.getTitle());
            stmt.setString(2,note.getContent());
            stmt.setLong(3, Instant.now().toEpochMilli());
            stmt.setString(4, note.getImgUrl());

            stmt.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Note> getAllNotes() {
        List<Note> notes = null;

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM notes");

            ResultSet rs = stmt.executeQuery();

            Note[] tempList = (Note[]) Utils.readResultSetToObject(rs, Note[].class);
            notes = List.of(tempList);

        } catch (SQLException | JsonProcessingException throwables) {
            throwables.printStackTrace();
        }
        return notes;
    }



    public String uploadImage(FileItem image){
        String imageUrl = "/uploads/" + image.getName();

        try(var os = new FileOutputStream(Paths.get("src/www" + imageUrl).toString())){

            os.write(image.get());

        }catch (Exception e){
            e.printStackTrace();

            return null;
        }

        return imageUrl;
    }

}