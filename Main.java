import java.io.IOException;
import java.util.*;

public class Main {
    private static final Shop shop = new Shop();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            ReaderExcel.readExcel("shop.xlsx", shop);
        } catch (IOException e) {
            System.out.println("Не получилось прочитать данные:( " + e.getMessage());
        }

        boolean flag = true;
        while (flag) {
            System.out.println("        Магазин SHOP ");
            System.out.println("         * Склады *            (1)");
            System.out.println("      * Пункты выдачи *        (2)");
            System.out.println("         * Товары *            (3)");
            System.out.println("        * Работники *          (4)");
            System.out.println("        * Покупатели *         (5)");
            System.out.println("   * Выполнение транзакций *   (6)");
            System.out.println("     * Просмотр отчетов *      (7)");
            System.out.println("    * Выход и сохранение *     (8)");
            System.out.print("Что выбираем? ");
            int choice = 0;
            try {
                choice = scanner.nextInt();
                // обработка выбора
            } catch (InputMismatchException e) {
                System.out.println("Пожалуйста, введите число.");
                scanner.nextLine(); // очистка буфера
            }
            scanner.nextLine();

            switch (choice) {
                case 1:
                    //ClearConsole.clearConsole();
                    manageWarehouses();
                    break;
                case 2:
                    manageSalePoints();
                    break;
                case 3:
                    manageProducts();
                    break;
                case 4:
                    manageWorkers();
                    break;
                case 5:
                    manageBuyers();
                    break;
                case 6:
                    showTransactions();
                    break;
                case 7:
                    showReport();
                    break;
                case 8:
                    try {
                        ReaderExcel.saveToExcel("shop.xlsx", shop);
                    } catch (IOException e) {
                        System.out.println("Не удалось сохранить данные:( " + e.getMessage());
                    }
                    flag = false;
                    break;
                default:
                    System.out.println("Неверная опция.");
            }
        }
        scanner.close();
    }

    private static void manageWarehouses() {
        boolean flag = true;
        while (flag) {
            System.out.println("\n* Склады *");
            System.out.println("1. Список складов");
            System.out.println("2. Добавить склад");
            System.out.println("3. Закрыть склад");
            System.out.println("4. Показать товары на складе");
            System.out.println("5. Назначить работника на склад");
            System.out.println("6. В меню");
            System.out.print("Что выбираем? ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    ClearConsole.clearConsole();
                    shop.getWarehouses().forEach(w -> System.out.println("ID: " + w.getId() + ", Открыт: " + w.isOpen()));
                    break;
                case 2:
                    System.out.print("Введите ID нового склада: ");
                    int id = scanner.nextInt();
                    shop.openWarehouse(new Warehouse(id));
                    System.out.println("Склад добавлен.");
                    break;
                case 3:
                    System.out.print("Введите ID склада для закрытия: ");
                    int closeId = scanner.nextInt();
                    Warehouse warehouse = shop.getWarehouses().stream().filter(w -> w.getId() == closeId).findFirst().orElse(null);
                    if (warehouse != null) {
                        warehouse.closeWarehouse();
                        System.out.println("Склад закрыт.");
                    } else {
                        System.out.println("Склад не найден.");
                    }
                    break;
                case 4:
                    System.out.print("Введите ID склада: ");
                    int showId = scanner.nextInt();
                    Warehouse wh = shop.getWarehouses().stream().filter(w -> w.getId() == showId).findFirst().orElse(null);
                    if (wh != null) {
                        wh.getCells().forEach(cell -> System.out.println("Товар: " + cell.getProduct().getName() + ", Кол-во: " + cell.getQuantity()));
                    } else {
                        System.out.println("Склад не найден.");
                    }
                    break;
                case 5:
                    System.out.print("Введите ID склада: ");
                    int whId = scanner.nextInt();
                    System.out.print("Введите ID работника: ");
                    int workerId = scanner.nextInt();
                    Warehouse targetWh = shop.getWarehouses().stream().filter(w -> w.getId() == whId).findFirst().orElse(null);
                    Worker worker = shop.getWorkers().stream().filter(w -> w.getId() == workerId).findFirst().orElse(null);
                    if (targetWh != null && worker != null) {
                        targetWh.getCells().forEach(cell -> cell.setWorker(worker));
                        System.out.println("Работник назначен.");
                    } else {
                        System.out.println("Склад или работник не найдены.");
                    }
                    break;
                case 6:
                    flag = false;
                    break;
                default:
                    System.out.println("Неверная опция.");
            }
        }
    }

    private static void manageSalePoints() {
        ClearConsole.clearConsole();
        boolean flag = true;
        while (flag) {
            System.out.println("\n* Пункты продаж *");
            System.out.println("1. Список пунктов продаж");
            System.out.println("2. Добавить пункт продаж");
            System.out.println("3. Закрыть пункт продаж");
            System.out.println("4. Показать товары на пункте продаж");
            System.out.println("5. Назначить работника");
            System.out.println("6. В меню");
            System.out.print("Что выбираем? ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    shop.getSalePoints().forEach(sp -> System.out.println("ID: " + sp.getId() + ", Открыт: " + sp.isOpen()));
                    break;
                case 2:
                    System.out.print("Введите ID нового пункта продаж: ");
                    int id = scanner.nextInt();
                    shop.getSalePoints().add(new SalePoint(id));
                    System.out.println("Пункт продаж добавлен.");
                    break;
                case 3:
                    System.out.print("Введите ID пункта продаж для закрытия: ");
                    int closeId = scanner.nextInt();
                    SalePoint sp = shop.getSalePoints().stream().filter(s -> s.getId() == closeId).findFirst().orElse(null);
                    if (sp != null) {
                        sp.closePoint();
                        System.out.println("Пункт продаж закрыт.");
                    } else {
                        System.out.println("Пункт продаж не найден.");
                    }
                    break;
                case 4:
                    System.out.print("Введите ID пункта продаж: ");
                    int showId = scanner.nextInt();
                    SalePoint salePoint = shop.getSalePoints().stream().filter(s -> s.getId() == showId).findFirst().orElse(null);
                    if (salePoint != null) {
                        salePoint.getProducts().forEach((prod, qty) -> System.out.println("Товар: " + prod.getName() + ", Кол-во: " + qty));
                    } else {
                        System.out.println("Пункт продаж не найден.");
                    }
                    break;
                case 5:
                    System.out.print("Введите ID пункта продаж: ");
                    int spId = scanner.nextInt();
                    System.out.print("Введите ID работника: ");
                    int workerId = scanner.nextInt();
                    SalePoint targetSp = shop.getSalePoints().stream().filter(s -> s.getId() == spId).findFirst().orElse(null);
                    Worker worker = shop.getWorkers().stream().filter(w -> w.getId() == workerId).findFirst().orElse(null);
                    if (targetSp != null && worker != null) {
                        targetSp.setWorker(worker);
                        System.out.println("Работник назначен.");
                    } else {
                        System.out.println("Пункт продаж или работник не найдены.");
                    }
                    break;
                case 6:
                    flag = false;
                    break;
                default:
                    System.out.println("Неверная опция.");
            }
        }
    }

    private static void manageProducts() {
        ClearConsole.clearConsole();
        boolean flag = true;
        while (flag) {
            System.out.println("\n* Товары *");
            System.out.println("1. Список товаров");
            System.out.println("2. Добавить товар");
            System.out.println("3. В меню");
            System.out.print("Выберите опцию: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    shop.getProducts().forEach(p -> System.out.println("ID: " + p.getId() + ", Название: " + p.getName() + ", Цена: " + p.getPrice()));
                    break;
                case 2:
                    System.out.print("Введите ID товара: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Введите название товара: ");
                    String name = scanner.nextLine();
                    System.out.print("Введите цену товара: ");
                    double price = scanner.nextDouble();
                    shop.getProducts().add(new Product(id, name, price));
                    if (shop.getProducts().stream().noneMatch(p -> p.getId() == id)) {
                        shop.getProducts().add(new Product(id, name, price));
                        System.out.println("Товар добавлен.");
                    } else {
                        System.out.println("Товар с таким ID уже существует.");
                    }
                    break;
                case 3:
                    flag = false;
                    break;
                default:
                    System.out.println("Неверная опция.");
            }
        }
    }

    private static void manageWorkers() {
        ClearConsole.clearConsole();
        boolean flag = true;
        while (flag) {
            System.out.println("\n* Работники *");
            System.out.println("1. Список работников");
            System.out.println("2. Нанять работника");
            System.out.println("3. Уволить работника");
            System.out.println("4. В меню");
            System.out.print("Что выбираем? ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    shop.getWorkers().forEach(w -> System.out.println("ID: " + w.getId() + ", Имя: " + w.getName() + ", Активен: " + w.getActivity()));
                    break;
                case 2:
                    System.out.print("Введите ID работника: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Введите имя работника: ");
                    String name = scanner.nextLine();
                    shop.hireWorker(new Worker(id, name, true));
                    System.out.println("Работник нанят.");
                    break;
                case 3:
                    System.out.print("Введите ID работника для увольнения: ");
                    int fireId = scanner.nextInt();
                    Worker worker = shop.getWorkers().stream().filter(w -> w.getId() == fireId).findFirst().orElse(null);
                    if (worker != null) {
                        shop.fireWorker(worker);
                        System.out.println("Работник уволен.");
                    } else {
                        System.out.println("Работник не найден.");
                    }
                    break;
                case 4:
                    flag = false;
                    break;
                default:
                    System.out.println("Неверная опция.");
            }
        }
    }

    private static void manageBuyers() {
        ClearConsole.clearConsole();
        boolean flag = true;
        while (flag) {
            System.out.println("\n* Покупатели *");
            System.out.println("1. Список покупателей");
            System.out.println("2. Зарегистрировать покупателя");
            System.out.println("3. Список покупок покупателя");
            System.out.println("4. В меню");
            System.out.print("Что выбираем? ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    shop.getBuyers().forEach(b -> System.out.println("ID: " + b.getId() + ", Имя: " + b.getName()));
                    break;
                case 2:
                    System.out.print("Введите ID покупателя: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Введите имя покупателя: ");
                    String name = scanner.nextLine();
                    shop.regBuyer(id, name);
                    System.out.println("Покупатель зарегистрирован.");
                    break;
                case 3:
                    System.out.print("Введите ID покупателя: ");
                    int buyerId = scanner.nextInt();
                    Buyer buyer = shop.findBiyerById(buyerId);
                    if (buyer != null) {
                        buyer.getPurchases().forEach(p -> System.out.println("Товар: " + p.getProduct().getName() + ", Кол-во: " + p.getQuantity()));
                    }
                    break;
                case 4:
                    flag = false;
                    break;
                default:
                    System.out.println("Неверная опция.");
            }
        }
    }

    private static void showTransactions() {
        ClearConsole.clearConsole();
        boolean flag = true;
        while (flag) {
            System.out.println("\n* Транзакции *");
            System.out.println("1. Закупка товара");
            System.out.println("2. Продажа товара");
            System.out.println("3. Возврат товара");
            System.out.println("4. Перемещение товара");
            System.out.println("5. В меню");
            System.out.print("Что выбираем? ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Введите ID склада: ");
                    int whId = scanner.nextInt();
                    System.out.print("Введите ID товара: ");
                    int prodId = scanner.nextInt();
                    System.out.print("Введите количество: ");
                    int qty = scanner.nextInt();
                    Warehouse warehouse = shop.getWarehouses().stream().filter(w -> w.getId() == whId).findFirst().orElse(null);
                    Product product = shop.getProducts().stream().filter(p -> p.getId() == prodId).findFirst().orElse(null);
                    if (warehouse != null && product != null) {
                        StorageCell cell = warehouse.getCells().stream().filter(c -> c.getProduct().getId() == prodId).findFirst().orElse(null);
                        if (cell != null) {
                            cell.addProduct(qty);
                        } else {
                            warehouse.addCell(new StorageCell(warehouse.getCells().size() + 1, product, qty, null));
                        }
                        System.out.println("Товар закуплен.");
                    } else {
                        System.out.println("Склад или товар не найдены.");
                    }
                    break;
                case 2:
                    System.out.print("Введите ID пункта продаж: ");
                    int spId = scanner.nextInt();
                    System.out.print("Введите ID товара: ");
                    int sellProdId = scanner.nextInt();
                    System.out.print("Введите количество: ");
                    int sellQty = scanner.nextInt();
                    System.out.print("Введите ID покупателя: ");
                    int buyerId = scanner.nextInt();
                    SalePoint salePoint = shop.getSalePoints().stream().filter(sp -> sp.getId() == spId).findFirst().orElse(null);
                    Product sellProduct = shop.getProducts().stream().filter(p -> p.getId() == sellProdId).findFirst().orElse(null);
                    Buyer buyer = shop.findBiyerById(buyerId);
                    if (salePoint != null && sellProduct != null && buyer != null) {
                        if (salePoint.isOpen() && salePoint.getWorker() != null && salePoint.getWorker().getActivity()) {
                            if (salePoint.getProducts().getOrDefault(sellProduct, 0) >= sellQty) {
                                salePoint.sellProduct(sellProduct, sellQty, buyer);
                                System.out.println("Товар продан.");
                            } else {
                                System.out.println("Недостаточно товара в пункте продаж.");
                            }
                        } else {
                            System.out.println("Пункт продаж закрыт или работник неактивен.");
                            break;
                        }
                    } else {
                        System.out.println("Пункт продаж, товар или покупатель не найдены.");
                    }
                    break;
                case 3:
                    System.out.print("Введите ID пункта продаж: ");
                    int returnSpId = scanner.nextInt();
                    System.out.print("Введите ID покупателя: ");
                    int returnBuyerId = scanner.nextInt();
                    System.out.print("Введите ID товара для возврата: ");
                    int returnProdId = scanner.nextInt();
                    SalePoint returnSp = shop.getSalePoints().stream().filter(sp -> sp.getId() == returnSpId).findFirst().orElse(null);
                    Buyer returnBuyer = shop.findBiyerById(returnBuyerId);
                    if (returnSp != null && returnBuyer != null) {
                        Purchase purchase = returnBuyer.getPurchases().stream()
                                .filter(p -> p.getProduct().getId() == returnProdId && !p.isReturned())
                                .findFirst().orElse(null);
                        if (purchase != null) {
                            returnSp.returnProduct(purchase, returnBuyer);
                            System.out.println("Товар возвращен.");
                        } else {
                            System.out.println("Покупка не найдена или уже возвращена.");
                        }
                    } else {
                        System.out.println("Пункт продаж или покупатель не найдены.");
                    }
                    break;
                case 4:
                    System.out.print("Введите ID склада-источника: ");
                    int srcWhId = scanner.nextInt();
                    System.out.print("Введите ID пункта продаж-назначения: ");
                    int destSpId = scanner.nextInt();
                    System.out.print("Введите ID товара: ");
                    int moveProdId = scanner.nextInt();
                    System.out.print("Введите количество: ");
                    int moveQty = scanner.nextInt();
                    Warehouse srcWh = shop.getWarehouses().stream().filter(w -> w.getId() == srcWhId).findFirst().orElse(null);
                    SalePoint destSp = shop.getSalePoints().stream().filter(sp -> sp.getId() == destSpId).findFirst().orElse(null);
                    Product moveProduct = shop.getProducts().stream().filter(p -> p.getId() == moveProdId).findFirst().orElse(null);
                    if (srcWh != null && destSp != null && moveProduct != null) {
                        StorageCell cell = srcWh.getCells().stream().filter(c -> c.getProduct().getId() == moveProdId).findFirst().orElse(null);
                        if (cell != null && cell.getQuantity() >= moveQty) {
                            cell.addProduct(-moveQty);
                            destSp.getProducts().put(moveProduct, destSp.getProducts().getOrDefault(moveProduct, 0) + moveQty);
                            System.out.println("Товар перемещен.");
                        } else {
                            System.out.println("Недостаточно товара на складе.");
                        }
                    } else {
                        System.out.println("Склад, пункт продаж или товар не найдены.");
                    }
                    break;
                case 5:
                    flag = false;
                    break;
                default:
                    System.out.println("Неверная опция.");
            }
        }
    }

    private static void showReport() {
        ClearConsole.clearConsole();
        boolean flag = true;
        while (flag) {
            System.out.println("\n* Отчёты *");
            System.out.println("1. Доход по пунктам продаж");
            System.out.println("2. Резерв товаров");
            System.out.println("3. в меню");
            System.out.print("Что выбираем? ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:

                    shop.getSalePoints().forEach(sp -> System.out.println("ID: " + sp.getId() + ", Доход: " + sp.getIncome()));
                    break;
                case 2:
                    shop.getWarehouses().forEach(w -> w.getCells().forEach(cell ->
                            System.out.println("Склад ID: " + w.getId() + ", Товар: " + cell.getProduct().getName() + ", Кол-во: " + cell.getQuantity())));
                    shop.getSalePoints().forEach(sp -> sp.getProducts().forEach((prod, qty) ->
                            System.out.println("Пункт продаж ID: " + sp.getId() + ", Товар: " + prod.getName() + ", Кол-во: " + qty)));
                    break;
                case 3:
                    flag = false;
                    break;
                default:
                    System.out.println("Неверная опция.");
            }
        }
    }
}