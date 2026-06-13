import javax.swing.*;
import java.awt.Color;
import java.awt.Font;

/**
 * Classe principal da aplicação. Inicializa a Interface Gráfica e aplica validações de entrada.
 */
public class AgendaTeste extends JFrame {
    private JTextField txtNome;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextArea areaResultado;
    private AgendaTelefonica agenda;

    public AgendaTeste() {
        this.agenda = new AgendaTelefonica();
        configurarJanela();
        inicializarComponentes();
        configurarAcoes();
    }

    private void configurarJanela() {
        setTitle("Agenda Telefônica - Projeto Integrador");
        setSize(400, 560);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
    }

    private void inicializarComponentes() {
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(20, 20, 60, 25);
        add(lblNome);
        txtNome = new JTextField();
        txtNome.setBounds(80, 20, 280, 25);
        add(txtNome);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setBounds(20, 60, 60, 25);
        add(lblTelefone);
        txtTelefone = new JTextField();
        txtTelefone.setBounds(80, 60, 280, 25);
        add(txtTelefone);

        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setBounds(20, 100, 60, 25);
        add(lblEmail);
        txtEmail = new JTextField();
        txtEmail.setBounds(80, 100, 280, 25);
        add(txtEmail);

        JLabel lblAviso = new JLabel("Instrução: Para Buscar ou Remover, preencha apenas o Nome.");
        lblAviso.setBounds(20, 135, 360, 20);
        lblAviso.setForeground(Color.RED);
        lblAviso.setFont(new Font("Arial", Font.BOLD, 11));
        add(lblAviso);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setBounds(20, 310, 340, 180);
        add(scroll);
    }

    private void configurarAcoes() {
        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.setBounds(20, 170, 170, 35);
        add(btnAdicionar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(200, 170, 160, 35);
        add(btnAtualizar);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(20, 215, 170, 35);
        add(btnBuscar);

        JButton btnRemover = new JButton("Remover");
        btnRemover.setBounds(200, 215, 160, 35);
        add(btnRemover);

        JButton btnListar = new JButton("Listar Contatos");
        btnListar.setBounds(20, 260, 170, 35);
        add(btnListar);

        JButton btnSair = new JButton("Sair do Programa");
        btnSair.setBounds(200, 260, 160, 35);
        add(btnSair);

        btnAdicionar.addActionListener(e -> processarAdicao());
        btnAtualizar.addActionListener(e -> processarAtualizacao());
        btnBuscar.addActionListener(e -> processarBusca());
        btnRemover.addActionListener(e -> processarRemocao());
        btnListar.addActionListener(e -> processarListagem());
        btnSair.addActionListener(e -> System.exit(0));
    }

    /**
     * Realiza a validação de formato via Expressões Regulares (Regex).
     */
    private boolean validarEntradas(String nome, String telefone, String email, boolean exigeTodos) {
        if (nome.isEmpty() || !nome.matches("^[a-zA-ZÀ-ÿ\\s']{2,100}$")) {
            mostrarErro("Nome inválido. Utilize apenas letras e possua no mínimo 2 caracteres.");
            return false;
        }

        if (exigeTodos) {
            if (telefone.isEmpty() || !telefone.matches("^[0-9\\(\\)\\-\\s]{8,20}$")) {
                mostrarErro("Telefone inválido. Utilize apenas números, espaços ou hifens (Mínimo 8 dígitos).");
                return false;
            }
            if (email.isEmpty() || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                mostrarErro("E-mail com formato inválido.");
                return false;
            }
        }
        return true;
    }

    private void processarAdicao() {
        String nome = txtNome.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();

        if (validarEntradas(nome, telefone, email, true)) {
            String resposta = agenda.adicionarContato(new Contato(nome, telefone, email));
            areaResultado.setText(resposta);
            limparCampos();
        }
    }

    private void processarAtualizacao() {
        String nome = txtNome.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();

        if (validarEntradas(nome, telefone, email, true)) {
            String resposta = agenda.atualizarContato(nome, telefone, email);
            areaResultado.setText(resposta);
            limparCampos();
        }
    }

    private void processarBusca() {
        String nome = txtNome.getText().trim();
        if (validarEntradas(nome, "", "", false)) {
            String resposta = agenda.buscarContato(nome);
            areaResultado.setText("🔍 Resultado:\n\n" + resposta);
        }
    }

    private void processarRemocao() {
        String nome = txtNome.getText().trim();
        if (validarEntradas(nome, "", "", false)) {
            String resposta = agenda.removerContato(nome);
            areaResultado.setText(resposta);
            limparCampos();
        }
    }

    private void processarListagem() {
        String resposta = agenda.listarContatos();
        areaResultado.setText("📋 Lista de Contatos:\n\n" + resposta);
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Falha de Validação", JOptionPane.ERROR_MESSAGE);
    }

    private void limparCampos() {
        txtNome.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AgendaTeste().setVisible(true));
    }
}
