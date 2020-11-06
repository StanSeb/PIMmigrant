import com.fasterxml.jackson.core.JsonProcessingException;
import express.utils.Utils;

import java.sql.*;
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

    public void createNotes(Note notes){
        try{
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO notes (title, content) VALUES (?, ?)");
            stmt.setString(1, notes.getTitle());
            stmt.setString(2, notes.getContent());

            stmt.executeUpdate();

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
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM notes INNER JOIN files ON notes.id = files.notes_id WHERE notes.title = ?");
            stmt.setString(1, title);

            ResultSet rs = stmt.executeQuery();

            Note[] notesFromRs = (Note[]) Utils.readResultSetToObject(rs, Note[].class);
            notes = notesFromRs[0];

        } catch (SQLException | JsonProcessingException throwables) {
            throwables.printStackTrace();
        }
        return notes;


    }

}
