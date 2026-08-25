package com.login.atividade.controllers;

import com.login.atividade.dtos.UsuarioDto;
import com.login.atividade.entities.UsuarioEntity;
import com.login.atividade.services.UsuarioService;
import com.login.atividade.sessoes.SessaoDto;
import com.login.atividade.sessoes.SessaoUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String exibirLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String processarLogin(@RequestParam("email") String email,
                                 @RequestParam("senha") String senha,
                                 HttpSession session,
                                 Model model) {
        try {
            UsuarioEntity usuarioLogado = usuarioService.fazerLogin(email, senha);

            SessaoDto sessaoDto = new SessaoDto();
            sessaoDto.setUsuarioId(usuarioLogado.getId());
            sessaoDto.setUsuarioNome(usuarioLogado.getNome());

            SessaoUtil.RegistrarSessao(session, sessaoDto);

            return "redirect:/usuarios";

        } catch (Exception e) {
            model.addAttribute("erro", "Usuário ou senha inválidos.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String deslogar(HttpSession session) {
        SessaoUtil.RemoverSessao(session);
        return "redirect:/login";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model, HttpSession session) {
        SessaoDto usuarioLogado = SessaoUtil.ObterSessao(session);

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        // Passa o nome do usuário logado e a lista para a view
        model.addAttribute("nomeUsuario", usuarioLogado.getUsuarioNome());
        model.addAttribute("usuarios", usuarioService.listarUsuarios());

        return "usuarios";
    }

    @GetMapping("/cadastro-usuario")
    public String exibirCadastro(Model model) {
        model.addAttribute("usuario", new UsuarioDto());
        return "cadastro-usuario";
    }

    @PostMapping("/usuarios/salvar")
    public String salvar(
            @Valid @ModelAttribute("usuario") UsuarioDto usuario,
            BindingResult result,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "cadastro-usuario";
        }

        try {
            usuarioService.cadastrarUsuario(usuario);

            SessaoDto usuarioLogado = SessaoUtil.ObterSessao(session);

            if (usuarioLogado == null) {
                redirectAttributes.addFlashAttribute(
                        "mensagem",
                        "Conta criada com sucesso! Insira suas credenciais."
                );
                return "redirect:/login";
            }

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Usuário salvo com sucesso!"
            );

            return "redirect:/usuarios";

        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            return "cadastro-usuario";
        }
    }

    @GetMapping("/usuario/editar/{id}")
    public String editar(@PathVariable Long id,
                         Model model,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {

        SessaoDto usuarioLogado = SessaoUtil.ObterSessao(session);

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (!usuarioLogado.getUsuarioId().equals(id)) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Você só pode editar o seu próprio perfil!"
            );
            return "redirect:/usuarios";
        }

        UsuarioEntity usuario = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", usuario);

        return "cadastro-usuario";
    }

    @PostMapping("/usuario/atualizar/{id}")
    public String atualizar(@PathVariable Long id,
                            @ModelAttribute UsuarioDto usuario,
                            Model model,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        SessaoDto usuarioLogado = SessaoUtil.ObterSessao(session);

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (!usuarioLogado.getUsuarioId().equals(id)) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Ação não autorizada!"
            );
            return "redirect:/usuarios";
        }

        try {
            usuarioService.atualizarUsuario(id, usuario);

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Usuário atualizado com sucesso!"
            );

            return "redirect:/usuarios";

        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            return "cadastro-usuario";
        }
    }

    @GetMapping("/usuario/excluir/{id}")
    public String excluir(@PathVariable Long id,
                          RedirectAttributes redirectAttributes,
                          HttpSession session) {

        SessaoDto usuarioLogado = SessaoUtil.ObterSessao(session);

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (!usuarioLogado.getUsuarioId().equals(id)) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Você não tem permissão para excluir outro usuário!"
            );
            return "redirect:/usuarios";
        }

        try {
            usuarioService.excluirUsuario(id);
            SessaoUtil.RemoverSessao(session);

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Sua conta foi excluída com sucesso."
            );

            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Não foi possível excluir sua conta."
            );
            return "redirect:/usuarios";
        }
    }
}