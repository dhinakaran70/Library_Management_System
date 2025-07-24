package Model;

import java.time.LocalDate;

public class BorrowRecord {
    public Book book;
    public Borrower borrower;
    public LocalDate borrowDate;
    public LocalDate returnDate;
    public boolean isLost = false;
    int extensions = 0;

    public BorrowRecord(Book book, Borrower borrower, LocalDate borrowDate) {
        this.book = book;
        this.borrower = borrower;
        this.borrowDate = borrowDate;
    }
}
