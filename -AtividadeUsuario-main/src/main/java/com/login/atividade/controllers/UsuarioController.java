package com.login.atividade.controllers;

import com.login.atividade.entities.UsuarioEntity;
import com.login.atividade.exceptions.UsuarioException;
import com.login.atividade.services.UsuarioService;
import com.login.atividade.sessoes.SessaoDto;
import com.login.atividade.sessoes.SessaoUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String realizarLogin(@RequestParam String email,
                                @RequestParam String senha,
                                HttpSession session,
                                Model model) {
        try {
            UsuarioEntity usuario = usuarioService.fazerLogin(email, senha);

            SessaoDto sessao = new SessaoDto();
            sessao.setUsuarioId(usuario.getId());
            sessao.setUsuarioNome(usuario.getNome());
            SessaoUtil.RegistrarSessao(session, sessao);

            return "redirect:/usuarios";
        } catch (UsuarioException e) {
            model.addAttribute("erro", e.getMessage());
            return "login";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", e.getMessage() != null ? e.getMessage() : "Erro inesperado ao realizar login.");
            return "login";
        }
    }

    @GetMapping("/usuarios")
    public String inicio(HttpSession session, Model model) {
        SessaoDto sessao = SessaoUtil.ObterSessao(session);
        if (sessao == null) {
            return "redirect:/login";
        }

        model.addAttribute("nomeUsuario", sessao.getUsuarioNome());
        return "usuarios";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        SessaoUtil.RemoverSessao(session);
        return "redirect:/login";
    }

    @GetMapping("/cadastro-usuario")
    public String exibirFormularioCadastro(Model model) {
        // Envia o DTO para o formulário
        model.addAttribute("usuario", new com.login.atividade.dtos.UsuarioDto());
        return "cadastro-usuario";
    }

    @PostMapping("/salvar-usuario")
    public String salvarUsuario(@ModelAttribute("usuario") com.login.atividade.dtos.UsuarioDto dto,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        try {
            // Chama o método correto do UsuarioService passando o DTO
            usuarioService.cadastrarUsuario(dto);
            redirectAttributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso! Faça seu login.");
            return "redirect:/login";
        } catch (UsuarioException e) {
            // Captura as exceções de validação (ex: menor de idade, senha fraca, e-mail duplicado)
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", dto);
            return "cadastro-usuario";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", e.getMessage() != null ? e.getMessage() : "Erro ao cadastrar usuário.");
            model.addAttribute("usuario", dto);
            return "cadastro-usuario";
        }
    }
}
