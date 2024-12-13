/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fix_Code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import qlchxd.Infor_Bikes;
/**
 *
 * @author Admin
 */
public class Sort {
    private void Sorting(boolean ascending, JTable Data_Bikes) {
    String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
    String user = "sa"; 
    String password = "123456789"; 

    // Sử dụng ORDER BY trong SQL để sắp xếp theo giá bán
    String sql = "SELECT MaXe, TenXe, HangXe, LoaiXe, GiaBan, SoLuongTonKho FROM ThongTinXeDap ORDER BY GiaBan " 
                 + (ascending ? "ASC" : "DESC"); // ASC cho tăng dần, DESC cho giảm dần

    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        // Tạo mô hình cho JTable
        DefaultTableModel model = (DefaultTableModel) Data_Bikes.getModel();
        model.setRowCount(0); // Xóa hết dữ liệu cũ trong bảng

        // Thêm dữ liệu vào bảng
        while (rs.next()) {
            Object[] row = {
                rs.getString("MaXe"),
                rs.getString("TenXe"),
                rs.getString("HangXe"),
                rs.getString("LoaiXe"),
                rs.getFloat("GiaBan"),
                rs.getInt("SoLuongTonKho")
            };
            model.addRow(row);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
