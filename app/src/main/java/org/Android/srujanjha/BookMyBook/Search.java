package org.android.srujanjha.bookmybook;

/**
 * Created by Srujan Jha on 28-12-2014.
 */

public class Search {

    private String price=null;
    private int img=0;
    private String link=null;
    private String title=null;
    private String image=null;
    private String isbn=null;
    private String author=null;
    private String publisher=null;
    private String sprice=null;
    public Search()
    {
        setPrice("N.A.");
        setLink("N.A.");
        setImage("N.A.");
        setImg(0);
        setTitle("N.A.");
       setAuthor("N.A.");
        setPublisher("");
        setIsbn("");
    }
    /**
     * @return the price
     */
    public String getPrice() {
        return price;
    }
    /**
     * @param price the price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }
    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }
    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }
    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }
    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return the img
     */
    public int getImg() {
        return img;
    }
    /**
     * @param img the img to set
     */
    public void setImg(int img) {
        this.img = img;
    }
    /**
     * @return the bimage
     */
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getSprice() {
        return sprice;
    }

    public void setSprice(String sprice) {
        this.sprice = sprice;
    }
}
