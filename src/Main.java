import express.Express;
import express.middleware.Middleware;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Express app = new Express();
        app.get("/",(req, res)->{
            res.send();
        });

        app.get("/rest/Note", (req, res)->{
            List<Note> notes = db.getNotes();

            res.json(notes);
        });

        try{
            app.use(Middleware.statics(Paths.get("src/www").toString()));
        }catch(IOException e){e.printStackTrace();}


        app.listen(1000);
        System.out.println("System started at port 1000");

    }
}
