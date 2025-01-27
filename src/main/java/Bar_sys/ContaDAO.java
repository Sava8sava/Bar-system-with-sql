package Bar_sys;

import java.sql.*;

public class ContaDAO {

    protected static final String ENDE   = "jdbc:mysql://localhost:3306/Cliente";
    protected static final String USER = "root";
    protected static final String PASSWORD = "S3cur3!Key";

    public Connection getConnection(){
        try{
            return DriverManager.getConnection(ENDE,USER,PASSWORD);
        }catch(SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void setContaDAO(int id) throws SQLException,ClienteJaCadastrado{
        try{
            Conta conta = SearchCONDAO(id);
            throw new ClienteJaCadastrado("ja cad") ;
        }catch (ContaInexistente e) {
            Connection con = getConnection();
            String sql = "INSERT INTO Conta (id_cliente,bebida,comida,pagamento,valor_final) VALUES (?,?,?,?,?)";
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setInt(1,id);
            psmt.setDouble(2,0);
            psmt.setDouble(3,0);
            psmt.setDouble(4,0);
            psmt.setDouble(5,0);
            psmt.executeUpdate();
            psmt.close();
            con.close();
        }
    }

    public Conta SearchCONDAO(int id) throws SQLException, ContaJaCadastrada {
        Connection con = getConnection();
        String query = "SELECT * FROM Conta WHERE id_cliente = ?";
        PreparedStatement psmt = con.prepareStatement(query);
        psmt.setInt(1,id);

        ResultSet rs = psmt.executeQuery();
        Conta aux = null;
        if(rs.next()){
            double tipo_1 = rs.getDouble("valor_final");
            double bebida = rs.getDouble("bebida");
            double comida = rs.getDouble("comida");
            double pagamento = rs.getDouble("pagamento");
            aux = new Conta();

            tipo_1 -= (bebida + (0.1 * bebida) + comida + (0.15 * comida));

            aux.addInConta(tipo_1,1);
            aux.addInConta(bebida,2);
            aux.addInConta(comida,3);
            aux.setPagamento(pagamento);

        }else{
            throw new ContaInexistente("conta não existe");
        }
        rs.close();
        psmt.close();
        con.close();
        return aux;
    }

    public void addpedido(int id,double valor,int tp) throws SQLException,ContaInexistente{
        try {
            Conta conta = SearchCONDAO(id);
            conta.addInConta(valor,tp);

            Connection con = getConnection();
            String sql = "UPDATE Conta SET bebida = ?, comida = ?, valor_final = ? WHERE id_cliente = ?";
            PreparedStatement psmt = con.prepareStatement(sql);

            psmt.setDouble(1,conta.getBebida());
            psmt.setDouble(2,conta.getComida());
            psmt.setDouble(3,conta.getContafinal());
            psmt.setInt(4,id);
            psmt.executeUpdate();

            psmt.close();
            con.close();
        }catch (ContaInexistente e){
            throw new ContaInexistente("Conta inexistente");
        }
    }

    public void setPagamentoDAO(int id,double valor) throws SQLException,ContaInexistente{
        try {
            Conta conta = SearchCONDAO(id);
            conta.setPagamento(valor);

            Connection con = getConnection();
            String sql = "UPDATE Conta SET pagamento = ? WHERE id_cliente = ?";
            PreparedStatement psmt = con.prepareStatement(sql);

            psmt.setDouble(1,conta.getAux_pagamento());
            psmt.setInt(2,id);
            psmt.executeUpdate();

            psmt.close();
            con.close();

        }catch (ContaInexistente e){
            throw new ContaInexistente("conta inexistente");
        }
    }

    public double getContaFinal(int id)throws SQLException,ContaInexistente{
        try {
            Conta aux = SearchCONDAO(id);
            return aux.getContafinal();
        }catch (ContaInexistente e){
            throw new ContaInexistente("conta com o id passado não existe");
        }
    }

    public void rmvAllc() throws SQLException{
        try {
            Connection con = getConnection();
            String rmv = "DELETE FROM Conta WHERE id_conta >0";
            Statement st = con.createStatement();
            st.execute(rmv);
            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
