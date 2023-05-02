package br.com.alura.bytebank;

import java.sql.DriverManager;
import java.sql.SQLException;

// 2.0- Criar Classe que faz a conexão com o BD
public class ConectionDB {
    
    public void connectToDB(){
        //2.1- Usar Classe DrivenManager para pegar a conexão
        try {
            DriverManager
                .getConnection("jdbc:mysql://localhost:3306/byte_bank?user=root&password=root");
             //2.2 - dentro do () passar ("tipoConexão:tipoDoBanco:localHost:porta:nomeBanco?user=usuario%password=senha"
            } catch (SQLException e) {
            System.out.println(e);
        }
       
    }

    public static void main(String[] args) {
        ConectionDB conectionDB = new ConectionDB();
        conectionDB.connectToDB();
    }

}



