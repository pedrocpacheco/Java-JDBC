package br.com.alura.bytebank;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

// 2.0- Criar Classe que faz a conexão com o BD
public class ConnectionFactory{
    
    public Connection returnConnection(){
        //2.1- Usar Classe DrivenManager para pegar a conexão
        try {
            // CONEXÃO ANTIGA: AQUI A GENTE FAZIA VARIAS CONEXÕES, USAMOS UM POOL AGORA
            //2.2 - Criar Objeto Connection e salvar um getConnection do DrivenManager
            // return DriverManager //2.4- Podemos retornar direto a conexão
            //     .getConnection("jdbc:mysql://localhost:3306/bytebank?user=root&password=YES");
             //2.3 - dentro do () passar ("tipoConexão:tipoDoBanco:localHost:porta:nomeBanco?user=usuario%password=senha"    
            
            // AFTER- So retornamos agora a conexão feita no createDataSource() methods
            return createDataSource().getConnection();

            } catch (SQLException e) {
                throw new RuntimeException(e);
        }
    }

    private HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        // É aqui que definimos tudo agora da conexão
        config.setJdbcUrl("jdbc:mysql://localhost:3306/byte_bank");
        config.setUsername("root");
        config.setPassword("root");
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }

}



