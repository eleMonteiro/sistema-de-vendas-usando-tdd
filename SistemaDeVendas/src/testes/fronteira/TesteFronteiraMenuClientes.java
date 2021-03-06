package testes.fronteira;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import controladores.ControladorCliente;
import entidades.Cliente;
import excecoes.CampoComValorInvalidoException;
import fronteira.MenuClientes;
import repositorios.RepositorioClientes;

class TesteFronteiraMenuClientes {

	private final String stringMenuClientes = "# MENU DE CLIENTES #\n" + "[0] Voltar\n" + "[1] Criar\n" + "[2] Editar\n"
			+ "[3] Remover\n" + "[4] Procurar\n" + "[5] Listar\n" + "$ Digite a sua opção: \n";
	private final String stringOpcaoNaoEInteiro = "ERR: A opção precisa ser um inteiro\n";
	private final String stringOpcaoInvalida = "ERR: Opção inválida\n";

	@Test
	void testeOpcaoSendoUmaLetra() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		System.setIn(new ByteArrayInputStream("a\n0".getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + stringOpcaoNaoEInteiro + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeOpcaoSendoUmCaractereEspecial() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		System.setIn(new ByteArrayInputStream("@\n0".getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + stringOpcaoNaoEInteiro + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeOpcaoSendoUmDouble() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		System.setIn(new ByteArrayInputStream("1.0\n0".getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + stringOpcaoNaoEInteiro + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeOpcaoInvalidaMenorQue0() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		System.setIn(new ByteArrayInputStream("-1\n0".getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + stringOpcaoInvalida + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeOpcaoInvalidaMaiorQue5() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		System.setIn(new ByteArrayInputStream("6\n0".getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + stringOpcaoInvalida + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeCriarClienteComNomeContendoNumeros() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		System.setIn(new ByteArrayInputStream("1\nJohn Doe 10\n0".getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite o nome do novo cliente: \n"
				+ "ERR: O nome não pode conter números ou caracteres especiais\n" + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeCriarClienteComNomeContendoCaracteresEspeciais() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		System.setIn(new ByteArrayInputStream("1\n@John_Doe\n0".getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite o nome do novo cliente: \n"
				+ "ERR: O nome não pode conter números ou caracteres especiais\n" + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeCriarClienteCorretamente() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		System.setIn(new ByteArrayInputStream("1\nJohn Doe\n0".getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite o nome do novo cliente: \n"
				+ "MSG: Novo cliente criado\n" + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeEditarClienteComIDNaoInteira() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "2\nA\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite a id do cliente: \n"
				+ "ERR: A id tem que ser um inteiro\n" + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeEditarClienteComIDInvalida() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "2\n" + 0 + "\nJohn Doe\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite a id do cliente: \n"
				+ "$ Digite o novo nome do cliente: \n" + "ERR: A ID tem que ser >= 1\n" + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeEditarClienteQueNaoExiste() {
		// Garante que o cliente não existe
		long idCliente = new Cliente("John Doe").getId();

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "2\n" + idCliente + "\nJohn Doe\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite a id do cliente: \n"
				+ "$ Digite o novo nome do cliente: \n" + "ERR: O cliente a ser editado não existe\n"
				+ stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeEditarClienteComNomeVazio() throws CampoComValorInvalidoException {
		// Garante que o cliente existe
		long idCliente = new ControladorCliente().criarCliente("John Doe");

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "2\n" + idCliente + "\n\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite a id do cliente: \n"
				+ "$ Digite o novo nome do cliente: \n" + "ERR: O nome não pode ser vazio\n" + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeEditarClienteComNomeContendoNumeros() throws CampoComValorInvalidoException {
		// Garante que o cliente existe
		long idCliente = new ControladorCliente().criarCliente("John Doe");

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "2\n" + idCliente + "\nJohn Doe 10\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite a id do cliente: \n"
				+ "$ Digite o novo nome do cliente: \n"
				+ "ERR: O nome não pode conter números ou caracteres especiais\n" + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeEditarClienteComNomeContendoCaracteresEspeciais() throws CampoComValorInvalidoException {
		// Garante que o cliente existe
		long idCliente = new ControladorCliente().criarCliente("John Doe");

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "2\n" + idCliente + "\n@John_Doe\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite a id do cliente: \n"
				+ "$ Digite o novo nome do cliente: \n"
				+ "ERR: O nome não pode conter números ou caracteres especiais\n" + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeEditarClienteCorretamente() throws CampoComValorInvalidoException {
		// Garante que o cliente existe
		long idCliente = new ControladorCliente().criarCliente("John Doe");

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "2\n" + idCliente + "\nJane Doe\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite a id do cliente: \n"
				+ "$ Digite o novo nome do cliente: \n" + "MSG: O cliente foi editado\n" + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeRemoverClienteComIDNaoInteira() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "3\nA\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite a id do cliente: \n"
				+ "ERR: A id tem que ser um inteiro\n" + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeRemoverClienteComIDInvalida() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "3\n0\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite a id do cliente: \n" + "ERR: A id tem que ser >= 1\n"
				+ stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeRemoverClienteQueNaoExiste() {
		// Garante que o cliente não existe
		long idCliente = new Cliente("John Doe").getId();

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "3\n" + idCliente + "\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite a id do cliente: \n"
				+ "ERR: O cliente a ser removido não existe\n" + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeRemoverClienteCorretamente() throws CampoComValorInvalidoException {
		// Garante que o cliente existe
		long idCliente = new ControladorCliente().criarCliente("John Doe");

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "3\n" + idCliente + "\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite a id do cliente: \n" + "MSG: O cliente foi removido\n"
				+ stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeProcurarClientes() throws CampoComValorInvalidoException {
		// Garante que só existem os seguintes clientes
		RepositorioClientes repositorioClientes = RepositorioClientes.getInstance();
		repositorioClientes.getClienteList().clear();
		ControladorCliente controladorCliente = new ControladorCliente();
		controladorCliente.criarCliente("John Doe");
		long idCliente2 = controladorCliente.criarCliente("Jane Doe");
		Cliente cliente2 = controladorCliente.getCliente(idCliente2);

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "4\nane\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "$ Digite o filtro da pesquisa: \n" + "(" + cliente2.getId()
				+ ") " + cliente2.getNome() + "\n" + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

	@Test
	void testeListarClientes() throws CampoComValorInvalidoException {
		// Garante que só existem os seguintes clientes
		RepositorioClientes repositorioClientes = RepositorioClientes.getInstance();
		repositorioClientes.getClienteList().clear();
		ControladorCliente controladorCliente = new ControladorCliente();
		long idCliente1 = controladorCliente.criarCliente("John Doe");
		Cliente cliente1 = controladorCliente.getCliente(idCliente1);
		long idCliente2 = controladorCliente.criarCliente("Jane Doe");
		Cliente cliente2 = controladorCliente.getCliente(idCliente2);

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		String entrada = "5\n0";
		System.setIn(new ByteArrayInputStream(entrada.getBytes()));

		new MenuClientes().iniciar();
		String resultadoEsperado = stringMenuClientes + "(" + cliente1.getId() + ") " + cliente1.getNome() + "\n("
				+ cliente2.getId() + ") " + cliente2.getNome() + "\n" + stringMenuClientes;
		assertEquals(resultadoEsperado, outputStream.toString());
	}

}
