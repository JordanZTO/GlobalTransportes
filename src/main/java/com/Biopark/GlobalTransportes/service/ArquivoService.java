package com.Biopark.GlobalTransportes.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ArquivoService {

    private final String uploadDir = "C:\\Users\\Cliente\\Desktop\\GlobalTransportes\\uploads\\imagens"; // caminho base

    public String salvarImagem(MultipartFile imagem) {
        if (imagem == null || imagem.isEmpty()) {
            return null;
        }

        try {
            Path pasta = Paths.get(uploadDir);
            Files.createDirectories(pasta);

            String nomeOriginal = imagem.getOriginalFilename();
            String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            String nomeArquivo = UUID.randomUUID().toString() + extensao;

            Path caminhoArquivo = pasta.resolve(nomeArquivo);
            imagem.transferTo(caminhoArquivo.toFile());

            return nomeArquivo;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem", e);
        }
    }

    public void excluirImagem(String nomeArquivo) {
        if (nomeArquivo == null || nomeArquivo.isBlank()) return;

        try {
            Path caminhoArquivo = Paths.get(uploadDir).resolve(nomeArquivo);
            Files.deleteIfExists(caminhoArquivo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao excluir imagem: " + nomeArquivo, e);
        }
    }


}
