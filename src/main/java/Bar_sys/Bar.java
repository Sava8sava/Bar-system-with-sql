package Bar_sys;

import java.sql.SQLException;
import java.util.ArrayList;

public class Bar implements InterfaceBar{
     ClienteDAO lista_de_clientes = new ClienteDAO();
    CardapioDAO cardapio = new CardapioDAO();
    ArrayList<Consumo> cons = new ArrayList<>();

    public void abrirConta(int numConta, int cpf, String nomeCliente)throws SQLException,ContaAberta, ClienteInexistente,ContaInexistente, DadosInvalidos{
        try {
           Cliente aux = lista_de_clientes.SearchCLDAO(numConta);
           throw new ContaAberta("conta ja aberta");
        }catch ( ContaInexistente e){
            if(numConta <= 0 || cpf <= 0 || nomeCliente == null || nomeCliente.isEmpty() ){
                throw new DadosInvalidos("Dado(s) inválidos");
            }
            lista_de_clientes.addClient(numConta, cpf, nomeCliente);
            String gorjeta = "Gorjeta";
            addInConsumo(gorjeta);
        }
     //abrir conta feito
    }

    public void addPedido(int numConta, int numItem, int quant) throws ContaFechada, ContaInexistente,ItemInexistente, DadosInvalidos,SQLException {
        try {
            if (numConta <= 0 || numItem <= 0 || quant <= 0) {
                throw new DadosInvalidos("Dado(s) inválidos");
            }

            Item it = pesquisarItem(numItem);
            Cliente aux = lista_de_clientes.SearchCLDAO(numConta);

            if (!aux.isContaaberta()) {
                throw new ContaFechada("conta ja foi fechada");
            }

            String Monext = "Item: " + it.getDesc() + ", valor unitário: " +
                    it.getValor() + ", valor total: " + it.getItemPrec(quant) + "\n";

            int tipoPedido;
            if (it instanceof Bebida) {
                tipoPedido = 2;
            } else if (it instanceof Comida) {
                tipoPedido = 3;
            } else if (it instanceof Item) {
                tipoPedido = 1;
            } else {
                tipoPedido = 0;
            }

            aux.addped(it.getItemPrec(quant), tipoPedido);
            addInConsumo(Monext);
            lista_de_clientes.addPedido(numConta, tipoPedido, it.getItemPrec(quant));

        }catch(DadosInvalidos e) {
            System.out.println("Dado(s) inválido(s): " + e.getMessage());
            throw e;
        } catch(ContaFechada e) {
            System.out.println("Conta já foi fechada: " + e.getMessage());
            throw e;
        } catch(ContaInexistente e) {
            System.out.println("Erro inesperado: " + e.getMessage());
            throw e;
        }catch(ClienteInexistente e){
            System.out.println("erro inesperado: "+ e.getMessage());
            throw e;
        }catch(ItemInexistente e){
            System.out.println("Item inexistente "+ e.getMessage());
            throw e;
        }
    }

    public double valorDaConta(int numConta) throws SQLException,ContaInexistente{
        //todo fazer esse metodo 
        try {
            double conta = lista_de_clientes.getConta(numConta);
            return conta;
        }catch (ContaInexistente e){
            throw new ContaInexistente("Conta Inexistente");
        }
    }

    public double fecharConta(int numConta) throws SQLException,ContaInexistente,ClienteInexistente{
        try {
            Cliente aux = pesquisarCliente(numConta);
            double valor = lista_de_clientes.getConta(numConta);
            aux.setContaaberta(false);
            lista_de_clientes.setContaState(numConta, false);
            //TODO criar um metodo cliente para atualizar o estado de abertura de conta(feito,mas ainda falta testar)
            return valor;
        }catch ( ClienteInexistente | ContaInexistente e){
            throw new ContaInexistente("conta não existe");
        }
    }

    public void addCardapio(int nu, String n, double x, int tp) throws ItemJaCadastrado, DadosInvalidos, SQLException {
        if(nu <= 0 || x <= 0 ||  tp < 0 || n == null || n.isEmpty()){
           throw new DadosInvalidos("Dado(s) inválidos");
        }

        cardapio.addCardDAO(nu, n, x, tp);
    }

    public void registrarPagamento(int numConta, double val) throws SQLException,ClienteInexistente,PagamentoMaior, ContaInexistente, DadosInvalidos {
        if (numConta <= 0 || val <= 0) {
            throw new DadosInvalidos("Dado(s) inválidos");
        }

        double valor = lista_de_clientes.getConta(numConta);

        if (val > valor) {
            throw new PagamentoMaior("Pagamento maior que o valor em Conta");
        }

        try {
            lista_de_clientes.setPagamento(numConta, val);
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
            throw e; // Re-lança SQLException para ser tratado externamente
        } catch (ContaInexistente e) {
            System.out.println("Conta inexistente: " + e.getMessage());
            throw e; // Re-lança ContaInexistente
        } catch (ClienteInexistente e) {
            System.out.println("Cliente inexistente: " + e.getMessage());
            throw e; // Re-lança ClienteInexistente
        }
    }

    public ArrayList<Consumo> extratoDeConta(int numConta) throws SQLException,ContaInexistente{
         ArrayList<Consumo> aux = this.cons;
        return aux;
    }

    private Cliente pesquisarCliente(int num) throws SQLException,ClienteInexistente {
        try {
            Cliente aux = lista_de_clientes.SearchCLDAO(num);
            return aux;
        }catch (ClienteInexistente e){
            throw new ClienteInexistente("cliente inexistente");
        }
    }

    private Item pesquisarItem(int num) throws SQLException,ItemInexistente{
        try{
            return cardapio.SearchCADAO(num);
        }catch (ItemInexistente e){
            throw  new ItemInexistente("item Inexistente");
        }
    }

    //função para usar nos testes
    public void apagarTudo() throws SQLException{
        cardapio.RemoveAll();
        lista_de_clientes.rmvAllDados();
    }

    public void UpdateCardapio(int nu, String n, double x, int tp) throws  SQLException{
        cardapio.UpdateProd(nu, n, x, tp);
    }

    public void addInConsumo(String s){
        Consumo c = new Consumo(s);
        cons.add(c);
    }
}
