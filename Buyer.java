import java.util.ArrayList;
import java.util.List;

//покупатель и его список покупок (в каждой покупке возможно несколько товаров)
public class Buyer {
    private int id;
    private String name;
    private List<Purchase> purchases = new ArrayList<>();

    public Buyer(int id, String name) {
        this.id = id;
        this.name = name;
        this.purchases = new ArrayList<>();
    }

    //добавление покупки в список покупок
    public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }
}
