import express.Express;
import express.middleware.Middleware;
import org.apache.commons.fileupload.FileItem;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Express app = new Express();
        Database db = new Database();

        app.post("/api/imgs", (req, res)->{
            String fileName = null;

            try {
                List<FileItem> files = req.getFormData("files");
                fileName = db.uploadImage(files.get(0));
                /////Upload image function finns som mall i Database Class.

        }catch (Exception e){
            /// no image upploaded
            e.printStackTrace();
            }
        res.send(fileName);

        });

        app.get("/rest/Notes",(req, res) ->{
           List<Note> notes = db.getAllNotes();

           res.json(notes);
        });

        app.get("/rest/Notes/:title", (req, res)->{

            String title = req.getParam("title");

            Note notes = db.getNoteByTitle(title);

            res.json(notes);

        });

        app.post("/rest/Notes",(req, res)->{

            Note note = (Note) req.getBody(Note.class);

            System.out.println(note.toString());

            db.updateNote(note);


        });

        app.delete("/rest/Notes",(req, res)->{

            Note note = (Note) req.getBody(Note.class);

            System.out.println(note.toString());

            db.deleteNote(note);


        });

        app.post("/rest/Notes",(req, res)->{

            Note note = (Note)req.getBody(Note.class);

            System.out.println(note.toString());

            db.createNotes(note);


        });
        ///////////////////////////


        app.post("/api/imgs", (req, res)->{
            String filename = null;

            try {
                List<FileItem> files = req.getFormData("files");
                filename = db.uploadImage(files.get(0));
                /////Upload image function finns som mall i Database Class.

        }catch (Exception e){
            /// no image upploaded
            e.printStackTrace();
            }
        res.send(filename);

        });




        try{
            app.use(Middleware.statics(Paths.get("src/www").toString()));
        }catch(IOException e){e.printStackTrace();}


        app.listen(1000);
        System.out.println("System started at port 1000");
        System.out.println("Testing");

    }
}
