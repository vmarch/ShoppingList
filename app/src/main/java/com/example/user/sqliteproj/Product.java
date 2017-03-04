package com.example.user.sqliteproj;


public class Product {
    private int idProd;
    private String nameProd;
    private double priceProd;
    private double qtProd;
    private int kindProd;
    private double costProd;

    public Product(
            int idProd,
            String nameProd,
            double priceProd,
            double qtProd,
            int kindProd,
            double costProd) {

        this.idProd = idProd;
        this.nameProd = nameProd;
        this.priceProd = priceProd;
        this.qtProd = qtProd;
        this.kindProd = kindProd;
        this.costProd = costProd;

    }

    public int getIdProd() {
        return idProd;
    }

    public void setIdProd(int idProd) {
        this.idProd = idProd;
    }

    public String getNameProd() {
        return nameProd;
    }

    public void setNameProd(String nameProd) {
        this.nameProd = nameProd;
    }

    public double getPriceProd() {
        return priceProd;
    }

    public void setPriceProd(double priceProd) {
        this.priceProd = priceProd;
    }

    public double getQtProd() {
        return qtProd;
    }

    public void setQtProd(double qtProd) {
        this.qtProd = qtProd;
    }

    public int getKindProd() {
        return kindProd;
    }

    public void setKindProd(int kindProd) {
        this.kindProd = kindProd;
    }

    public double getCostProd() {
        return costProd;
    }

    public void setCostProd(double costProd) {
        this.costProd = costProd;
    }

}
