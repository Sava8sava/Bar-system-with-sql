package Bar_sys;

import java.sql.*;

public class ClienteDAO {

    protected static final String ENDE   = "jdbc:mysql://localhost:3306/Cliente";
    protected static final String USER = "root";
    protected static final String PASSWORD = "S3cur3!Key";

    //todo caso eu volte,fazer um ConsumoDAO

    public Connection getConnection(){
        try{
            return DriverManager.getConnection(ENDE,USER,PASSWORD);
        }catch(SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void addClient(int num,int cpf,String nome) throws SQLException,ClienteJaCadastrado{
        try{
            Cliente cliente = SearchCLDAO(num);
            throw new ClienteJaCadastrado("ja cad") ;
        }catch (ContaInexistente e) {
            Connection con = getConnection();
            String sql = "INSERT INTO Clientes (id_cliente,cpf,nome) VALUES (?,?,?)";
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setInt(1, num);
            psmt.setInt(2, cpf);
            psmt.setString(3, nome);
            psmt.executeUpdate();
            psmt.close();
            con.close();

            //criar a conta associada
            ContaDAO auxcon = new ContaDAO();
            auxcon.setContaDAO(num);
        }
    }

    public Cliente SearchCLDAO(int id) throws SQLException,ClienteInexistente,ContaInexistente{
        Connection con = getConnection();
        String query = "SELECT * FROM Clientes WHERE id_cliente = ?";
        PreparedStatement psmt = con.prepareStatement(query);
        psmt.setInt(1,id);

        ResultSet rs = psmt.executeQuery();
        Cliente cliente = null;

        if(rs.next()){
            int t_id = rs.getInt("id_cliente");
            int cpf = rs.getInt("cpf");
            String nome = rs.getString("nome");
            cliente = new Cliente(t_id,cpf,nome);
            cliente.setContaaberta(rs.getBoolean("conta_aberta"));
        }else{
            throw new ContaInexistente("cliente não existe(123)");
        }
        rs.close();
        psmt.close();
        con.close();
        return cliente;
    }
    public void addPedido(int num,int tp,double valor) throws SQLException,ContaInexistente{
       try {
           ContaDAO auxcon = new ContaDAO();
           auxcon.addpedido(num, valor, tp);
           // TODO colocar uma exeção para caso a conta esteja fechada
       }catch (ContaInexistente e){
           System.out.println("passou aqui brother");
           throw new ContaInexistente("conta não existe");
       }
    }

    public void setPagamento(int num,double valor)throws SQLException,ClienteInexistente{
        ContaDAO auxcon = new ContaDAO();
        auxcon.setPagamentoDAO(num, valor);
    }

    public void setContaState(int num,boolean state) throws SQLException{
        String sql = "UPDATE Clientes SET conta_aberta = ? WHERE id_cliente = ?";

        try( Connection con = getConnection();
             PreparedStatement psmt = con.prepareStatement(sql);){
            psmt.setBoolean(1,state);
            psmt.setInt(2,num);
            psmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public double getConta(int id)throws SQLException,ContaInexistente{
        try {
            ContaDAO conta = new ContaDAO();
            double valor = conta.getContaFinal(id);
            return valor;
        }catch (ContaInexistente e){
            throw new ContaInexistente("a conta com esse id não existe");
        }
    }

    public void rmvAllClients()throws SQLException{
        try {
            Connection con = getConnection();
            String rmv = "DELETE FROM Clientes WHERE id_cliente >0";
            Statement st = con.createStatement();
            st.execute(rmv);
            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void rmvAllDados() throws SQLException{
        try{
            ContaDAO conta = new ContaDAO();
            conta.rmvAllc();
            rmvAllClients();
        } catch (Exception e) {
            System.err.println("Erro ao remover dados: " + e.getMessage());
            throw e;
        }
    }

}



