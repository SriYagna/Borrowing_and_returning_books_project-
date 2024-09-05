import java.sql.*;
import java.util.Scanner;

// Custom Exceptions
class BorrowingAndReturningException extends Exception {
    public BorrowingAndReturningException(String message) {
        super(message);
    }
}

class BookNotAvailableException extends BorrowingAndReturningException {
    public BookNotAvailableException(String message) {
        super(message);
    }
}

class MemberNotFoundException extends BorrowingAndReturningException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}

public class BorrowingAndReturningBooks {
    private int bookId;
    private int memberId;
    private Date issueDate;
    private Date returnDate;

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    // Issue a book to a member
    public void issueBook(Connection conn, Scanner scanner) throws BorrowingAndReturningException {
        System.out.print("Enter book ID to issue: ");
        setBookId(scanner.nextInt());
        System.out.print("Enter member ID to issue to: ");
        setMemberId(scanner.nextInt());
        scanner.nextLine(); // Consume newline
        System.out.print("Enter issue date (YYYY-MM-DD): ");
        setIssueDate(Date.valueOf(scanner.nextLine()));

        try {
            // Check if book is available
            String checkAvailabilitySql = "SELECT quantity_available FROM Book WHERE book_id = ?";
            try (PreparedStatement pstmt1 = conn.prepareStatement(checkAvailabilitySql)) {
                pstmt1.setInt(1, getBookId());
                ResultSet rs = pstmt1.executeQuery();
                if (rs.next()) {
                    int quantityAvailable = rs.getInt("quantity_available");
                    if (quantityAvailable > 0) {
                        // Issue the book
                        String issueBookSql = "INSERT INTO BorrowingHistory (book_id, member_id, issue_date) VALUES (?, ?, ?)";
                        try (PreparedStatement pstmt2 = conn.prepareStatement(issueBookSql)) {
                            pstmt2.setInt(1, getBookId());
                            pstmt2.setInt(2, getMemberId());
                            pstmt2.setDate(3, getIssueDate());
                            pstmt2.executeUpdate();
                        }

                        // Update quantity
                        String updateQuantitySql = "UPDATE Book SET quantity_available = quantity_available - 1 WHERE book_id = ?";
                        try (PreparedStatement pstmt3 = conn.prepareStatement(updateQuantitySql)) {
                            pstmt3.setInt(1, getBookId());
                            pstmt3.executeUpdate();
                        }

                        System.out.println("Book issued successfully!");
                    } else {
                        throw new BookNotAvailableException("Book with ID " + getBookId() + " is not available.");
                    }
                } else {
                    throw new BookNotAvailableException("Book with ID " + getBookId() + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new BorrowingAndReturningException("Error accessing database: " + e.getMessage());
        }
    }

    // Return a book from a member
    public void returnBook(Connection conn, Scanner scanner) throws BorrowingAndReturningException {
        System.out.print("Enter book ID to return: ");
        setBookId(scanner.nextInt());
        System.out.print("Enter member ID returning the book: ");
        setMemberId(scanner.nextInt());
        scanner.nextLine(); // Consume newline
        System.out.print("Enter return date (YYYY-MM-DD): ");
        setReturnDate(Date.valueOf(scanner.nextLine()));

        try {
            // Check if the book was borrowed
            String checkBorrowingSql = "SELECT * FROM BorrowingHistory WHERE book_id = ? AND member_id = ? AND return_date IS NULL";
            try (PreparedStatement pstmt1 = conn.prepareStatement(checkBorrowingSql)) {
                pstmt1.setInt(1, getBookId());
                pstmt1.setInt(2, getMemberId());
                ResultSet rs = pstmt1.executeQuery();
                if (rs.next()) {
                    // Update return date
                    String updateReturnDateSql = "UPDATE BorrowingHistory SET return_date = ? WHERE book_id = ? AND member_id = ? AND return_date IS NULL";
                    try (PreparedStatement pstmt2 = conn.prepareStatement(updateReturnDateSql)) {
                        pstmt2.setDate(1, getReturnDate());
                        pstmt2.setInt(2, getBookId());
                        pstmt2.setInt(3, getMemberId());
                        pstmt2.executeUpdate();
                    }

                    // Update quantity
                    String updateQuantitySql = "UPDATE Book SET quantity_available = quantity_available + 1 WHERE book_id = ?";
                    try (PreparedStatement pstmt3 = conn.prepareStatement(updateQuantitySql)) {
                        pstmt3.setInt(1, getBookId());
                        pstmt3.executeUpdate();
                    }

                    System.out.println("Book returned successfully!");
                } else {
                    throw new BorrowingAndReturningException(
                            "No record of this book being borrowed by member ID " + getMemberId() + ".");
                }
            }
        } catch (SQLException e) {
            throw new BorrowingAndReturningException("Error accessing database: " + e.getMessage());
        }
    }

    // View borrowing history of a member
    public void viewBorrowingHistory(Connection conn, Scanner scanner) throws BorrowingAndReturningException {
        System.out.print("Enter member ID to view borrowing history: ");
        setMemberId(scanner.nextInt());
        scanner.nextLine(); // Consume newline

        try {
            String sql = "SELECT b.title, bh.issue_date, bh.return_date FROM BorrowingHistory bh JOIN Book b ON bh.book_id = b.book_id WHERE bh.member_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, getMemberId());
                ResultSet rs = pstmt.executeQuery();
                System.out.println("Borrowing History for Member ID " + getMemberId() + ":");
                while (rs.next()) {
                    String title = rs.getString("title");
                    Date issueDate = rs.getDate("issue_date");
                    Date returnDate = rs.getDate("return_date");

                    System.out.println("Title: " + title);
                    System.out.println("Issue Date: " + issueDate);
                    if (returnDate != null) {
                        System.out.println("Return Date: " + returnDate);
                    } else {
                        System.out.println("Return Date: Not yet returned");
                    }
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            throw new BorrowingAndReturningException("Error accessing database: " + e.getMessage());
        }
    }
}
