import Model.Admin;
import Model.Book;
import Model.Borrower;
import Model.BorrowRecord;
import Service.AdminService;
import Service.BorrowerService;
import java.util.*;

public class LibraryManagementSystem {

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
         HashMap<String, Admin> admins = new HashMap<>();
         HashMap<String, Borrower> borrowers = new HashMap<>();
         List<Book> books = new ArrayList<>();
         List<BorrowRecord> borrowRecords = new ArrayList<>();

        admins.put("admin@kpr.com", new Admin("admin@kpr.com", "admin123", "Default Admin"));

        while (true) {
            System.out.println("+-------------------------------+");
            System.out.println("|   WELCOME TO THE KPR LIBRARY  |");
            System.out.println("+-------------------------------+");
            System.out.println("1. ADMIN LOGIN");
            System.out.println("2. BORROWER LOGIN");
            System.out.println("3. BORROWER REGISTRATION");
            System.out.println("4. EXIT");
            System.out.print("ENTER A CHOICE:");
            int ch = scn.nextInt();
            scn.nextLine();
            switch (ch) {
                case 1 -> AdminService.adminLogin(scn, admins, books, borrowRecords, borrowers);
                case 2 -> BorrowerService.borrowerLogin(scn, borrowers, books, borrowRecords);
                case 3 -> BorrowerService.registerBorrower(scn, borrowers);
                case 4 -> {
                    System.out.println("Thank you!! come again");
                    return; }
                default -> {
                    System.out.println();
                    System.out.println("    INVALID CHOICE, TRY AGAIN      ");
                    System.out.println();

                }
            }
        }
    }
}
