import express.Express;
import express.middleware.Middleware;
import org.apache.commons.fileupload.FileItem;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Express app = new Express(); // Creating a Express variable to be able to get the methods it needs
        Database db = new Database(); // Creating our variable for the Database class


        //Express method calling our Database method getAllNotes
        app.get("/rest/Notes",(req, res) ->{
           List<Note> notes = db.getAllNotes();

           res.json(notes);
        });


        //Express method calling our Database method createNotes
        app.post("/rest/Notes",(req, res)->{

            Note note = (Note)req.getBody(Note.class);

            //Printing out the object we are adding
            System.out.println(note.toString());

            db.createNotes(note);

        });

        //Express method calling our Database method uploadImage
        app.post("/api/file-upload", (request, response) -> {
           String imageUrl = null;

           try{
               List<FileItem> files = request.getFormData("files");

               //This if-else statement removes the exception error when not
               //adding a file to a note
               if(!(files == null)){
                   imageUrl = db.uploadImage(files.get(0));
               }else{
                   System.out.println("No file added.");
               }

           }catch (Exception e){e.printStackTrace();}

            response.send(imageUrl);
        });


        //Express method calling our Database method getNoteById
        app.get("/rest/Notes/:id", (req, res)->{

            //Making a variable to parse it from String to Integer
            int id = Integer.parseInt(req.getParam("id"));

            Note notes = db.getNoteById(id);

            res.json(notes);

        });

        //Express method calling our Database method updateNote
        app.post("/rest/Notes/update",(req, res)->{

            //Getting the whole note and parsing it as a Note class
            Note note = (Note) req.getBody(Note.class);

            System.out.println(note.toString());

            db.updateNote(note);

        });

        //Express method calling our Database method deleteNote
        app.delete("/rest/Notes/delete",(req, res)->{

            //Getting the whole note and parsing it as a Note class
            Note note = (Note) req.getBody(Note.class);

            System.out.println(note.toString());

            db.deleteNote(note);

        });


        //Connection to frontend by localizing the folder "www"
        try{
            app.use(Middleware.statics(Paths.get("src/www").toString()));
        }catch(IOException e){e.printStackTrace();}


        //Express method declaring which port we are running our program on
        app.listen(1000);
        System.out.println("System started at port 1000");
        System.out.println("Testing");

    }
}
