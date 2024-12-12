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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import Tools.CodeFetchData;
import Tools.CodeClearText;
import Tools.CodeDeleteButton;
import Tools.ShowInformation;
import Tools.ShowInformation;

import Table.TableActionCellRender;
import Table.TableActionCellEditor;
import Table.TableActionEvents;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/**
 *
 * @author Admin
 */
public class Employees extends javax.swing.JInternalFrame {

    CodeFetchData fetchData;
    CodeClearText clearText;

    private CodeFetchData fetcher = new CodeFetchData();
    private CodeClearText clear;
    private CodeDeleteButton delete;
    private ShowInformation show_infor;
    /**
     * Creates new form Employees
     */
    public Employees() {
        clearText = new CodeClearText();
        
        delete = new CodeDeleteButton(fetchData, clearText);
        show_infor = new ShowInformation();
        
        initComponents();
        addSearchListener();
        
        TableActionEvents event = new TableActionEvents() {
            @Override
            public void onEdit(int row) {
                int i = Data_Employee.getSelectedRow();
                IDEmploy_Field.setText(Data_Employee.getValueAt(i, 0).toString());
                NameEmploy_Field.setText(Data_Employee.getValueAt(i, 1).toString());
                Position_Field.setText(Data_Employee.getValueAt(i, 2).toString());
                PhoneNum_Field.setText(Data_Employee.getValueAt(i, 3).toString());
                Email_Field.setText(Data_Employee.getValueAt(i, 4).toString());

                show_infor.ShowMenuInformation(Panel_ThongTin, Panel_ChucNang, Panel_DuLieu);
            }

            @Override
            public void onDelete(int row) {
                if (Data_Employee.isEditing()) {
                    Data_Employee.getCellEditor().stopCellEditing();
                }
                delete.DeleteButtonEmployees(row, Data_Employee);
            }
            
            public void onAdd(int row) {
                show_infor.ShowMenuInformation(Panel_ThongTin, Panel_ChucNang, Panel_DuLieu);
                int i = Data_Employee.getSelectedRow();
                IDEmploy_Field.setText(Data_Employee.getValueAt(i, 0).toString());
                NameEmploy_Field.setText(Data_Employee.getValueAt(i, 1).toString());
                Position_Field.setText(Data_Employee.getValueAt(i, 2).toString());
                PhoneNum_Field.setText(Data_Employee.getValueAt(i, 3).toString());
                Email_Field.setText(Data_Employee.getValueAt(i, 4).toString());
            }
        };
        
        Data_Employee.getColumnModel().getColumn(5).setCellRenderer(new TableActionCellRender());
        Data_Employee.getColumnModel().getColumn(5).setCellEditor(new TableActionCellEditor(event)); 
        
        fetcher.fetchDataEmployees(Data_Employee);

        Panel_ThongTin.setVisible(false);
        Panel_ChucNang.setVisible(false);
        Panel_DuLieu.setPreferredSize(new java.awt.Dimension(1000, 600));
    }

    
    //Tìm kiếm
    private void getSanPham() {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; 
        String password = "123456789"; 

        String sql = "SELECT MaNV, HoTen, ChucVu, SoDienThoai, Email FROM NhanVien";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Tạo mô hình cho JTable
            DefaultTableModel model = (DefaultTableModel) Data_Employee.getModel();
            model.setRowCount(0); 

            // Thêm dữ liệu vào bảng
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
        String sql = "SELECT MaNV, HoTen, ChucVu, SoDienThoai, Email FROM NhanVien WHERE ";        
        switch (selectedCriteria) {
            case "Tất cả":
                sql += "(MaNV LIKE N'%" + Search_field.getText() + "%' OR "
                + "HoTen LIKE N'%" + Search_field.getText() + "%' OR "
                + "ChucVu LIKE N'%" + Search_field.getText() + "%' OR "
                + "SoDienThoai LIKE N'%" + Search_field.getText() + "%' OR "
                + "Email LIKE N'%" + Search_field.getText() + "%')";
                 break;
                
            case "Mã NV":
                sql += "MaNV LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Họ tên ":
                sql += "HoTen LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Chức vụ":
                sql += "ChucVu LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "SĐT":
                sql += "SoDienThoai LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Email":
                sql += "Email LIKE N'%" + Search_field.getText() + "%'";
                break;
        }

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Tạo mô hình cho JTable
            DefaultTableModel model = (DefaultTableModel) Data_Employee.getModel();
            model.setRowCount(0); // Xóa hết dữ liệu cũ

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
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Panel_ThongTin = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        IDEmploy_Field = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        Position_Field = new javax.swing.JTextField();
        NameEmploy_Field = new javax.swing.JTextField();
        PhoneNum_Field = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        Email_Field = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        Panel_DuLieu = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        Search_field = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Data_Employee = new javax.swing.JTable();
        jComboBox1 = new javax.swing.JComboBox<>();
        Panel_ChucNang = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        Exit_btn = new javax.swing.JButton();
        Update_btn = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("QUẢN LÝ NHÂN VIÊN");
        setPreferredSize(new java.awt.Dimension(1000, 600));

        Panel_ThongTin.setBackground(new java.awt.Color(0, 102, 102));
        Panel_ThongTin.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        Panel_ThongTin.setForeground(new java.awt.Color(255, 255, 255));
        Panel_ThongTin.setPreferredSize(new java.awt.Dimension(302, 600));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Nhập email");

        IDEmploy_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDEmploy_FieldActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Nhập chức vụ");

        Position_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Position_FieldActionPerformed(evt);
            }
        });

        NameEmploy_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NameEmploy_FieldActionPerformed(evt);
            }
        });

        PhoneNum_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PhoneNum_FieldActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Nhập SDT");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Nhập tên NV");

        Email_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Email_FieldActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Nhập mã NV");

        jLabel18.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Thông tin");

        javax.swing.GroupLayout Panel_ThongTinLayout = new javax.swing.GroupLayout(Panel_ThongTin);
        Panel_ThongTin.setLayout(Panel_ThongTinLayout);
        Panel_ThongTinLayout.setHorizontalGroup(
            Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                        .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel14))
                        .addGap(12, 12, 12)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PhoneNum_Field)
                    .addComponent(Position_Field)
                    .addComponent(NameEmploy_Field)
                    .addComponent(IDEmploy_Field)
                    .addComponent(Email_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );
        Panel_ThongTinLayout.setVerticalGroup(
            Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(IDEmploy_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(NameEmploy_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(Position_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PhoneNum_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Email_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Tìm kiếm");

        Search_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Search_fieldActionPerformed(evt);
            }
        });

        jButton15.setText("Tìm kiếm");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        Data_Employee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Họ tên", "Chức vụ", "Số điện thoại", "Email", "Tùy chọn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Data_Employee.setRowHeight(50);
        Data_Employee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Data_EmployeeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Data_Employee);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã NV", "Họ tên", "Chức vụ", "SĐT", "Email" }));
        jComboBox1.setPreferredSize(new java.awt.Dimension(79, 22));

        javax.swing.GroupLayout Panel_DuLieuLayout = new javax.swing.GroupLayout(Panel_DuLieu);
        Panel_DuLieu.setLayout(Panel_DuLieuLayout);
        Panel_DuLieuLayout.setHorizontalGroup(
            Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_DuLieuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_DuLieuLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Search_field, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton15)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE))
                .addContainerGap())
        );
        Panel_DuLieuLayout.setVerticalGroup(
            Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_DuLieuLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Search_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                .addContainerGap())
        );

        Panel_ChucNang.setBackground(new java.awt.Color(0, 102, 102));
        Panel_ChucNang.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        Panel_ChucNang.setForeground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Chức năng");

        Exit_btn.setText("Hủy");
        Exit_btn.setPreferredSize(new java.awt.Dimension(80, 25));
        Exit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Exit_btnActionPerformed(evt);
            }
        });

        Update_btn.setText("Cập nhật");
        Update_btn.setPreferredSize(new java.awt.Dimension(80, 25));
        Update_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Update_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel_ChucNangLayout = new javax.swing.GroupLayout(Panel_ChucNang);
        Panel_ChucNang.setLayout(Panel_ChucNangLayout);
        Panel_ChucNangLayout.setHorizontalGroup(
            Panel_ChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ChucNangLayout.createSequentialGroup()
                .addGroup(Panel_ChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_ChucNangLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Panel_ChucNangLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Update_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Exit_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Panel_ChucNangLayout.setVerticalGroup(
            Panel_ChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_ChucNangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_ChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Exit_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Update_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Panel_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                    .addComponent(Panel_ChucNang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Panel_DuLieu, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Panel_DuLieu, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Panel_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Panel_ChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Update_button
    public void insertData() {
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
                fetcher.fetchDataEmployees(Data_Employee); // Cập nhật lại bảng sau khi thêm

                ClearText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateData() {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; // Thay bằng username của bạn
        String password = "123456789"; // Thay bằng password của bạn

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "UPDATE NhanVien SET HoTen = ?, ChucVu = ?, SoDienThoai = ?, Email = ? WHERE MaNV = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, NameEmploy_Field.getText());              
            pstmt.setString(2, Position_Field.getText());                
            pstmt.setString(3,  PhoneNum_Field.getText());                
            pstmt.setString(4, Email_Field.getText()); 
            pstmt.setInt(5, Integer.parseInt( IDEmploy_Field.getText()));   

            pstmt.executeUpdate(); // Thực thi truy vấn
            fetcher.fetchDataEmployees(Data_Employee); // Cập nhật lại dữ liệu hiển thị
            ClearText();
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
    
    private void IDEmploy_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDEmploy_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDEmploy_FieldActionPerformed

    private void Position_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Position_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Position_FieldActionPerformed

    private void NameEmploy_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NameEmploy_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NameEmploy_FieldActionPerformed

    private void PhoneNum_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PhoneNum_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PhoneNum_FieldActionPerformed

    private void Email_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Email_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Email_FieldActionPerformed

    private void Search_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Search_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Search_fieldActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        SearchData();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void Data_EmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Data_EmployeeMouseClicked
        int i = Data_Employee.getSelectedRow();
        IDEmploy_Field.setText(Data_Employee.getValueAt(i, 0).toString());
        NameEmploy_Field.setText(Data_Employee.getValueAt(i, 1).toString());
        Position_Field.setText(Data_Employee.getValueAt(i, 2).toString());
        PhoneNum_Field.setText(Data_Employee.getValueAt(i, 3).toString());
        Email_Field.setText(Data_Employee.getValueAt(i, 4).toString());
    }//GEN-LAST:event_Data_EmployeeMouseClicked

    private void Update_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Update_btnActionPerformed
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa";
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String checkSql = "SELECT COUNT(*) FROM NhanVien WHERE MaNV = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, IDEmploy_Field.getText()); // Kiểm tra MaXe

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

    private void Exit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Exit_btnActionPerformed
        show_infor.ExitMenuInformation(Panel_ThongTin, Panel_ChucNang, Panel_DuLieu);
    }//GEN-LAST:event_Exit_btnActionPerformed

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

    public void ClearText() {
        IDEmploy_Field.setText("");
        NameEmploy_Field.setText("");
        Position_Field.setText("");
        PhoneNum_Field.setText("");
        Email_Field.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Data_Employee;
    private javax.swing.JTextField Email_Field;
    private javax.swing.JButton Exit_btn;
    private javax.swing.JTextField IDEmploy_Field;
    private javax.swing.JTextField NameEmploy_Field;
    private javax.swing.JPanel Panel_ChucNang;
    private javax.swing.JPanel Panel_DuLieu;
    private javax.swing.JPanel Panel_ThongTin;
    private javax.swing.JTextField PhoneNum_Field;
    private javax.swing.JTextField Position_Field;
    private javax.swing.JTextField Search_field;
    private javax.swing.JButton Update_btn;
    private javax.swing.JButton jButton15;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
