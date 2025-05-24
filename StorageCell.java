//ячейка склада где хранится определенный товар
public class StorageCell {
    private int id;
    private final Product product;
    private int quantity;
    private Worker worker;

    public StorageCell(int id, Product product, int quantity, Worker worker) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.worker = worker;
    }

    //меняем кол-во товараа
    public void addProduct(int count) {
        quantity += count;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        if (worker.workerActive()) {
            this.worker = worker;
        }
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getId() {
        return id;
    }
}
