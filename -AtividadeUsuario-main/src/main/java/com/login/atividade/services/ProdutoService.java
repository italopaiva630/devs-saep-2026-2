package com.login.atividade.services;




import com.login.atividade.dtos.ProdutoDto;
import com.login.atividade.entities.ProdutoEntity;
import com.login.atividade.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Optional<ProdutoEntity> buscarPorIdOptional(Long id) {
        return produtoRepository.findById(id);
    }

    public ProdutoEntity buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public List<ProdutoEntity> listarProdutos() {
        return produtoRepository.findAll();
    }

    @Transactional
    public ProdutoDto cadastrarProduto(ProdutoDto produtoDto) {
        if (produtoDto.getNome() == null || produtoDto.getNome().isBlank()) {
            throw new RuntimeException("O nome do produto é obrigatório.");
        }
        if (produtoDto.getPreco() == null || produtoDto.getPreco() <= 0) {
            throw new RuntimeException("O preço deve ser maior que zero.");
        }
        if (produtoDto.getQuantidadeEstoque() == null || produtoDto.getQuantidadeEstoque() < 0) {
            throw new RuntimeException("A quantidade em estoque não pode ser negativa.");
        }

        ProdutoEntity produto = new ProdutoEntity();

        // Se houver ID, é uma atualização
        if (produtoDto.getId() != null) {
            produto = buscarPorId(produtoDto.getId());
        }

        produto.setNome(produtoDto.getNome());
        produto.setDescricao(produtoDto.getDescricao());
        produto.setPreco(produtoDto.getPreco());
        produto.setQuantidadeEstoque(produtoDto.getQuantidadeEstoque());

        produto = produtoRepository.save(produto);
        produtoDto.setId(produto.getId());

        return produtoDto;
    }

    @Transactional
    public void deletarProduto(Long id) {
        ProdutoEntity produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }
}
