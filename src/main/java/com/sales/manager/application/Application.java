package com.sales.manager.application;

import com.sales.manager.application.entity.Sale;
import com.sales.manager.application.entity.Seller;
import com.sales.manager.application.repository.SaleRepository;
import com.sales.manager.application.repository.SellerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.*;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner run(SaleRepository saleRepository, SellerRepository sellerRepository) {
        return args -> {
            String option;

            sellerRepository.save(new Seller("Henrique"));
            sellerRepository.save(new Seller("Jonas"));
            sellerRepository.save(new Seller("Raquel"));
            sellerRepository.save(new Seller("Marjorie"));
            sellerRepository.save(new Seller("Penelope"));

            try {
                do {
                    option = showMenu(sellerRepository);

                    switch (option) {
                        case "1":
                            makeNewSale(saleRepository, sellerRepository);
                            break;
                        case "2":
                            listSales(saleRepository, sellerRepository);
                            break;
                        case "3":
                            removeSale(saleRepository, sellerRepository);
                            break;
                        case "4":
                            listBestSellers(saleRepository, sellerRepository);
                            break;
                        default:
                            break;
                    }
                } while (!option.equals("5"));
            }catch (Exception ex){
                log.error(ex.getMessage());
            }
        };
    }

    private void listSellers(Iterable<Seller> sellers) {
        System.out.println("All sellers: ");
        sellers.forEach(seller -> {
            System.out.print(seller.getName() + " | ");
        });
        System.out.println();
    }

    private void listSales(SaleRepository repo, SellerRepository sellerRepo) {
        List<Sale> sales;
        List<Map<Sale, Double>> salesOrdered = null;

        sales = repo.getAllSales();

        if(sales.isEmpty())
            showMenu(sellerRepo);

        sales.forEach((sale) -> {
            System.out.printf(
                    "Id: %d, seller: %s, item: %s, customer: %s, value: %.2f data: %s%n",
                    sale.getId(),
                    sale.getSeller().getName(), sale.getItemName(), sale.getCustomer(),
                    sale.getValue().floatValue(), sale.getDate().toString()
            );
        });
    }

    private String showMenu(SellerRepository repo) {
        listSellers(repo.findAll());

        System.out.println("___Sales Manager___");
        System.out.println("1. New Sale");
        System.out.println("2. List Sales");
        System.out.println("3. Remove Sale");
        System.out.println("4. Best Sellers");
        System.out.println("5. Exit");

        return in.nextLine();
    }

    private void newSale(SaleRepository saleRepo, Sale sale) {
        try {
            saleRepo.save(sale);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private void makeNewSale(SaleRepository saleRepository, SellerRepository sellerRepository) {
        List<Seller> sellersFound;
        String sellerName;
        String customer;
        String itemName;
        double value;
        Date saleDate = Date.from(Instant.now());

        System.out.println("___New sale___");

        do {
            System.out.print("Seller: ");
            sellerName = in.nextLine();
            sellersFound = sellerRepository.findByName(sellerName);
        } while (sellersFound.isEmpty());

        System.out.print("Customer: ");
        customer = in.nextLine();

        System.out.print("Item: ");
        itemName = in.nextLine();

        System.out.print("Value: ");
        value = Double.parseDouble(in.nextLine());

        newSale(saleRepository, new Sale(sellersFound.get(0), customer, itemName, saleDate, value));

        System.out.println();
    }

    private void removeSale(SaleRepository repo, SellerRepository sellerRepo) {
        Long saleId;
        List<Sale> sales = repo.getAllSales();

        if(sales.isEmpty()){
            System.out.println("No sells yet");
            showMenu(sellerRepo);
            return;
        }

        sales.forEach(sale -> {
            System.out.printf("%d -> item: %s value: %.2f data: %s%n",
                    sale.getId(), sale.getItemName(), sale.getValue(), sale.getDate().toString()
            );
        });

        do {
            System.out.print("Please type the sell id you want do be deleted: ");
            saleId = Long.parseLong(in.nextLine());
        } while (repo.findById(saleId).isEmpty());

        try {
            repo.delete(repo.findById(saleId).get());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        System.out.println("Sale deleted successfully");
    }

    private void listBestSellers(SaleRepository repo, SellerRepository sellerRepo){
        System.out.println();

        HashMap<Long, Double> bestSellers = new HashMap<>();
        HashMap<Long, Double> bestSellersOrdered = new HashMap<>();

        repo.getAllSales().forEach(sale -> {
            bestSellers.merge(sale.getSeller().getId(), sale.getValue(), Double::sum);
        });

        //Sorting by amount
        bestSellers.entrySet().stream().sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                .forEach(k -> {
                    Seller seller = sellerRepo.findById(k.getKey()).get();
                    Double amount = k.getValue();

                    System.out.printf(
                            "Id: %d, seller: %s, amount: %.2f%n",
                            seller.getId(), seller.getName(), amount
                    );
                });

    }
}
