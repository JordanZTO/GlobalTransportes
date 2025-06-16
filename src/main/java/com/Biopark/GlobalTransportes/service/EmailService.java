package com.Biopark.GlobalTransportes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmail(String assunto, String mensagem, String remetenteNome, String remetenteEmail) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo("globaltransportes3p@gmail.com");
        email.setSubject("[Contato] " + assunto);
        email.setText("Remetente: " + remetenteNome + "\nEmail: " + remetenteEmail + "\n\nDetalhes:\n" + mensagem);
        mailSender.send(email);
    }
}
