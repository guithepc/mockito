package br.com.alura.leilao.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;

class GeradorDePagamentoTest {

	
	private GeradorDePagamento gerador;
	
	@Mock
	private PagamentoDao pagamentoDao;
	
	@Captor
	private ArgumentCaptor<Pagamento> captor;
	
	@Mock
	private Clock clock;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
		this.gerador = new GeradorDePagamento(pagamentoDao, clock);
	}
	
	@Test
	void deveriaCriarPagamentoVencedor() {
		Leilao leilao = leilao();
		Lance vencedor = leilao.getLanceVencedor();
		
		LocalDate dataFixa = LocalDate.of(2020, 12, 7);
 		
		Instant instant = dataFixa.atStartOfDay(ZoneId.systemDefault()).toInstant();
		
		Mockito.when(clock.instant()).thenReturn(instant);
		Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
		
		gerador.gerarPagamento(vencedor);
		
		Mockito.verify(pagamentoDao).salvar(captor.capture());
		
		Pagamento pagamento = captor.getValue();
		
		Assertions.assertEquals(dataFixa, pagamento.getVencimento());
		Assertions.assertEquals(vencedor.getValor(), pagamento.getValor());
		Assertions.assertFalse(pagamento.getPago());
		Assertions.assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
		Assertions.assertEquals(leilao, pagamento.getLeilao());
	}

	private Leilao leilao(){
		
		Leilao leilao = new Leilao ("iPhone 5S", new BigDecimal("500"), new Usuario("Fulano"));
		
		Lance primeiroLance = new Lance(new Usuario("Guilherme"), new BigDecimal("600"));
		
		leilao.propoe(primeiroLance);
		leilao.setLanceVencedor(primeiroLance);
		return leilao;
		
	}
	
}
