package br.com.alura.bytebank.domain.conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.alura.bytebank.domain.cliente.Cliente;

public class ContaDAO {

    private Connection connection;

    ContaDAO(Connection connection){
        this.connection = connection;
    }

    public void save(DadosAberturaConta dadosDaConta){
        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), cliente);

        // 2.6 - Criando o texto que será lançado para adicionar ao DB
        String sql = "INSERT INTO conta(numero, saldo, cliente_nome, cliente_email)" +
                "VALUES (?, ?, ?, ?, ?)";
        

        try{
            PreparedStatement prepareStatement = connection.prepareStatement(sql); // 2.8 - Passando a nossa String com os placeholder para o PrepareStatemant

            prepareStatement.setInt(1, conta.getNumero());
            prepareStatement.setBigDecimal(2, BigDecimal.ZERO);
            prepareStatement.setString(3, dadosDaConta.dadosCliente().nome());
            prepareStatement.setString(4, dadosDaConta.dadosCliente().cpf());
            prepareStatement.setString(5, dadosDaConta.dadosCliente().email());

            prepareStatement.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }


}
