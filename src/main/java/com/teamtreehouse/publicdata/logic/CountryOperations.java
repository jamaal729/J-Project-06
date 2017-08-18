package com.teamtreehouse.publicdata.logic;

import com.teamtreehouse.publicdata.model.Country;
import com.teamtreehouse.publicdata.model.Country.CountryBuilder;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.Criteria;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingDouble;

public class CountryOperations {

    private static String code;
    private static String name;
    private static double internetUsage;
    private static double adultLiteracy;
    private static Scanner scanner = new Scanner(System.in);

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    private static void printOneCountry(Country itemToPrint) {
        printHeading();
        printCurrentCountry(itemToPrint);
    }

    private static void printCountries(List<Country> listToPrint) {
        printHeading();
        for (Country country : listToPrint) printCurrentCountry(country);
    }

    private static void printHeading() {
        System.out.println();
        System.out.printf("%-6s %-24s %18s %18s %n", "CODE", "COUNTRY NAME", "% INTERNET", "% LITERACY");
    }

    private static void printCurrentCountry(Country c1) {
        String internetUsageString;
        String adultLiteracyString;

        if (c1.getInternetUsage() == null) internetUsageString = "--";
        else internetUsageString = String.valueOf(new DecimalFormat("#.00").format(c1.getInternetUsage()));

        if (c1.getAdultLiteracy() == null) adultLiteracyString = "--";
        else adultLiteracyString = String.valueOf(new DecimalFormat("#.00").format(c1.getAdultLiteracy()));

        System.out.printf("%-6s %-24s %18s %18s %n", c1.getCode(), c1.getName(), internetUsageString, adultLiteracyString);
    }

    public static void listAllCountries() {
        List<Country> allCountries = getAllCountries();
        printCountries(allCountries);
        System.out.print("\n");
    }

    @SuppressWarnings("unchecked")
    public static List<Country> getAllCountries() {

        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Country.class);

        List<Country> countries = criteria.list();

        session.close();
        return countries;
    }

    public static Country findCountryByCode() {

        System.out.println("Enter country code (in Uppercase): ");
        code = scanner.nextLine();

        Session session = sessionFactory.openSession();
        Country country = session.get(Country.class, code);

        if (country == null) {
            System.out.println("Country is not in the list");
        } else
            printOneCountry(country);

        session.close();
        return country;
    }

    public static void editCountryInfo() {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        System.out.println("Enter country code (in Uppercase): ");
        code = scanner.nextLine();
        Country country = session.get(Country.class, code);

        if (country != null) {
            enterCountryDetails();
            country.setName(name);
            country.setInternetUsage(internetUsage);
            country.setAdultLiteracy(adultLiteracy);

            session.update(country);
            System.out.printf("Country with code '%s' updated successfully%n", code);
        } else {
            System.out.printf("Country with code '%s' not found, cannot update%n", code);
        }

        session.getTransaction().commit();
        session.close();
    }

    public static void addCountry() {

        System.out.println("Enter country code (in Uppercase): ");
        code = scanner.nextLine();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Country country = session.get(Country.class, code);

        if (country == null) {
            enterCountryDetails();
            country = new CountryBuilder(code)
                    .withName(name)
                    .withInternetUsers(internetUsage)
                    .withAdultLiteracyRate(adultLiteracy)
                    .build();
            session.save(country);
            System.out.printf("Country with code '%s' added successfully%n", code);
        } else {
            System.out.printf("A country with code '%s' exists, try again%n", code);
        }

        session.getTransaction().commit();
        session.close();
    }

    private static void enterCountryDetails() {
        System.out.print("Enter country name: ");
        name = scanner.nextLine();

        System.out.print("Enter internet usage: ");
        internetUsage = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter adult literacy: ");
        adultLiteracy = Double.parseDouble(scanner.nextLine());
    }

    public static void deleteCountry() {

        System.out.println("Enter country code (in Uppercase): ");
        code = scanner.nextLine();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Country country = session.get(Country.class, code);

        if (country != null) {
            session.delete(country);
            System.out.printf("Country with code '%s' deleted successfully%n", code);
        } else {
            System.out.printf("Country with code '%s' not found, cannot delete%n", code);
        }

        session.getTransaction().commit();
        session.close();
    }

    public static void statsInternetUsage(List<Country> countries) {

        Country maxInternetUsage = countries
                .stream()
                .filter(country -> country.getInternetUsage() != null)
                .max(comparingDouble(Country::getInternetUsage))
                .get();

        Country minInternetUsage = countries
                .stream()
                .filter(country -> country.getInternetUsage() != null)
                .min(comparingDouble(Country::getInternetUsage))
                .get();

        System.out.printf(" Maximum internet usage: %s, %5.2f%% %n",
                maxInternetUsage.getName(), maxInternetUsage.getInternetUsage());

        System.out.printf(" Minimum internet usage: %s, %5.2f%% %n",
                minInternetUsage.getName(), minInternetUsage.getInternetUsage());
    }

    public static void statsAdultLiteracy(List<Country> countries) {

        Country maxAdultLiteracy = countries
                .stream()
                .filter(country -> country.getAdultLiteracy() != null)
                .max(comparingDouble(Country::getAdultLiteracy))
                .get();

        Country minAdultLiteracy = countries
                .stream()
                .filter(country -> country.getAdultLiteracy() != null)
                .min(comparingDouble(Country::getAdultLiteracy))
                .get();

        System.out.printf(" Maximum adult literacy: %s, %5.2f%% %n",
                maxAdultLiteracy.getName(), maxAdultLiteracy.getAdultLiteracy());

        System.out.printf(" Minimum adult literacy: %s, %5.2f%% %n",
                minAdultLiteracy.getName(), minAdultLiteracy.getAdultLiteracy());
    }

    public static List<Country> removeNulls(List<Country> countries) {

        List<Country> countriesWithNullsRemoved = countries
                .stream()
                .filter(country ->
                        ((country.getInternetUsage() != null) & (country.getAdultLiteracy() != null)))
                .collect(Collectors.toList());

        return countriesWithNullsRemoved;
    }

    public static void calculateCorrelation(List<Country> countries) {
        // Using Pearson correlation coefficient:

        Double x;
        Double y;
        Double xSquared;
        Double ySquared;
        Double productXY;

        Double sumX = 0.0;
        Double sumY = 0.0;
        Double sumXSquared = 0.0;
        Double sumYSquared = 0.0;
        Double sumProductXY = 0.0;

        Double correlationCoefficient;

        List<Country> countriesWithoutNulls = removeNulls(getAllCountries());
        int n = countriesWithoutNulls.size();

        for (Country country : countriesWithoutNulls) {
            x = country.getInternetUsage();
            y = country.getAdultLiteracy();
            xSquared = x * x;
            ySquared = y * y;
            productXY = x * y;

            sumX += x;
            sumY += y;
            sumXSquared += xSquared;
            sumYSquared += ySquared;
            sumProductXY += productXY;
        }

        Double covarianceXY = (n * sumProductXY) - (sumX * sumY);
        Double stdDevX = (n * sumXSquared) - (sumX * sumX);
        Double stdDevY = (n * sumYSquared) - (sumY * sumY);

        correlationCoefficient = covarianceXY / Math.sqrt(stdDevX * stdDevY);
        System.out.printf(" Correlation Coefficient: %5.2f", correlationCoefficient);
    }

    public static void calculateStatistics() {

        // System.out.println("Statistics:");
        statsInternetUsage(getAllCountries());
        statsAdultLiteracy(getAllCountries());
        calculateCorrelation(getAllCountries());
        System.out.println();
    }
}
