import java.sql.*;
import java.util.Scanner;

// Custom Exceptions
class AuthorManagementException extends Exception {
    public AuthorManagementException(String message) {
        super(message);
    }
}

class AuthorNotFoundException extends AuthorManagementException {
    public AuthorNotFoundException(String message) {
        super(message);
    }
}

public class AuthorManagement {
    private int authorId;
    private String name;
    private String biography;

    // Getters and Setters
    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    // Add a new author
    public void addAuthor(Connection conn, Scanner scanner) throws AuthorManagementException {
        try {
            System.out.print("Enter author name: ");
            setName(scanner.nextLine());
            System.out.print("Enter author biography: ");
            setBiography(scanner.nextLine());

            String sql = "INSERT INTO Author (name, biography) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, getName());
                pstmt.setString(2, getBiography());
                pstmt.executeUpdate();
                System.out.println("Author added successfully!");
            }
        } catch (SQLException e) {
            throw new AuthorManagementException("Error adding author: " + e.getMessage());
        }
    }

    // View author details
    public void viewAuthorDetails(Connection conn, Scanner scanner)
            throws AuthorNotFoundException, AuthorManagementException {
        try {
            System.out.print("Enter author ID to view details: ");
            setAuthorId(scanner.nextInt());
            scanner.nextLine(); // Consume newline

            String sql = "SELECT * FROM Author WHERE author_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, getAuthorId());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    setName(rs.getString("name"));
                    setBiography(rs.getString("biography"));

                    System.out.println("Author ID: " + getAuthorId());
                    System.out.println("Name: " + getName());
                    System.out.println("Biography: " + getBiography());
                } else {
                    throw new AuthorNotFoundException("Author with ID " + getAuthorId() + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new AuthorManagementException("Error viewing author details: " + e.getMessage());
        }
    }

    // Update author information
    public void updateAuthorInformation(Connection conn, Scanner scanner)
            throws AuthorNotFoundException, AuthorManagementException {
        try {
            System.out.print("Enter author ID to update: ");
            setAuthorId(scanner.nextInt());
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new author name: ");
            setName(scanner.nextLine());
            System.out.print("Enter new author biography: ");
            setBiography(scanner.nextLine());

            String sql = "UPDATE Author SET name = ?, biography = ? WHERE author_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, getName());
                pstmt.setString(2, getBiography());
                pstmt.setInt(3, getAuthorId());
                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Author information updated successfully!");
                } else {
                    throw new AuthorNotFoundException("Author with ID " + getAuthorId() + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new AuthorManagementException("Error updating author information: " + e.getMessage());
        }
    }

    // Delete an author
    public void deleteAuthor(Connection conn, Scanner scanner)
            throws AuthorNotFoundException, AuthorManagementException {
        try {
            System.out.print("Enter author ID to delete: ");
            setAuthorId(scanner.nextInt());
            scanner.nextLine(); // Consume newline

            String deleteBooksSql = "DELETE FROM Book WHERE author_id = ?";
            try (PreparedStatement pstmt1 = conn.prepareStatement(deleteBooksSql)) {
                pstmt1.setInt(1, getAuthorId());
                pstmt1.executeUpdate();
            }

            String deleteAuthorSql = "DELETE FROM Author WHERE author_id = ?";
            try (PreparedStatement pstmt2 = conn.prepareStatement(deleteAuthorSql)) {
                pstmt2.setInt(1, getAuthorId());
                int rowsDeleted = pstmt2.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Author deleted successfully!");
                } else {
                    throw new AuthorNotFoundException("Author with ID " + getAuthorId() + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new AuthorManagementException("Error deleting author: " + e.getMessage());
        }
    }
}
