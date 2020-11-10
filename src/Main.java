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

        app.get("/rest/Notes",(req, res) ->{
           List<Note> notes = db.getAllNotes();

           res.json(notes);
        });


        app.post("/rest/Notes",(req, res)->{

            Note note = (Note)req.getBody(Note.class);

            System.out.println(note.toString());

            db.createNotes(note);

        });

        app.post("/api/file-upload", (request, response) -> {
           String imageUrl = null;

           try{
               List<FileItem> files = request.getFormData("files");
               imageUrl = db.uploadImage(files.get(0));
           }catch (Exception e){e.printStackTrace();}

            response.send(imageUrl);
        });


        try{
            app.use(Middleware.statics(Paths.get("src/www").toString()));
        }catch(IOException e){e.printStackTrace();}


        app.listen(1000);
        System.out.println("System started at port 1000");
        System.out.println("Testing");

    }
}
