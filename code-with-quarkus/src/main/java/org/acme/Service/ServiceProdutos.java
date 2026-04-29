package org.acme.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.Model.DTO.Produtos;
import org.acme.Model.ModelProduto;
import org.acme.Repository.RepositoryProdutos;

import java.sql.SQLException;
import java.util.List;

@ApplicationScoped
public class ServiceProdutos {
    @Inject
    RepositoryProdutos repositoryProdutos;

    public boolean verificarProduto(Produtos produtos){
        if (produtos.getNome().isEmpty() || produtos.getNome() == null){
            throw new IllegalArgumentException ("Nome vazio");
        }
        if (produtos.getPreco() == 0){
            throw new IllegalArgumentException ("Valor vazio");
        }
        if (produtos.getQuantidade() == 0){
            throw new IllegalArgumentException ("Quantidade vazio");
        }
        return true;
    }

    public boolean verificarModelProduto(ModelProduto produtos){
        if (produtos.getId() <= 0){
            throw new IllegalArgumentException ("Id inválido");
        }
        if (produtos.getNome().isEmpty() || produtos.getNome() == null){
            throw new IllegalArgumentException ("Nome vazio");
        }
        if (produtos.getPreco() == 0){
            throw new IllegalArgumentException ("Valor vazio");
        }
        if (produtos.getQuantidade() == 0){
            throw new IllegalArgumentException ("Quantidade vazio");
        }
        return true;
    }

    public void inserirProduto(Produtos produto) throws SQLException {
        if (verificarProduto(produto)){
            repositoryProdutos.inserir(produto);
        }
    }

    public List<ModelProduto> readProdutos() throws SQLException {
        return repositoryProdutos.readProdutos();
    }

    public void atualizarProduto(ModelProduto produtos) throws SQLException {
        if (verificarModelProduto(produtos)){
            repositoryProdutos.atualizarProdutos(produtos);
        }
    }

    public void deletarProduto(int id) throws SQLException {
        repositoryProdutos.deletarDesafio(id);
    }
}
