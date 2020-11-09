public class Note {

    private int id;
    private String title;
    private String content;
    private String filename;
    private int note_id;
    private long timestamp;

    public Note() {}

    public Note(int id, String title, String content, long timestamp, String filename, int note_id) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.filename = filename;
        this.note_id = note_id;
    }

    public Note( String title, String content,  long timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Note(int id, String title, String filename, int note_id, long timestamp) {
        this.id = id;
        this.title = title;
        this.filename = filename;
        this.note_id = note_id;
        this.timestamp = timestamp;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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


