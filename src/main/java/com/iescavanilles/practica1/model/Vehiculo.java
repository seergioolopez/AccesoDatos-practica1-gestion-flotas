/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iescavanilles.practica1.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author sergi
 */
public class Vehiculo implements Serializable {
    
 private static final long serialversionUID = 1L;
    private String matricula;
    private String marca;
    private String modelo;
    private int ano;
    
    @SerializedName("km_recorridos")
    private double kilometraje;
    private String categoriaMantenimiento;
    
    public Vehiculo(){
        
    }
    public Vehiculo(String matricula, String marca, String modelo, int ano, 
                    double kilometraje, String categoriaMantenimiento) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo; 
        this.ano = ano;
        this.kilometraje = kilometraje;
        this.categoriaMantenimiento = categoriaMantenimiento;
        
        
    } 

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public double getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(double kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getCategoriaMantenimiento() {
        return categoriaMantenimiento;
    }

    public void setCategoriaMantenimiento(String categoriaMantenimiento) {
        this.categoriaMantenimiento = categoriaMantenimiento;
    }
    
    @Override
    public String toString(){
        return matricula + "-" + marca + " " + modelo + "(" +ano+ ")" 
                + kilometraje + "km [" + categoriaMantenimiento + "]";
    }
}

