import express.Express;
import express.middleware.Middleware;

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

        app.get("/rest/Notes/:title", (req, res)->{

            String title = req.getParam("title");


            Note notes = db.getNoteByTitle(title);

            res.json(notes);

        });

           app.post("/rest/Notes",(req, res)->{

               Note note = (Note)req.getBody(Note.class);
              //Note content = (Note)req.getBody(Note.class);

                System.out.println(note.toString());
                //System.out.println(content.toString());

                db.createNotes(note);

        });

        try{
            app.use(Middleware.statics(Paths.get("src/www").toString()));
        }catch(IOException e){e.printStackTrace();}


        app.listen(1000);
        System.out.println("System started at port 1000");

    }
}
