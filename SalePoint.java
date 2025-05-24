import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//управление пунктом продаж, возврат и продажа товаров, прибыль
public class SalePoint {
    private int id;
    private Map<Product, Integer> products; //key - товар, value - кол-во товара
    private Worker worker;
    private double income;
    private boolean isOpen;
    private List<Buyer> buyers = new ArrayList<>();

    public SalePoint(int id) {
        this.id = id;
        this.products = new HashMap<>();
        this.isOpen = true;
    }

    public void sellProduct(Product product, int quantity, Buyer buyer) {
        if (products.containsKey(product) && products.get(product) >= quantity) {
            products.put(product, products.get(product) - quantity); //уменьшается кол-во товара
            income += product.getPrice() * quantity; // находим доход
            buyer.addPurchase(new Purchase(product, quantity)); // сохраняем данные о покупке
            //System.out.println("Продажа успешна");
            if (!buyers.contains(buyer)) {
                buyers.add(buyer);
                System.out.println("Покупатель добавлен в buyers: " + buyer.getName());
            }
        } else {
            System.out.println("Недостаточно товара на складе");
        }
    }

    //возврат товара
    public void returnProduct(Purchase purchase, Buyer buyer) {
        double price = purchase.getProduct().getPrice();
        int quantity = purchase.getQuantity();
        System.out.println("Цена товара: " + price + ", Количество: " + quantity);
        if (buyers.contains(buyer) && buyer.getPurchases().contains(purchase)) {
            purchase.returnedPurchase(); // даем покупке состояние возратной покупки
            //вернем товар обратно в магазин
            products.put(purchase.getProduct(),
                    products.getOrDefault(purchase.getProduct(), 0) + purchase.getQuantity());
            double refundAmount = purchase.getProduct().getPrice() * purchase.getQuantity();
            income -= refundAmount;
        }
    }

    public void closePoint() {
        isOpen = false;
    }

    public List<Buyer> getBuyers() {
        return buyers;
    }

    public Worker getWorker() {
        return worker;
    }
    public void setWorker(Worker worker) {
        if (worker.workerActive()) {
            this.worker = worker;
        }
    }

    public int getId() {
        return id;
    }

    public boolean isOpen() {
        return isOpen;
    }
    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public double getIncome() {
        return income;
    }
    public void setIncome(double income) {
        this.income = income;
    }
}
