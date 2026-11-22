package com.pagoflex.usuarios_service.controller;

import com.pagoflex.usuarios_service.dto.LoginRequest;
import com.pagoflex.usuarios_service.dto.ActualizarUsuarioRequest;
import com.pagoflex.usuarios_service.dto.CambiarContrasenaRequest;
import com.pagoflex.usuarios_service.dto.RegistroRequest;
import com.pagoflex.usuarios_service.dto.UsuarioResponse;
import com.pagoflex.usuarios_service.service.UsuarioService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponse> listar(@RequestParam(required = false) String buscar) {
        return usuarioService.listar(buscar);
    }

    @GetMapping("/{id}")
    public UsuarioResponse obtener(@PathVariable Long id) {
        return usuarioService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse registrar(@RequestBody RegistroRequest datos) {
        return usuarioService.registrar(datos);
    }

    @PostMapping("/login")
    public UsuarioResponse iniciarSesion(@RequestBody LoginRequest datos) {
        return usuarioService.iniciarSesion(datos);
    }

    @PutMapping("/{id}")
    public UsuarioResponse actualizar(@PathVariable Long id, @RequestBody ActualizarUsuarioRequest datos) {
        return usuarioService.actualizar(id, datos);
    }

    @PutMapping("/{id}/contrasena")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cambiarContrasena(@PathVariable Long id, @RequestBody CambiarContrasenaRequest datos) {
        usuarioService.cambiarContrasena(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UsuarioResponse subirAvatar(@PathVariable Long id, @RequestPart("archivo") MultipartFile archivo)
            throws IOException {
        return usuarioService.subirAvatar(id, archivo);
    }

    @GetMapping(value = "/{id}/avatar", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> obtenerAvatar(@PathVariable Long id) throws IOException {
        Resource avatar = usuarioService.obtenerAvatar(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .contentType(MediaType.IMAGE_JPEG)
                .body(avatar);
    }
}
