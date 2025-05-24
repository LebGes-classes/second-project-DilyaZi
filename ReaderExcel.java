
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReaderExcel {
    //Чтение данных
    public static void readExcel(String filepath, Shop shop) throws IOException {
        FileInputStream file = new FileInputStream(filepath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        // Очистка существующих данных
        shop.setProducts(new ArrayList<>());
        shop.setWorkers(new ArrayList<>());
        shop.setWarehouses(new ArrayList<>());
        shop.setBuyers(new ArrayList<>());
        shop.setSalePoints(new ArrayList<>());

        // Чтение товаров
        XSSFSheet productsSheet = workbook.getSheet("Товары");
        if (productsSheet != null) {
            for (Row row : productsSheet) {
                if (row.getRowNum() == 0) continue;
                int id = (int) row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                double price = row.getCell(2).getNumericCellValue();
                shop.getProducts().add(new Product(id, name, price));
            }
        }

        // Чтение работников
        XSSFSheet workersSheet = workbook.getSheet("Работники");
        if (workersSheet != null) {
            for (Row row : workersSheet) {
                if (row.getRowNum() == 0) continue;
                int id = (int) row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                boolean activity = row.getCell(2).getBooleanCellValue();
                shop.getWorkers().add(new Worker(id, name, activity));
            }
        }

        // Чтение складов
        XSSFSheet warehousesSheet = workbook.getSheet("Склады");
        if (warehousesSheet != null) {
            for (Row row : warehousesSheet) {
                if (row.getRowNum() == 0) continue;
                int id = (int) row.getCell(0).getNumericCellValue();
                boolean isOpen = row.getCell(1).getBooleanCellValue();
                Warehouse warehouse = new Warehouse(id);
                warehouse.setOpen(isOpen);
                shop.getWarehouses().add(warehouse);
            }
        }

        // Чтение ячеек склада
        XSSFSheet storageCellsSheet = workbook.getSheet("Ячейки склада");
        if (storageCellsSheet != null) {
            for (Row row : storageCellsSheet) {
                if (row.getRowNum() == 0) continue;
                int cellId = (int) row.getCell(0).getNumericCellValue();
                int warehouseId = (int) row.getCell(1).getNumericCellValue();
                int productId = (int) row.getCell(2).getNumericCellValue();
                int quantity = (int) row.getCell(3).getNumericCellValue();
                int workerId = (int) row.getCell(4).getNumericCellValue();

                Warehouse warehouse = findWarehouseById(shop.getWarehouses(), warehouseId);
                Product product = findProductById(shop.getProducts(), productId);
                Worker worker = findWorkerById(shop.getWorkers(), workerId);

                if (warehouse != null && product != null && worker != null) {
                    StorageCell cell = new StorageCell(cellId, product, quantity, worker);
                    warehouse.addCell(cell);
                }
            }
        }

        // Чтение покупателей
        XSSFSheet buyersSheet = workbook.getSheet("Покупатели");
        if (buyersSheet != null) {
            for (Row row : buyersSheet) {
                if (row.getRowNum() == 0) continue;
                int id = (int) row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                shop.getBuyers().add(new Buyer(id, name));
            }
        }

        // Чтение покупок
        XSSFSheet purchasesSheet = workbook.getSheet("Покупки");
        if (purchasesSheet != null) {
            for (Row row : purchasesSheet) {
                if (row.getRowNum() == 0) continue;
                int buyerId = (int) row.getCell(0).getNumericCellValue();
                int productId = (int) row.getCell(1).getNumericCellValue();
                int quantity = (int) row.getCell(2).getNumericCellValue();
                Date purchaseDate = row.getCell(3).getDateCellValue();
                boolean isReturned = row.getCell(4).getBooleanCellValue();
                Buyer buyer = findBuyerById(shop.getBuyers(), buyerId);
                Product product = findProductById(shop.getProducts(), productId);
                if (buyer != null && product != null) {
                    Purchase purchase = new Purchase(product, quantity);
                    purchase.setPurchaseDate(purchaseDate);
                    purchase.setReturned(isReturned);
                    buyer.addPurchase(purchase);
                }
            }
        }

        // Чтение пунктов продаж
        XSSFSheet salePointsSheet = workbook.getSheet("Пункты Продаж");
        if (salePointsSheet != null) {
            for (Row row : salePointsSheet) {
                if (row.getRowNum() == 0) continue;
                int id = (int) row.getCell(0).getNumericCellValue();
                boolean isOpen = row.getCell(1).getBooleanCellValue();
                int workerId = (int) row.getCell(2).getNumericCellValue();
                double income = row.getCell(3) != null ? row.getCell(3).getNumericCellValue() : 0.0;

                SalePoint salePoint = new SalePoint(id);
                salePoint.setOpen(isOpen);
                salePoint.setIncome(income);
                Worker worker = findWorkerById(shop.getWorkers(), workerId);
                if (worker != null) {
                    salePoint.setWorker(worker);
                }
                shop.getSalePoints().add(salePoint);
            }
        }

        // Чтение товаров пункта продаж
        XSSFSheet salePointProductsSheet = workbook.getSheet("Товары ПП");
        if (salePointProductsSheet != null) {
            for (Row row : salePointProductsSheet) {
                if (row.getRowNum() == 0) continue;
                int salePointId = (int) row.getCell(0).getNumericCellValue();
                int productId = (int) row.getCell(1).getNumericCellValue();
                int quantity = (int) row.getCell(2).getNumericCellValue();

                SalePoint sp = findSalePointById(shop.getSalePoints(), salePointId);
                Product product = findProductById(shop.getProducts(), productId);

                if (sp != null && product != null) {
                    sp.getProducts().put(product, quantity);
                }
            }
        }
        workbook.close();
        file.close();
    }

    //Нахождение склада по ID
    private static Warehouse findWarehouseById(List<Warehouse> warehouses, int id) {
        return warehouses.stream().filter(w -> w.getId() == id).findFirst().orElse(null);
    }

    //Нахождение товара по ID
    private static Product findProductById(List<Product> products, int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    //Нахождение работника по ID
    private static Worker findWorkerById(List<Worker> workers, int id) {
        return workers.stream().filter(w -> w.getId() == id).findFirst().orElse(null);
    }

    //Нахождение покупателя по ID
    private static Buyer findBuyerById(List<Buyer> buyers, int id) {
        return buyers.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    //Нахождение пункта продаж по ID
    private static SalePoint findSalePointById(List<SalePoint> salePoints, int id) {
        return salePoints.stream().filter(sp -> sp.getId() == id).findFirst().orElse(null);
    }

    //Сохранение данных
    public static void saveToExcel(String filePath, Shop shop) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Сохранение товаров
        XSSFSheet productsSheet = workbook.createSheet("Товары");
        Row header = productsSheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Имя");
        header.createCell(2).setCellValue("Цена");
        int rowNum = 1;
        for (Product product: shop.getProducts()) {
            Row row = productsSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getId());
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getPrice());
        }
        //Сохранение работников
        XSSFSheet workersSheet = workbook.createSheet("Работники");
        header = workersSheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Имя");
        header.createCell(2).setCellValue("Активен");
        rowNum = 1;
        for (Worker worker : shop.getWorkers()) {
            Row row = workersSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(worker.getId());
            row.createCell(1).setCellValue(worker.getName());
            row.createCell(2).setCellValue(worker.getActivity());
        }
        //Сохранение складов
        XSSFSheet warehousesSheet = workbook.createSheet("Склады");
        header = warehousesSheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Открыт");
        rowNum = 1;
        for (Warehouse wh : shop.getWarehouses()) {
            Row row = warehousesSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(wh.getId());
            row.createCell(1).setCellValue(wh.isOpen());
        }
        //Сохранение ячеек склада
        XSSFSheet storageCellsSheet = workbook.createSheet("Ячейки склада");
        header = storageCellsSheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("ID склада");
        header.createCell(2).setCellValue("ID товара");
        header.createCell(3).setCellValue("Шт");
        header.createCell(4).setCellValue("ID работника");
        rowNum = 1;
        for (Warehouse wh : shop.getWarehouses()) {
            for (StorageCell cell : wh.getCells()) {
                Row row = storageCellsSheet.createRow(rowNum++);
                row.createCell(0).setCellValue(cell.getId());
                row.createCell(1).setCellValue(wh.getId());
                row.createCell(2).setCellValue(cell.getProduct().getId());
                row.createCell(3).setCellValue(cell.getQuantity());
                if (cell.getWorker() != null) {
                    row.createCell(4).setCellValue(cell.getWorker().getId());
                } else {
                    row.createCell(4).setCellValue(-1);
                }
            }
        }
        //Сохранение покупателей
        XSSFSheet buyersSheet = workbook.createSheet("Покупатели");
        header = buyersSheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Имя");
        rowNum = 1;
        for (Buyer b : shop.getBuyers()) {
            Row row = buyersSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(b.getId());
            row.createCell(1).setCellValue(b.getName());
        }
        //Сохранение покупок
        XSSFSheet purchasesSheet = workbook.createSheet("Покупки");
        header = purchasesSheet.createRow(0);
        header.createCell(0).setCellValue("ID покупателя");
        header.createCell(1).setCellValue("ID товара");
        header.createCell(2).setCellValue("Шт");
        header.createCell(3).setCellValue("Дата");
        header.createCell(4).setCellValue("Возвращён");
        rowNum = 1;
        for (Buyer b : shop.getBuyers()) {
            for (Purchase p : b.getPurchases()) {
                Row row = purchasesSheet.createRow(rowNum++);
                row.createCell(0).setCellValue(b.getId());
                row.createCell(1).setCellValue(p.getProduct().getId());
                row.createCell(2).setCellValue(p.getQuantity());
                Cell dateCell = row.createCell(3);
                dateCell.setCellValue(p.getPurchaseDate());
                CellStyle dateCellStyle = workbook.createCellStyle();
                dateCellStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy.MM.dd"));
                dateCell.setCellStyle(dateCellStyle);
                row.createCell(4).setCellValue(p.isReturned());
            }
        }
        //Сохранение пунктов продаж
        XSSFSheet salePointsSheet = workbook.createSheet("Пункты Продаж");
        header = salePointsSheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("IsOpen");
        header.createCell(2).setCellValue("ID работника");
        header.createCell(3).setCellValue("Доход");
        rowNum = 1;
        for (SalePoint sp : shop.getSalePoints()) {
            Row row = salePointsSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(sp.getId());
            row.createCell(1).setCellValue(sp.isOpen());
            if (sp.getWorker() != null) {
                row.createCell(2).setCellValue(sp.getWorker().getId());
            } else {
                row.createCell(2).setCellValue(-1);
            }
            row.createCell(3).setCellValue(sp.getIncome());
        }
        //Сохранение товаров пунктов продаж
        XSSFSheet salePointProductsSheet = workbook.createSheet("Товары ПП");
        header = salePointProductsSheet.createRow(0);
        header.createCell(0).setCellValue("ID ПП");
        header.createCell(1).setCellValue("ID товара");
        header.createCell(2).setCellValue("Шт");
        rowNum = 1;
        for (SalePoint sp : shop.getSalePoints()) {
            for (Map.Entry<Product, Integer> entry : sp.getProducts().entrySet()) {
                Row row = salePointProductsSheet.createRow(rowNum++);
                row.createCell(0).setCellValue(sp.getId());
                row.createCell(1).setCellValue(entry.getKey().getId());
                row.createCell(2).setCellValue(entry.getValue());
            }
        }

        FileOutputStream file = new FileOutputStream(filePath);
        workbook.write(file);
        workbook.close();
        file.close();
    }
}