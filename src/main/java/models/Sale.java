/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 *
 * @author usuario
 */
public class Sale {
    
    private int id;
    private LocalDate date;
    private LocalTime hora;
    private String client;
    private List<String[]> productData;
    private Double totalAmount;

    public Sale(int id, LocalDate date, LocalTime hora, String client, List<String[]> productData, Double totalAmount) {
        this.id = id;
        this.date = date;
        this.hora = hora;
        this.client = client;
        this.productData = productData;
        this.totalAmount = totalAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public List<String[]> getProductData() {
        return productData;
    }

    public void setProductData(List<String[]> productData) {
        this.productData = productData;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    
    
}
