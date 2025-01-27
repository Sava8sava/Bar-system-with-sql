package Bar_sys;

import java.sql.*;
import java.util.ArrayList;

public class CardapioDAO {
    protected static final String ENDE   = "jdbc:mysql://localhost:3306/Cardapio";
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

    public void addCardDAO(int nu, String n, double x, int tp) throws SQLException,ItemJaCadastrado{
        Connection con = getConnection();
        String sql = "INSERT INTO TabelaItens (id, nome, preco, tipo) VALUES (" + nu + ", '" + n + "', " + x + ", " + tp + ")";
        Statement st = con.createStatement();
        //TODO criar a função procurar quando voltar
        try{
            Item aux = SearchCADAO(nu);
            throw new ItemJaCadastrado("item ja foi cadastrado no banco de dados");
        }catch(ItemInexistente e){
            st.execute(sql);
        }
        st.close();
    }

    public Item SearchCADAO(int numped)throws SQLException,DadosInvalidos,ItemInexistente{
        Connection con = getConnection();
        String query = "SELECT * FROM TabelaItens WHERE id = "+ numped;
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        Item item = null;
        if(rs.next()){
            String nome = rs.getString("nome");
            double preco = rs.getDouble("preco");
            int tipo = rs.getInt("tipo");

            if(tipo == 1){
                item = new Item(numped,nome,preco);
            }
            else if(tipo == 2){
               item = new Bebida(numped,nome,preco);
            }else if (tipo == 3) {
                item = new Comida(numped,nome,preco);
            }else{
                throw new DadosInvalidos("tipo não consta");
            }
        }else{
            throw new ItemInexistente("");
        }
        st.close();
        return item;
    }

    public void RemoveItem(int id)throws SQLException{
        Connection con = getConnection();
        String rmv = "DELETE FROM TabelaItens WHERE id = "+ id;
        Statement st = con.createStatement();
        st.execute(rmv);
        st.close();
    }

    public ArrayList<Item> getTable() throws SQLException{
        Connection con = getConnection();
        String query = "SELECT * FROM TabelaItens";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        Item item = null;
        ArrayList<Item> todos = new ArrayList<Item>();
        while(rs.next()) {
            int numped = rs.getInt("id");
            String nome = rs.getString("nome");
            double preco = rs.getDouble("preco");
            int tipo = rs.getInt("tipo");

            if (tipo == 2) {
                item = new Bebida(numped, nome, preco);
                todos.add(item);
            } else if (tipo == 3) {
                item = new Comida(numped, nome, preco);
                todos.add(item);
            }
        }
        st.close();
        return todos;
    }

    public void RemoveAll()throws SQLException{
        Connection con = getConnection();
        String rmv = "DELETE FROM TabelaItens WHERE id >0";
        Statement st = con.createStatement();
        st.execute(rmv);
        st.close();
        con.close();
    }

    public void UpdateProd(int nu, String n, double x, int tp)throws SQLException{
        Connection con = getConnection();
        String sql = "UPDATE TabelaItens SET nome = '" + n + "', preco = " + x + ", tipo = " + tp + " WHERE id = " + nu;
        Statement st = con.createStatement();
        try {
            Item aux = SearchCADAO(nu);
            st.execute(sql);
        }catch (ItemInexistente e){
            System.out.println("item não adicionado");
        }
    }

}
