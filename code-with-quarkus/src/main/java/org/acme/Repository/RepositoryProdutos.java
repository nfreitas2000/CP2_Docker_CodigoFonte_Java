package org.acme.Repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.acme.Model.DTO.Produtos;
import org.acme.Model.ModelProduto;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RepositoryProdutos {

    @Inject
    DataSource dataSource;

    public void inserir(Produtos produtos) throws SQLException {
        String sql = "insert into T_LUTAN_PRODUTOS(nome, preco, quantidade) values (?, ?, ?)";
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1, produtos.getNome());
            ps.setDouble(2, produtos.getPreco());
            ps.setInt(3, produtos.getQuantidade());
            ps.executeUpdate();
        }
    }

    public List<ModelProduto> readProdutos() throws SQLException {
        String sql = "SELECT * FROM T_LUTAN_PRODUTOS";
        ArrayList<ModelProduto> produtos = new ArrayList<ModelProduto>();
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql))
        {

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                produtos.add(new ModelProduto(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getInt(4)));
            }
        }
        return produtos;
    }

    public void atualizarProdutos(ModelProduto produto) throws SQLException {
        String sql = "UPDATE T_LUTAN_PRODUTOS SET nome = ?, preco = ?, quantidade = ? WHERE id = ?";
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getPreco());
            ps.setInt(3, produto.getQuantidade());
            ps.setInt(4, produto.getId());

            int linhas = ps.executeUpdate();
            if (linhas == 0) {
                throw new NotFoundException("Produto não encontrado");
            }
        }
    }

    public void deletarDesafio(int id) throws SQLException{
        String sql = "Delete from T_LUTAN_PRODUTOS where id = ?";

        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement st = con.prepareStatement(sql)) {
                st.setInt(1, id);
                int funcionou = st.executeUpdate();
                if (funcionou > 0) {
                    con.commit();
                } else {
                    con.rollback();
                    throw new NotFoundException ("ID não encontrado");
                }
            }
        }
    }

}
