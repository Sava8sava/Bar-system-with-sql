package Bar_sys;

import java.sql.Connection;
import java.sql.SQLException;

public class mainteste {
    public static void main(String[] args) {
        ClienteDAO clienteDAO = new ClienteDAO();
        try (Connection connection = clienteDAO.getConnection()) {
            if (connection != null) {
                System.out.println("✅ Conexão bem-sucedida com o banco de dados!");
            clienteDAO.addClient(1,234,"marcelo");
            //TODO terminar os metodos de conta e debugar
            } else {
                System.out.println("❌ Falha na conexão com o banco de dados.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }

    }
}
