import java.sql.*;
import java.util.Scanner;

public class Main {
    private static Connection getConnection() throws SQLException {
        // Change the following URL, username, and password according to your setup
        String url = "jdbc:mysql://localhost:3306/library_db";
        String user = "root";
        String password = "Sriyagna@573";

        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        // Use try-with-resources for Scanner to ensure it gets closed
        try (Scanner scanner = new Scanner(System.in); Connection conn = getConnection()) {
            while (true) {
                System.out.println("Library Management System");
                System.out.println("1. Book Management");
                System.out.println("2. Author Management");
                System.out.println("3. Library Member Management");
                System.out.println("4. Borrowing and Returning Books");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        BookManagement bookManagement = new BookManagement();
                        System.out.println("Book Management");
                        System.out.println("1. Add a new book");
                        System.out.println("2. View book details");
                        System.out.println("3. Update book information");
                        System.out.println("4. Delete a book");
                        System.out.print("Choose an option: ");
                        int bookChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        try {
                            switch (bookChoice) {
                                case 1:
                                    bookManagement.addBook(conn, scanner);
                                    break;
                                case 2:
                                    bookManagement.viewBookDetails(conn, scanner);
                                    break;
                                case 3:
                                    bookManagement.updateBookInformation(conn, scanner);
                                    break;
                                case 4:
                                    bookManagement.deleteBook(conn, scanner);
                                    break;
                                default:
                                    System.out.println("Invalid option.");
                            }
                        } catch (BookManagementException e) {
                            System.err.println(e.getMessage());
                        }
                        break;

                    case 2:
                        AuthorManagement authorManagement = new AuthorManagement();
                        System.out.println("Author Management");
                        System.out.println("1. Add a new author");
                        System.out.println("2. View author details");
                        System.out.println("3. Update author information");
                        System.out.println("4. Delete an author");
                        System.out.print("Choose an option: ");
                        int authorChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        try {
                            switch (authorChoice) {
                                case 1:
                                    authorManagement.addAuthor(conn, scanner);
                                    break;
                                case 2:
                                    authorManagement.viewAuthorDetails(conn, scanner);
                                    break;
                                case 3:
                                    authorManagement.updateAuthorInformation(conn, scanner);
                                    break;
                                case 4:
                                    authorManagement.deleteAuthor(conn, scanner);
                                    break;
                                default:
                                    System.out.println("Invalid option.");
                            }
                        } catch (AuthorManagementException e) {
                            System.err.println(e.getMessage());
                        }
                        break;

                    case 3:
                        LibraryMemberManagement libraryMemberManagement = new LibraryMemberManagement();
                        System.out.println("Library Member Management");
                        System.out.println("1. Register a new member");
                        System.out.println("2. View member details");
                        System.out.println("3. Update member information");
                        System.out.println("4. Delete a member");
                        System.out.print("Choose an option: ");
                        int memberChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        try {
                            switch (memberChoice) {
                                case 1:
                                    libraryMemberManagement.registerMember(conn, scanner);
                                    break;
                                case 2:
                                    libraryMemberManagement.viewMemberDetails(conn, scanner);
                                    break;
                                case 3:
                                    libraryMemberManagement.updateMemberInformation(conn, scanner);
                                    break;
                                case 4:
                                    libraryMemberManagement.deleteMember(conn, scanner);
                                    break;
                                default:
                                    System.out.println("Invalid option.");
                            }
                        } catch (MemberNotFoundException e) {
                            System.err.println(e.getMessage());
                        } catch (LibraryMemberManagementException e) {
                            System.err.println(e.getMessage());
                        }
                        break;

                    case 4:
                        BorrowingAndReturningBooks borrowingAndReturningBooks = new BorrowingAndReturningBooks();
                        System.out.println("Borrowing and Returning Books");
                        System.out.println("1. Issue a book");
                        System.out.println("2. Return a book");
                        System.out.println("3. View borrowing history");
                        System.out.print("Choose an option: ");
                        int borrowingChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        try {
                            switch (borrowingChoice) {
                                case 1:
                                    borrowingAndReturningBooks.issueBook(conn, scanner);
                                    break;
                                case 2:
                                    borrowingAndReturningBooks.returnBook(conn, scanner);
                                    break;
                                case 3:
                                    borrowingAndReturningBooks.viewBorrowingHistory(conn, scanner);
                                    break;
                                default:
                                    System.out.println("Invalid option.");
                            }
                        } catch (BorrowingAndReturningException e) {
                            System.err.println(e.getMessage());
                        }
                        break;

                    case 5:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid option.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }
}
