package kz.runtime.jpa;

import jakarta.persistence.*;
import kz.runtime.jpa.entity.Category;
import kz.runtime.jpa.entity.Characteristic;
import kz.runtime.jpa.entity.Product;
import kz.runtime.jpa.entity.ProductCharacteristic;

import java.util.List;
import java.util.Scanner;

public class Application {
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.print("- Создать категорию [1]\n" +
                    "- Добавить товар [2]\n" +
                    "- Изменить товар [3]\n" +
                    "- Удалить товар [4]\n" +
                    "- Exit [5]\n" +
                    "Выберите действие: ");

            int action = Integer.parseInt(scan.nextLine());
            switch (action) {
                case 1 -> createCategory();
                case 2 -> createProduct();
                case 3 -> updateProduct();
                case 4 -> deleteProduct();
                case 5 -> {
                    System.out.println("Выход...");
                    return;
                }
            }
        }
    }

    private static void createCategory() {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();

            String categoryName;
            while (true) {
                System.out.print("Название категории: ");
                categoryName = scan.nextLine();
                TypedQuery<Long> categoryCount = manager.createQuery(
                        "select count(*) from Category c where c.categoryName = ?1", Long.class);
                categoryCount.setParameter(1, categoryName);
                if (categoryCount.getSingleResult() != 0) {
                    System.out.println("Эта категория существует. Повторите запрос!");
                    continue;
                }

                break;
            }

            System.out.print("Введите характеристики категории: ");
            String[] arg = scan.nextLine().split(", ");

            Category category = new Category();
            category.setCategoryName(categoryName);
            manager.persist(category);

            for (String str : arg) {
                Characteristic characteristic = new Characteristic();
                characteristic.setCharacteristicName(str);
                characteristic.setCategory(category);
                manager.persist(characteristic);
            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            System.out.println("Было поймано исключение " + e);
        } finally {
            manager.close();
        }
    }

    private static void createProduct() {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();

            Product product = new Product();
            String str;

            while (true) {
                System.out.print("Название продукта:  ");
                str = scan.nextLine();
                if (str.length() > 0 && str.length() <= 20) {
                    product.setProductName(str);
                    break;
                }
                System.out.println("Неверные данные, повторите запрос!");
            }

            while (true) {
                System.out.print("ID категории:  ");
                str = scan.nextLine();
                Category category = manager.find(Category.class, Long.parseLong(str));
                if (category != null) {
                    product.setCategory(category);
                    break;
                }
                System.out.println("Неверные данные, повторите запрос!");
            }

            while (true) {
                System.out.print("Стоимость продукта:  ");
                Long cost = 0L;
                try {
                    cost = Long.parseLong(scan.nextLine());
                    if (cost < 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    System.out.println("Неверные данные, повторите запрос!");
                    continue;
                }

                product.setCost(cost);
                break;
            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            System.out.println("Было поймано исключение " + e);
        } finally {
            manager.close();
        }
    }

    private static void updateProduct() {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();

            System.out.print("ProductID: ");
            int productId = Integer.parseInt(scan.nextLine());

            Product product = manager.find(Product.class, productId);
            System.out.println(product);
            for (ProductCharacteristic productCharacteristic : product.getProductCharacteristics()) {
                System.out.printf("%s : %s\n",
                        productCharacteristic.getCharacteristic().getCharacteristicName(),
                        productCharacteristic.getCharacteristicText());
            }

            String str;
            System.out.println("\nВнесите следующие изменение:");

            System.out.println("ProductID: " + product.getProductID());
            System.out.println("Категория: " + product.getCategory().getCategoryName());

            System.out.print("Название:  ");
            if (!(str = scan.nextLine()).isEmpty()) product.setProductName(str);

            System.out.print("Стоймость: ");
            if (!(str = scan.nextLine()).isEmpty()) product.setCost(Long.parseLong(str));

            List<Characteristic> characteristics = product.getCategory().getCharacteristics();
            for (Characteristic characteristic : characteristics) {
                TypedQuery<ProductCharacteristic> typedQuery = manager.createQuery(
                                "select pc from ProductCharacteristic pc where pc.product = ?1 and pc.characteristic = ?2",
                                ProductCharacteristic.class)
                        .setParameter(1, product)
                        .setParameter(2, characteristic);
                ;
                try {
                    System.out.printf("%s : ", characteristic.getCharacteristicName());
                    str = scan.nextLine();
                    ProductCharacteristic productCharacteristic = typedQuery.getSingleResult();
                    productCharacteristic.setCharacteristicText(str);
                } catch (NoResultException e) {
                    ProductCharacteristic productCharacteristic = new ProductCharacteristic();
                    if (!str.isEmpty()){
                        productCharacteristic.setProduct(product);
                        productCharacteristic.setCharacteristic(characteristic);
                        productCharacteristic.setCharacteristicText(str);
                        manager.persist(productCharacteristic);
                    }
                }

            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            System.out.println("Было поймано исключение " + e);
        } finally {
            manager.close();
        }
    }

    private static void deleteProduct() {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();

            System.out.print("ProductID: ");
            int productId = Integer.parseInt(scan.nextLine());
            Product product = manager.find(Product.class, productId);

            for (ProductCharacteristic productCharacteristic : product.getProductCharacteristics()) {
                manager.remove(productCharacteristic);
            }

            manager.remove(product);
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            System.out.println("Было поймано исключение " + e);
        } finally {
            manager.close();
        }
    }
}
