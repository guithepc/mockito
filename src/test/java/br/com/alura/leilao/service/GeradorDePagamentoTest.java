package br.com.alura.leilao.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

class GeradorDePagamentoTest {

	
	private GeradorDePagamento gerador;
	
	@Mock
	private PagamentoDao pagamentoDao;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
		this.gerador = new GeradorDePagamento(pagamentoDao);
	}
	
	@Test
	void deveriaCriarPagamentoVencedor() {
		Leilao leilao = leilao();
		Lance vencedor = leilao.getLanceVencedor();
		gerador.gerarPagamento(vencedor);
		
		Mockito.verify(pagamentoDao).salvar(pagamento);
	}

	private Leilao leilao(){
		
		Leilao leilao = new Leilao ("iPhone 5S", new BigDecimal("500"), new Usuario("Fulano"));
		
		Lance primeiroLance = new Lance(new Usuario("Guilherme"), new BigDecimal("600"));
		
		leilao.propoe(primeiroLance);
		leilao.setLanceVencedor(primeiroLance);
		return leilao;
	}
	
}
