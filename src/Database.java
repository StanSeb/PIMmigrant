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

    public Database(){

        try{
            conn = DriverManager.getConnection("jdbc:sqlite:PIMmigrant_DB.db");
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
    public String uploadImage(FileItem image) {
        String filename = "/IMAGES/" + image.getName();

        try (var os = new FileOutputStream(Paths.get("src/www" + filename).toString())) {
            os.write(image.get());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return filename;
    }

    public void createNotes(Note note){
        try{
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO notes (title, content, timestamp) VALUES (?,?,?)");
            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO files (filename, note_id) VALUES (?,?)");
            stmt.setString(1, note.getTitle());
            stmt.setString(2, note.getContent());
            stmt.setLong(3, Instant.now().toEpochMilli());
            stmt1.setString(1, note.getFilename());
            stmt1.setInt(2, note.getId());

            stmt.executeUpdate();
            stmt1.executeUpdate();

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    public List<Note> getAllNotes(){
        List<Note> notes = null;

        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM notes INNER JOIN files ON notes.id = files.note_id");

            ResultSet rs = stmt.executeQuery();

            Note[] tempList = (Note[]) Utils.readResultSetToObject(rs, Note[].class);
            notes = List.of(tempList);
            System.out.println(notes);

        }catch(SQLException | JsonProcessingException throwables){
            throwables.printStackTrace();
        }
        return notes;
    }

    public Note getNoteByTitle(String title){
        Note notes= null;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM notes INNER JOIN files ON notes.id = files.note_id WHERE notes.title = ?");
            stmt.setString(1, title);

            ResultSet rs = stmt.executeQuery();

            Note[] notesFromRs = (Note[]) Utils.readResultSetToObject(rs, Note[].class);
            notes = notesFromRs[0];

        } catch (SQLException | JsonProcessingException throwables) {
            throwables.printStackTrace();
        }
        return notes;
    }

    public void updateNote(Note note){
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE notes SET title = ?, content = ?, timestamp = ? WHERE notes.id = ?");
            PreparedStatement stmt1 = conn.prepareStatement("UPDATE files SET filename = ? WHERE files.note_id = ?");
            stmt.setString(1, note.getTitle());
            stmt.setString(2, note.getContent());
            stmt.setLong(3, note.getTimestamp());
            stmt.setInt(4, note.getId());
            stmt1.setString(1, note.getFilename());
            stmt1.setInt(2, note.getId());

            stmt.executeUpdate();
            stmt1.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void deleteNote(Note note) {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM notes  WHERE notes.id = ?");
            PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM files WHERE files.note_id = ?");
            stmt.setInt(1, note.getId());
            stmt1.setInt(1, note.getId());

            stmt.executeUpdate();
            stmt1.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
