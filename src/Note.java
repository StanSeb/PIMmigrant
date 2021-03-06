public class Note {

    private int id;
    private String title;
    private String content;
    private long timestamp;
    private String imgUrl;

    public Note(int id, String title, String content, long timestamp, String imgUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.imgUrl = imgUrl;
    }

    public Note() {}


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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}