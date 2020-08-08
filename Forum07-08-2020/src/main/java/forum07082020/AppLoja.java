package forum07082020;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AppLoja {

	public static void main(String[] args) throws Exception {
		Connection conex�o = null;
		try {
			conex�o = abrirConex�o();
			
			criarTabelaDeProdutos(conex�o);
			
			inserirDoisMilProdutos(conex�o);
			
			alterarNomeDoProduto(conex�o, 1l, "Mouse");

			long quantidadeDeProdutosAntesDeDeletar = contarProdutos(conex�o);

			System.out.println("Antes: " + quantidadeDeProdutosAntesDeDeletar);

			listarProdutos(conex�o);

			deletar10Produtos(conex�o);
			long quantidadeDeProdutosDepoisDeDeletar = contarProdutos(conex�o);
			System.out.println("\nDepois de deletar 10: " + quantidadeDeProdutosDepoisDeDeletar);

			alterarPrimeiro(conex�o,"Teclado");
			listarOs30Primeiros(conex�o);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex�o.close();
		}
		System.out.println("Conectou.");
	}

	private static void listarProdutos(Connection conex�o) throws Exception {
		Statement recuperarProdutos = null;
		try {
			recuperarProdutos = conex�o.createStatement();
			ResultSet resultado = recuperarProdutos.executeQuery("select id, nome from produtos");
			System.out.println("Listando Produtos...");
			while (resultado.next()) {
				System.out.println("- " + resultado.getString("nome") + ", id=" + resultado.getLong("id"));
			}
			System.out.println("Listagem conclu�da.");
		} finally {
			recuperarProdutos.close();
		}
	}

	private static void listarOs30Primeiros(Connection conex�o) throws Exception {
		Statement mostrar30produtos = null;

		try {
			mostrar30produtos = conex�o.createStatement();
			ResultSet resultado = mostrar30produtos.executeQuery("select id, nome FROM produtos limit 0,30");
			System.out.println("\nLISTANDO OS 30 PROUDTOS");

			while (resultado.next()) {
				System.out.println("- " + resultado.getString("nome") + ", id=" + resultado.getLong("id"));
			}
			System.out.println("Listagem dos 30 conclu�da.\n");
		} finally {
			mostrar30produtos.close();
		}

	}

	private static long contarProdutos(Connection conex�o) throws Exception {
		long quantidadeDeProdutos = 0;
		Statement contarProdutos = null;
		try {
			contarProdutos = conex�o.createStatement();
			ResultSet resultado = contarProdutos.executeQuery("select count(*) as quantidade from produtos");
			if (resultado.next()) {
				quantidadeDeProdutos = resultado.getLong("quantidade");
			}
		} finally {
			contarProdutos.close();
		}
		return quantidadeDeProdutos;
	}

	private static void alterarNomeDoProduto(Connection conex�o, Long id, String novoNome) throws Exception {
		PreparedStatement alterarProdutos = null;
		try {
			alterarProdutos = conex�o.prepareStatement("update produtos set nome = ? where id = ?");
			alterarProdutos.setLong(2, id);
			alterarProdutos.setString(1, novoNome);
			alterarProdutos.execute();
		} finally {
			alterarProdutos.close();
		}
	}

	
	
	private static void alterarPrimeiro(Connection conex�o, String novoNome) throws Exception {
		PreparedStatement alterarProdutos = null;
		try {
			alterarProdutos = conex�o.prepareStatement("update produtos set nome = ? where id = (select MIN(id) from produtos)");
			alterarProdutos.setString(1, novoNome);
			alterarProdutos.execute();
		} finally {
			alterarProdutos.close();
		}
	}
	
	
	private static void deletar10Produtos(Connection conex�o) throws Exception {
		PreparedStatement deletarProdutos = null;
		try {
			deletarProdutos = conex�o.prepareStatement("delete from produtos where id = ?");
			for (int i = 1; i < 11; i++) {
				deletarProdutos.setInt(1, i);
				deletarProdutos.executeUpdate();
			}
		} finally {
			deletarProdutos.close();
		}
	}

	private static void inserirDoisMilProdutos(Connection conex�o) throws Exception {
		PreparedStatement inserirProdutos = null;
		try {
			inserirProdutos = conex�o.prepareStatement("insert into produtos (id, nome) values (?,?)");
			for (int contador = 1; contador <= 2000; contador++) {
				inserirProdutos.setLong(1, contador);
				inserirProdutos.setString(2, "Produto" + contador);
				inserirProdutos.execute();
			}
		} finally {
			inserirProdutos.close();
		}
	}

	private static void criarTabelaDeProdutos(Connection conex�o) throws Exception {
		Statement criarTabela = null;
		try {
			criarTabela = conex�o.createStatement();
			criarTabela.execute("create table if not exists produtos (" + "id long not null primary key,"
					+ "nome varchar(255) not null unique" + ")");
		} finally {
			criarTabela.close();
		}
	}


	private static Connection abrirConex�o() throws Exception {
		Connection c = DriverManager.getConnection("jdbc:h2:~/loja", "sa", "");
		return c;
	}

}