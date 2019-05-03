package entidades;

public class Produto {

	private static long nextCodigo = 1;
	private long id;
	private String nome;
	private float preco;

	public Produto() {
		this.id = nextCodigo;
		nextCodigo++;
	}

	public Produto(String nome, float preco) {
		this.id = nextCodigo;
		this.nome = nome;
		this.preco = preco;

		nextCodigo++;
	}

	public Produto(long codigo, String nome, float preco) {
		this.id = codigo;
		this.nome = nome;
		this.preco = preco;

		nextCodigo++;
	}

	public Produto(String nome) {
		this.nome = nome;
	}

	public Produto(float preco) {
		this.preco = preco;
	}

	public Produto(long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public float getPreco() {
		return preco;
	}

	public void setPreco(float preco) {
		this.preco = preco;
	}
}
