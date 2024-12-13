/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author NAM
 */
public class CodeDeleteButton {
    private CodeFetchData fetchData;
    private CodeClearText clearText;

    // Constructor nhận vào CodeFetchData và CodeClearText
    public CodeDeleteButton(CodeFetchData fetchData, CodeClearText clearText) {
        this.fetchData = fetchData;
        this.clearText = clearText;
    }
    public void DeleteButtonAccountLogin(JTextField Acc_Field,JTable Data_Accounts){
    String ID = Acc_Field.getText();

        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa";
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "DELETE FROM DangNhap WHERE ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, ID);
                pstmt.executeUpdate();
                fetchData.fetchDataAccountLogin(Data_Accounts); // Cập nhật lại bảng sau khi xóa
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    public void DeleteButtonCustomers(int row, JTable Data_Customers){
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa";
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            // Lấy giá trị ID từ dòng được chọn
            DefaultTableModel model = (DefaultTableModel) Data_Customers.getModel();
            String id = model.getValueAt(row, 0).toString(); // Giả sử cột 0 là MaXe

            String sql = "DELETE FROM KhachHang WHERE MaKH = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Xóa thông tin thành công");
                model.removeRow(row); // Xóa dòng khỏi JTable
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy dữ liệu để xóa");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Xóa không thành công: " + e.getMessage());
        }
    }
    
    public void DeleteButtonEmployees(int row, JTable Data_Employee){
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa";
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            // Lấy giá trị ID từ dòng được chọn
            DefaultTableModel model = (DefaultTableModel) Data_Employee.getModel();
            String id = model.getValueAt(row, 0).toString(); // Giả sử cột 0 là MaXe

            String sql = "DELETE FROM NhanVien WHERE MaNV = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Xóa thông tin thành công");
                model.removeRow(row); // Xóa dòng khỏi JTable
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy dữ liệu để xóa");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Xóa không thành công: " + e.getMessage());
        }
    } 
    
    public void DeleteButtonInforBikes(int row, JTable Data_bikes){
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa";
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            // Lấy giá trị ID từ dòng được chọn
            DefaultTableModel model = (DefaultTableModel) Data_bikes.getModel();
            String id = model.getValueAt(row, 0).toString(); // Giả sử cột 0 là MaXe

            String sql = "DELETE FROM ThongTinXeDap WHERE MaXe = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Xóa thông tin thành công");
                model.removeRow(row); // Xóa dòng khỏi JTable
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy dữ liệu để xóa");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Xóa không thành công: " + e.getMessage());
        }
    }
}
