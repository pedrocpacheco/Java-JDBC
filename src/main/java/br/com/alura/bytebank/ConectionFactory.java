package br.com.alura.bytebank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// 2.0- Criar Classe que faz a conexão com o BD
public class ConectionFactory {
    
    public Connection connectToDB(){
        //2.1- Usar Classe DrivenManager para pegar a conexão
        try {
            //2.2 - Criar Objeto Connection e salvar um getConnection do DrivenManager
            return DriverManager //2.4- Podemos retornar direto a conexão
                .getConnection("jdbc:mysql://localhost:3306/bytebank?user=root&password=YES");
             //2.3 - dentro do () passar ("tipoConexão:tipoDoBanco:localHost:porta:nomeBanco?user=usuario%password=senha"    
            
            } catch (SQLException e) {
                throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ConectionFactory conectionDB = new ConectionFactory();
        conectionDB.connectToDB();
    }

}



