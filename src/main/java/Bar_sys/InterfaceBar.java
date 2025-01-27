package Bar_sys;

import java.sql.SQLException;
import java.util.ArrayList;

public interface InterfaceBar {
    public void abrirConta(int numConta, int cpf, String nomeCliente) throws ContaAberta, ContaInexistente, DadosInvalidos, SQLException;
    public void addPedido(int numConta, int numItem, int quant) throws ContaFechada, ContaInexistente, ItemInexistente, DadosInvalidos,SQLException;
    public double valorDaConta(int numConta) throws SQLException,ContaInexistente;
    public double fecharConta(int numConta) throws SQLException,ContaInexistente,ClienteInexistente;
    public void addCardapio(int nu, String n, double x, int tp) throws ItemJaCadastrado, DadosInvalidos, SQLException;
    public void registrarPagamento(int numConta, double val) throws SQLException,PagamentoMaior,ClienteInexistente,ContaInexistente, DadosInvalidos;
    public ArrayList<Consumo> extratoDeConta(int numConta) throws SQLException,ContaInexistente;

}
