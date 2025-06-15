package com.Biopark.GlobalTransportes.service;

import com.Biopark.GlobalTransportes.model.Usuario;
import com.Biopark.GlobalTransportes.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.Biopark.GlobalTransportes.model.PasswordResetToken;
import com.Biopark.GlobalTransportes.model.Usuario;
import com.Biopark.GlobalTransportes.repository.PasswordResetTokenRepository;
import com.Biopark.GlobalTransportes.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import java.util.Optional;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    public boolean emailJaCadastrado(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    public void cadastrarUsuario(Usuario usuario) {
        // Criptografa a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }


    public boolean enviarLinkRedefinicaoSenha(String email) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            String token = UUID.randomUUID().toString();
            LocalDateTime expiration = LocalDateTime.now().plusHours(1);

            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUsuario(usuario);
            resetToken.setExpirationDate(expiration);
            tokenRepository.save(resetToken);

            String link = "http://localhost:8080/redefinir_senha?token=" + token;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Redefinição de Senha - Global Transportes");
            message.setText("Clique no link para redefinir sua senha: " + link);

            mailSender.send(message);

            return true;
        }
        return false;
    }

    public boolean validarToken(String token) {
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(token);
        if (optionalToken.isPresent()) {
            PasswordResetToken resetToken = optionalToken.get();
            return resetToken.getExpirationDate().isAfter(LocalDateTime.now());
        }
        return false;
    }

    public boolean redefinirSenha(String token, String novaSenha) {
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(token);
        if (optionalToken.isPresent()) {
            PasswordResetToken resetToken = optionalToken.get();
            if (resetToken.getExpirationDate().isAfter(LocalDateTime.now())) {
                Usuario usuario = resetToken.getUsuario();
                usuario.setSenha(passwordEncoder.encode(novaSenha));
                usuarioRepository.save(usuario);

                tokenRepository.delete(resetToken);

                return true;
            }
        }
        return false;
    }

}
