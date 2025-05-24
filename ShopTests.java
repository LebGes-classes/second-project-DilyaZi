import static org.junit.jupiter.api.Assertions.*;

class SalePointTest {
    private SalePoint salePoint;
    private Product product;
    private Buyer buyer;
    private Worker worker;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        salePoint = new SalePoint(1);
        product = new Product(1, "Test Product", 10.0);
        buyer = new Buyer(1, "Test Buyer");
        salePoint.getProducts().put(product, 10);
    }

    @org.junit.jupiter.api.Test
    void testSellProduct() {
        salePoint.sellProduct(product, 2, buyer);
        assertEquals(8, salePoint.getProducts().get(product));
        assertEquals(20.0, salePoint.getIncome());
    }

    @org.junit.jupiter.api.Test
    void testReturnProduct() {
        salePoint.sellProduct(product, 2, buyer);
        Purchase purchase = buyer.getPurchases().get(0);
        salePoint.returnProduct(purchase, buyer);

        assertEquals(10, salePoint.getProducts().get(product));
        assertEquals(0.0, salePoint.getIncome());
        assertTrue(purchase.isReturned());
    }
}

class ProductTest {
    @org.junit.jupiter.api.Test
    void testCreateProduct() {
        Product product = new Product(6, "Лепешка", 10.0);
        assertEquals(6, product.getId());
        assertEquals("Лепешка", product.getName());
        assertEquals(10.0, product.getPrice());
    }
}

class WorkerTest {
    @org.junit.jupiter.api.Test
    void testCreateWorker() {
        Worker worker = new Worker(2, "Маврихард", true);
        assertEquals(2, worker.getId());
        assertEquals("Маврихард", worker.getName());
        assertTrue(worker.getActivity());
    }
}

class BuyerTest {
    @org.junit.jupiter.api.Test
    void testAddPurchaseToBuyer() {
        Buyer buyer = new Buyer(1, "Арялид");
        Product product = new Product(2, "Печеньки", 2);
        Purchase purchase = new Purchase(product, 5);
        buyer.addPurchase(purchase);
        assertEquals(1, buyer.getPurchases().size());
    }
}

class PurchaseTest {
    @org.junit.jupiter.api.Test
    void testCreateAndReturnPurchase() {
        Product product = new Product(2, "Печеньки", 2);
        Purchase purchase = new Purchase(product, 5);
        assertEquals(product, purchase.getProduct());
        assertEquals(5, purchase.getQuantity());
        assertFalse(purchase.isReturned());
        purchase.returnedPurchase(); //возвращаем товар
        assertTrue(purchase.isReturned());
    }
}

class WarehouseTest {
    @org.junit.jupiter.api.Test
    void addCellToWarehouse() {
        Warehouse warehouse = new Warehouse(1);
        Product product = new Product(2, "Печеньки", 2);
        StorageCell cell = new StorageCell(2, product, 10, null);
        warehouse.addCell(cell);
        assertEquals(1, warehouse.getCells().size());
    }
}

class StorageCellTest {
    @org.junit.jupiter.api.Test
    void addProductToStorageCell() {
        Product product = new Product(2, "Печеньки", 2);
        StorageCell cell = new StorageCell(2, product, 10, null);
        cell.addProduct(3);
        assertEquals(13, cell.getQuantity());
    }
}

class ShopTest {
    @org.junit.jupiter.api.Test
    void testRegBuyer() {
        Shop shop = new Shop();
        Buyer buyer = shop.regBuyer(5, "Арс");
        assertEquals(1, shop.getBuyers().size());
        assertEquals("Арс", shop.findBuyerById(5).getName());
    }
}
