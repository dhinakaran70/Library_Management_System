package Model;

import java.util.ArrayList;
import java.util.List;

public class Borrower extends User {
    public List<Book> borrowedBooks = new ArrayList<>();
    public double deposit = 1500;
    int extensionsUsed = 0;

    public Borrower(String email, String password, String name) {
        super(email, password, name);
    }

    public boolean canBorrow(Book book) {
        if (borrowedBooks.size() >= 3) return false;
        for (Book b : borrowedBooks) {
            if (b.id == book.id) return false;
        }
        return deposit >= 500;
    }
}