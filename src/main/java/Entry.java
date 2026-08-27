public class Entry {
    private int rowId;
    private int entryId;
    private String entryUrl;
    private String entryTitle;
    private String entryBody;
    private String entryNotes;
	private final int DEFAULT_ROW_ID = -1;
	private final int DEFAULT_ENTRY_ID = -1;

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getEntryUrl() {
        return entryUrl;
    }

    public void setEntryUrl(String entryUrl) {
        this.entryUrl = entryUrl;
    }

    public String getEntryTitle() {
		return entryTitle;
	}

    public void setEntryTitle(String entryTitle) {
        this.entryTitle = entryTitle;
    }

    public String getEntryBody() {
		return entryBody;
	}

    public void setEntryBody(String entryBody) {
        this.entryBody = entryBody;
    }

    public String getEntryNotes() {
		return entryNotes;
	}

    public void setEntryNotes(String entryNotes) {
		this.entryNotes = entryNotes;
	}

    public Entry(int rowId, int entryId, String entryUrl, String entryTitle, String entryBody, String entryNotes) {
        this.rowId = rowId;
        this.entryId = entryId;
        this.entryUrl = entryUrl;
        this.entryTitle = entryTitle;
        this.entryBody = entryBody;
        this.entryNotes = entryNotes;
    }

    public Entry(int entryId, String entryUrl, String entryTitle, String entryBody, String entryNotes) {
        this.rowId = DEFAULT_ROW_ID;
        this.entryId = entryId;
        this.entryUrl = entryUrl;
        this.entryTitle = entryTitle;
        this.entryBody = entryBody;
        this.entryNotes = entryNotes;
    }

    public Entry(String entryUrl, String entryTitle, String entryBody, String entryNotes) {
        this.rowId = DEFAULT_ROW_ID;
        this.entryId = DEFAULT_ENTRY_ID;
        this.entryUrl = entryUrl;
        this.entryTitle = entryTitle;
        this.entryBody = entryBody;
        this.entryNotes = entryNotes;
    }

    public Entry(){
        this.rowId = DEFAULT_ROW_ID;
        this.entryId = DEFAULT_ENTRY_ID;
        this.entryUrl = null;
        this.entryTitle = null;
        this.entryBody = null;
        this.entryNotes = null;
    }
}
