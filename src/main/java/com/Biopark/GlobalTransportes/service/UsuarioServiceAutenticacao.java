package com.Biopark.GlobalTransportes.service;

import com.Biopark.GlobalTransportes.model.Usuario;
import com.Biopark.GlobalTransportes.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceAutenticacao implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Cria a autoridade com o prefixo "ROLE_" (necessário para compatibilidade com Spring Security)
        String role = "ROLE_" + usuario.getTipo().getNome();
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(new SimpleGrantedAuthority(role))
                .build();
    }
}
