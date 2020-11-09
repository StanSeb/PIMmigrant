public class Note {

    private int id;
    private String title;
    private String content;
    private String filename;
    private int note_id;
    private int timestamp;


    public Note(int id, String title, String content, String filename, int note_id, int timestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.filename = filename;
        this.note_id = note_id;
        this.timestamp = timestamp;
    }
    public Note() {}

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", filename='" + filename + '\'' +
                ", note_id=" + note_id +
                ", timestamp=" + timestamp +
                '}';
    }
}


