package com.login.atividade.dtos;

import jakarta.validation.constraints.Pattern; // IMPORT ADICIONADO AQUI
import java.time.LocalDate;

public class UsuarioDto {

    private Long id;
    private String email;

    // ALTERAÇÃO AQUI: Adicionada a validação idêntica à da Entity
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&.]).{5,}$",
            message = "A senha deve conter no mínimo 5 caracteres, incluindo letras, números e caracteres especiais (ex: @, $, !, %, *, #, ?, &, .)."
    )
    private String senha;

    private String nome;
    private String matricula;
    private LocalDate dataNascimento;

    // Construtor vazio
    public UsuarioDto() {
    }

    // Construtor completo
    public UsuarioDto(Long id, String email, String senha,
                      String nome, String matricula, LocalDate dataNascimento) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.matricula = matricula;
        this.dataNascimento = dataNascimento;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
