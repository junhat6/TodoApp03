CREATE TABLE
    todos (
        id UUID PRIMARY KEY,
        text VARCHAR(100) NOT NULL,
        category VARCHAR(50) NOT NULL,
        completed BOOLEAN NOT NULL DEFAULT FALSE,
        created_at TIMESTAMP NOT NULL,
        updated_at TIMESTAMP NOT NULL
    );