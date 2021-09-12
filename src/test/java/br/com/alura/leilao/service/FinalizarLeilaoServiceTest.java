package br.com.alura.leilao.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

class FinalizarLeilaoServiceTest {

	
	private FinalizarLeilaoService service;
	
	@Mock
	private LeilaoDao leilaoDao;
	
	@Mock
	private EnviadorDeEmails enviadorDeEmails;
	
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
		this.service = new FinalizarLeilaoService(leilaoDao, enviadorDeEmails);
	}
	
	@Test
	void deveriaFinalizarLeilao() {

		List<Leilao> leiloes = leiloes();
		
		Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);
		
		service.finalizarLeiloesExpirados();
		
		Leilao leilao = leiloes.get(0);
		
		Assertions.assertTrue(leilao.isFechado());
		Assertions.assertEquals(new BigDecimal ("650"), leilao.getLanceVencedor().getValor());
		
		Mockito.verify(leilaoDao).salvar(leilao);
	}
	
	@Test
	void deveriaEnviarEmailVencedor() {

		List<Leilao> leiloes = leiloes();
		
		Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);
		
		service.finalizarLeiloesExpirados();
		
		Leilao leilao = leiloes.get(0);
		
		Lance lanceVencedor = leilao.getLanceVencedor();
		
		Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(lanceVencedor); //verifica se o método enviaEmail foi chamado p/ lance vencedor
		Mockito.verify(leilaoDao).salvar(leilao);
	}
	
	@Test
	void naoDeveEnviarEmailEmCasoDeErro() {

		List<Leilao> leiloes = leiloes();
		
		Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);
		
		Mockito.when(leilaoDao.salvar(Mockito.any())).thenThrow(RuntimeException.class);
		
		try {
			service.finalizarLeiloesExpirados();
			Mockito.verifyNoInteractions(enviadorDeEmails);
			
		} catch (Exception e) {
			
		}
		
		
		
		
	}
	
	private List<Leilao> leiloes(){
		List<Leilao> lista = new ArrayList<>();
		
		Leilao leilao = new Leilao ("iPhone 5S", new BigDecimal("500"), new Usuario("Fulano"));
		
		Lance primeiroLance = new Lance(new Usuario("Guilherme"), new BigDecimal("600"));
		Lance segundoLance = new Lance(new Usuario("Nicole"), new BigDecimal("650"));
		
		leilao.propoe(primeiroLance);
		leilao.propoe(segundoLance);
		
		lista.add(leilao);
		return lista;
	}
	

}
