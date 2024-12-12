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
public class Infor_Bikes extends javax.swing.JInternalFrame {
    CodeFetchData fetchData;
    CodeClearText clearText;
    

    private CodeFetchData fetcher = new CodeFetchData();
    private CodeClearText clear;
    private CodeDeleteButton delete;
    private ShowInformation show_infor;
    
    public static int _type = 1;

    public Infor_Bikes() {
        clearText = new CodeClearText();
        
        delete = new CodeDeleteButton(fetchData, clearText);
        show_infor = new ShowInformation();
        
        initComponents();
        addSearchListener();
        
        TableActionEvents event = new TableActionEvents() {
            @Override
            public void onEdit(int row) {
                int i = Data_Bikes.getSelectedRow();
                ID_Trans.setText(Data_Bikes.getValueAt(i, 0).toString());
                Name_Trans.setText(Data_Bikes.getValueAt(i, 1).toString());
                Brand_Trans.setText(Data_Bikes.getValueAt(i, 2).toString());
                Style_Trans.setText(Data_Bikes.getValueAt(i, 3).toString());
                Price_Trans.setText(Data_Bikes.getValueAt(i, 4).toString());
                quantity_Trans.setText(Data_Bikes.getValueAt(i, 5).toString());

                show_infor.ShowMenuInformation(Panel_ThongTin, Panel_ChucNang, Panel_DuLieu);
            }

            @Override
            public void onDelete(int row) {
                if (Data_Bikes.isEditing()) {
                    Data_Bikes.getCellEditor().stopCellEditing();
                }
                delete.DeleteButtonInforBikes(row, Data_Bikes);
            }
            
            public void onAdd(int row) {
                show_infor.ShowMenuInformation(Panel_ThongTin, Panel_ChucNang, Panel_DuLieu);
                int i = Data_Bikes.getSelectedRow();
                ID_Trans.setText(Data_Bikes.getValueAt(i, 0).toString());
                Name_Trans.setText(Data_Bikes.getValueAt(i, 1).toString());
                Brand_Trans.setText(Data_Bikes.getValueAt(i, 2).toString());
                Style_Trans.setText(Data_Bikes.getValueAt(i, 3).toString());
                Price_Trans.setText(Data_Bikes.getValueAt(i, 4).toString());
                quantity_Trans.setText(Data_Bikes.getValueAt(i, 5).toString());
            }
        };
        
        Data_Bikes.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        Data_Bikes.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor(event)); 
        
        fetcher.fetchDataInforBikes(Data_Bikes);

        Panel_ThongTin.setVisible(false);
        Panel_ChucNang.setVisible(false);
        Panel_DuLieu.setPreferredSize(new java.awt.Dimension(1000, 600));
    }

    private void getSanPham() {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; 
        String password = "123456789"; 

        String sql = "SELECT MaXe, TenXe, HangXe, LoaiXe, GiaBan, SoLuongTonKho FROM ThongTinXeDap";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Tạo mô hình cho JTable
            DefaultTableModel model = (DefaultTableModel) Data_Bikes.getModel();
            model.setRowCount(0); 

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
                    SearchDataByBrand();
                }
            }
        });
    }
    
    private void SearchDataByBrand() {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; 
        String password = "123456789"; 
        
        String selectedCriteria = jComboBox1.getSelectedItem().toString(); // ComboBox chứa tiêu chí tìm kiếm
        String searchValue = Search_field.getText(); // Giá trị tìm kiếm

    // Map tiêu chí từ ComboBox sang cột cơ sở dữ liệu
        String sql = "SELECT MaXe, TenXe, HangXe, LoaiXe, GiaBan, SoLuongTonKho FROM ThongTinXeDap WHERE ";        
        switch (selectedCriteria) {
            case "Tất cả":
                sql += "(MaXe LIKE N'%" + Search_field.getText() + "%' OR "
                + "TenXe LIKE N'%" + Search_field.getText() + "%' OR "
                + "HangXe LIKE N'%" + Search_field.getText() + "%' OR "
                + "LoaiXe LIKE N'%" + Search_field.getText() + "%')";
                 break;
                
            case "Mã xe":
                sql += "MaXe LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Tên xe":
                sql += "TenXe LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Hãng xe":
                sql += "HangXe LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Loại xe":
                sql += "LoaiXe LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Giá bán":
                sql += "GiaBan LIKE N'%" + Search_field.getText() + "%'";
                break;
            default:
                sql += "SoLuongTonKho LIKE N'%" + Search_field.getText() + "%' OR TenXe LIKE N'%" + Search_field.getText() + "%' OR ...";

        }

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Tạo mô hình cho JTable
            DefaultTableModel model = (DefaultTableModel) Data_Bikes.getModel();
            model.setRowCount(0); // Xóa hết dữ liệu cũ

            // Thêm dữ liệu vào JTable
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Panel_ThongTin = new javax.swing.JPanel();
        Label_Style = new javax.swing.JLabel();
        ID_Trans = new javax.swing.JTextField();
        Label_Brand = new javax.swing.JLabel();
        Brand_Trans = new javax.swing.JTextField();
        Name_Trans = new javax.swing.JTextField();
        Style_Trans = new javax.swing.JTextField();
        Label_Price = new javax.swing.JLabel();
        Label_Name = new javax.swing.JLabel();
        Price_Trans = new javax.swing.JTextField();
        Label_ID = new javax.swing.JLabel();
        ThongTin = new javax.swing.JLabel();
        Lable_quantity = new javax.swing.JLabel();
        quantity_Trans = new javax.swing.JTextField();
        Panel_DuLieu = new javax.swing.JPanel();
        Label_TimKiem = new javax.swing.JLabel();
        Search_field = new javax.swing.JTextField();
        Search_btn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Data_Bikes = new javax.swing.JTable();
        jComboBox1 = new javax.swing.JComboBox<>();
        Panel_ChucNang = new javax.swing.JPanel();
        ChucNang = new javax.swing.JLabel();
        Update_btn = new javax.swing.JButton();
        Exit_btn = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("THÔNG TIN XE ĐẠP");
        setToolTipText("");
        setPreferredSize(new java.awt.Dimension(1000, 600));
        setVisible(true);

        Panel_ThongTin.setBackground(new java.awt.Color(0, 102, 102));
        Panel_ThongTin.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        Panel_ThongTin.setForeground(new java.awt.Color(255, 255, 255));
        Panel_ThongTin.setPreferredSize(new java.awt.Dimension(300, 600));

        Label_Style.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Label_Style.setForeground(new java.awt.Color(255, 255, 255));
        Label_Style.setText("Nhập loại xe");

        ID_Trans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ID_TransActionPerformed(evt);
            }
        });

        Label_Brand.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Label_Brand.setForeground(new java.awt.Color(255, 255, 255));
        Label_Brand.setText("Nhập hãng xe");

        Brand_Trans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Brand_TransActionPerformed(evt);
            }
        });

        Name_Trans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Name_TransActionPerformed(evt);
            }
        });

        Style_Trans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Style_TransActionPerformed(evt);
            }
        });

        Label_Price.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Label_Price.setForeground(new java.awt.Color(255, 255, 255));
        Label_Price.setText("Nhập giá bán");

        Label_Name.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Label_Name.setForeground(new java.awt.Color(255, 255, 255));
        Label_Name.setText("Nhập tên xe");

        Price_Trans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Price_TransActionPerformed(evt);
            }
        });

        Label_ID.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Label_ID.setForeground(new java.awt.Color(255, 255, 255));
        Label_ID.setText("Nhập mã xe");

        ThongTin.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        ThongTin.setForeground(new java.awt.Color(255, 255, 255));
        ThongTin.setText("Thông tin");

        Lable_quantity.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Lable_quantity.setForeground(new java.awt.Color(255, 255, 255));
        Lable_quantity.setText("Nhập số lượng");

        quantity_Trans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantity_TransActionPerformed(evt);
            }
        });

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
                        .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Label_Price, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(Label_Brand, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addComponent(Label_Style, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(Lable_quantity))
                        .addGap(18, 18, 18)
                        .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Price_Trans)
                            .addComponent(Style_Trans)
                            .addComponent(Brand_Trans)
                            .addComponent(Name_Trans)
                            .addComponent(quantity_Trans, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                            .addComponent(ID_Trans))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        Panel_ThongTinLayout.setVerticalGroup(
            Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Label_ID)
                    .addComponent(ID_Trans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Label_Name)
                    .addComponent(Name_Trans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Label_Brand)
                    .addComponent(Brand_Trans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Label_Style)
                    .addComponent(Style_Trans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Label_Price)
                    .addComponent(Price_Trans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Lable_quantity)
                    .addComponent(quantity_Trans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        Label_TimKiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Label_TimKiem.setForeground(new java.awt.Color(255, 255, 255));
        Label_TimKiem.setText("Tìm kiếm");

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

        Data_Bikes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã xe", "Tên xe", "Hãng xe", "Loại xe", "Giá bán", "Tồn kho", "Tùy chọn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Integer.class, java.lang.Object.class
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
        Data_Bikes.setRowHeight(40);
        Data_Bikes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Data_BikesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Data_Bikes);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Mã xe", "Tên xe", "Hãng xe", "Loại xe" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel_DuLieuLayout = new javax.swing.GroupLayout(Panel_DuLieu);
        Panel_DuLieu.setLayout(Panel_DuLieuLayout);
        Panel_DuLieuLayout.setHorizontalGroup(
            Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_DuLieuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(Panel_DuLieuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Search_field, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Search_btn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Panel_DuLieuLayout.setVerticalGroup(
            Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_DuLieuLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Label_TimKiem)
                    .addComponent(Search_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Search_btn)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                .addContainerGap())
        );

        Panel_ChucNang.setBackground(new java.awt.Color(0, 102, 102));
        Panel_ChucNang.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        Panel_ChucNang.setForeground(new java.awt.Color(255, 255, 255));
        Panel_ChucNang.setPreferredSize(new java.awt.Dimension(300, 100));

        ChucNang.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        ChucNang.setForeground(new java.awt.Color(255, 255, 255));
        ChucNang.setText("Chức năng");

        Update_btn.setText("Cập nhật");
        Update_btn.setMaximumSize(new java.awt.Dimension(80, 25));
        Update_btn.setPreferredSize(new java.awt.Dimension(80, 25));
        Update_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Update_btnActionPerformed(evt);
            }
        });

        Exit_btn.setText("Hủy");
        Exit_btn.setPreferredSize(new java.awt.Dimension(80, 25));
        Exit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Exit_btnActionPerformed(evt);
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
                        .addComponent(ChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Panel_ChucNangLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(Update_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Exit_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        Panel_ChucNangLayout.setVerticalGroup(
            Panel_ChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_ChucNangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Panel_ChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Update_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Exit_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Panel_ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Panel_ChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Panel_DuLieu, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Panel_DuLieu, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Panel_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Panel_ChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ID_TransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ID_TransActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ID_TransActionPerformed

    private void Brand_TransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Brand_TransActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Brand_TransActionPerformed

    private void Name_TransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Name_TransActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Name_TransActionPerformed

    private void Style_TransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Style_TransActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Style_TransActionPerformed

    private void Price_TransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Price_TransActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Price_TransActionPerformed

    private void quantity_TransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantity_TransActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantity_TransActionPerformed

    private void Search_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Search_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Search_fieldActionPerformed

    private void Search_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Search_btnActionPerformed
        SearchDataByBrand();
    }//GEN-LAST:event_Search_btnActionPerformed

    private void Data_BikesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Data_BikesMouseClicked
        int i = Data_Bikes.getSelectedRow();
        ID_Trans.setText(Data_Bikes.getValueAt(i, 0).toString());
        Name_Trans.setText(Data_Bikes.getValueAt(i, 1).toString());
        Brand_Trans.setText(Data_Bikes.getValueAt(i, 2).toString());
        Style_Trans.setText(Data_Bikes.getValueAt(i, 3).toString());
        Price_Trans.setText(Data_Bikes.getValueAt(i, 4).toString());
        quantity_Trans.setText(Data_Bikes.getValueAt(i, 5).toString());
    }//GEN-LAST:event_Data_BikesMouseClicked
    public void insertBikes() {
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
            fetcher.fetchDataInforBikes(Data_Bikes);
            JOptionPane.showMessageDialog(this, "Thêm thông tin thành công");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thêm được thông tin");
        }
    }
    
    public void updateBikes() {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa";
        String password = "123456789";
        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "UPDATE ThongTinXeDap SET TenXe = ?, HangXe = ?, LoaiXe = ?, GiaBan = ?, SoLuongTonKho = ? WHERE MaXe = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, Name_Trans.getText());                 // TenXe
            pstmt.setString(2, Brand_Trans.getText());                 // HangXe
            pstmt.setString(3, Style_Trans.getText());                 // LoaiXe
            pstmt.setFloat(4, Float.parseFloat(Price_Trans.getText())); // GiaBan
            pstmt.setInt(5, Integer.parseInt(quantity_Trans.getText()));   // SoLuongTonKho
            pstmt.setString(6, ID_Trans.getText());   // MaXe

            pstmt.executeUpdate(); // Thực thi truy vấn
            fetcher.fetchDataInforBikes(Data_Bikes); // Cập nhật lại dữ liệu hiển thị
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không cập nhật được thông tin");
        }
    }
    private void Update_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Update_btnActionPerformed
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa";
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String checkSql = "SELECT COUNT(*) FROM ThongTinXeDap WHERE MaXe = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, ID_Trans.getText()); // Kiểm tra MaXe

            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1); // Lấy số lượng kết quả

            if (count > 0) {
                // Nếu tồn tại, gọi hàm update
                updateBikes();
            } else {
                // Nếu không tồn tại, gọi hàm insert
                insertBikes();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi kiểm tra dữ liệu");
        }

        ClearText();
    }//GEN-LAST:event_Update_btnActionPerformed

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

    private void Exit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Exit_btnActionPerformed
        show_infor.ExitMenuInformation(Panel_ThongTin, Panel_ChucNang, Panel_DuLieu);
    }//GEN-LAST:event_Exit_btnActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    public void ClearText() {
        Name_Trans.setText("");
        Brand_Trans.setText("");
        Style_Trans.setText("");
        Price_Trans.setText("");
        quantity_Trans.setText("");
        ID_Trans.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Brand_Trans;
    private javax.swing.JLabel ChucNang;
    private javax.swing.JTable Data_Bikes;
    private javax.swing.JButton Exit_btn;
    private javax.swing.JTextField ID_Trans;
    private javax.swing.JLabel Label_Brand;
    private javax.swing.JLabel Label_ID;
    private javax.swing.JLabel Label_Name;
    private javax.swing.JLabel Label_Price;
    private javax.swing.JLabel Label_Style;
    private javax.swing.JLabel Label_TimKiem;
    private javax.swing.JLabel Lable_quantity;
    private javax.swing.JTextField Name_Trans;
    private javax.swing.JPanel Panel_ChucNang;
    private javax.swing.JPanel Panel_DuLieu;
    private javax.swing.JPanel Panel_ThongTin;
    private javax.swing.JTextField Price_Trans;
    private javax.swing.JButton Search_btn;
    private javax.swing.JTextField Search_field;
    private javax.swing.JTextField Style_Trans;
    private javax.swing.JLabel ThongTin;
    private javax.swing.JButton Update_btn;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField quantity_Trans;
    // End of variables declaration//GEN-END:variables
}
