package org.acme.Resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.Model.DTO.Produtos;
import org.acme.Model.ModelProduto;
import org.acme.Service.ServiceProdutos;

import java.sql.SQLException;
import java.util.List;

@Path("/produto")
public class ResourceProduto {
    @Inject
    ServiceProdutos serviceProdutos;

    @Path("/inserir")
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response inserirProduto(Produtos produtos){
        try{
            serviceProdutos.inserirProduto(produtos);
            return Response.status(Response.Status.CREATED).entity("Criado com sucesso").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro com o banco de dados" + e).build();
        } catch (IllegalArgumentException e){
            return Response.status(422).entity(e).build();
        }
    }

    @Path("/read")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response readProdutos(){
        try{
            List<ModelProduto> list = serviceProdutos.readProdutos();
            return Response.status(Response.Status.FOUND).entity(list).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro com o banco de dados" + e).build();
        }
    }

    @Path("/deletar/{id}")
    @DELETE
    public Response deletar(@PathParam("id") int id){
        try{
            serviceProdutos.deletarProduto(id);
            return Response.status(Response.Status.OK).entity("Item deletado!").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro com o banco de dados" + e).build();
        }
    }

    @Path("/atualizar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizarProduto(ModelProduto produto){
        try{
            serviceProdutos.atualizarProduto(produto);
            return Response.status(Response.Status.OK).entity("Atualizado!").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro com o banco de dados" + e).build();
        }
    }


}
