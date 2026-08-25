package com.login.atividade.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

@Entity
@Table(name = "usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id;

    @Email(message = "Informe um e-mail válido")
    @NotBlank(message = "O e-mail é obrigatório")
    @Column(name = "usuario_email", nullable = false)
    private String email;

    @NotBlank(message = "O nome é obrigatório")
    @Column(name = "usuario_nome", nullable = false)
    private String nome;

    @NotBlank(message = "A matrícula é obrigatória")
    @Column(name = "usuario_matricula", nullable = false)
    private String matricula;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Column(name = "usuario_dataNascimento", nullable = false)
    private LocalDate dataNascimento;

    @NotBlank(message = "A senha é obrigatória")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&.]).{5,}$",
            message = "A senha deve conter no mínimo 5 caracteres, incluindo letras, números e caracteres especiais."
    )
    @Column(name = "usuario_senha", nullable = false)
    private String senha;

    public UsuarioEntity() {
    }

    public UsuarioEntity(Long id, String email, String nome, String matricula,
                         LocalDate dataNascimento, String senha) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.matricula = matricula;
        this.dataNascimento = dataNascimento;
        this.senha = senha;
    }

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}