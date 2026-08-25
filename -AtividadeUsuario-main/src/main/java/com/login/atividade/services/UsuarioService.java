package com.login.atividade.services;

import com.login.atividade.dtos.UsuarioDto;
import com.login.atividade.entities.UsuarioEntity;
import com.login.atividade.exceptions.UsuarioException;
import com.login.atividade.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<UsuarioEntity> listarUsuarios() {
        return repository.findAll();
    }

    public UsuarioEntity buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new UsuarioException("Usuário não encontrado com o ID: " + id));
    }

    public UsuarioEntity cadastrarUsuario(UsuarioDto dto) {

        validarIdade(dto.getDataNascimento());

        Optional<UsuarioEntity> existente =
                repository.findByEmail(dto.getEmail());

        if (existente.isPresent()) {
            throw new UsuarioException(
                    "Já existe um usuário registrado com este e-mail!"
            );
        }

        validarFormatoSenha(dto.getSenha());

        UsuarioEntity usuario = new UsuarioEntity();

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setMatricula(dto.getMatricula());
        usuario.setDataNascimento(dto.getDataNascimento());

        return repository.save(usuario);
    }

    public UsuarioEntity atualizarUsuario(Long id, UsuarioDto dto) {

        validarIdade(dto.getDataNascimento());

        UsuarioEntity usuarioExistente = buscarPorId(id);

        if (!usuarioExistente.getEmail().equals(dto.getEmail())) {

            Optional<UsuarioEntity> emailEmUso =
                    repository.findByEmail(dto.getEmail());

            if (emailEmUso.isPresent()) {
                throw new UsuarioException(
                        "Este e-mail já está sendo utilizado por outro usuário!"
                );
            }
        }

        validarFormatoSenha(dto.getSenha());

        usuarioExistente.setNome(dto.getNome());
        usuarioExistente.setEmail(dto.getEmail());
        usuarioExistente.setSenha(dto.getSenha());
        usuarioExistente.setMatricula(dto.getMatricula());
        usuarioExistente.setDataNascimento(dto.getDataNascimento());

        return repository.save(usuarioExistente);
    }

    public void excluirUsuario(Long id) {

        UsuarioEntity usuario = buscarPorId(id);

        repository.delete(usuario);
    }

    public UsuarioEntity fazerLogin(String email, String senha) {

        UsuarioEntity usuario = repository.findByEmail(email)
                .orElseThrow(() ->
                        new UsuarioException(
                                "Usuario ou Senha Inválidos!"
                        ));

        if (!usuario.getSenha().equals(senha)) {
            throw new UsuarioException(
                    "Usuario ou Senha Inválidos!"
            );
        }

        return usuario;
    }

    private void validarFormatoSenha(String senha) {

        if (senha == null || senha.trim().isEmpty()) {
            throw new UsuarioException(
                    "A senha é obrigatória."
            );
        }

        String regexSenha =
                "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&.]).{5,}$";

        if (!senha.matches(regexSenha)) {
            throw new UsuarioException(
                    "A senha deve conter no mínimo 5 caracteres, incluindo letras, números e pelo menos um caractere especial (ex: @, $, !, %, *, #, ?, &, .)."
            );
        }
    }

    private void validarIdade(LocalDate dataNascimento) {

        if (dataNascimento == null) {
            throw new UsuarioException(
                    "A data de nascimento é obrigatória."
            );
        }

        int idade =
                Period.between(dataNascimento, LocalDate.now()).getYears();

        if (idade < 18) {
            throw new UsuarioException(
                    "Operação negada: O colaborador deve ter pelo menos 18 anos."
            );
        }

        if (idade > 500) {
            throw new UsuarioException(
                    "Operação negada: Data de nascimento inválida (limite de 500 anos excedido)."
            );
        }
    }
}