package com.pagoflex.usuarios_service.service;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AlmacenamientoService {

    private static final long TAMANO_MAXIMO_BYTES = 5L * 1024 * 1024;
    private static final int DIMENSION_MAXIMA = 8_000;
    private static final long PIXELES_MAXIMOS = 40_000_000L;

    private final Path directorioAvatares;

    public AlmacenamientoService(@Value("${app.avatar.storage-dir:uploads/avatars}") String directorioAvatares) {
        this.directorioAvatares = Path.of(directorioAvatares).toAbsolutePath().normalize();
    }

    public void guardarAvatar(Long usuarioId, MultipartFile archivo) throws IOException {
        if (archivo == null || archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes seleccionar una imagen");
        }
        if (archivo.getSize() > TAMANO_MAXIMO_BYTES) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "La imagen no puede superar 5 MB");
        }

        BufferedImage imagen = leerImagen(archivo);
        Path destino = rutaAvatar(usuarioId);
        Files.createDirectories(directorioAvatares);
        Path temporal = Files.createTempFile(directorioAvatares, "avatar-", ".tmp");
        try {
            if (!ImageIO.write(imagen, "jpg", temporal.toFile())) {
                throw new IOException("No fue posible convertir la imagen a JPEG");
            }
            try {
                Files.move(temporal, destino, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporal, destino, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporal);
            imagen.flush();
        }
    }

    public Resource cargarAvatar(Long usuarioId) {
        Path ruta = rutaAvatar(usuarioId);
        Resource recurso = new FileSystemResource(ruta);
        if (!recurso.exists() || !recurso.isReadable()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no tiene un avatar");
        }
        return recurso;
    }

    public void eliminarAvatar(Long usuarioId) throws IOException {
        Files.deleteIfExists(rutaAvatar(usuarioId));
    }

    private BufferedImage leerImagen(MultipartFile archivo) throws IOException {
        try (ImageInputStream entrada = ImageIO.createImageInputStream(archivo.getInputStream())) {
            if (entrada == null) {
                throw imagenInvalida();
            }
            Iterator<ImageReader> lectores = ImageIO.getImageReaders(entrada);
            if (!lectores.hasNext()) {
                throw imagenInvalida();
            }
            ImageReader lector = lectores.next();
            try {
                lector.setInput(entrada, true, true);
                int ancho = lector.getWidth(0);
                int alto = lector.getHeight(0);
                if (ancho <= 0 || alto <= 0 || ancho > DIMENSION_MAXIMA || alto > DIMENSION_MAXIMA
                        || (long) ancho * alto > PIXELES_MAXIMOS) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Las dimensiones de la imagen no son válidas");
                }
                BufferedImage original = lector.read(0);
                if (original == null) {
                    throw imagenInvalida();
                }
                BufferedImage rgb = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
                Graphics2D graficos = rgb.createGraphics();
                try {
                    graficos.drawImage(original, 0, 0, java.awt.Color.WHITE, null);
                } finally {
                    graficos.dispose();
                }
                original.flush();
                return rgb;
            } finally {
                lector.dispose();
            }
        }
    }

    private Path rutaAvatar(Long usuarioId) {
        return directorioAvatares.resolve("avatar-" + usuarioId + ".jpg").normalize();
    }

    private ResponseStatusException imagenInvalida() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo no es una imagen válida");
    }
}
