import java.util.Date;

//инф-ция о покупке, которую совершил покупатель
public class Purchase {
    private Product product;
    private int quantity;
    private Date purchaseDate;
    private boolean isReturned;

    public Purchase(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.purchaseDate = new Date();
        this.isReturned = false;
    }

    public void returnedPurchase() {

        this.isReturned = true;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setPurchaseDate(Date date) {
        this.purchaseDate = date;
    }

    public void setReturned(boolean returned) {
        this.isReturned = returned;
    }


}
