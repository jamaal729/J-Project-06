package com.teamtreehouse.publicdata.ui;

import com.teamtreehouse.publicdata.logic.CountryOperations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Console {

    private static Scanner scanner = new Scanner(System.in);
    private static Map<Integer, String> menu;


    // Create a menu
    public static void createMenu() {

        menu = new HashMap<>();
        menu.put(1, "View all countries");
        menu.put(2, "Calculate statistics");
        menu.put(3, "View a country");
        menu.put(4, "Edit a country");
        menu.put(5, "Add a country");
        menu.put(6, "Delete a country");
        menu.put(7, "Quit");
    }

     private static int selectFromMenu() {

        System.out.printf("\nChoices:\n");

        for (Map.Entry<Integer, String> option : menu.entrySet()) {
            System.out.printf("%d: %s %n", option.getKey(), option.getValue());
        }
        System.out.printf("\nPlease choose an option (1-7): ");
        int selection = scanner.nextInt();

        for (Map.Entry<Integer, String> option : menu.entrySet()) {
            if (selection == option.getKey()) {
                System.out.printf("%d: %s %n", option.getKey(), option.getValue());
            }
        }
        return selection;
    }

    public void run() {

        System.out.printf("\nAnalyzing public data with Hibernate\n\n");
        int numOfCountries = CountryOperations.getAllCountries().size();
        System.out.print("\nThere are " + numOfCountries + " countries in the database\n");

        createMenu();

        int choice = 0;

        do {
            switch (choice = selectFromMenu()) {
                case 1:
                    CountryOperations.listAllCountries();
                    break;
                case 2:
                    CountryOperations.calculateStatistics();
                    break;
                case 3:
                    CountryOperations.findCountryByCode();
                    break;
                case 4:
                    CountryOperations.editCountryInfo();
                    break;
                case 5:
                    CountryOperations.addCountry();
                    break;
                case 6:
                    CountryOperations.deleteCountry();
                    break;
                case 7:
                    System.out.println("Exiting Application\n");
                    break;
                default:
                    System.out.println("Please enter a valid choice.\n");
                    break;
            }
        } while (!(choice == 7));
    }
}
