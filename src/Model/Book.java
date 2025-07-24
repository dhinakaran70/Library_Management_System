package Model;

public class Book {
    public int id;
    public String title;
    public String author;
    public String genre;
    public int quantity;
    public double price;

    public Book(int id, String title, String author, String genre, int quantity, double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.quantity = quantity;
        this.price = price;
    }
}