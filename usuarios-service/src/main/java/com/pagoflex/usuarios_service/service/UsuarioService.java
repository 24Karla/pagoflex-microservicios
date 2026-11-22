package com.pagoflex.usuarios_service.service;

import com.pagoflex.usuarios_service.client.RolCLiente;
import com.pagoflex.usuarios_service.dto.ActualizarUsuarioRequest;
import com.pagoflex.usuarios_service.dto.CambiarContrasenaRequest;
import com.pagoflex.usuarios_service.dto.LoginRequest;
import com.pagoflex.usuarios_service.dto.RegistroRequest;
import com.pagoflex.usuarios_service.dto.RolDto;
import com.pagoflex.usuarios_service.dto.UsuarioResponse;
import com.pagoflex.usuarios_service.model.Usuario;
import com.pagoflex.usuarios_service.repository.UsuarioRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Long ROL_USUARIO = 3L;

    private final UsuarioRepository usuarioRepository;
    private final RolCLiente rolCliente;
    private final PasswordEncoder passwordEncoder;
    private final AlmacenamientoService almacenamientoService;

    public List<UsuarioResponse> listar(String buscar) {
        List<Usuario> usuarios = vacio(buscar)
                ? usuarioRepository.findAll()
                : usuarioRepository.findByNombreContainingIgnoreCaseOrCuentaContainingIgnoreCaseOrRutContainingIgnoreCase(
                        buscar.trim(), buscar.trim(), buscar.trim());
        return usuarios.stream().map(this::aRespuesta).toList();
    }

    public UsuarioResponse obtener(Long id) {
        return aRespuesta(buscar(id));
    }

    public UsuarioResponse registrar(RegistroRequest datos) {
        if (datos == null || vacio(datos.nombre()) || vacio(datos.rut()) || vacio(datos.cuenta())
                || vacio(datos.ciudad()) || vacio(datos.contrasena())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nombre, RUT, cuenta, ciudad y contraseña son obligatorios");
        }
        if (datos.contrasena().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres");
        }
        String cuenta = datos.cuenta().trim().toLowerCase();
        String rut = datos.rut().trim();
        if (usuarioRepository.existsByCuentaIgnoreCase(cuenta)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esa cuenta ya está registrada");
        }
        if (usuarioRepository.existsByRut(rut)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese RUT ya está registrado");
        }

        Long rolId = datos.rolId() == null ? ROL_USUARIO : datos.rolId();
        validarRol(rolId);
        Usuario nuevo = Usuario.builder()
                .nombre(datos.nombre().trim())
                .rut(rut)
                .cuenta(cuenta)
                .ciudad(datos.ciudad().trim())
                .contrasena(passwordEncoder.encode(datos.contrasena()))
                .rolId(rolId)
                .build();
        return aRespuesta(usuarioRepository.save(nuevo));
    }

    public UsuarioResponse iniciarSesion(LoginRequest datos) {
        if (datos == null || vacio(datos.cuenta()) || vacio(datos.contrasena())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cuenta y contraseña son obligatorias");
        }
        Usuario usuario = usuarioRepository.findByCuentaIgnoreCase(datos.cuenta().trim())
                .filter(u -> passwordEncoder.matches(datos.contrasena(), u.getContrasena()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Cuenta o contraseña incorrectas"));
        if (datos.rolId() != null && !datos.rolId().equals(usuario.getRolId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La cuenta no pertenece al rol seleccionado");
        }
        return aRespuesta(usuario);
    }

    public UsuarioResponse actualizar(Long id, ActualizarUsuarioRequest datos) {
        if (datos == null || vacio(datos.nombre()) || vacio(datos.cuenta()) || vacio(datos.ciudad())
                || vacio(datos.rut()) || datos.rolId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nombre, cuenta, ciudad, RUT y rol son obligatorios");
        }

        Usuario usuario = buscar(id);
        String cuenta = datos.cuenta().trim().toLowerCase();
        String rut = datos.rut().trim();
        if (usuarioRepository.existsByCuentaIgnoreCaseAndIdNot(cuenta, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esa cuenta ya está registrada");
        }
        if (usuarioRepository.existsByRutAndIdNot(rut, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese RUT ya está registrado");
        }
        validarRol(datos.rolId());

        usuario.setNombre(datos.nombre().trim());
        usuario.setCuenta(cuenta);
        usuario.setCiudad(datos.ciudad().trim());
        usuario.setRut(rut);
        usuario.setRolId(datos.rolId());
        return aRespuesta(usuarioRepository.save(usuario));
    }

    public void cambiarContrasena(Long id, CambiarContrasenaRequest datos) {
        if (datos == null || vacio(datos.actual()) || vacio(datos.nueva())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña actual y la nueva son obligatorias");
        }
        if (datos.nueva().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La nueva contraseña debe tener al menos 8 caracteres");
        }
        Usuario usuario = buscar(id);
        if (!passwordEncoder.matches(datos.actual(), usuario.getContrasena())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La contraseña actual es incorrecta");
        }
        usuario.setContrasena(passwordEncoder.encode(datos.nueva()));
        usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) throws IOException {
        Usuario usuario = buscar(id);
        usuarioRepository.delete(usuario);
        almacenamientoService.eliminarAvatar(id);
    }

    public UsuarioResponse subirAvatar(Long id, MultipartFile archivo) throws IOException {
        Usuario usuario = buscar(id);
        almacenamientoService.guardarAvatar(id, archivo);
        usuario.setAvatarUrl("/api/usuarios/" + id + "/avatar");
        return aRespuesta(usuarioRepository.save(usuario));
    }

    public Resource obtenerAvatar(Long id) {
        buscar(id);
        return almacenamientoService.cargarAvatar(id);
    }

    private Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    private void validarRol(Long rolId) {
        RolDto rol = rolCliente.obtener(rolId);
        if (rol == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol de usuario no existe");
        }
    }

    private UsuarioResponse aRespuesta(Usuario usuario) {
        RolDto rol = rolCliente.obtener(usuario.getRolId());
        return new UsuarioResponse(usuario.getId(), usuario.getNombre(), usuario.getRut(), usuario.getCuenta(),
                usuario.getCiudad(), usuario.getRolId(), rol == null ? "" : rol.nombre(), usuario.getAvatarUrl());
    }

    private boolean vacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
