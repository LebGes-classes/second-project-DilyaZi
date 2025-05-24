import java.util.List;

public class Shop {
    private List<Warehouse> warehouses;
    private List<Product> products;
    private List<Worker> workers;
    private List<Buyer> buyers;
    private List<SalePoint> salePoints;

    public Shop() {
    }

    public void hireWorker(Worker worker) {
        workers.add(worker);
    }

    public void fireWorker(Worker worker) {
        worker.setActivity(false);
    }

    public void openWarehouse(Warehouse warehouse) {
        warehouses.add(warehouse);
    }

    //поиск покупателя
    public Buyer findBiyerById(int id) {
        return buyers.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    //регистрация покупателя
    public Buyer regBuyer(int id, String name) {
        Buyer buyer = new Buyer(id, name);
        buyers.add(buyer);
        return buyer;

    }
    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Worker> getWorkers(){
        return workers;
    }
    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public List<Buyer> getBuyers() {
        return buyers;
    }

    public void setBuyers(List<Buyer> buyers) {
        this.buyers = buyers;
    }

    public List<SalePoint> getSalePoints() {
        return salePoints;
    }

    public void setSalePoints(List<SalePoint> salePoints) {
        this.salePoints = salePoints;
    }
}
