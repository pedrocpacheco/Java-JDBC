package br.com.alura.bytebank.domain.conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.mysql.cj.xdevapi.PreparableStatement;

import br.com.alura.bytebank.ConnectionFactory;
import br.com.alura.bytebank.domain.RegraDeNegocioException;
import br.com.alura.bytebank.domain.cliente.Cliente;

public class ContaService {
   
    //2.5 - Criar ConnectionFactory na classe
    private ConnectionFactory connection;
    private Set<Conta> contas = new HashSet<>();
    

    public ContaService(){
         this.connection = new ConnectionFactory();
    }

    public Set<Conta> listarContasAbertas() {
        return contas;
    }

    public BigDecimal consultarSaldo(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        return conta.getSaldo();
    }

    public void abrir(DadosAberturaConta dadosDaConta) throws SQLException {
        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), cliente);
        if (contas.contains(conta)) {
            throw new RegraDeNegocioException("Já existe outra conta aberta com o mesmo número!");
        }

        // 2.6 - Criando o texto que será lançado para adicionar ao DB
        String sql = "INSERT INTO conta(numero, saldo, cliente_nome, cliente_email)" +
                "VALUES (?, ?, ?, ?, ?)";
        
        Connection connect = connection.returnConnection(); // 2.7 - Pegando nossa Conexão
        PreparedStatement prepareStatement = connect.prepareStatement(sql); // 2.8 - Passando a nossa String com os placeholder para o PrepareStatemant

        prepareStatement.setInt(1, conta.getNumero());
        prepareStatement.setBigDecimal(2, BigDecimal.ZERO);
        prepareStatement.setString(3, dadosDaConta.dadosCliente().nome());
        prepareStatement.setString(4, dadosDaConta.dadosCliente().cpf());
        
    }

    public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
        }

        if (valor.compareTo(conta.getSaldo()) > 0) {
            throw new RegraDeNegocioException("Saldo insuficiente!");
        }

        conta.sacar(valor);
    }

    public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
        }

        conta.depositar(valor);
    }

    public void encerrar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()) {
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
        }

        contas.remove(conta);
    }

    private Conta buscarContaPorNumero(Integer numero) {
        return contas
                .stream()
                .filter(c -> c.getNumero() == numero)
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Não existe conta cadastrada com esse número!"));
    }
}
