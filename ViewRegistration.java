import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewRegistrations extends JFrame {
    JTable table;
    DefaultTableModel model;
    JButton refreshBtn, deleteBtn, exitBtn;

    public ViewRegistrations() {
        setTitle("All Registrations");
        setSize(700, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone", "Event", "College"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        refreshBtn = new JButton("Refresh");
        deleteBtn = new JButton("Delete Selected");
        exitBtn = new JButton("Exit");

        panel.add(refreshBtn);
        panel.add(deleteBtn);
        panel.add(exitBtn);
        add(panel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadData());
        deleteBtn.addActionListener(e -> deleteSelected());
        exitBtn.addActionListener(e -> dispose());

        loadData();
        setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM registrations")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("event_name"),
                        rs.getString("college")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error loading data: " + ex.getMessage());
        }
    }

    private void deleteSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM registrations WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Record Deleted!");
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error deleting record: " + ex.getMessage());
        }
    }
}
