CREATE DATABASE library_db;

USE library_db;

CREATE TABLE Author (
    author_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    biography TEXT
);

CREATE TABLE Book (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    author_id INT,
    ISBN VARCHAR(20),
    quantity_available INT,
    FOREIGN KEY (author_id) REFERENCES Author(author_id)
);

CREATE TABLE LibraryMember (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(15),
    email VARCHAR(100)
);

CREATE TABLE BorrowingHistory (
    borrowing_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT,
    member_id INT,
    issue_date DATE,
    return_date DATE,
    FOREIGN KEY (book_id) REFERENCES Book(book_id),
    FOREIGN KEY (member_id) REFERENCES LibraryMember(member_id)
);
INSERT INTO author(author_id,name,biography) VALUES(3,"Albert Camus","The Stranger"),(4,"Charles Dickens","A Tale of Two Cities"),(5,"Leo Tolstoy","War and Peace");
INSERT INTO Book (book_id, title, author_id, ISBN, quantity_available) VALUES
(2, 'The Golden Compass', 2, '978-0440418320', 3),
(3, 'The Lightning Thief', 3, '978-0786838653', 4),
(4, 'The Magicians', 4, '978-0452296299', 2);
INSERT INTO LibraryMember (name, address, phone_number, email)
VALUES 
('Jane Smith', '456 Elm St, Metropolis', '987-654-3210', 'jane.smith@example.com'),
('Alice Johnson', '789 Oak St, Gotham', '555-123-4567', 'alice.johnson@example.com'),
('Bob Brown', '101 Maple St, Star City', '555-987-6543', 'bob.brown@example.com'),
('Charlie Davis', '202 Pine St, Central City', '555-456-7890', 'charlie.davis@example.com');
INSERT INTO BorrowingHistory (book_id, member_id, issue_date, return_date)
VALUES 
(2, 1, '2024-08-01', '2024-08-15'),
(3, 6, '2024-08-05', '2024-08-20'),
(4, 7, '2024-08-10', '2024-08-25'),
(2, 8, '2024-08-12', '2024-08-27'),
(3, 9, '2024-08-15', '2024-08-30');

