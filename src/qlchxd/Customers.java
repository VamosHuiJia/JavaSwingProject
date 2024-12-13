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

import Table.TableActionCellRender;
import Table.TableActionCellEditor;
import Table.TableActionEvents;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/**
 *
 * @author Admin
 */
public class Customers extends javax.swing.JInternalFrame {

    CodeFetchData fetchData;
    CodeClearText clearText;
    

    private CodeFetchData fetcher = new CodeFetchData();
    private CodeClearText clear;
    private CodeDeleteButton delete;
    private ShowInformation show_infor;
    
    
    public static int _type = 1;
    
    public Customers() {
        clearText = new CodeClearText();
        
        delete = new CodeDeleteButton(fetchData, clearText);
        show_infor = new ShowInformation();
        
        initComponents();
        addSearchListener();
        
        TableActionEvents event = new TableActionEvents() {
            @Override
            public void onEdit(int row) {
                int i = Data_Customers.getSelectedRow();
                IDCus_field.setText(Data_Customers.getValueAt(i, 0).toString());
                NameCus_Field.setText(Data_Customers.getValueAt(i, 1).toString());
                PhoneNum_Field.setText(Data_Customers.getValueAt(i, 2).toString());
                Email_Field.setText(Data_Customers.getValueAt(i, 3).toString());
                Address_Field.setText(Data_Customers.getValueAt(i, 4).toString());

                show_infor.ShowMenuInformation(Panel_ThongTin, Panel_ChucNang, Panel_DuLieu);
            }

            @Override
            public void onDelete(int row) {
                if (Data_Customers.isEditing()) {
                    Data_Customers.getCellEditor().stopCellEditing();
                }
                delete.DeleteButtonCustomers(row, Data_Customers);
            }
            
            public void onAdd(int row) {
                show_infor.ShowMenuInformation(Panel_ThongTin, Panel_ChucNang, Panel_DuLieu);
                int i = Data_Customers.getSelectedRow();
                IDCus_field.setText(Data_Customers.getValueAt(i, 0).toString());
                NameCus_Field.setText(Data_Customers.getValueAt(i, 1).toString());
                PhoneNum_Field.setText(Data_Customers.getValueAt(i, 2).toString());
                Email_Field.setText(Data_Customers.getValueAt(i, 3).toString());
                Address_Field.setText(Data_Customers.getValueAt(i, 4).toString());
            }
        };
        
        Data_Customers.getColumnModel().getColumn(5).setCellRenderer(new TableActionCellRender());
        Data_Customers.getColumnModel().getColumn(5).setCellEditor(new TableActionCellEditor(event)); 
        
        fetcher.fetchDataCustomers(Data_Customers);

        Panel_ThongTin.setVisible(false);
        Panel_ChucNang.setVisible(false);
        Panel_DuLieu.setPreferredSize(new java.awt.Dimension(1000, 600));
    }

    
    //Tìm kiếm
    private void getSanPham() {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; 
        String password = "123456789"; 

        String sql = "SELECT MaKH, HoTen, SoDienThoai, Email, DiaChi FROM KhachHang";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Tạo mô hình cho JTable
            DefaultTableModel model = (DefaultTableModel) Data_Customers.getModel();
            model.setRowCount(0); 

            // Thêm dữ liệu vào bảng
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
        String sql = "SELECT MaKH, HoTen, SoDienThoai, Email, DiaChi FROM KhachHang WHERE ";        
        switch (selectedCriteria) {
            case "Tất cả":
                sql += "(MaKH LIKE N'%" + Search_field.getText() + "%' OR "
                    + "HoTen LIKE N'%" + Search_field.getText() + "%' OR "
                    + "SoDienThoai LIKE N'%" + Search_field.getText() + "%' OR "
                    + "Email LIKE N'%" + Search_field.getText() + "%' OR "
                    + "DiaChi LIKE N'%" + Search_field.getText() + "%')";

                 break;
                
            case "Mã KH":
                sql += "MaKH LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Họ tên":
                sql += "HoTen LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "SĐT":
                sql += "SoDienThoai LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Email":
                sql += "Email LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Địa chỉ":
                sql += "DiaChi LIKE N'%" + Search_field.getText() + "%'";
                break;
        }

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
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
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        Panel_DuLieu = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Data_Customers = new javax.swing.JTable();
        Search_Label = new javax.swing.JLabel();
        Search_field = new javax.swing.JTextField();
        Search_btn = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        Panel_ThongTin = new javax.swing.JPanel();
        Email_Label = new javax.swing.JLabel();
        IDCus_field = new javax.swing.JTextField();
        PhoneNum_Label = new javax.swing.JLabel();
        PhoneNum_Field = new javax.swing.JTextField();
        NameCus_Field = new javax.swing.JTextField();
        Email_Field = new javax.swing.JTextField();
        Address_Label = new javax.swing.JLabel();
        NameCus_Label = new javax.swing.JLabel();
        Address_Field = new javax.swing.JTextField();
        IDCus_Label = new javax.swing.JLabel();
        ThongTin = new javax.swing.JLabel();
        Panel_ChucNang = new javax.swing.JPanel();
        Exit_btn = new javax.swing.JButton();
        Update_btn = new javax.swing.JButton();
        ChucNang = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("THÔNG TIN KHÁCH HÀNG");
        setPreferredSize(new java.awt.Dimension(1000, 600));

        Panel_DuLieu.setBackground(new java.awt.Color(0, 102, 102));
        Panel_DuLieu.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        Panel_DuLieu.setForeground(new java.awt.Color(255, 255, 255));
        Panel_DuLieu.setPreferredSize(new java.awt.Dimension(500, 600));
        Panel_DuLieu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel_DuLieuMouseClicked(evt);
            }
        });

        Data_Customers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã KH", "Họ tên", "Số điện thoại", "Email", "Địa chỉ", "Tùy chọn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Data_Customers.setRowHeight(50);
        Data_Customers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Data_CustomersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Data_Customers);

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

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Mã KH", "Họ tên", "SĐT", "Email", "Địa chỉ" }));

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
                .addGap(22, 22, 22)
                .addGroup(Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Search_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Search_btn)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Search_Label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                .addContainerGap())
        );

        Panel_ThongTin.setBackground(new java.awt.Color(0, 102, 102));
        Panel_ThongTin.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        Panel_ThongTin.setForeground(new java.awt.Color(255, 255, 255));
        Panel_ThongTin.setPreferredSize(new java.awt.Dimension(300, 600));

        Email_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Email_Label.setForeground(new java.awt.Color(255, 255, 255));
        Email_Label.setText("Nhập email");

        IDCus_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDCus_fieldActionPerformed(evt);
            }
        });

        PhoneNum_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        PhoneNum_Label.setForeground(new java.awt.Color(255, 255, 255));
        PhoneNum_Label.setText("Nhập SDT");

        PhoneNum_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PhoneNum_FieldActionPerformed(evt);
            }
        });

        NameCus_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NameCus_FieldActionPerformed(evt);
            }
        });

        Email_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Email_FieldActionPerformed(evt);
            }
        });

        Address_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Address_Label.setForeground(new java.awt.Color(255, 255, 255));
        Address_Label.setText("Nhập dia chi");

        NameCus_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NameCus_Label.setForeground(new java.awt.Color(255, 255, 255));
        NameCus_Label.setText("Nhập tên KH");

        Address_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Address_FieldActionPerformed(evt);
            }
        });

        IDCus_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        IDCus_Label.setForeground(new java.awt.Color(255, 255, 255));
        IDCus_Label.setText("Nhập mã KH");

        ThongTin.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        ThongTin.setForeground(new java.awt.Color(255, 255, 255));
        ThongTin.setText("Thông tin");

        javax.swing.GroupLayout Panel_ThongTinLayout = new javax.swing.GroupLayout(Panel_ThongTin);
        Panel_ThongTin.setLayout(Panel_ThongTinLayout);
        Panel_ThongTinLayout.setHorizontalGroup(
            Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Address_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(IDCus_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NameCus_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Email_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PhoneNum_Label))
                        .addGap(18, 18, 18)
                        .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Email_Field)
                            .addComponent(PhoneNum_Field)
                            .addComponent(NameCus_Field)
                            .addComponent(IDCus_field)
                            .addComponent(Address_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        Panel_ThongTinLayout.setVerticalGroup(
            Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IDCus_Label)
                    .addComponent(IDCus_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NameCus_Label)
                    .addComponent(NameCus_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PhoneNum_Label)
                    .addComponent(PhoneNum_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Email_Label)
                    .addComponent(Email_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Address_Label)
                    .addComponent(Address_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Panel_ChucNang.setBackground(new java.awt.Color(0, 102, 102));
        Panel_ChucNang.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        Panel_ChucNang.setForeground(new java.awt.Color(255, 255, 255));
        Panel_ChucNang.setPreferredSize(new java.awt.Dimension(300, 100));

        Exit_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-exit-20.png"))); // NOI18N
        Exit_btn.setText("Hủy");
        Exit_btn.setPreferredSize(new java.awt.Dimension(80, 25));
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
                .addContainerGap()
                .addComponent(ChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(175, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_ChucNangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Update_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Exit_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Panel_ChucNangLayout.setVerticalGroup(
            Panel_ChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_ChucNangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel_ChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Update_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Exit_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
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

    //Update_Button
    
    public void insertData() {
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
                fetcher.fetchDataCustomers(Data_Customers); // Cập nhật lại bảng sau khi thêm
                ClearText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }    
    } 
    
    public void updateData() {
        String maKH = IDCus_field.getText();
        String tenKH = NameCus_Field.getText();
        String soDienThoai = PhoneNum_Field.getText();
        String email = Email_Field.getText();
        String diaChi = Address_Field.getText();

        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa";
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "UPDATE KhachHang SET HoTen = ?, SoDienThoai = ?, Email = ?, DiaChi = ? WHERE MaKH = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, tenKH);
                pstmt.setString(2, soDienThoai);
                pstmt.setString(3, email);
                pstmt.setString(4, diaChi);
                pstmt.setString(5, maKH);
                pstmt.executeUpdate();
                fetcher.fetchDataCustomers(Data_Customers); // Cập nhật lại bảng sau khi cập nhật

                ClearText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void Update_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Update_btnActionPerformed
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa";
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String checkSql = "SELECT COUNT(*) FROM KhachHang WHERE MaKH = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, IDCus_field.getText()); // Kiểm tra MaXe

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

    private void Search_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Search_btnActionPerformed
        SearchData();
    }//GEN-LAST:event_Search_btnActionPerformed

    private void Search_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Search_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Search_fieldActionPerformed

    private void Data_CustomersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Data_CustomersMouseClicked
        int i = Data_Customers.getSelectedRow();
        IDCus_field.setText(Data_Customers.getValueAt(i, 0).toString());
        NameCus_Field.setText(Data_Customers.getValueAt(i, 1).toString());
        PhoneNum_Field.setText(Data_Customers.getValueAt(i, 2).toString());
        Email_Field.setText(Data_Customers.getValueAt(i, 3).toString());
        Address_Field.setText(Data_Customers.getValueAt(i, 4).toString());
    }//GEN-LAST:event_Data_CustomersMouseClicked

    private void Address_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Address_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Address_FieldActionPerformed

    private void Email_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Email_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Email_FieldActionPerformed

    private void NameCus_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NameCus_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NameCus_FieldActionPerformed

    private void PhoneNum_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PhoneNum_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PhoneNum_FieldActionPerformed

    private void IDCus_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDCus_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDCus_fieldActionPerformed

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
        IDCus_field.setText("");
        NameCus_Field.setText("");
        PhoneNum_Field.setText("");
        Email_Field.setText("");
        Address_Field.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Address_Field;
    private javax.swing.JLabel Address_Label;
    private javax.swing.JLabel ChucNang;
    private javax.swing.JTable Data_Customers;
    private javax.swing.JTextField Email_Field;
    private javax.swing.JLabel Email_Label;
    private javax.swing.JButton Exit_btn;
    private javax.swing.JLabel IDCus_Label;
    private javax.swing.JTextField IDCus_field;
    private javax.swing.JTextField NameCus_Field;
    private javax.swing.JLabel NameCus_Label;
    private javax.swing.JPanel Panel_ChucNang;
    private javax.swing.JPanel Panel_DuLieu;
    private javax.swing.JPanel Panel_ThongTin;
    private javax.swing.JTextField PhoneNum_Field;
    private javax.swing.JLabel PhoneNum_Label;
    private javax.swing.JLabel Search_Label;
    private javax.swing.JButton Search_btn;
    private javax.swing.JTextField Search_field;
    private javax.swing.JLabel ThongTin;
    private javax.swing.JButton Update_btn;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
