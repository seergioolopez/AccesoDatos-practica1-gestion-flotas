package com.iescavanilles.practica1.dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class GestorConfiguracion {

    /** Datos globales de la empresa. */
    public static class Configuracion {
        private final String empresa;
        private final String version;
        private final int vehiculosActivos;
        private final double kmAcumulados;

        public Configuracion(String empresa, String version,
                             int vehiculosActivos, double kmAcumulados) {
            this.empresa = empresa;
            this.version = version;
            this.vehiculosActivos = vehiculosActivos;
            this.kmAcumulados = kmAcumulados;
        }

        public String getEmpresa() { return empresa; }
        public String getVersion() { return version; }
        public int getVehiculosActivos() { return vehiculosActivos; }
        public double getKmAcumulados() { return kmAcumulados; }

        @Override
        public String toString() {
            return empresa + " v" + version + " | activos: " + vehiculosActivos
                    + " | km: " + kmAcumulados;
        }
    }


    public static void guardar(Path ruta, Configuracion c) {
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(Files.newOutputStream(ruta)))) {
            out.writeUTF(c.getEmpresa());
            out.writeUTF(c.getVersion());
            out.writeInt(c.getVehiculosActivos());
            out.writeDouble(c.getKmAcumulados());
        } catch (IOException e) {
            System.err.println("Error escribiendo la configuración: " + e.getMessage());
        }
    }

    public static Configuracion leer(Path ruta) {
        Configuracion ultima = null;
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(Files.newInputStream(ruta)))) {
            while (true) {
                String empresa = in.readUTF();
                String version = in.readUTF();
                int activos = in.readInt();
                double km = in.readDouble();
                ultima = new Configuracion(empresa, version, activos, km);
            }
        } catch (EOFException e) {
            // Fin normal de la lectura: no queda nada más por leer
        } catch (NoSuchFileException e) {
            System.err.println("No existe el fichero de configuración: " + ruta);
        } catch (IOException e) {
            System.err.println("Error leyendo la configuración: " + e.getMessage());
        }
        return ultima;
    }
}