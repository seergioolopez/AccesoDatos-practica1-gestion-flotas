package com.iescavanilles.practica1.dao;

import com.iescavanilles.practica1.model.Vehiculo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class GestorBackup {
    public static void guardar(Path ruta, ArrayList<Vehiculo> flota) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(ruta)))) {
            out.writeObject(flota);
        } catch (IOException e) {
            System.err.println("Error creando el backup: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Vehiculo> restaurar(Path ruta) {
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(ruta)))) {
            return (ArrayList<Vehiculo>) in.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println("La clase del objeto guardado no existe: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error leyendo el backup: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}