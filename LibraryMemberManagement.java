import java.sql.*;
import java.util.Scanner;

// Custom Exceptions
class LibraryMemberManagementException extends Exception {
    public LibraryMemberManagementException(String message) {
        super(message);
    }
}

class MemberNotFoundException extends LibraryMemberManagementException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}

public class LibraryMemberManagement {
    private int memberId;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;

    // Getters and Setters
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Register a new library member
    public void registerMember(Connection conn, Scanner scanner) throws LibraryMemberManagementException {
        try {
            System.out.print("Enter member name: ");
            setName(scanner.nextLine());
            System.out.print("Enter address: ");
            setAddress(scanner.nextLine());
            System.out.print("Enter phone number: ");
            setPhoneNumber(scanner.nextLine());
            System.out.print("Enter email: ");
            setEmail(scanner.nextLine());

            String sql = "INSERT INTO LibraryMember (name, address, phone_number, email) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, getName());
                pstmt.setString(2, getAddress());
                pstmt.setString(3, getPhoneNumber());
                pstmt.setString(4, getEmail());
                pstmt.executeUpdate();
                System.out.println("Library member registered successfully!");
            }
        } catch (SQLException e) {
            throw new LibraryMemberManagementException("Error registering library member: " + e.getMessage());
        }
    }

    // View member details
    public void viewMemberDetails(Connection conn, Scanner scanner)
            throws MemberNotFoundException, LibraryMemberManagementException {
        try {
            System.out.print("Enter member ID to view details: ");
            setMemberId(scanner.nextInt());
            scanner.nextLine(); // Consume newline

            String sql = "SELECT * FROM LibraryMember WHERE member_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, getMemberId());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    setName(rs.getString("name"));
                    setAddress(rs.getString("address"));
                    setPhoneNumber(rs.getString("phone_number"));
                    setEmail(rs.getString("email"));

                    System.out.println("Member ID: " + getMemberId());
                    System.out.println("Name: " + getName());
                    System.out.println("Address: " + getAddress());
                    System.out.println("Phone Number: " + getPhoneNumber());
                    System.out.println("Email: " + getEmail());
                } else {
                    throw new MemberNotFoundException("Member with ID " + getMemberId() + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new LibraryMemberManagementException("Error viewing member details: " + e.getMessage());
        }
    }

    // Update member information
    public void updateMemberInformation(Connection conn, Scanner scanner)
            throws MemberNotFoundException, LibraryMemberManagementException {
        try {
            System.out.print("Enter member ID to update: ");
            setMemberId(scanner.nextInt());
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new member name: ");
            setName(scanner.nextLine());
            System.out.print("Enter new address: ");
            setAddress(scanner.nextLine());
            System.out.print("Enter new phone number: ");
            setPhoneNumber(scanner.nextLine());
            System.out.print("Enter new email: ");
            setEmail(scanner.nextLine());

            String sql = "UPDATE LibraryMember SET name = ?, address = ?, phone_number = ?, email = ? WHERE member_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, getName());
                pstmt.setString(2, getAddress());
                pstmt.setString(3, getPhoneNumber());
                pstmt.setString(4, getEmail());
                pstmt.setInt(5, getMemberId());
                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Library member information updated successfully!");
                } else {
                    throw new MemberNotFoundException("Member with ID " + getMemberId() + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new LibraryMemberManagementException("Error updating member information: " + e.getMessage());
        }
    }

    // Delete a member
    public void deleteMember(Connection conn, Scanner scanner)
            throws MemberNotFoundException, LibraryMemberManagementException {
        try {
            System.out.print("Enter member ID to delete: ");
            setMemberId(scanner.nextInt());
            scanner.nextLine(); // Consume newline

            String deleteBorrowingHistorySql = "DELETE FROM BorrowingHistory WHERE member_id = ?";
            try (PreparedStatement pstmt1 = conn.prepareStatement(deleteBorrowingHistorySql)) {
                pstmt1.setInt(1, getMemberId());
                pstmt1.executeUpdate();
            }

            String deleteMemberSql = "DELETE FROM LibraryMember WHERE member_id = ?";
            try (PreparedStatement pstmt2 = conn.prepareStatement(deleteMemberSql)) {
                pstmt2.setInt(1, getMemberId());
                int rowsDeleted = pstmt2.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Library member deleted successfully!");
                } else {
                    throw new MemberNotFoundException("Member with ID " + getMemberId() + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new LibraryMemberManagementException("Error deleting library member: " + e.getMessage());
        }
    }
}
