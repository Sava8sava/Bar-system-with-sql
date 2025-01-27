package Bar_sys;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

public class TesteBarResumido {
	@Test
	public void testarContaSimples() throws ContaAberta, ContaFechada, ContaInexistente, DadosInvalidos, ItemJaCadastrado, ItemInexistente, ClassNotFoundException, SQLException, ContaJaCadastrada{
		Bar b = new Bar();
		b.apagarTudo();
		b.addCardapio(1, "Cerveja Brahma", 5.5, 2);
		b.addCardapio(2, "File com Fritas", 28, 3);
		b.abrirConta(1, 1, "Pedro");
		
		//3 pedidos de cerveja
		b.addPedido(1, 1, 2);
		b.addPedido(1, 1, 2);
		b.addPedido(1, 1, 2);
		//Pedido de file
		b.addPedido(1, 2, 1);
		
		double val = b.fecharConta(1);
		//# cervejas = 33 + 3,30; file = 28 + 4.2; Total = 68,5
		assertEquals(68.5, val, 0.0001);
	}

	@Test
	public void testarInserirContaFechada() throws ContaAberta, ContaInexistente, ContaFechada, DadosInvalidos, ItemJaCadastrado, ItemInexistente, ClassNotFoundException, SQLException, ContaJaCadastrada{
		Bar b = new Bar();
		b.apagarTudo();
		b.addCardapio(1, "Cerveja Brahma", 5.5, 2);
		b.addCardapio(2, "File com Fritas", 28, 3);
		b.abrirConta(1, 1, "Pedro");
		
		//3 pedidos de cerveja
		b.addPedido(1, 1, 2);
		b.addPedido(1, 1, 2);
		b.addPedido(1, 1, 2);
		//Pedido de filŽ 
		b.addPedido(1, 2, 1);
		
		double val = b.fecharConta(1);
		//# cervejas = 33; file = 28; subtotal = 61; 10% = 6.1
		
		try {
			b.addPedido(1, 2, 1);
			fail("Deveria ter dado exce�‹o de conta fechada.");
		} catch (ContaFechada e) {
			// Valor n‹o foi alterado
			System.out.println(b.valorDaConta(1));
			assertEquals(68.5, b.valorDaConta(1), 0.0001);
		}
	}
	
	@Test
	public void testarPagarContaEmPartes() throws ContaAberta, ContaInexistente, ContaFechada, PagamentoMaior, DadosInvalidos, ItemJaCadastrado, ItemInexistente, ClassNotFoundException, SQLException, ContaJaCadastrada{
		Bar b = new Bar();
		b.apagarTudo();
		b.addCardapio(1, "Cerveja Brahma", 5.5, 2);
		b.addCardapio(2, "File com Fritas", 28, 3);
		
		b.abrirConta(1, 1, "Pedro");
		
		//3 pedidos de cerveja
		b.addPedido(1, 1, 2);
		b.addPedido(1, 1, 2);
		b.addPedido(1, 1, 2);
		//Pedido de filŽ 
		b.addPedido(1, 2, 1);
		
		double val = b.fecharConta(1);
		//# cervejas = 33; file = 28; subtotal = 61; 10% = 6.1

		b.registrarPagamento(1, 20);
		b.registrarPagamento(1, 20);
		b.registrarPagamento(1, 20);
		b.registrarPagamento(1, 8.1);
		
		try {
			b.registrarPagamento(1, 0.50);
            fail("Deveria ter dado exce�‹o.");
		} catch (PagamentoMaior e1) {
			assertEquals(0.4, b.valorDaConta(1), 0.001);
		}
	}
	
	@Test
	public void testarContaInexistente() throws ContaAberta, ContaFechada, PagamentoMaior, ContaInexistente, DadosInvalidos, ItemJaCadastrado, ItemInexistente, ClassNotFoundException, SQLException, ContaJaCadastrada{
		Bar b = new Bar();
		b.apagarTudo();
		b.addCardapio(1, "Cerveja Brahma", 5.5, 2);
		b.addCardapio(2, "File com Fritas", 28, 3);
		
		b.abrirConta(1, 1, "Pedro");
		b.abrirConta(2, 2, "Raimundo");
		b.abrirConta(3, 3, "Maria");

		try {
			b.addPedido(4, 2, 1);
			fail("N‹o era para ter registrado pedido em conta inexistente!");
		} catch (ContaInexistente e) {
          // N‹o era para ter registrado pedido em conta inexistente!
		}
		
		try {
			double val = b.fecharConta(4);
			fail("N‹o era para fechar conta inexistente!");
		} catch (ContaInexistente e) {
			// N‹o era para fechar conta inexistente!
		}

		try {
			b.registrarPagamento(4, 0.50);
            fail("Deveria ter dado exce�‹o de conta inexistente.");
		} catch (ContaInexistente e1) {
			//Deveria ter dado exce�‹o de conta inexistente.
		}
	}
	
	@Test
	public void testarContaJaExistente() throws ContaInexistente, ContaAberta, DadosInvalidos, ClassNotFoundException, SQLException, ContaJaCadastrada, ItemInexistente{
		Bar b = new Bar();
		b.apagarTudo();
		b.abrirConta(1, 1, "Pedro");
		try {
			b.abrirConta(1, 2, "Raimundo");
			fail("Deveria ter dado exce�‹o de conta j‡ aberta.");
		} catch (ContaAberta e) {
            // Excecao correta.
		}
		b.fecharConta(1);
	}
	
	@Test
	public void testarExtratoItens() throws ContaAberta, ContaFechada, PagamentoMaior, ContaInexistente, DadosInvalidos, ItemJaCadastrado, ItemInexistente, ClassNotFoundException, SQLException, ContaJaCadastrada{
		Bar b = new Bar();
		b.apagarTudo();
		b.addCardapio(1, "Cerveja Brahma", 5.5, 2);
		b.addCardapio(2, "File com Fritas", 28, 3);
		
		b.abrirConta(1, 1, "Pedro");
		b.addPedido(1, 1, 1);
		b.addPedido(1, 1, 1);
		b.addPedido(1, 1, 1);
		b.addPedido(1, 2, 1);
		double val = b.fecharConta(1);
		ArrayList<Consumo> itens = b.extratoDeConta(1);
		System.out.println(itens.size());
		assertEquals(5, itens.size());
	}
	
//************************* testes adicionados**********************
	
	@Test
	public void testarAdicionarItemNaoExistente() throws ClassNotFoundException, SQLException, ContaJaCadastrada, DadosInvalidos, ContaInexistente, ContaFechada, ContaAberta, ItemInexistente {
	    Bar b = new Bar();
	    b.apagarTudo();
	    b.abrirConta(1, 1, "Carlos");
	    
	    try {
	        b.addPedido(1, 99, 1); // Item 99 não existe no cardápio
	        fail("Deveria ter lançado exceção de ItemInexistente.");
	    } catch (ItemInexistente e) {
	        // Exceção esperada
	    }
	}

	@Test
	public void testarFecharContaSemPedidos() throws ClassNotFoundException, SQLException, ContaJaCadastrada, ContaInexistente, ContaFechada, ContaAberta, DadosInvalidos, ItemInexistente {
	    Bar b = new Bar();
	    b.apagarTudo();
	    b.abrirConta(1, 1, "Ana");
	    double valor = b.fecharConta(1);
	    assertEquals(0.0, valor, 0.0001); // Conta sem pedidos deve ser 0
	}

	@Test
	public void testarPagamentoMaiorQueConta() throws ClassNotFoundException, SQLException, ContaJaCadastrada, DadosInvalidos, ContaInexistente, ContaFechada, PagamentoMaior, ItemInexistente, ItemJaCadastrado, ContaAberta {
	    Bar b = new Bar();
	    b.apagarTudo();
	    b.addCardapio(1, "Cerveja", 5.0, 1);
	    b.abrirConta(1, 1, "João");
	    b.addPedido(1, 1, 2); // Total: 10
	    b.fecharConta(1);
	    
	    try {
	        b.registrarPagamento(1, 15.0); // Pagamento maior que o valor da conta
	        fail("Deveria ter lançado exceção de PagamentoMaior.");
	    } catch (PagamentoMaior e) {
	        // Exceção esperada
	    }
	}


	@Test
	public void testarAddCardapioComCodigoDuplicado() throws ClassNotFoundException, SQLException, ItemJaCadastrado, DadosInvalidos {
	    Bar b = new Bar();
	    b.apagarTudo();
	    b.addCardapio(1, "Pizza", 25.0, 1);
	    try {
	        b.addCardapio(1, "Hambúrguer", 15.0, 1); // Código duplicado
	        fail("Deveria ter lançado exceção de ItemJaCadastrado.");
	    } catch (ItemJaCadastrado e) {
	        // Exceção esperada
	    }
	}

	@Test
	public void testarContaVaziaSemFechar() throws ClassNotFoundException, SQLException, ContaJaCadastrada, ContaInexistente, ContaAberta, DadosInvalidos, ItemInexistente {
	    Bar b = new Bar();
	    b.apagarTudo();
	    b.abrirConta(1, 1, "Lucas");
	    
	    double valorAntesFechamento = b.valorDaConta(1);
	    assertEquals(0.0, valorAntesFechamento, 0.0001); // Conta ainda sem pedidos
	}

	@Test
	public void testarAdicionarPedidoComQuantidadeNegativa() throws ClassNotFoundException, SQLException, ContaJaCadastrada, ContaInexistente, DadosInvalidos, ItemJaCadastrado, ContaAberta, ItemInexistente, ContaFechada {
	    Bar b = new Bar();
	    b.apagarTudo();
	    b.addCardapio(1, "Cerveja", 5.0, 1);
	    b.abrirConta(1, 1, "João");
	    try {
	        b.addPedido(1, 1, -2); // Quantidade negativa
	        fail("Deveria ter lançado exceção de DadosInvalidos.");
	    } catch (DadosInvalidos e) {
	        // Exceção esperada
	    }
	}



	@Test
	public void testarFecharContaComPagamentoParcial() throws ClassNotFoundException, SQLException, ContaJaCadastrada, DadosInvalidos, ContaInexistente, ContaFechada, PagamentoMaior, ItemJaCadastrado, ItemInexistente, ContaAberta {
	    Bar b = new Bar();
	    b.apagarTudo();
	    b.addCardapio(1, "Refrigerante", 3.5, 1);
	    b.abrirConta(1, 1, "Maria");
	    b.addPedido(1, 1, 3); // Total: 10.5
	    b.fecharConta(1);
	    b.registrarPagamento(1, 5.0);
	    double valorRestante = b.valorDaConta(1);
	    assertEquals(5.5, valorRestante, 0.0001);
	}

	@Test
	public void testarFecharContaComPagamentoZerado() throws ClassNotFoundException, SQLException, ContaJaCadastrada, ContaInexistente, ContaFechada, ItemInexistente, ContaAberta, DadosInvalidos, ItemJaCadastrado, PagamentoMaior {
	    Bar b = new Bar();
	    b.apagarTudo();
	    b.addCardapio(1, "Suco", 4.0, 1);
	    b.abrirConta(1, 1, "Carlos");
	    b.addPedido(1, 1, 2); // Total: 8.0
	    b.fecharConta(1);
	    b.registrarPagamento(1, 8.0);
	    double valorFinal = b.valorDaConta(1);
	    assertEquals(0.0, valorFinal, 0.0001);
	}

	@Test
	public void testarPagamentoDeContaNaoExistente() throws ClassNotFoundException, SQLException, ContaInexistente, PagamentoMaior, DadosInvalidos, ItemInexistente {
	    Bar b = new Bar();
	    b.apagarTudo();
	    try {
	        b.registrarPagamento(999, 10.0); // Conta inexistente
	        fail("Deveria ter lançado exceção de ContaInexistente.");
	    } catch (ContaInexistente e) {
	        // Exceção esperada
	    }
	}


	@Test
	public void testarExtratoDeContaVazia() throws ClassNotFoundException, SQLException, ContaJaCadastrada, ContaInexistente, ContaAberta, DadosInvalidos, ItemInexistente {
	    Bar b = new Bar();
	    b.apagarTudo();
	    b.abrirConta(1, 1, "Joana");
	    ArrayList<Consumo> extrato = b.extratoDeConta(1);
	    assertEquals(1, extrato.size()); // Conta sem pedidos
	}

	@Test
	public void testarAdicionarCardapioComPrecoNegativo() throws ClassNotFoundException, SQLException, ItemJaCadastrado {
	    Bar b = new Bar();
	    b.apagarTudo();
	    try {
	        b.addCardapio(1, "Item inválido", -5.0, 1); // Preço negativo
	        fail("Deveria ter lançado exceção de DadosInvalidos.");
	    } catch (DadosInvalidos e) {
	        // Exceção esperada
	    }
	}

	@Test
	public void testarQuantidadeDisponivelNoCardapio() throws ClassNotFoundException, SQLException, ItemJaCadastrado, ContaJaCadastrada, DadosInvalidos, ContaInexistente, ItemInexistente, ContaFechada, ContaAberta {
	    Bar b = new Bar();
	    b.apagarTudo();
	    b.addCardapio(1, "Bolo", 6.0, 2);
	    b.abrirConta(1, 1, "Pedro");
	    b.addPedido(1, 1, 1);
	    b.addPedido(1, 1, 1);
	    try {
	        b.addPedido(1, 3, 1); // Quantidade excedida
	        fail("Deveria ter lançado exceção de ItemInexistente.");
	    } catch (ItemInexistente e) {
	        // Exceção esperada
	    }
	}


	@Test
	public void testarFecharContaSemPedidosMasComItens() throws ClassNotFoundException, SQLException, ContaJaCadastrada, ContaInexistente, ContaFechada, ItemJaCadastrado, ItemInexistente, ContaAberta, DadosInvalidos {
	    Bar b = new Bar();
	    b.apagarTudo();
	    b.addCardapio(1, "Chá", 3.0, 1);
	    b.abrirConta(1, 1, "Paulo");
	    double valor = b.fecharConta(1); // Conta aberta sem pedidos
	    assertEquals(0.0, valor, 0.0001);
	}

	@Test
	public void testarAdicionarPedidoComContaFechada() throws ClassNotFoundException, SQLException, ContaJaCadastrada, DadosInvalidos, ContaInexistente, ContaFechada, ItemJaCadastrado, ItemInexistente, ContaAberta {
	    Bar b = new Bar();
	    b.apagarTudo();
	    b.addCardapio(1, "Sorvete", 7.0, 3);
		//nesse teste o tipo estava 5 inves de 3 que era o tipo definido para comidas,então eu mudei
	    b.abrirConta(1, 1, "Fernanda");
	    b.fecharConta(1);
	    try {
	        b.addPedido(1, 1, 1); // Conta já fechada
	        fail("Deveria ter lançado exceção de ContaFechada.");
	    } catch (ContaFechada e) {
	        // Exceção esperada
	    }
	}

	@Test
	public void testarAbrirContaComCodigoDuplicado() throws ClassNotFoundException, SQLException, ContaJaCadastrada, ContaAberta, ContaInexistente, DadosInvalidos, ItemInexistente {
	    Bar b = new Bar();
	    b.apagarTudo();
	    b.abrirConta(1, 1, "Rafael");
	    try {
	        b.abrirConta(1, 2, "Marcos"); // Código duplicado
	        fail("Deveria ter lançado exceção de ContaAberta.");
	    } catch (ContaAberta e) {
	        // Exceção esperada
	    }
	}

	@Test
	public void testarFecharContaComItensEQuantidadesZeradas() throws ClassNotFoundException, SQLException, ContaJaCadastrada, ContaInexistente, ItemJaCadastrado, DadosInvalidos, ContaAberta, ItemInexistente {
	    Bar b = new Bar();
	    b.apagarTudo();
	    b.addCardapio(1, "Água", 1.0, 0);
	    b.abrirConta(1, 1, "Sofia");
	    double valor = b.fecharConta(1);
	    assertEquals(0.0, valor, 0.0001);
	}

	
}



