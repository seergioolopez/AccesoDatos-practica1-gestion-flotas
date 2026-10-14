package com.iescavanilles.practica1.dao;

import com.iescavanilles.practica1.model.Vehiculo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GestorReportesXML {

    public static void generar(Path ruta, List<Vehiculo> flota) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();

            Element raiz = doc.createElement("flota");
            doc.appendChild(raiz);

            for (Vehiculo v : flota) {
                Element vehiculo = doc.createElement("vehiculo");
                vehiculo.setAttribute("matricula", v.getMatricula());
                vehiculo.appendChild(crearElemento(doc, "marca", v.getMarca()));
                vehiculo.appendChild(crearElemento(doc, "modelo", v.getModelo()));
                vehiculo.appendChild(crearElemento(doc, "anio", String.valueOf(v.getAno())));
                vehiculo.appendChild(crearElemento(doc, "kilometraje", String.valueOf(v.getKilometraje())));
                vehiculo.appendChild(crearElemento(doc, "categoriaMantenimiento", v.getCategoriaMantenimiento()));
                raiz.appendChild(vehiculo);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            try (OutputStream os = Files.newOutputStream(ruta)) {
                transformer.transform(new DOMSource(doc), new StreamResult(os));
            }
        } catch (ParserConfigurationException | TransformerException | IOException e) {
            System.err.println("Error generando el XML: " + e.getMessage());
        }
    }

    private static Element crearElemento(Document doc, String nombre, String texto) {
        Element e = doc.createElement(nombre);
        e.setTextContent(texto == null ? "" : texto);
        return e;
    }

    public static Set<String> leerCategorias(Path ruta) {
        Set<String> categorias = new HashSet<>();

        DefaultHandler handler = new DefaultHandler() {
            private boolean dentroDeCategoria = false;
            private final StringBuilder texto = new StringBuilder();

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attrs) {
                if (qName.equals("categoriaMantenimiento")) {
                    dentroDeCategoria = true;
                    texto.setLength(0);
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) {
                if (dentroDeCategoria) {
                    texto.append(ch, start, length);
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) {
                if (qName.equals("categoriaMantenimiento")) {
                    String categoria = texto.toString().trim();
                    if (!categoria.isEmpty()) {
                        categorias.add(categoria);   // el Set descarta los repetidos
                    }
                    dentroDeCategoria = false;
                }
            }
        };

        try (InputStream is = Files.newInputStream(ruta)) {
            SAXParserFactory.newInstance().newSAXParser().parse(is, handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println("Error leyendo el XML con SAX: " + e.getMessage());
        }
        return categorias;
    }
}