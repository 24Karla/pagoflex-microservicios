package com.pagoflex.usuarios_service.service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private static final long ROL_USUARIO = 3L;

    private final UsuarioRepository usuarioRepository;
    private final RolCliente rolCliente;
    private final AlmacenamientoService almacenamientoService;

}
