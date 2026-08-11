public class Entry {
    private int row_id;
    private int entry_id;
    private String url;
    private String title;
    private String content;
    private String notes;

    public int getRowId() {
        return row_id;
    }

    public void setRowId(int row_id) {
        this.row_id = row_id;
    }

    public int getEntryId() {
        return entry_id;
    }

    public void setEntryId(int entry_id) {
        this.entry_id = entry_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() { return content; }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }

    public Entry(int row_id, int entry_id, String url, String title, String content, String notes) {
        this.row_id = row_id;
        this.entry_id = entry_id;
        this.url = url;
        this.title = title;
        this.content = content;
        this.notes = notes;
    }

    public Entry(int entry_id, String url, String title, String content, String notes) {
        this.row_id = -1;
        this.entry_id = entry_id;
        this.url = url;
        this.title = title;
        this.content = content;
        this.notes = notes;
    }

    public Entry(String url, String title, String content, String notes) {
        this.row_id = -1;
        this.entry_id = -1;
        this.url = url;
        this.title = title;
        this.content = content;
        this.notes = notes;
    }

    public Entry(){
        this.row_id = -1;
        this.entry_id = -1;
        this.url = null;
        this.title = null;
        this.content = null;
        this.notes = null;
    }
}
