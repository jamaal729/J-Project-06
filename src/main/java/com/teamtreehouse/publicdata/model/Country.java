package com.teamtreehouse.publicdata.model;

import javax.persistence.*;

@Entity
public class Country {

    // code: VARCHAR(3)
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String code;

    @Column // @Column(columnDefinition = "VARCHAR(32)")
    private String name;

    @Column // @Column(columnDefinition = "DECIMAL(11,8)")
    private Double internetUsers;

    @Column // @Column(columnDefinition = "DECIMAL(11,8)")
    private Double adultLiteracyRate;

    public Country() {}

    public Country(CountryBuilder builder) {
        this.code = builder.code;
        this.name = builder.name;
        this.internetUsers = builder.internetUsers;
        this.adultLiteracyRate = builder.adultLiteracyRate;
    }

    public static class CountryBuilder {
        private String code;
        private String name;
        private Double internetUsers;
        private Double adultLiteracyRate;

        public CountryBuilder(String code) {
            this.code = code;
        }

        public CountryBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CountryBuilder withInternetUsers(Double internetUsers) {
            this.internetUsers = internetUsers;
            return this;
        }

        public CountryBuilder withAdultLiteracyRate(Double adultLiteracyRate) {
            this.adultLiteracyRate = adultLiteracyRate;
            return this;
        }

        public Country build() {
            return new Country(this);
        }
    }

    @Override
    public String toString() {
        return "Country{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", internetUsers=" + internetUsers +
                ", adultLiteracyRate=" + adultLiteracyRate +
                '}';
    }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Double getInternetUsage() { return internetUsers; }

    public void setInternetUsage(Double internetUsers) { this.internetUsers = internetUsers; }

    public Double getAdultLiteracy() { return adultLiteracyRate; }

    public void setAdultLiteracy(Double adultLiteracyRate) { this.adultLiteracyRate = adultLiteracyRate; }
}
