/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author NAM
 */
public class CodeFetchData {
    public void fetchDataAccountLogin(JTable Data_Accounts) {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; // Thay bằng username của bạn
        String password = "123456789"; // Thay bằng password của bạn

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "SELECT HoTen, SoDienThoai, ID, PASS, ROLE FROM DangNhap";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Tạo mô hình cho JTable
            DefaultTableModel model = (DefaultTableModel) Data_Accounts.getModel();
            model.setRowCount(0); // Xóa hết dữ liệu cũ

            // Thêm dữ liệu vào JTable
            while (rs.next()) {
                Object[] row = {
                    rs.getString("HoTen"), 
                    rs.getString("SoDienThoai"),
                    rs.getString("ID"), 
                    rs.getString("PASS"),
                    rs.getString("ROLE"),           
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void fetchDataCustomers(JTable Data_Customers) {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; // Thay bằng username của bạn
        String password = "123456789"; // Thay bằng password của bạn

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "SELECT MaKH, HoTen, SoDienThoai, Email, DiaChi FROM KhachHang";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Tạo mô hình cho JTable
            DefaultTableModel model = (DefaultTableModel) Data_Customers.getModel();
            model.setRowCount(0); // Xóa hết dữ liệu cũ

            // Thêm dữ liệu vào JTable
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaKH"), 
                    rs.getString("HoTen"),
                    rs.getString("SoDienThoai"),
                    rs.getString("Email"),
                    rs.getString("DiaChi"),
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    public void fetchDataEmployees(JTable Data_Employee) {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; 
        String password = "123456789"; 

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "SELECT MaNV, HoTen, ChucVu, SoDienThoai, Email FROM NhanVien";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Tạo mô hình cho JTable
            DefaultTableModel model = (DefaultTableModel) Data_Employee.getModel();
            model.setRowCount(0); 

            // Thêm dữ liệu vào JTable
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaNV"), 
                    rs.getString("HoTen"),
                    rs.getString("ChucVu"),
                    rs.getString("SoDienThoai"),
                    rs.getString("Email"),
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    public void fetchDataInforBikes(JTable Data_Bikes) {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; 
        String password = "123456789"; 

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "SELECT MaXe, TenXe, HangXe, LoaiXe, GiaBan, SoLuongTonKho FROM ThongTinXeDap ";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

           
            DefaultTableModel model = (DefaultTableModel) Data_Bikes.getModel();
            model.setRowCount(0); 

         
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
