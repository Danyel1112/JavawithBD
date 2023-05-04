package jdbc;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

public class BancoDados implements InterfaceBancoDados {

    private Connection connection = null;
    
    public void logger() throws IOException {
        Log startLogger = new Log("Log1.txt");
    	try {
    		startLogger.logger.setLevel(Level.FINE);
    		startLogger.logger.info("Iniciando a aplicação.");
    		
    	} catch (Exception e) {
    		startLogger.logger.info("Exception:" + e.getMessage()); //escrever no arquivo de log o erro
    		startLogger.logger.severe("O programa teve erro na inserção.");
            e.printStackTrace();//escrever no console o erro

    	}
    }
    
    public void infoLogger() throws IOException {
    	Log infoLooger = new Log("Log2.txt");
    	try {
    		infoLooger.logger.setLevel(Level.FINE);
    		infoLooger.logger.warning("Programa executando.");
    		infoLooger.logger.info("\n O programa iniciou a chamada dos dados do banco com sucesso!!");
    	} catch (Exception e) {
    		infoLooger.logger.info("Exception:" + e.getMessage()); //escrever no arquivo de log o erro
    		infoLooger.logger.severe("\n O programa teve erro na inserção.");
            e.printStackTrace();//escrever no console o erro
    	}
    }

    public void endLogger() throws IOException {
    	Log endLooger = new Log("Log3.txt");
    	try {
    		endLooger.logger.setLevel(Level.FINE);
    		endLooger.logger.info("\n O programa terminou as operações do banco de dados com sucesso");
    		
    	} catch (Exception e) {
    		endLooger.logger.info("Exception:" + e.getMessage());
    		endLooger.logger.severe("\n O programa teve erro na operação final.");
    		e.printStackTrace();
    	}
    }
    
    @Override
    public void conectar(String dbUrl, String dbUser, String dbPassword) {
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            System.out.println("Conexão estabelecida com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    @Override
    public void desconectar() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Desconectado com sucesso");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao desconectar: " + e.getMessage());
        }
    }

    @Override
    public void consultar(String dbQuery) {
        try {
            // Para ver se foi conectado com banco de dados.
            if (connection == null) {
                throw new SQLException("A conexão com o banco de dados não foi estabelecida.");
            }
            
            // Cria o objeto do Statement para executar a consulta
            Statement statement = connection.createStatement();
            
            // Executa a query e depois armazena na resultset
            ResultSet resultSet = statement.executeQuery(dbQuery);
            
            // Consulta as informações do banco de dados.
            ResultSetMetaData rsmt = resultSet.getMetaData();
            int columnCount = rsmt.getColumnCount();
            
            // Faz o laço e depois exibe o resultado da consulta e exibe as colunas
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String nomeColuna = rsmt.getColumnName(i);
                    Object valorColuna = resultSet.getObject(i);
                    System.out.print(nomeColuna + ": " + valorColuna + " | ");
                }
                System.out.println(); // Adicionado para quebrar a linha
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar execução: " + e.getMessage());
        }
    }

    @Override
    public int inserirAlterarExcluir(String dbQuery) {
        int rowsAffected = 0;
        try {
            if (connection == null) {
                throw new SQLException("A conexão com o banco de dados não foi estabelecida.");
            }
            Statement statement = connection.createStatement();
            rowsAffected = statement.executeUpdate(dbQuery);
        } catch (SQLException e) {
            System.out.println("Erro na execução: " + e.getMessage());
        }
        return rowsAffected;
    }

    public static void main(String[] args) throws IOException {
 
        BancoDados bd = new BancoDados();
        String dbUrl = "jdbc:mysql://localhost:3306/javateste";
        String dbUser = "root";
        String dbPassword = "";
        
        bd.logger();
        bd.conectar(dbUrl, dbUser, dbPassword);
        
        // Inserção de três registros na tabela "pessoa"
        bd.inserirAlterarExcluir("INSERT INTO pessoa(nome, idade) VALUES('Viera', 21)");
    
        
        bd.infoLogger();
        // Consulta dos valores inseridos
        bd.consultar("SELECT * FROM pessoa");
       
        bd.endLogger();
        
        bd.desconectar();
    }
}