CREATE TABLE IF NOT EXISTS fetched_photos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    image_binary BLOB NOT NULL,
    timestamp TEXT NOT NULL,
    source_website TEXT NOT NULL
);