import java.sql.*;
import java.util.Scanner;

class BookManagementException extends Exception {
    public BookManagementException(String message) {
        super(message);
    }
}

class BookNotFoundException extends BookManagementException {
    public BookNotFoundException(String message) {
        super(message);
    }
}

public class BookManagement {
    private int bookId;
    private String title;
    private int authorId;
    private String isbn;
    private int quantity;

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Add a new book
    public void addBook(Connection conn, Scanner scanner) throws BookManagementException {
        try {
            System.out.print("Enter book title: ");
            setTitle(scanner.nextLine());
            System.out.print("Enter author ID: ");
            setAuthorId(scanner.nextInt());
            scanner.nextLine(); // Consume newline
            System.out.print("Enter ISBN: ");
            setIsbn(scanner.nextLine());
            System.out.print("Enter quantity available: ");
            setQuantity(scanner.nextInt());

            String sql = "INSERT INTO Book (title, author_id, ISBN, quantity_available) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, getTitle());
                pstmt.setInt(2, getAuthorId());
                pstmt.setString(3, getIsbn());
                pstmt.setInt(4, getQuantity());
                pstmt.executeUpdate();
                System.out.println("Book added successfully!");
            }
        } catch (SQLException e) {
            throw new BookManagementException("Error adding book: " + e.getMessage());
        }
    }

    // View book details
    public void viewBookDetails(Connection conn, Scanner scanner)
            throws BookNotFoundException, BookManagementException {
        try {
            System.out.print("Enter book ID to view details: ");
            setBookId(scanner.nextInt());

            String sql = "SELECT * FROM Book WHERE book_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, getBookId());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    setTitle(rs.getString("title"));
                    setAuthorId(rs.getInt("author_id"));
                    setIsbn(rs.getString("ISBN"));
                    setQuantity(rs.getInt("quantity_available"));

                    System.out.println("Book ID: " + getBookId());
                    System.out.println("Title: " + getTitle());
                    System.out.println("Author ID: " + getAuthorId());
                    System.out.println("ISBN: " + getIsbn());
                    System.out.println("Quantity Available: " + getQuantity());
                } else {
                    throw new BookNotFoundException("Book with ID " + getBookId() + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new BookManagementException("Error viewing book details: " + e.getMessage());
        }
    }

    // Update book information
    public void updateBookInformation(Connection conn, Scanner scanner)
            throws BookNotFoundException, BookManagementException {
        try {
            System.out.print("Enter book ID to update: ");
            setBookId(scanner.nextInt());
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new book title: ");
            setTitle(scanner.nextLine());
            System.out.print("Enter new author ID: ");
            setAuthorId(scanner.nextInt());
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new ISBN: ");
            setIsbn(scanner.nextLine());
            System.out.print("Enter new quantity available: ");
            setQuantity(scanner.nextInt());

            String sql = "UPDATE Book SET title = ?, author_id = ?, ISBN = ?, quantity_available = ? WHERE book_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, getTitle());
                pstmt.setInt(2, getAuthorId());
                pstmt.setString(3, getIsbn());
                pstmt.setInt(4, getQuantity());
                pstmt.setInt(5, getBookId());
                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Book information updated successfully!");
                } else {
                    throw new BookNotFoundException("Book with ID " + getBookId() + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new BookManagementException("Error updating book information: " + e.getMessage());
        }
    }

    // Delete a book
    public void deleteBook(Connection conn, Scanner scanner) throws BookNotFoundException, BookManagementException {
        try {
            System.out.print("Enter book ID to delete: ");
            setBookId(scanner.nextInt());

            // Delete borrowing history associated with the book
            String deleteBorrowingHistorySql = "DELETE FROM BorrowingHistory WHERE book_id = ?";
            try (PreparedStatement pstmt1 = conn.prepareStatement(deleteBorrowingHistorySql)) {
                pstmt1.setInt(1, getBookId());
                pstmt1.executeUpdate();
            }

            // Delete the book
            String deleteBookSql = "DELETE FROM Book WHERE book_id = ?";
            try (PreparedStatement pstmt2 = conn.prepareStatement(deleteBookSql)) {
                pstmt2.setInt(1, getBookId());
                int rowsDeleted = pstmt2.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Book deleted successfully!");
                } else {
                    throw new BookNotFoundException("Book with ID " + getBookId() + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new BookManagementException("Error deleting book: " + e.getMessage());
        }
    }
}
