import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

//Vinnicius O. Rodrigues

public class Tp4 {
    public static void main(String[] args) throws Exception {
        Connection connection = DatabaseConnection.getConnectionToMySQL();

        try {
            createTables(connection);
        } catch (Exception e) {
        }

        JFrame frame = new JFrame("TP 4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 250);
        frame.setLayout(new GridLayout(2, 1));

        JLabel nomeLabel = new JLabel("Nome: ");
        JTextField nomeInput = new JTextField(20);
        JButton pesquisarButton = new JButton("Pesquisar");
        JPanel topRow = new JPanel(new FlowLayout());
        topRow.add(nomeLabel);
        topRow.add(nomeInput);
        topRow.add(pesquisarButton);

        JLabel nameLabel = new JLabel("Nome: ");
        JTextField nameInput = new JTextField(20);
        JLabel salarioLabel = new JLabel("Salário: ");
        JTextField salarioInput = new JTextField(20);
        JLabel cargoLabel = new JLabel("Cargo: ");
        JTextField cargoInput = new JTextField(20);
        JButton anteriorButton = new JButton("Anterior");
        JButton proximoButton = new JButton("Próximo");
        JPanel bottomRow = new JPanel(new GridLayout(4, 2));
        bottomRow.add(nameLabel);
        bottomRow.add(nameInput);
        bottomRow.add(salarioLabel);
        bottomRow.add(salarioInput);
        bottomRow.add(cargoLabel);
        bottomRow.add(cargoInput);
        bottomRow.add(anteriorButton);
        bottomRow.add(proximoButton);

        frame.add(topRow);
        frame.add(bottomRow);

        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeInput.getText();
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(
                            "SELECT cod_funcionario, nome_funcionario, salfuncionario, ds_cargo FROM tbfuncionarios INNER JOIN tbcargos ON tbfuncionarios.cod_cargo = tbcargos.cd_cargo WHERE nome_funcionario = '"
                                    + nome + "'");
                    if (resultSet.next()) {
                        nameInput.setText(resultSet.getString("nome_funcionario"));
                        salarioInput.setText(resultSet.getString("salfuncionario"));
                        cargoInput.setText(resultSet.getString("ds_cargo"));
                        currentRecord = resultSet.getInt("cod_funcionario");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Funcionário não encontrado");
                        nameInput.setText("");
                        salarioInput.setText("");
                        cargoInput.setText("");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        proximoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(
                            "SELECT cod_funcionario, nome_funcionario, salfuncionario, ds_cargo FROM tbfuncionarios INNER JOIN tbcargos ON tbfuncionarios.cod_cargo = tbcargos.cd_cargo WHERE cod_funcionario > "
                                    + currentRecord + " ORDER BY cod_funcionario ASC");
                    if (resultSet.next()) {
                        nameInput.setText(resultSet.getString("nome_funcionario"));
                        salarioInput.setText(resultSet.getString("salfuncionario"));
                        cargoInput.setText(resultSet.getString("ds_cargo"));
                        currentRecord = resultSet.getInt("cod_funcionario");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Não há mais registros");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        anteriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(
                            "SELECT cod_funcionario, nome_funcionario, salfuncionario, ds_cargo FROM tbfuncionarios INNER JOIN tbcargos ON tbfuncionarios.cod_cargo = tbcargos.cd_cargo WHERE cod_funcionario < "
                                    + currentRecord + " ORDER BY cod_funcionario DESC");
                    if (resultSet.next()) {
                        nameInput.setText(resultSet.getString("nome_funcionario"));
                        salarioInput.setText(resultSet.getString("salfuncionario"));
                        cargoInput.setText(resultSet.getString("ds_cargo"));
                        currentRecord = resultSet.getInt("cod_funcionario");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Não há mais registros");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.setVisible(true);
    }

    public static void createTables(Connection connection) throws Exception {
        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "CREATE TABLE tbcargos (cd_cargo SMALLINT NOT NULL, ds_cargo CHAR(20) NULL, PRIMARY KEY (cd_cargo))");
        statement.executeUpdate(
                "CREATE TABLE tbfuncionarios (cod_funcionario DECIMAL(9) NOT NULL, nome_funcionario CHAR(30) NULL, salfuncionario MONEY NULL, cod_cargo SMALLINT NULL, PRIMARY KEY (cod_funcionario), FOREIGN KEY (cod_cargo) REFERENCES tbcargos(cd_cargo))");
        statement.executeUpdate("INSERT INTO tbcargos (cd_cargo, ds_cargo) VALUES (1, 'Gerente')");
        statement.executeUpdate("INSERT INTO tbcargos (cd_cargo, ds_cargo) VALUES (2, 'Analista')");
        statement.executeUpdate("INSERT INTO tbcargos (cd_cargo, ds_cargo) VALUES (3, 'Programador')");

        statement.executeUpdate(
                "INSERT INTO tbfuncionarios (cod_funcionario, nome_funcionario, salfuncionario, cod_cargo) VALUES (1, 'João', 5000.00, 1)");
        statement.executeUpdate(
                "INSERT INTO tbfuncionarios (cod_funcionario, nome_funcionario, salfuncionario, cod_cargo) VALUES (2, 'Maria', 4000.00, 2)");
        statement.executeUpdate(
                "INSERT INTO tbfuncionarios (cod_funcionario, nome_funcionario, salfuncionario, cod_cargo) VALUES (3, 'Pedro', 3000.00, 3)");

        statement.close();
    }

    private static Integer currentRecord;
}
