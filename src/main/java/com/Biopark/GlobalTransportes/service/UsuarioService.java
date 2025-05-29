package com.Biopark.GlobalTransportes.service;

import com.Biopark.GlobalTransportes.model.Usuario;
import com.Biopark.GlobalTransportes.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean emailJaCadastrado(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    public void cadastrarUsuario(Usuario usuario) {
        // Criptografa a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        usuarioRepository.save(usuario);
    }


}
