package forum07082020;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AppLoja {

	public static void main(String[] args) throws Exception {
		Connection conexão = null;
		try {
			conexão = abrirConexão();
			
			criarTabelaDeProdutos(conexão);
			
			inserirDoisMilProdutos(conexão);
			
			alterarNomeDoProduto(conexão, 1l, "Mouse");

			long quantidadeDeProdutosAntesDeDeletar = contarProdutos(conexão);

			System.out.println("Antes: " + quantidadeDeProdutosAntesDeDeletar);

			listarProdutos(conexão);

			deletar10Produtos(conexão);
			long quantidadeDeProdutosDepoisDeDeletar = contarProdutos(conexão);
			System.out.println("\nDepois de deletar 10: " + quantidadeDeProdutosDepoisDeDeletar);

			alterarPrimeiro(conexão,"Teclado");
			listarOs30Primeiros(conexão);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conexão.close();
		}
		System.out.println("Conectou.");
	}

	private static void listarProdutos(Connection conexão) throws Exception {
		Statement recuperarProdutos = null;
		try {
			recuperarProdutos = conexão.createStatement();
			ResultSet resultado = recuperarProdutos.executeQuery("select id, nome from produtos");
			System.out.println("Listando Produtos...");
			while (resultado.next()) {
				System.out.println("- " + resultado.getString("nome") + ", id=" + resultado.getLong("id"));
			}
			System.out.println("Listagem concluída.");
		} finally {
			recuperarProdutos.close();
		}
	}

	private static void listarOs30Primeiros(Connection conexão) throws Exception {
		Statement mostrar30produtos = null;

		try {
			mostrar30produtos = conexão.createStatement();
			ResultSet resultado = mostrar30produtos.executeQuery("select id, nome FROM produtos limit 0,30");
			System.out.println("\nLISTANDO OS 30 PROUDTOS");

			while (resultado.next()) {
				System.out.println("- " + resultado.getString("nome") + ", id=" + resultado.getLong("id"));
			}
			System.out.println("Listagem dos 30 concluída.\n");
		} finally {
			mostrar30produtos.close();
		}

	}

	private static long contarProdutos(Connection conexão) throws Exception {
		long quantidadeDeProdutos = 0;
		Statement contarProdutos = null;
		try {
			contarProdutos = conexão.createStatement();
			ResultSet resultado = contarProdutos.executeQuery("select count(*) as quantidade from produtos");
			if (resultado.next()) {
				quantidadeDeProdutos = resultado.getLong("quantidade");
			}
		} finally {
			contarProdutos.close();
		}
		return quantidadeDeProdutos;
	}

	private static void alterarNomeDoProduto(Connection conexão, Long id, String novoNome) throws Exception {
		PreparedStatement alterarProdutos = null;
		try {
			alterarProdutos = conexão.prepareStatement("update produtos set nome = ? where id = ?");
			alterarProdutos.setLong(2, id);
			alterarProdutos.setString(1, novoNome);
			alterarProdutos.execute();
		} finally {
			alterarProdutos.close();
		}
	}

	
	
	private static void alterarPrimeiro(Connection conexão, String novoNome) throws Exception {
		PreparedStatement alterarProdutos = null;
		try {
			alterarProdutos = conexão.prepareStatement("update produtos set nome = ? where id = (select MIN(id) from produtos)");
			alterarProdutos.setString(1, novoNome);
			alterarProdutos.execute();
		} finally {
			alterarProdutos.close();
		}
	}
	
	
	private static void deletar10Produtos(Connection conexão) throws Exception {
		PreparedStatement deletarProdutos = null;
		try {
			deletarProdutos = conexão.prepareStatement("delete from produtos where id = ?");
			for (int i = 1; i < 11; i++) {
				deletarProdutos.setInt(1, i);
				deletarProdutos.executeUpdate();
			}
		} finally {
			deletarProdutos.close();
		}
	}

	private static void inserirDoisMilProdutos(Connection conexão) throws Exception {
		PreparedStatement inserirProdutos = null;
		try {
			inserirProdutos = conexão.prepareStatement("insert into produtos (id, nome) values (?,?)");
			for (int contador = 1; contador <= 2000; contador++) {
				inserirProdutos.setLong(1, contador);
				inserirProdutos.setString(2, "Produto" + contador);
				inserirProdutos.execute();
			}
		} finally {
			inserirProdutos.close();
		}
	}

	private static void criarTabelaDeProdutos(Connection conexão) throws Exception {
		Statement criarTabela = null;
		try {
			criarTabela = conexão.createStatement();
			criarTabela.execute("create table if not exists produtos (" + "id long not null primary key,"
					+ "nome varchar(255) not null unique" + ")");
		} finally {
			criarTabela.close();
		}
	}


	private static Connection abrirConexão() throws Exception {
		Connection c = DriverManager.getConnection("jdbc:h2:~/loja", "sa", "");
		return c;
	}

}