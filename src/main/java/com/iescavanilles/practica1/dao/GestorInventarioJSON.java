package com.iescavanilles.practica1.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iescavanilles.practica1.model.Vehiculo;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class GestorInventarioJSON {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void exportar(Path ruta, List<Vehiculo> flota) {
        try (Writer writer = Files.newBufferedWriter(ruta)) {
            GSON.toJson(flota, writer);
        } catch (IOException e) {
            System.err.println("Error exportando JSON: " + e.getMessage());
        }
    }


    public static void importar(Path ruta, List<Vehiculo> lista, Map<String, Vehiculo> mapa) {
        Type tipoLista = new TypeToken<List<Vehiculo>>() { }.getType();

        try (Reader reader = Files.newBufferedReader(ruta)) {
            List<Vehiculo> leidos = GSON.fromJson(reader, tipoLista);
            lista.clear();
            mapa.clear();
            if (leidos != null) {
                for (Vehiculo v : leidos) {
                    lista.add(v);
                    mapa.put(v.getMatricula(), v);
                }
            }
        } catch (IOException e) {
            System.err.println("Error importando JSON: " + e.getMessage());
        }
    }
}