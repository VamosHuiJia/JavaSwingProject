/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author NAM
 */
public class CodeAddButton {
    private CodeFetchData fetchData;
    private CodeClearText clearText;

    // Constructor nhận vào CodeFetchData và CodeClearText
    public CodeAddButton(CodeFetchData fetchData, CodeClearText clearText) {
        this.fetchData = fetchData;
        this.clearText = clearText;
    }
    public void AddButtonAccountLogin(JTextField Name_Field,JTextField PhoneNum_Field,JTextField Acc_Field,JTextField Pass_Field,JComboBox ComboBox_Position,JTable Data_Accounts){
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; // Thay bằng username của bạn
        String password = "123456789"; // Thay bằng password của bạn

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "INSERT INTO DangNhap (HoTen, SoDienThoai, ID, PASS, ROLE) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, Name_Field.getText()); 
            pstmt.setString(2, PhoneNum_Field.getText()); 
            pstmt.setString(3, Acc_Field.getText()); 
            pstmt.setString(4, Pass_Field.getText()); 
  
            pstmt.setString(5, ComboBox_Position.getSelectedItem().toString()); 
           
            pstmt.executeUpdate(); // Thực thi truy vấn
            fetchData.fetchDataAccountLogin(Data_Accounts); // Cập nhật lại dữ liệu hiển thị

            clearText.ClearTextAccountLogin(Name_Field,PhoneNum_Field,Acc_Field,Pass_Field);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void AddButtonCustomers(JTextField IDCus_field,JTextField NameCus_Field,JTextField PhoneNum_Field,JTextField Email_Field,JTextField Address_Field ,JComboBox ComboBox_Position,JTable Data_Customers){
        // Thêm khách hàng
        String maKH = IDCus_field.getText();
        String tenKH = NameCus_Field.getText();
        String soDienThoai = PhoneNum_Field.getText();
        String email = Email_Field.getText();
        String diaChi = Address_Field.getText();

        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa";
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "INSERT INTO KhachHang (MaKH, HoTen, SoDienThoai, Email, DiaChi) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maKH);
                pstmt.setString(2, tenKH);
                pstmt.setString(3, soDienThoai);
                pstmt.setString(4, email);
                pstmt.setString(5, diaChi);
                
                pstmt.executeUpdate();
                fetchData.fetchDataCustomers(Data_Customers); // Cập nhật lại bảng sau khi cập nhật

                clearText.ClearTextCustomers(IDCus_field,NameCus_Field,PhoneNum_Field,Email_Field,Address_Field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }    
    
    }
    
    
    public void AddButtonEmployees(JTextField IDEmploy_Field,JTextField NameEmploy_Field,JTextField Position_Field,JTextField PhoneNum_Field,JTextField Email_Field,JTable Data_Employee){
        String maNV = IDEmploy_Field.getText();
        String hoTen = NameEmploy_Field.getText();
        String chucVu = Position_Field.getText();
        String SoDienThoai = PhoneNum_Field.getText();
        String Email = Email_Field.getText();

        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; 
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "INSERT INTO NhanVien (MaNV, HoTen, ChucVu, SoDienThoai, Email) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maNV);
                pstmt.setString(2, hoTen);
                pstmt.setString(3, chucVu );
                pstmt.setString(4, SoDienThoai);
                pstmt.setString(5, Email);
                pstmt.executeUpdate();
                fetchData.fetchDataEmployees(Data_Employee); // Cập nhật lại bảng sau khi thêm
                clearText.ClearTextEmployees(IDEmploy_Field,NameEmploy_Field,Position_Field,PhoneNum_Field, Email_Field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void AddButtonInforBikes(JTextField ID_Trans,JTextField Name_Trans,JTextField Brand_Trans,JTextField Style_Trans,JTextField Price_Trans,JTextField quantity_Trans,JTable Data_bikes){
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; 
        String password = "123456789"; 

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "INSERT INTO ThongTinXeDap (MaXe, TenXe, HangXe, LoaiXe, GiaBan, SoLuongTonKho) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ID_Trans.getText());  // MaXe
            pstmt.setString(2, Name_Trans.getText());                // TenXe
            pstmt.setString(3, Brand_Trans.getText());                // HangXe
            pstmt.setString(4, Style_Trans.getText());                // LoaiXe
            pstmt.setFloat(5, Float.parseFloat(Price_Trans.getText())); // GiaBan
            pstmt.setInt(6, Integer.parseInt(quantity_Trans.getText()));  // SoLuongTonKho

            pstmt.executeUpdate(); 
            fetchData.fetchDataInforBikes(Data_bikes); 
            JOptionPane.showMessageDialog(null, "Thêm thông tin thành công");
            clearText.ClearTextInforBikes(ID_Trans, Name_Trans, Brand_Trans, Style_Trans, Price_Trans, quantity_Trans);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Không thêm được thông tin");
        }
    }
}
