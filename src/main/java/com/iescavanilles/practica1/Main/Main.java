package com.iescavanilles.practica1.Main;

import com.iescavanilles.practica1.dao.GestorBackup;
import com.iescavanilles.practica1.dao.GestorConfiguracion;
import com.iescavanilles.practica1.dao.GestorInventarioJSON;
import com.iescavanilles.practica1.dao.GestorReportesXML;
import com.iescavanilles.practica1.model.Vehiculo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class Main {

    public static void main(String[] args) {
        Path dirConfig = Paths.get("datos", "config");
        Path dirJson = Paths.get("datos", "json");
        Path dirBackup = Paths.get("datos", "backup");
        Path dirReports = Paths.get("datos", "reports");

        // 1. Estructura de directorios (NIO.2)
        try {
            Files.createDirectories(dirConfig);
            Files.createDirectories(dirJson);
            Files.createDirectories(dirBackup);
            Files.createDirectories(dirReports);
        } catch (IOException e) {
            System.err.println("No se pudieron crear los directorios: " + e.getMessage());
            return;
        }

        // 2. Colecciones en memoria
        ArrayList<Vehiculo> flota = new ArrayList<>();
        HashMap<String, Vehiculo> indice = new HashMap<>();
        Set<String> matriculas = new TreeSet<>();

        Vehiculo[] ejemplo = {
                new Vehiculo("1234ABC", "Renault", "Master", 2020, 85000, "Preventivo"),
                new Vehiculo("5678DEF", "Iveco", "Daily", 2018, 150000, "Correctivo"),
                new Vehiculo("9012GHI", "Mercedes", "Sprinter", 2021, 40000, "Preventivo"),
                new Vehiculo("1234ABC", "Ford", "Transit", 2019, 99000, "Predictivo")  // matrícula repetida
        };

        for (Vehiculo v : ejemplo) {
            if (matriculas.add(v.getMatricula())) {      // add() devuelve false si ya existía
                flota.add(v);
                indice.put(v.getMatricula(), v);
            } else {
                System.out.println("Matrícula duplicada, se ignora: " + v.getMatricula());
            }
        }

        // 3. Configuración binaria
        double kmTotales = 0;
        for (Vehiculo v : flota) {
            kmTotales += v.getKilometraje();
        }
        Path rutaConfig = dirConfig.resolve("empresa.dat");
        GestorConfiguracion.guardar(rutaConfig,
                new GestorConfiguracion.Configuracion("LogiTrans", "1.0", flota.size(), kmTotales));
        System.out.println("Configuración leída: " + GestorConfiguracion.leer(rutaConfig));

        // 4. JSON: exportar e importar a colecciones nuevas
        Path rutaJson = dirJson.resolve("flota.json");
        GestorInventarioJSON.exportar(rutaJson, flota);

        ArrayList<Vehiculo> listaJson = new ArrayList<>();
        HashMap<String, Vehiculo> mapaJson = new HashMap<>();
        GestorInventarioJSON.importar(rutaJson, listaJson, mapaJson);
        System.out.println("Vehículos importados del JSON: " + listaJson.size());
        System.out.println("Búsqueda directa 5678DEF: " + mapaJson.get("5678DEF"));

        // 5. Backup nativo
        Path rutaBackup = dirBackup.resolve("flota_backup.ser");
        GestorBackup.guardar(rutaBackup, flota);
        ArrayList<Vehiculo> restaurada = GestorBackup.restaurar(rutaBackup);
        System.out.println("Vehículos restaurados del backup: " + restaurada.size());

        // 6. XML: DOM para escribir, SAX para leer
        Path rutaXml = dirReports.resolve("informe_flota.xml");
        GestorReportesXML.generar(rutaXml, flota);
        Set<String> categorias = GestorReportesXML.leerCategorias(rutaXml);
        System.out.println("Categorías de mantenimiento únicas: " + categorias);
    }
}