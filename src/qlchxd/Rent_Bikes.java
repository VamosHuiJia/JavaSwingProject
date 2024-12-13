/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package qlchxd;

import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import Tools.CodeFetchData;
import Tools.CodeClearText;
import Tools.CodeDeleteButton;
import Tools.ShowInformation;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import Table.TableActionCellRender;
import Table.TableActionCellEditor;
import Table.TableActionEvents;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/**
 *
 * @author Admin
 */
public class Rent_Bikes extends javax.swing.JInternalFrame {

    private ShowInformation show_infor = new ShowInformation();
    /**
     * Creates new form Rent_Bikes
     */
    public Rent_Bikes() {
        initComponents();
        addSearchListener();
        
        TableActionEvents event = new TableActionEvents() {
            @Override
            public void onEdit(int row) {
                show_infor.ShowMenuInformation(Panel_ThongTin, Panel_ChucNang, Panel_DuLieu);
                InputInformation();

            }

            @Override
            public void onDelete(int row) {
                if (Data_Rents.isEditing()) {
                    Data_Rents.getCellEditor().stopCellEditing();
                }
                deleteRowFromDatabase(row);
                ClearText();
            }
            
            public void onAdd(int row) {
                show_infor.ShowMenuInformation(Panel_ThongTin, Panel_ChucNang, Panel_DuLieu);
                InputInformation();
                ClearText();
            }
        };
        
        Data_Rents.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        Data_Rents.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor(event)); 
        
        fetchData();

        Panel_ThongTin.setVisible(false);
        Panel_ChucNang.setVisible(false);
        Panel_DuLieu.setPreferredSize(new java.awt.Dimension(1000, 600));
    }
    
    
    //Xóa
    private void deleteRowFromDatabase(int row) {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa";
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            // Lấy giá trị ID từ dòng được chọn
            DefaultTableModel model = (DefaultTableModel) Data_Rents.getModel();
            String id = model.getValueAt(row, 0).toString(); // Lấy giá trị từ cột đầu tiên (giả sử là ID)

            String sql = "DELETE FROM ThueXe WHERE MaPhieuThue = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id); // Gán giá trị ID vào câu truy vấn

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Xóa thông tin thành công");
                model.removeRow(row); // Xóa dòng khỏi JTable
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu để xóa");
            }
            ClearText();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Xóa không thành công: " + e.getMessage());
        }
    }
    
    //Nhập
    public void InputInformation() {
        int i = Data_Rents.getSelectedRow();
        IDRent_Field.setText(Data_Rents.getValueAt(i, 0).toString());
        IDCus_Field.setText(Data_Rents.getValueAt(i, 1).toString());
        IDTrans_Field.setText(Data_Rents.getValueAt(i, 2).toString());
        PriceRent_Field.setText(Data_Rents.getValueAt(i, 4).toString());
        Time_Field.setText(Data_Rents.getValueAt(i, 5).toString());
        String dateStr = Data_Rents.getValueAt(i, 3).toString();
        try {
            // Định dạng ngày tháng (định dạng này có thể thay đổi theo định dạng của dữ liệu)
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = dateFormat.parse(dateStr);
            DateChooser.setDate(date); // Đặt giá trị cho DateChooser
            
            ClearText();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    //Xuất thông tin
    private void fetchData() {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; 
        String password = "123456789"; 

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "SELECT MaPhieuThue, MaKH, MaXe, NgayThue, GiaThue, TongThoiGianThue FROM ThueXe";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Tạo mô hình cho JTable
            DefaultTableModel model = (DefaultTableModel) Data_Rents.getModel();
            model.setRowCount(0); // Xóa hết dữ liệu cũ

            // Thêm dữ liệu vào JTable
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaPhieuThue"),
                    rs.getString("MaKH"),
                    rs.getString("MaXe"),
                    rs.getDate("NgayThue"),
                    rs.getFloat("GiaThue"),
                    rs.getString("TongThoiGianThue")
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    
    //Tìm kiếm
    private void getSanPham() {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; 
        String password = "123456789"; 

        String sql = "SELECT MaPhieuThue, MaKH, MaXe, NgayThue, GiaThue, TongThoiGianThue FROM ThueXe";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Tạo mô hình cho JTable
            DefaultTableModel model = (DefaultTableModel) Data_Rents.getModel();
            model.setRowCount(0); 

            // Thêm dữ liệu vào bảng
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaPhieuThue"),
                    rs.getString("MaKH"),
                    rs.getString("MaXe"),
                    rs.getDate("NgayThue"),
                    rs.getFloat("GiaThue"),
                    rs.getString("TongThoiGianThue")
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private void addSearchListener() {
        Search_field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleSearch();
            }

            private void handleSearch() {
                // Kiểm tra nếu trường tìm kiếm trống
                String keyword = Search_field.getText().trim();
                if (keyword.isEmpty()) {
                    // Nếu rỗng, hiển thị tất cả sản phẩm
                    getSanPham(); 
                } else {
                    // Tìm kiếm theo tiêu chí
                    SearchData();
                }
            }
        });
    }
    
    private void SearchData() {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; 
        String password = "123456789"; 
        
        String selectedCriteria = jComboBox1.getSelectedItem().toString(); // ComboBox chứa tiêu chí tìm kiếm
        String searchValue = Search_field.getText(); // Giá trị tìm kiếm

    // Map tiêu chí từ ComboBox sang cột cơ sở dữ liệu
        String sql = "SELECT MaPhieuThue, MaKH, MaXe, NgayThue, GiaThue, TongThoiGianThue FROM ThueXe WHERE ";        
        switch (selectedCriteria) {
            case "Tất cả":
                sql += "(MaPhieuThue LIKE N'%" + Search_field.getText() + "%' OR "
                + "MaKH LIKE N'%" + Search_field.getText() + "%' OR "
                + "MaXe LIKE N'%" + Search_field.getText() + "%' OR "
                + "NgayThue LIKE N'%" + Search_field.getText() + "%' OR "
                + "GiaThue LIKE N'%" + Search_field.getText() + "%' OR "
                + "TongThoiGianThue LIKE N'%" + Search_field.getText() + "%')";
                break;
                
            case "Mã phiếu":
                sql += "MaPhieuThue LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Mã KH":
                sql += "MaKH LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Mã xe":
                sql += "MaXe LIKE N'%" + Search_field.getText() + "%'";
                break;    
            case "Ngày thuê":
                sql += "NgayThue LIKE N'%" + Search_field.getText() + "%'";
                break;
       
        }

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Tạo mô hình cho JTable
            DefaultTableModel model = (DefaultTableModel) Data_Rents.getModel();
            model.setRowCount(0); // Xóa hết dữ liệu cũ

            // Thêm dữ liệu vào JTable
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaPhieuThue"),
                    rs.getString("MaKH"),
                    rs.getString("MaXe"),
                    rs.getDate("NgayThue"),
                    rs.getFloat("GiaThue"),
                    rs.getString("TongThoiGianThue")
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    //Thêm thông tin
    public void insertData() {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; // Thay bằng username của bạn
        String password = "123456789"; // Thay bằng password của bạn

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "INSERT INTO ThueXe (MaPhieuThue, MaKH, MaXe, NgayThue, GiaThue, TongThoiGianThue) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, IDRent_Field.getText()); 
            pstmt.setString(2, IDCus_Field.getText());
            pstmt.setString(3, IDTrans_Field.getText());

            // Chuyển đổi java.util.Date sang java.sql.Date
            pstmt.setDate(4, new java.sql.Date(DateChooser.getDate().getTime()));

            // Sử dụng getSelectedItem().toString() cho JComboBox
            pstmt.setFloat(5, Float.parseFloat(PriceRent_Field.getText())); 
            pstmt.setString(6, Time_Field.getText()); 

            pstmt.executeUpdate(); // Thực thi truy vấn
            fetchData(); // Cập nhật lại dữ liệu hiển thị

            ClearText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateData() {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; // Thay bằng username của bạn
        String password = "123456789"; // Thay bằng password của bạn

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "UPDATE ThueXe SET MaKH = ?, MaXe = ?, NgayThue = ?, GiaThue = ?, TongThoiGianThue = ? WHERE MaPhieuThue = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, IDCus_Field.getText());
            pstmt.setString(2, IDTrans_Field.getText());
            pstmt.setDate(3, new java.sql.Date(DateChooser.getDate().getTime()));
            pstmt.setFloat(4, Float.parseFloat(PriceRent_Field.getText())); 
            pstmt.setString(5, Time_Field.getText()); 
            pstmt.setString(6, IDRent_Field.getText()); 


            pstmt.executeUpdate(); // Thực thi truy vấn
            fetchData(); // Cập nhật lại dữ liệu hiển thị
            ClearText(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Panel_ThongTin = new javax.swing.JPanel();
        Email_Label = new javax.swing.JLabel();
        IDRent_Field = new javax.swing.JTextField();
        PhoneNum_Label = new javax.swing.JLabel();
        IDTrans_Field = new javax.swing.JTextField();
        IDCus_Field = new javax.swing.JTextField();
        Address_Label = new javax.swing.JLabel();
        NameCus_Label = new javax.swing.JLabel();
        PriceRent_Field = new javax.swing.JTextField();
        IDCus_Label = new javax.swing.JLabel();
        ThongTin = new javax.swing.JLabel();
        Time_Field = new javax.swing.JTextField();
        Address_Label1 = new javax.swing.JLabel();
        DateChooser = new com.toedter.calendar.JDateChooser();
        Panel_ChucNang = new javax.swing.JPanel();
        Exit_btn = new javax.swing.JButton();
        Update_btn = new javax.swing.JButton();
        ChucNang = new javax.swing.JLabel();
        Panel_DuLieu = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Data_Rents = new javax.swing.JTable();
        Search_Label = new javax.swing.JLabel();
        Search_field = new javax.swing.JTextField();
        Search_btn = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("QUẢN LÝ CHO THUÊ XE ĐẠP");

        Panel_ThongTin.setBackground(new java.awt.Color(0, 102, 102));
        Panel_ThongTin.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        Panel_ThongTin.setForeground(new java.awt.Color(255, 255, 255));
        Panel_ThongTin.setPreferredSize(new java.awt.Dimension(300, 600));

        Email_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Email_Label.setForeground(new java.awt.Color(255, 255, 255));
        Email_Label.setText("Nhập ngày thuê");

        IDRent_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDRent_FieldActionPerformed(evt);
            }
        });

        PhoneNum_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        PhoneNum_Label.setForeground(new java.awt.Color(255, 255, 255));
        PhoneNum_Label.setText("Nhập mã xe");

        IDTrans_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDTrans_FieldActionPerformed(evt);
            }
        });

        IDCus_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDCus_FieldActionPerformed(evt);
            }
        });

        Address_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Address_Label.setForeground(new java.awt.Color(255, 255, 255));
        Address_Label.setText("Nhập giá thuê");

        NameCus_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NameCus_Label.setForeground(new java.awt.Color(255, 255, 255));
        NameCus_Label.setText("Nhập mã KH");

        PriceRent_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PriceRent_FieldActionPerformed(evt);
            }
        });

        IDCus_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        IDCus_Label.setForeground(new java.awt.Color(255, 255, 255));
        IDCus_Label.setText("Nhập phiếu thuê");

        ThongTin.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        ThongTin.setForeground(new java.awt.Color(255, 255, 255));
        ThongTin.setText("Thông tin");

        Time_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Time_FieldActionPerformed(evt);
            }
        });

        Address_Label1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Address_Label1.setForeground(new java.awt.Color(255, 255, 255));
        Address_Label1.setText("Tổng thời gian");

        javax.swing.GroupLayout Panel_ThongTinLayout = new javax.swing.GroupLayout(Panel_ThongTin);
        Panel_ThongTin.setLayout(Panel_ThongTinLayout);
        Panel_ThongTinLayout.setHorizontalGroup(
            Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                                .addComponent(Address_Label1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addComponent(Time_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Address_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NameCus_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(PhoneNum_Label)
                                    .addComponent(IDCus_Label)
                                    .addComponent(Email_Label))
                                .addGap(18, 18, 18)
                                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(IDTrans_Field)
                                    .addComponent(IDCus_Field)
                                    .addComponent(IDRent_Field)
                                    .addComponent(PriceRent_Field, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                                    .addComponent(DateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        Panel_ThongTinLayout.setVerticalGroup(
            Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IDCus_Label)
                    .addComponent(IDRent_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NameCus_Label)
                    .addComponent(IDCus_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PhoneNum_Label)
                    .addComponent(IDTrans_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(DateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Email_Label))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Address_Label)
                    .addComponent(PriceRent_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Address_Label1)
                    .addComponent(Time_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Panel_ChucNang.setBackground(new java.awt.Color(0, 102, 102));
        Panel_ChucNang.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        Panel_ChucNang.setForeground(new java.awt.Color(255, 255, 255));
        Panel_ChucNang.setPreferredSize(new java.awt.Dimension(300, 100));

        Exit_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-exit-20.png"))); // NOI18N
        Exit_btn.setText("Hủy");
        Exit_btn.setPreferredSize(new java.awt.Dimension(80, 25));
        Exit_btn.setVerifyInputWhenFocusTarget(false);
        Exit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Exit_btnActionPerformed(evt);
            }
        });

        Update_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-upload-to-cloud-20.png"))); // NOI18N
        Update_btn.setText("Cập nhật");
        Update_btn.setPreferredSize(new java.awt.Dimension(80, 25));
        Update_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Update_btnActionPerformed(evt);
            }
        });

        ChucNang.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        ChucNang.setForeground(new java.awt.Color(255, 255, 255));
        ChucNang.setText("Chức năng");

        javax.swing.GroupLayout Panel_ChucNangLayout = new javax.swing.GroupLayout(Panel_ChucNang);
        Panel_ChucNang.setLayout(Panel_ChucNangLayout);
        Panel_ChucNangLayout.setHorizontalGroup(
            Panel_ChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ChucNangLayout.createSequentialGroup()
                .addGroup(Panel_ChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_ChucNangLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Panel_ChucNangLayout.createSequentialGroup()
                        .addContainerGap(47, Short.MAX_VALUE)
                        .addComponent(Update_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Exit_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        Panel_ChucNangLayout.setVerticalGroup(
            Panel_ChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_ChucNangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel_ChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Exit_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Update_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        Panel_DuLieu.setBackground(new java.awt.Color(0, 102, 102));
        Panel_DuLieu.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        Panel_DuLieu.setForeground(new java.awt.Color(255, 255, 255));
        Panel_DuLieu.setPreferredSize(new java.awt.Dimension(500, 600));
        Panel_DuLieu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel_DuLieuMouseClicked(evt);
            }
        });

        Data_Rents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã phiếu ", "Mã khách hàng", "Mã xe", "Ngày thuê", "Giá thuê", "Thời gian thuê", "Tùy chọn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Data_Rents.setRowHeight(50);
        Data_Rents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Data_RentsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Data_Rents);

        Search_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Search_Label.setForeground(new java.awt.Color(255, 255, 255));
        Search_Label.setText("Tìm kiếm");

        Search_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Search_fieldActionPerformed(evt);
            }
        });

        Search_btn.setText("Tìm kiếm");
        Search_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Search_btnActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Mã phiếu", "Mã KH", "Mã xe", "Ngày thuê" }));

        javax.swing.GroupLayout Panel_DuLieuLayout = new javax.swing.GroupLayout(Panel_DuLieu);
        Panel_DuLieu.setLayout(Panel_DuLieuLayout);
        Panel_DuLieuLayout.setHorizontalGroup(
            Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_DuLieuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE)
                    .addGroup(Panel_DuLieuLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Search_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Search_field, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Search_btn)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Panel_DuLieuLayout.setVerticalGroup(
            Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_DuLieuLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Search_Label)
                    .addComponent(Search_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Search_btn)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Panel_ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Panel_ChucNang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Panel_DuLieu, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Panel_DuLieu, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Panel_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Panel_ChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IDRent_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDRent_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDRent_FieldActionPerformed

    private void IDTrans_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDTrans_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDTrans_FieldActionPerformed

    private void IDCus_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDCus_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDCus_FieldActionPerformed

    private void PriceRent_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PriceRent_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PriceRent_FieldActionPerformed

    private void Exit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Exit_btnActionPerformed
        show_infor.ExitMenuInformation(Panel_ThongTin, Panel_ChucNang, Panel_DuLieu);
    }//GEN-LAST:event_Exit_btnActionPerformed

    private void Update_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Update_btnActionPerformed
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa";
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String checkSql = "SELECT COUNT(*) FROM ThongTinXeDap WHERE MaXe = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, IDRent_Field.getText()); // Kiểm tra MaXe

            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1); // Lấy số lượng kết quả

            if (count > 0) {
                // Nếu tồn tại, gọi hàm update
                updateData();
            } else {
                // Nếu không tồn tại, gọi hàm insert
                insertData();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi kiểm tra dữ liệu");
        }

        ClearText();
    }//GEN-LAST:event_Update_btnActionPerformed

    private void Data_RentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Data_RentsMouseClicked
        int i = Data_Rents.getSelectedRow();
        IDRent_Field.setText(Data_Rents.getValueAt(i, 0).toString());
        IDCus_Field.setText(Data_Rents.getValueAt(i, 1).toString());
        IDTrans_Field.setText(Data_Rents.getValueAt(i, 2).toString());
        PriceRent_Field.setText(Data_Rents.getValueAt(i, 4).toString());
        Time_Field.setText(Data_Rents.getValueAt(i, 5).toString());
        String dateStr = Data_Rents.getValueAt(i, 3).toString();
        try {
            // Định dạng ngày tháng (định dạng này có thể thay đổi theo định dạng của dữ liệu)
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = dateFormat.parse(dateStr);
            DateChooser.setDate(date); // Đặt giá trị cho DateChooser
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_Data_RentsMouseClicked

    public void ClearText() {
        IDRent_Field.setText("");
        IDCus_Field.setText("");
        IDTrans_Field.setText("");
        DateChooser.setDate(null);

        PriceRent_Field.setText("");
        Time_Field.setText("");
    }
    
    private void Search_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Search_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Search_fieldActionPerformed

    private void Search_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Search_btnActionPerformed
        SearchData();
    }//GEN-LAST:event_Search_btnActionPerformed

    private void Time_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Time_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Time_FieldActionPerformed

    private void Panel_DuLieuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel_DuLieuMouseClicked
//        boolean isVisible = Panel_ThongTin.isVisible() && Panel_ChucNang.isVisible();
//
//        // Đổi trạng thái hiển thị của các panel
//        Panel_ThongTin.setVisible(!isVisible);
//        Panel_ChucNang.setVisible(!isVisible);
//
//        // Điều chỉnh kích thước của Panel_DuLieu dựa trên trạng thái hiển thị
//        if (isVisible) {
//            Panel_DuLieu.setPreferredSize(new java.awt.Dimension(1000, 600)); // Phóng to 
//        } else {
//            Panel_DuLieu.setPreferredSize(new java.awt.Dimension(500, 600));  // Trả về 
//        }
//
//        // Làm mới lại layout
//        this.revalidate();
//        this.repaint();
    }//GEN-LAST:event_Panel_DuLieuMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Address_Label;
    private javax.swing.JLabel Address_Label1;
    private javax.swing.JLabel ChucNang;
    private javax.swing.JTable Data_Rents;
    private com.toedter.calendar.JDateChooser DateChooser;
    private javax.swing.JLabel Email_Label;
    private javax.swing.JButton Exit_btn;
    private javax.swing.JTextField IDCus_Field;
    private javax.swing.JLabel IDCus_Label;
    private javax.swing.JTextField IDRent_Field;
    private javax.swing.JTextField IDTrans_Field;
    private javax.swing.JLabel NameCus_Label;
    private javax.swing.JPanel Panel_ChucNang;
    private javax.swing.JPanel Panel_DuLieu;
    private javax.swing.JPanel Panel_ThongTin;
    private javax.swing.JLabel PhoneNum_Label;
    private javax.swing.JTextField PriceRent_Field;
    private javax.swing.JLabel Search_Label;
    private javax.swing.JButton Search_btn;
    private javax.swing.JTextField Search_field;
    private javax.swing.JLabel ThongTin;
    private javax.swing.JTextField Time_Field;
    private javax.swing.JButton Update_btn;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
