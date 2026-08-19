CREATE TABLE entries (
    row_id INT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    entry_id INT,
    entry_url TEXT,
    entry_title TEXT,
    entry_body MEDIUMTEXT,
    entry_notes VARCHAR(2047),
    FULLTEXT INDEX search_index (entry_url,entry_title,entry_body,entry_notes)
) ENGINE=InnoDB;