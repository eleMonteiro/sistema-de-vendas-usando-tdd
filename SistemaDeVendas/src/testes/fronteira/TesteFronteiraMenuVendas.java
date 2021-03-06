package testes.fronteira;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import controladores.ControladorVenda;
import entidades.Cliente;
import entidades.ItemVenda;
import entidades.Produto;
import excecoes.CampoComValorInvalidoException;
import excecoes.ItemNaoEstaNoRepositorioException;
import excecoes.QuantidadeDoElementoInvalidaException;
import fronteira.MenuVendas;

class TesteFronteiraMenuVendas {

	private final String stringMenuVenda = "# MENU DE VENDAS #\r\n" + "[0] Voltar\r\n" + "[1] Criar\r\n"
			+ "[2] Procurar\r\n" + "$ Digite a sua opção:\r\n";
	private final String stringOpacaoNaoEInteiro = "ERR: A opção precisa ser um inteiro\r\n";
	private final String stringOpcaoInvalida = "ERR: Opção inválida\r\n";

	@Test
	void TesteOpcaoSendoUmaLetra() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		System.setIn(new ByteArrayInputStream("a\r\n0".getBytes()));

		new MenuVendas().iniciar();
		String resultadoEsperado = stringMenuVenda + stringOpacaoNaoEInteiro + stringMenuVenda;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void TesteOpcaoSendoUmCaractereEspecial() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		System.setIn(new ByteArrayInputStream("@\r\n0".getBytes()));

		new MenuVendas().iniciar();
		String resultadoEsperado = stringMenuVenda + stringOpacaoNaoEInteiro + stringMenuVenda;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void TesteOpcaoSendoUmDouble() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		System.setIn(new ByteArrayInputStream("3.5\r\n0".getBytes()));

		new MenuVendas().iniciar();
		String resultadoEsperado = stringMenuVenda + stringOpacaoNaoEInteiro + stringMenuVenda;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void TesteOpcaoInvalidaMenorQue0() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		System.setIn(new ByteArrayInputStream("-1\r\n0".getBytes()));

		new MenuVendas().iniciar();
		String resultadoEsperado = stringMenuVenda + stringOpcaoInvalida + stringMenuVenda;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void TesteOpcaoInvalidaMaiorQue3() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		System.setIn(new ByteArrayInputStream("4\r\n0".getBytes()));

		new MenuVendas().iniciar();
		String resultadoEsperado = stringMenuVenda + stringOpcaoInvalida + stringMenuVenda;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void TesteBuscarVendaComIDNaoInteiro() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "2\r\nA\r\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuVendas().iniciar();
		String resultadoEsperado = stringMenuVenda + "$ Digite o id da venda: \r\n"
				+ "ERR: A id tem que ser um inteiro\r\n" + stringMenuVenda;

		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void TesteCriarVendaComIDDoClienteNaoInteiro() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "1\r\nA\r\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuVendas().iniciar();
		String resultadoEsperado = stringMenuVenda + "$ Digite o id do cliente: \r\n"
				+ "ERR: O id e a quantidade tem que serem um inteiros\r\n" + stringMenuVenda;

		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void TesteCriarVendaComIDDoProdutoNaoInteiro() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "1\r\n1\r\nA\r\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuVendas().iniciar();
		String resultadoEsperado = stringMenuVenda + "$ Digite o id do cliente: \r\n"
				+ "$ Digite o id do produto a ser cadastrado OU -1 para terminar a inseção dos produtos: \r\n"
				+ "ERR: O id e a quantidade tem que serem um inteiros\r\n" + stringMenuVenda;

		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void TesteCriarVendaComQuantidadeNaoInteira() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "1\r\n1\r\n1\r\na\r\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuVendas().iniciar();
		String resultadoEsperado = stringMenuVenda + "$ Digite o id do cliente: \r\n"
				+ "$ Digite o id do produto a ser cadastrado OU -1 para terminar a inseção dos produtos: \r\n"
				+ "$ Digite a quantidade do produto: \r\n" + "ERR: O id e a quantidade tem que serem um inteiros\r\n"
				+ stringMenuVenda;

		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void TesteBuscarVenda() throws CampoComValorInvalidoException, QuantidadeDoElementoInvalidaException,
			ItemNaoEstaNoRepositorioException {
		// Garintir que a venda existe
		List<ItemVenda> itemVenda = new ArrayList<>();
		Produto produto = new Produto("Caderno", 25);
		ItemVenda item = new ItemVenda( produto.getId(), 1);
		itemVenda.add(item);
		Cliente cliente = new Cliente("Elenilson");
		long idVenda = new ControladorVenda().criarVenda(new Date(), cliente.getId(), 40.0, itemVenda);

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "2\r\n" + idVenda + "\r\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuVendas().iniciar();
		String resultadoEsperado = stringMenuVenda + "$ Digite o id da venda: \r\n" + "MSG: A venda foi encontrada\r\n"
				+ stringMenuVenda;

		assertEquals(resultadoEsperado, outputStream.toString());
	}
}