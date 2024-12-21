CREATE TABLE hotel (
    id INT PRIMARY KEY AUTO_INCREMENT,
    agent_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    city VARCHAR(100) NOT NULL,
    address VARCHAR(200) NOT NULL,
    stars INT NOT NULL CHECK (stars BETWEEN 1 AND 5),
    image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (agent_id) REFERENCES account(id)
);
