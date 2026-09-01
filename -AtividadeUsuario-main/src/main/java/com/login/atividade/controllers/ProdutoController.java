package com.login.atividade.controllers;

import com.login.atividade.dtos.ProdutoDto;
import com.login.atividade.entities.ProdutoEntity;
import com.login.atividade.services.ProdutoService;
import com.login.atividade.sessoes.SessaoDto;
import com.login.atividade.sessoes.SessaoUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // Retorna listasprodutos.html
    @GetMapping
    public String listar(Model model, HttpSession session) {
        SessaoDto usuarioLogado = SessaoUtil.ObterSessao(session);
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        model.addAttribute("produtos", produtoService.listarProdutos());
        return "listasprodutos";
    }

    // Retorna cadastroproduto.html
    @GetMapping("/novo")
    public String novoProduto(Model model, HttpSession session) {
        SessaoDto usuarioLogado = SessaoUtil.ObterSessao(session);
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        model.addAttribute("produto", new ProdutoDto());
        return "cadastroproduto";
    }

    // Retorna cadastroproduto.html para edição
    @GetMapping("/editar/{id}")
    public String editarProduto(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        SessaoDto usuarioLogado = SessaoUtil.ObterSessao(session);
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            ProdutoEntity entity = produtoService.buscarPorId(id);
            ProdutoDto dto = new ProdutoDto(
                    entity.getId(),
                    entity.getNome(),
                    entity.getDescricao(),
                    entity.getPreco(),
                    entity.getQuantidadeEstoque()
            );

            model.addAttribute("produto", dto);
            return "cadastroproduto";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/produtos";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("produto") ProdutoDto produtoDto,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        SessaoDto usuarioLogado = SessaoUtil.ObterSessao(session);
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            produtoService.cadastrarProduto(produtoDto);
            redirectAttributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");
            return "redirect:/produtos";
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("produto", produtoDto);
            return "cadastroproduto";
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        SessaoDto usuarioLogado = SessaoUtil.ObterSessao(session);
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            produtoService.deletarProduto(id);
            redirectAttributes.addFlashAttribute("mensagem", "Produto excluído com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/produtos";
    }
}