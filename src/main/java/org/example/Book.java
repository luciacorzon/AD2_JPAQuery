/*
 * Autor: Pepe Calo
 * Realizado con fines educativos.
 * Puede modificarlo siempre que no lo haga con fines comerciales.
 */
package org.example;
import jakarta.persistence.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author pepecalo
 */
@Entity
public class Book implements Serializable {

    //    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBook;
    @Column(length = 13)
    private String isbn;
    @Column(name = "titulo")
    private String title;
    @Column(name = "autor")
    private String author;
    @Column(name = "anho")
    private Short ano;
    @Column(name = "disponible")
    private Boolean available;
    private byte[] portada;

    private LocalDate dataPublicacion;

    //    @Transient
    transient private List<Contido> contenido;

    private static final long serialVersionUID = 1L;

    public Book() {
    }

    public Book(String title, String author, Short year, Boolean available) {
        this.title = title;
        this.author = author;
        this.ano = year;
        this.available = available;
    }

    public Book(String isbn, String title, String author, Short year,
                Boolean available) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.ano = year;
        this.available = available;
    }

    public Book(String isbn, String title, String author, Short year,
                Boolean available, byte[] portada) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.ano = year;
        this.available = available;
        this.portada = portada;
    }

    public Book(Long idBook, String isbn, String title, String author,
                Short year, Boolean available, byte[] portada) {
        this.idBook = idBook;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.ano = year;
        this.available = available;
        this.portada = portada;
    }

    public Long getIdBook() {
        return idBook;
    }

    public Book setIdBook(Long idBook) {
        this.idBook = idBook;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public Book setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Book setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Short getYear() {
        return ano;
    }

    public Book setAno(Short ano) {
        this.ano = ano;
        return this;
    }

    public Boolean isAvailable() {
        return available;
    }

    public Book setAvailable(Boolean available) {
        this.available = available;
        return this;
    }

    public byte[] getCover() {
        return portada;
    }

    public Book setCover(byte[] portada) {
        this.portada = portada;
        return this;
    }

    public LocalDate getDataPublicacion() {
        return dataPublicacion;
    }

    public Book setDataPublicacion(LocalDate dataPublicacion) {
        this.dataPublicacion = dataPublicacion;
        return this;
    }

    /**
     * Asigna la portada con flujos, leyendo los bytes.
     *
     * @param f
     */
    public Book setPortada(File f) {
        if (f == null || !f.exists())
            return this;
        Path p = Paths.get(f.getAbsolutePath());
        try (BufferedInputStream bi = new BufferedInputStream(Files.newInputStream(p));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesLidos;
            while ((bytesLidos = bi.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesLidos);
            }

            portada = outputStream.toByteArray();
        } catch (FileNotFoundException ex) {
            System.err.println("Archivo no encontrado: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Erro de E/S: " + ex.getMessage());
        }
        return this;
    }

    /**
     * Asigna la portada con Java NIO, leyendo los bytes.
     *
     * @param file
     */
    public Book setPortada(String file) {
        try {
            Path ruta = Paths.get(file);
            portada = Files.readAllBytes(ruta);
        } catch (IOException ex) {
            System.err.println("Error de E/S: " + ex.getMessage());
        }
        return this;
    }

    public Image getImage() {
        if (portada != null) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(portada)) {
                Image imaxe = ImageIO.read(bis);
                if(available) {
                    imaxe.getGraphics().drawLine(0,0, 100, 100);
                }

                return imaxe;
            } catch (IOException e) {
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.isbn);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Book other = (Book) obj;
        return Objects.equals(this.isbn, other.isbn);
    }

    @Override
    public String toString() {
        return idBook + "] [isbn: " + isbn + "] " + title + ". "
                + author + " (" + ano + ") [" + ((available) ? '*' : ' ') + ']';
    }

}