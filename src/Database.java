import com.fasterxml.jackson.core.JsonProcessingException;
import express.utils.Utils;
import org.apache.commons.fileupload.FileItem;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.sql.*;
import java.time.Instant;
import java.util.List;

public class Database {

    // Create variable for connection to the database
    private Connection conn;

    public Database() {

        //Connection to the database
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:PIMmigrant_DB.db");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Method for creating notes
    public void createNotes(Note note) {
        try {

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO notes (title, content, timestamp, imgUrl) VALUES (?,?,?,?)");
            stmt.setString(1,note.getTitle());
            stmt.setString(2,note.getContent());
            stmt.setLong(3, Instant.now().toEpochMilli()); //Second parameter creates a 13-digit timestamp
            stmt.setString(4, note.getImgUrl());

            stmt.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Method for showing all the saved notes
    public List<Note> getAllNotes() {
        List<Note> notes = null; //Declaring a list before try/catch statement

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM notes");

            ResultSet rs = stmt.executeQuery();

            //Making a temporary list to then add all objects as a Note-class
            Note[] tempList = (Note[]) Utils.readResultSetToObject(rs, Note[].class);
            notes = List.of(tempList);

        } catch (SQLException | JsonProcessingException throwables) {
            throwables.printStackTrace();
        }
        return notes;
    }

    //Method for getting a specific note - based on the notes id - that we are entering to the method
    public Note getNoteById(int id){
        Note notes= null;

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM notes WHERE notes.id = ?");
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            Note[] notesFromRs = (Note[]) Utils.readResultSetToObject(rs, Note[].class);
            notes = notesFromRs[0];

        } catch (SQLException | JsonProcessingException throwables) {
            throwables.printStackTrace();
        }
        return notes;
    }

    //Updating a already saved note in the database
    public void updateNote(Note note){
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE notes SET title = ?, content = ?, timestamp = ?, imgUrl = ? WHERE notes.id = ?");
            stmt.setString(1, note.getTitle());
            stmt.setString(2, note.getContent());
            stmt.setLong(3, Instant.now().toEpochMilli());
            stmt.setString(4, note.getImgUrl());
            stmt.setInt(5, note.getId());

            stmt.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    //Method for deleting a note from the database - based on the id of the note.
    public void deleteNote(Note note) {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM notes WHERE notes.id = ?");
            stmt.setInt(1, note.getId());

            stmt.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Adding/Uploading an image to the Note object in the database
    //FileItem is used to be able to handle files
    public String uploadImage(FileItem image){
        String imageUrl = "/uploads/" + image.getName(); //Adding the name of the file into the folder "uploads"

        try(var os = new FileOutputStream(Paths.get("src/www" + imageUrl).toString())){ //Wanted to add a Try-with-resources statement

            os.write(image.get());

        }catch (Exception e){
            e.printStackTrace();

            return null;
        }
        return imageUrl;
    }

}