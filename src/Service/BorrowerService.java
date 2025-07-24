package Service;

import Model.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class BorrowerService {
    public static void borrowerLogin(Scanner sc, HashMap<String, Borrower> borrowers, List<Book> books, List<BorrowRecord> borrowRecords) {
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        Borrower borrower = borrowers.get(email);
        if (borrower != null && borrower.password.equals(pass)) {
            borrowerMenu(sc, borrower, books, borrowRecords);
        } else {
            System.out.println("Invalid credentials");
        }
    }

    public static void registerBorrower(Scanner sc, HashMap<String, Borrower> borrowers) {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();
        borrowers.put(email, new Borrower(email, pass, name));
        System.out.println("Registered successfully.");
    }

    private static void borrowerMenu(Scanner sc, Borrower borrower, List<Book> books, List<BorrowRecord> borrowRecords) {
        while (true) {
            System.out.println("\n--- BORROWER MENU ---");
            System.out.println("1. View Available Books");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("4. Lost Book / Card");
            System.out.println("5. Logout");
            System.out.print("Choice: ");
            int ch = sc.nextInt(); sc.nextLine();

            switch (ch) {
                case 1 -> AdminService.viewBooks(books);
                case 2 -> borrowBook(sc, borrower, books, borrowRecords);
                case 3 -> returnBook(sc, borrower, borrowRecords);
                case 4 -> handleLoss(sc, borrower, borrowRecords);
                case 5 -> { return; }
            }
        }
    }

    private static void borrowBook(Scanner sc, Borrower b, List<Book> books, List<BorrowRecord> borrowRecords) {
        AdminService.viewBooks(books);
        System.out.print("Enter Book ID to borrow: ");
        int id = sc.nextInt(); sc.nextLine();
        for (Book book : books) {
            if (book.id == id && book.quantity > 0 && b.canBorrow(book)) {
                book.quantity--;
                b.borrowedBooks.add(book);
                borrowRecords.add(new BorrowRecord(book, b, LocalDate.now()));
                System.out.println("Book borrowed.");
                return;
            }
        }
        System.out.println("Cannot borrow this book.");
    }

    private static void returnBook(Scanner sc, Borrower b, List<BorrowRecord> borrowRecords) {
        System.out.print("Enter Book ID to return: ");
        int id = sc.nextInt(); sc.nextLine();
        for (BorrowRecord br : borrowRecords) {
            if (br.borrower == b && br.book.id == id && br.returnDate == null) {
                System.out.print("Enter return date (dd/MM/yyyy): ");
                String dateStr = sc.nextLine();
                br.returnDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                long days = Duration.between(br.borrowDate.atStartOfDay(), br.returnDate.atStartOfDay()).toDays();
                if (days > 15) {
                    long fineDays = days - 15;
                    double fine = Math.min(0.8 * br.book.price, fineDays * 2);
                    b.deposit -= fine;
                    System.out.println("Fine of ₹" + fine + " deducted.");
                }
                br.book.quantity++;
                b.borrowedBooks.remove(br.book);
                return;
            }
        }
        System.out.println("No such borrowed book.");
    }

    private static void handleLoss(Scanner sc, Borrower b, List<BorrowRecord> borrowRecords) {
        System.out.println("1. Lost Membership Card (₹10)");
        System.out.println("2. Lost Book (50% cost)");
        int opt = sc.nextInt(); sc.nextLine();
        if (opt == 1) {
            b.deposit -= 10;
            System.out.println("10 rupees deducted for card loss.");
        } else if (opt == 2) {
            System.out.print("Enter lost Book ID: ");
            int id = sc.nextInt(); sc.nextLine();
            for (BorrowRecord br : borrowRecords) {
                if (br.borrower == b && br.book.id == id && br.returnDate == null) {
                    double fine = br.book.price * 0.5;
                    b.deposit -= fine;
                    br.isLost = true;
                    b.borrowedBooks.remove(br.book);
                    System.out.println( fine + " rupees deducted for lost book.");
                    return;
                }
            }
            System.out.println("Book not found in your borrow list.");
        }
    }
}
