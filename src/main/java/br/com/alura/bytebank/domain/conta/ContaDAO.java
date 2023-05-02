package br.com.alura.bytebank.domain.conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

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
            prepareStatement.close();
            this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public Set<Conta> list(){
        // 1- Criamos uma Lista, nela, os dados que estão no DB serão salvos.
        Set<Conta> contas = new HashSet<>();

        // 2- Definimos o Texto SQL que será utilizado para pegar todas as contas
        String sqlAsString = "SELECT * FROM conta";

        // After- Definindo as variaveis antes para poder fecha-las
        PreparedStatement sqlPrepared;
        ResultSet listGotted;

        // 3- Bloco Try-Catch obrigatorio
        try{
            // 4- Pegamos o SQL, preparamos em uma Statement, excutamos a Consulta que retorna um ResultList
            sqlPrepared = connection.prepareStatement(sqlAsString);
            listGotted = sqlPrepared.executeQuery();

            // 5- Enquanto a lista recuperada tiver itens, nos adicionamos uma Conta a lista de conta
            while(listGotted.next()){

                // 6- Definimos o numero da conta como o int quetem no indice 1 do DB
                int accountNumber = listGotted.getInt(1);

                // 7- Definimos os dados DadosCliente dando os seus valores como os valores pegados dentro da listGotted
                DadosCadastroCliente dadosCliente = new DadosCadastroCliente(listGotted.getString(3), listGotted.getString(4), listGotted.getString(5));
                
                // 8- Criamos uma conta, e então adicionamos ela na lista
                contas.add(new Conta(accountNumber, new Cliente(dadosCliente)));
         
        }
        sqlPrepared.close();
        listGotted.close();
        } catch(SQLException e){
            e.printStackTrace();
        }

        return contas;
    }

    public void update(Integer number, BigDecimal value){
        String sqlString = "UPDATE conta SET saldo = ? WHERE number = ?";

        PreparedStatement preparedStatement;

    try{
        preparedStatement = connection.prepareStatement(sqlString);

        preparedStatement.setBigDecimal(1, value);
        preparedStatement.setInt(2, number);

        preparedStatement.execute();

        preparedStatement.close();
        this.connection.close();
        
    } catch(SQLException e){
        e.printStackTrace();
    }

    }

}