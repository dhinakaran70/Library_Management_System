package Service;

import Model.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class AdminService {
    public static void adminLogin(Scanner sc, HashMap<String, Admin> admins, List<Book> books, List<BorrowRecord> borrowRecords, Map<String, Borrower> borrowers) {
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        Admin admin = admins.get(email);
        if (admin != null && admin.password.equals(pass)) {
            adminMenu(sc, admins, books, borrowRecords, borrowers);
        } else {
            System.out.println("Invalid credentials");
        }
    }

    private static void adminMenu(Scanner sc, HashMap<String, Admin> admins, List<Book> books, List<BorrowRecord> borrowRecords, Map<String, Borrower> borrowers) {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. View All Books");
            System.out.println("4. Add New Admin");
            System.out.println("5. Reports");
            System.out.println("6. Logout");
            System.out.print("Choice: ");
            int ch = sc.nextInt(); sc.nextLine();

            switch (ch) {
                case 1 -> addBook(sc, books);
                case 2 -> removeBook(sc, books);
                case 3 -> viewBooks(books);
                case 4 -> {
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    System.out.print("Password: ");
                    String pass = sc.nextLine();
                    admins.put(email, new Admin(email, pass, name));
                    System.out.println("Admin added.");
                }
                case 5 -> showReports(sc, books, borrowRecords, borrowers);
                case 6 -> { return; }
            }
        }
    }

    private static void addBook(Scanner sc, List<Book> books) {
        System.out.print("ID: ");
        int id = sc.nextInt(); sc.nextLine();
        System.out.print("Title: ");
        String title = sc.nextLine();
        System.out.print("Author: ");
        String author = sc.nextLine();
        System.out.print("Genre: ");
        String genre = sc.nextLine();
        System.out.print("Quantity: ");
        int qty = sc.nextInt();
        System.out.print("Price: ");
        double price = sc.nextDouble(); sc.nextLine();
        books.add(new Book(id, title, author, genre, qty, price));
        System.out.println("Book added.");
    }

    private static void removeBook(Scanner sc, List<Book> books) {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        books.removeIf(book -> book.id == id);
        System.out.println("Book removed.");
    }

    public static void viewBooks(List<Book> books) {
        System.out.println("\n--- BOOK LIST ---");
        for (Book b : books) {
            System.out.printf("ID: %d | %s by \"%s\" | Qty: %d\n", b.id, b.title, b.author, b.quantity);
        }
    }

    private static void showReports(Scanner sc, List<Book> books, List<BorrowRecord> borrowRecords, Map<String, Borrower> borrowers) {
        System.out.println("\n--- REPORTS ---");
        System.out.println("1. Books with low quantity");
        System.out.println("2. Books never borrowed");
        System.out.println("3. Heavily borrowed books");
        System.out.println("4. Students with unreturned books");
        int ch = sc.nextInt(); sc.nextLine();
        switch (ch) {
            case 1 -> books.stream().filter(b -> b.quantity < 3).forEach(b -> System.out.println(b.title));
            case 2 -> books.stream().filter(b -> borrowRecords.stream().noneMatch(r -> r.book == b)).forEach(b -> System.out.println(b.title));
            case 3 -> {
                Map<Book, Long> countMap = new HashMap<>();
                for (BorrowRecord br : borrowRecords) {
                    countMap.put(br.book, countMap.getOrDefault(br.book, 0L) + 1);
                }
                countMap.entrySet().stream().filter(e -> e.getValue() >= 3).forEach(e -> System.out.println(e.getKey().title));
            }
            case 4 -> {
                for (Borrower b : borrowers.values()) {
                    boolean hasUnreturned = borrowRecords.stream().anyMatch(r -> r.borrower == b && r.returnDate == null);
                    if (hasUnreturned) System.out.println(b.name + " (" + b.email + ")");
                }
            }
        }
    }
}
