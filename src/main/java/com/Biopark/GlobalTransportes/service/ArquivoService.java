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

    private final String uploadDir = "uploads/imagens"; // caminho relativo ao projeto

    public String salvarImagem(MultipartFile imagem) {
        if (imagem == null || imagem.isEmpty()) {
            System.out.println("Arquivo de imagem está vazio ou nulo");
            return null;
        }

        try {
            Path pasta = Paths.get(uploadDir);
            Files.createDirectories(pasta);
            
            System.out.println("Diretório de upload criado/verificado: " + pasta.toAbsolutePath());

            String nomeOriginal = imagem.getOriginalFilename();
            String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            String nomeArquivo = UUID.randomUUID().toString() + extensao;

            Path caminhoArquivo = pasta.resolve(nomeArquivo);
            imagem.transferTo(caminhoArquivo.toFile());
            
            System.out.println("Imagem salva com sucesso: " + caminhoArquivo.toAbsolutePath());

            return nomeArquivo;

        } catch (IOException e) {
            System.err.println("Erro ao salvar imagem: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage(), e);
        }
    }

    public void excluirImagem(String nomeArquivo) {
        if (nomeArquivo == null || nomeArquivo.isBlank()) return;

        try {
            Path caminhoArquivo = Paths.get(uploadDir).resolve(nomeArquivo);
            Files.deleteIfExists(caminhoArquivo);
            System.out.println("Imagem excluída: " + caminhoArquivo.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erro ao excluir imagem: " + nomeArquivo + " - " + e.getMessage());
            throw new RuntimeException("Erro ao excluir imagem: " + nomeArquivo, e);
        }
    }


}
