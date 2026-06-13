import java.sql.*;

/**
 * Gerencia as operações de persistência (CRUD) para a entidade Contato.
 */
public class AgendaTelefonica {

    private static final String URL = "jdbc:mysql://localhost:3306/agenda_db";
    private static final String USER = "root";
    private static final String PASSWORD = "12345"; // Necessário configurar conforme o ambiente local

    /**
     * Estabelece conexão com o SGBD MySQL.
     */
    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Adiciona um novo registro de contato na base de dados.
     */
    public String adicionarContato(Contato contato) {
        String sql = "INSERT INTO contatos (nome, telefone, email) VALUES (?, ?, ?)";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getTelefone());
            stmt.setString(3, contato.getEmail());
            stmt.executeUpdate();
            return "✅ Contato '" + contato.getNome() + "' adicionado com sucesso.";
        } catch (SQLException e) {
            return "❌ Erro na operação de inserção: " + e.getMessage();
        }
    }

    /**
     * Remove um registro existente utilizando o nome como critério de busca.
     * Retorna alerta caso o contato seja inexistente.
     */
    public String removerContato(String nome) {
        String sql = "DELETE FROM contatos WHERE nome = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                return "✅ Contato '" + nome + "' removido com sucesso.";
            } else {
                return "⚠️ Operação inválida: Contato inexistente na base de dados.";
            }
        } catch (SQLException e) {
            return "❌ Erro na operação de remoção: " + e.getMessage();
        }
    }

    /**
     * Retorna os dados de um contato específico.
     */
    public String buscarContato(String nome) {
        String sql = "SELECT * FROM contatos WHERE nome = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return String.format("Nome: %s\nTelefone: %s\nE-mail: %s",
                        rs.getString("nome"), rs.getString("telefone"), rs.getString("email"));
            } else {
                return "⚠️ Operação inválida: Contato '" + nome + "' inexistente.";
            }
        } catch (SQLException e) {
            return "❌ Erro na operação de busca: " + e.getMessage();
        }
    }

    /**
     * Retorna uma lista formatada com todos os contatos persistidos.
     */
    public String listarContatos() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT * FROM contatos";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                sb.append(String.format("Nome: %s | Tel: %s | Email: %s\n",
                        rs.getString("nome"), rs.getString("telefone"), rs.getString("email")));
            }
        } catch (SQLException e) {
            return "❌ Erro na operação de listagem: " + e.getMessage();
        }

        return sb.length() == 0 ? "A agenda de contatos está vazia." : sb.toString();
    }

    /**
     * Método auxiliar para garantia do escopo completo do acrônimo CRUD.
     */
    public String atualizarContato(String nomeBusca, String novoTelefone, String novoEmail) {
        String sql = "UPDATE contatos SET telefone = ?, email = ? WHERE nome = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoTelefone);
            stmt.setString(2, novoEmail);
            stmt.setString(3, nomeBusca);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                return "✅ Contato '" + nomeBusca + "' atualizado com sucesso.";
            } else {
                return "⚠️ Operação inválida: Contato inexistente.";
            }
        } catch (SQLException e) {
            return "❌ Erro na operação de atualização: " + e.getMessage();
        }
    }
}
