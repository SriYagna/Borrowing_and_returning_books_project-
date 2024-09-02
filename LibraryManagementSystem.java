import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LibraryManagementSystem {

    public static void main(String[] args) {
        // Main method that establishes the database connection and provides the main
        // menu loop
        try (Connection connection = DatabaseConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                // Display the main menu
                System.out.println("Library Management System");
                System.out.println("1. Add a new book");
                System.out.println("2. View book details");
                System.out.println("3. Update book information");
                System.out.println("4. Delete a book");
                System.out.println("5. Add a new author");
                System.out.println("6. View author details");
                System.out.println("7. Update author information");
                System.out.println("8. Delete an author");
                System.out.println("9. Register a new library member");
                System.out.println("10. View member details");
                System.out.println("11. Update member information");
                System.out.println("12. Delete a member");
                System.out.println("13. Issue a book to a member");
                System.out.println("14. Return a book from a member");
                System.out.println("15. View borrowing history of a member");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                // Switch case to handle different menu choices
                switch (choice) {
                    case 1:
                        addBook(connection, scanner);
                        break;
                    case 2:
                        viewBookDetails(connection, scanner);
                        break;
                    case 3:
                        updateBookInformation(connection, scanner);
                        break;
                    case 4:
                        deleteBook(connection, scanner);
                        break;
                    case 5:
                        addAuthor(connection, scanner);
                        break;
                    case 6:
                        viewAuthorDetails(connection, scanner);
                        break;
                    case 7:
                        updateAuthorInformation(connection, scanner);
                        break;
                    case 8:
                        deleteAuthor(connection, scanner);
                        break;
                    case 9:
                        registerlibrarymember(connection, scanner);
                        break;
                    case 10:
                        viewlibrarymemberDetails(connection, scanner);
                        break;
                    case 11:
                        updatelibrarymemberInformation(connection, scanner);
                        break;
                    case 12:
                        deletelibrarymember(connection, scanner);
                        break;
                    case 13:
                        issueBookTolibrarymember(connection, scanner);
                        break;
                    case 14:
                        returnBookFromlibrarymember(connection, scanner);
                        break;
                    case 15:
                        viewborrowinghistory(connection, scanner);
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return; // Exit the program
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 1. Add a new book to the library database
    private static void addBook(Connection conn, Scanner scanner) {
        try {
            // Prompt the user for book details
            System.out.print("Enter book title: ");
            String title = scanner.nextLine();
            System.out.print("Enter author ID: ");
            int authorId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter ISBN: ");
            String isbn = scanner.nextLine();
            System.out.print("Enter quantity available: ");
            int quantity = scanner.nextInt();

            // SQL query to insert a new book
            String sql = "INSERT INTO Book (title, author_id, ISBN, quantity_available) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Set parameters and execute the query
                pstmt.setString(1, title);
                pstmt.setInt(2, authorId);
                pstmt.setString(3, isbn);
                pstmt.setInt(4, quantity);
                pstmt.executeUpdate();
                System.out.println("Book added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. View details of a specific book by its ID
    private static void viewBookDetails(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter book ID to view details: ");
            int bookId = scanner.nextInt();

            // SQL query to select a book based on its ID
            String sql = "SELECT * FROM Book WHERE book_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, bookId);
                ResultSet rs = pstmt.executeQuery(); // Execute query and get the result set
                if (rs.next()) {
                    // Print book details if found
                    System.out.println("Book ID: " + rs.getInt("book_id"));
                    System.out.println("Title: " + rs.getString("title"));
                    System.out.println("Author ID: " + rs.getInt("author_id"));
                    System.out.println("ISBN: " + rs.getString("ISBN"));
                    System.out.println("Quantity Available: " + rs.getInt("quantity_available"));
                } else {
                    System.out.println("Book not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. Update information of a specific book
    private static void updateBookInformation(Connection conn, Scanner scanner) {
        try {
            // Prompt user for new book details
            System.out.print("Enter book ID to update: ");
            int bookId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new book title: ");
            String title = scanner.nextLine();
            System.out.print("Enter new author ID: ");
            int authorId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new ISBN: ");
            String isbn = scanner.nextLine();
            System.out.print("Enter new quantity available: ");
            int quantity = scanner.nextInt();

            // Check if the author ID exists
            if (!doesAuthorExist(conn, authorId)) {
                System.out.println("Error: The specified author ID does not exist.");
                return; // Exit the method if author ID is invalid
            }

            // SQL query to update the book details
            String sql = "UPDATE Book SET title = ?, author_id = ?, ISBN = ?, quantity_available = ? WHERE book_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Set parameters and execute the update
                pstmt.setString(1, title);
                pstmt.setInt(2, authorId);
                pstmt.setString(3, isbn);
                pstmt.setInt(4, quantity);
                pstmt.setInt(5, bookId);
                int rowsUpdated = pstmt.executeUpdate(); // Execute update
                if (rowsUpdated > 0) {
                    System.out.println("Book information updated successfully!");
                } else {
                    System.out.println("Book not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to check if an author ID exists in the Author table
    private static boolean doesAuthorExist(Connection conn, int authorId) {
        String sql = "SELECT COUNT(*) FROM Author WHERE author_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, authorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Check if count > 0
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Delete a book by its ID
    private static void deleteBook(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter book ID to delete: ");
            int bookId = scanner.nextInt();

            // SQL query to delete borrowing history associated with the book
            String deleteBorrowingHistorySql = "DELETE FROM borrowinghistory WHERE book_id = ?";
            try (PreparedStatement pstmt1 = conn.prepareStatement(deleteBorrowingHistorySql)) {
                pstmt1.setInt(1, bookId);
                pstmt1.executeUpdate(); // Delete associated borrowing history
            }

            // SQL query to delete the book
            String deleteBookSql = "DELETE FROM Book WHERE book_id = ?";
            try (PreparedStatement pstmt2 = conn.prepareStatement(deleteBookSql)) {
                pstmt2.setInt(1, bookId);
                int rowsDeleted = pstmt2.executeUpdate(); // Execute delete
                if (rowsDeleted > 0) {
                    System.out.println("Book deleted successfully!");
                } else {
                    System.out.println("Book not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 5. Add a new author to the library database
    private static void addAuthor(Connection conn, Scanner scanner) {
        try {
            // Prompt the user for author details
            System.out.print("Enter author name: ");
            String name = scanner.nextLine();
            System.out.print("Enter author biography: ");
            String biography = scanner.nextLine();

            // SQL query to insert a new author
            String sql = "INSERT INTO Author (name, biography) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Set parameters and execute the query
                pstmt.setString(1, name);
                pstmt.setString(2, biography);
                pstmt.executeUpdate();
                System.out.println("Author added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 6. View details of a specific author by their ID
    private static void viewAuthorDetails(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter author ID to view details: ");
            int authorId = scanner.nextInt();

            // SQL query to select an author based on their ID
            String sql = "SELECT * FROM Author WHERE author_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, authorId);
                ResultSet rs = pstmt.executeQuery(); // Execute query and get the result set
                if (rs.next()) {
                    // Print author details if found
                    System.out.println("Author ID: " + rs.getInt("author_id"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Biography: " + rs.getString("biography"));
                } else {
                    System.out.println("Author not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 7. Update information of a specific author
    private static void updateAuthorInformation(Connection conn, Scanner scanner) {
        try {
            // Prompt user for new author details
            System.out.print("Enter author ID to update: ");
            int authorId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new author name: ");
            String name = scanner.nextLine();
            System.out.print("Enter new author biography: ");
            String biography = scanner.nextLine();

            // SQL query to update the author details
            String sql = "UPDATE Author SET name = ?, biography = ? WHERE author_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Set parameters and execute the update
                pstmt.setString(1, name);
                pstmt.setString(2, biography);
                pstmt.setInt(3, authorId);
                int rowsUpdated = pstmt.executeUpdate(); // Execute update
                if (rowsUpdated > 0) {
                    System.out.println("Author information updated successfully!");
                } else {
                    System.out.println("Author not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 8. Delete an author by their ID
    private static void deleteAuthor(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter author ID to delete: ");
            int authorId = scanner.nextInt();

            // SQL query to delete an author
            String sql = "DELETE FROM Author WHERE author_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, authorId);
                int rowsDeleted = pstmt.executeUpdate(); // Execute delete
                if (rowsDeleted > 0) {
                    System.out.println("Author deleted successfully!");
                } else {
                    System.out.println("Author not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 9. Register a new library member
    private static void registerlibrarymember(Connection conn, Scanner scanner) {
        try {
            // Prompt the user for member details
            System.out.print("Enter member name: ");
            String name = scanner.nextLine();
            System.out.print("Enter member email: ");
            String email = scanner.nextLine();
            System.out.print("Enter member phone number: ");
            String phone = scanner.nextLine();
            System.out.print("Enter member address: ");
            String address = scanner.nextLine();

            // SQL query to insert a new member
            String sql = "INSERT INTO librarymember (name, email, phone_number, address) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Set parameters and execute the query
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, phone);
                pstmt.setString(4, address);
                pstmt.executeUpdate();
                System.out.println("Member registered successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 10. View details of a specific member by their ID
    private static void viewlibrarymemberDetails(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter member ID to view details: ");
            int memberId = scanner.nextInt();

            // SQL query to select a member based on their ID
            String sql = "SELECT * FROM librarymember WHERE member_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, memberId);
                ResultSet rs = pstmt.executeQuery(); // Execute query and get the result set
                if (rs.next()) {
                    // Print member details if found
                    System.out.println("Member ID: " + rs.getInt("member_id"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("Phone Number: " + rs.getString("phone_number"));
                    System.out.println("Address: " + rs.getString("address")); // Added to show address
                } else {
                    System.out.println("Member not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 11. Update information of a specific member
    private static void updatelibrarymemberInformation(Connection conn, Scanner scanner) {
        try {
            // Prompt user for member details to update
            System.out.print("Enter member ID to update: ");
            int memberId = scanner.nextInt();
            scanner.nextLine(); // Consume the leftover newline character

            System.out.print("Enter new member name: ");
            String name = scanner.nextLine(); // Use nextLine() for string input

            System.out.print("Enter new member email: ");
            String email = scanner.nextLine(); // Use nextLine() for string input

            System.out.print("Enter new member phone number: ");
            String phoneNumber = scanner.nextLine(); // Use nextLine() for phone number to avoid InputMismatchException

            System.out.print("Enter new member address: ");
            String address = scanner.nextLine(); // Use nextLine() for address input

            // SQL query to update the member details
            String sql = "UPDATE librarymember SET name = ?, email = ?, phone_number = ?, address = ? WHERE member_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Set parameters and execute the update
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, phoneNumber); // Use setString for phone number
                pstmt.setString(4, address); // Use setString for address
                pstmt.setInt(5, memberId);

                int rowsUpdated = pstmt.executeUpdate(); // Execute update
                if (rowsUpdated > 0) {
                    System.out.println("Member information updated successfully!");
                } else {
                    System.out.println("Member not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter the correct data type for each field.");
            scanner.nextLine(); // Clear the scanner buffer
        }
    }

    // 12. Delete a member by their ID
    private static void deletelibrarymember(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter member ID to delete: ");
            int memberId = scanner.nextInt();

            // SQL query to delete a member
            String sql = "DELETE FROM librarymember WHERE member_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, memberId);
                int rowsDeleted = pstmt.executeUpdate(); // Execute delete
                if (rowsDeleted > 0) {
                    System.out.println("Member deleted successfully!");
                } else {
                    System.out.println("Member not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 13. Issue a book to a library member
    private static void issueBookTolibrarymember(Connection conn, Scanner scanner) {
        try {
            // Prompt user for issuing details
            System.out.print("Enter member ID: ");
            int memberId = scanner.nextInt();
            System.out.print("Enter book ID: ");
            int bookId = scanner.nextInt();

            // SQL queries to update the database for issuing a book
            String issueSql = "INSERT INTO borrowinghistory (member_id, book_id, issue_date, return_date) VALUES (?, ?, NOW(), NULL)";
            String updateBookSql = "UPDATE Book SET quantity_available = quantity_available - 1 WHERE book_id = ?";

            try (PreparedStatement issueStmt = conn.prepareStatement(issueSql);
                    PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql)) {

                // Execute book issue and update queries
                issueStmt.setInt(1, memberId);
                issueStmt.setInt(2, bookId);
                updateBookStmt.setInt(1, bookId);

                issueStmt.executeUpdate();
                updateBookStmt.executeUpdate();

                System.out.println("Book issued successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 14. Return a book from a member
    private static void returnBookFromlibrarymember(Connection conn, Scanner scanner) {
        try {
            // Prompt user for return details
            System.out.print("Enter member ID: ");
            int memberId = scanner.nextInt();
            System.out.print("Enter book ID: ");
            int bookId = scanner.nextInt();

            // SQL queries to update the database for returning a book
            String returnSql = "UPDATE borrowinghistory SET return_date = NOW() WHERE member_id = ? AND book_id = ? AND return_date IS NULL";
            String updateBookSql = "UPDATE Book SET quantity_available = quantity_available + 1 WHERE book_id = ?";

            try (PreparedStatement returnStmt = conn.prepareStatement(returnSql);
                    PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql)) {

                // Execute book return and update queries
                returnStmt.setInt(1, memberId);
                returnStmt.setInt(2, bookId);
                updateBookStmt.setInt(1, bookId);

                returnStmt.executeUpdate();
                updateBookStmt.executeUpdate();

                System.out.println("Book returned successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 15. View borrowing history of a specific member
    private static void viewborrowinghistory(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter member ID to view borrowing history: ");
            int memberId = scanner.nextInt();

            // SQL query to fetch borrowing history of a member
            String sql = "SELECT * FROM borrowinghistory WHERE member_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, memberId);
                ResultSet rs = pstmt.executeQuery(); // Execute query and get the result set
                System.out.println("Borrowing history for member ID: " + memberId);
                while (rs.next()) {
                    // Print borrowing details for each record found
                    System.out.println("Book ID: " + rs.getInt("book_id"));
                    System.out.println("Issue Date: " + rs.getDate("issue_date"));
                    System.out.println("Return Date: " + rs.getDate("return_date"));
                    System.out.println("---");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
