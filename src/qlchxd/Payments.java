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
public class Payments extends javax.swing.JInternalFrame {

    private ShowInformation show_infor = new ShowInformation();
    /**
     * Creates new form Payments
     */
    public Payments() {
        
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
                if (Data_Payment.isEditing()) {
                    Data_Payment.getCellEditor().stopCellEditing();
                }
                deleteRowFromDatabase(row);
            }
            
            public void onAdd(int row) {
                show_infor.ShowMenuInformation(Panel_ThongTin, Panel_ChucNang, Panel_DuLieu);
                ClearText();
            }
        };
        
        Data_Payment.getColumnModel().getColumn(7).setCellRenderer(new TableActionCellRender());
        Data_Payment.getColumnModel().getColumn(7).setCellEditor(new TableActionCellEditor(event)); 
        
        fetchData();

        Panel_ThongTin.setVisible(false);
        Panel_ChucNang.setVisible(false);
        Panel_DuLieu.setPreferredSize(new java.awt.Dimension(1000, 600));
    }

    // Xóa    
    private void deleteRowFromDatabase(int row) {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa";
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            // Lấy giá trị ID từ dòng được chọn
            DefaultTableModel model = (DefaultTableModel) Data_Payment.getModel();
            String id = model.getValueAt(row, 0).toString(); // Lấy giá trị từ cột đầu tiên (giả sử là ID)

            String sql = "DELETE FROM ThanhToan WHERE MaThanhToan = ?";
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
    
    //Hiển thị thông tin
    private void fetchData() {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; // Thay bằng username của bạn
        String password = "123456789"; // Thay bằng password của bạn

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "SELECT MaThanhToan, MaXe, MaKH, NgayThanhToan, PhuongThucThanhToan, SoLuongXeMua, ThanhTien FROM ThanhToan";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Tạo mô hình cho JTable
            DefaultTableModel model = (DefaultTableModel) Data_Payment.getModel();
            model.setRowCount(0); // Xóa hết dữ liệu cũ

            // Thêm dữ liệu vào JTable
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaThanhToan"),
                    rs.getString("MaXe"),
                    rs.getString("MaKH"),
                    rs.getDate("NgayThanhToan"),
                    rs.getString("PhuongThucThanhToan"),
                    rs.getInt("SoLuongXeMua"),
                    rs.getFloat("ThanhTien")
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

        String sql = "SELECT MaThanhToan, MaXe, MaKH, NgayThanhToan, PhuongThucThanhToan, SoLuongXeMua, ThanhTien FROM ThanhToan";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Tạo mô hình cho JTable
            DefaultTableModel model = (DefaultTableModel) Data_Payment.getModel();
            model.setRowCount(0); 

            // Thêm dữ liệu vào bảng
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaThanhToan"),
                    rs.getString("MaXe"),
                    rs.getString("MaKH"),
                    rs.getDate("NgayThanhToan"),
                    rs.getString("PhuongThucThanhToan"),
                    rs.getInt("SoLuongXeMua"),
                    rs.getFloat("ThanhTien")
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
        String sql = "SELECT MaThanhToan, MaXe, MaKH, NgayThanhToan, PhuongThucThanhToan, SoLuongXeMua, ThanhTien FROM ThanhToan WHERE ";        
        switch (selectedCriteria) {
            case "Tất cả":
                sql += "(MaThanhToan LIKE N'%" + Search_field.getText() + "%' OR "
                + "MaXe LIKE N'%" + Search_field.getText() + "%' OR "
                + "MaKH LIKE N'%" + Search_field.getText() + "%' OR "
                + "NgayThanhToan LIKE N'%" + Search_field.getText() + "%' OR "
                + "PhuongThucThanhToan LIKE N'%" + Search_field.getText() + "%' OR "
                + "SoLuongXeMua LIKE N'%" + Search_field.getText() + "%' OR "
                + "ThanhTien LIKE N'%" + Search_field.getText() + "%')";
                 break;
                
            case "Mã HĐ":
                sql += "MaThanhToan LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Mã xe":
                sql += "MaXe LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Mã KH":
                sql += "MaKH LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Ngày TT":
                sql += "NgayThanhToan LIKE N'%" + Search_field.getText() + "%'";
                break;
            case "Phương thức":
                sql += "PhuongThucThanhToan LIKE N'%" + Search_field.getText() + "%'";
                break;
       
        }

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Tạo mô hình cho JTable
            DefaultTableModel model = (DefaultTableModel) Data_Payment.getModel();
            model.setRowCount(0); // Xóa hết dữ liệu cũ

            // Thêm dữ liệu vào JTable
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaThanhToan"),
                    rs.getString("MaXe"),
                    rs.getString("MaKH"),
                    rs.getDate("NgayThanhToan"),
                    rs.getString("PhuongThucThanhToan"),
                    rs.getInt("SoLuongXeMua"),
                    rs.getFloat("ThanhTien")
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void InputInformation() {
        int i = Data_Payment.getSelectedRow();
        IDPay_Field.setText(Data_Payment.getValueAt(i, 0).toString());
        IDTrans_Field.setText(Data_Payment.getValueAt(i, 1).toString());
        IDCus_Field.setText(Data_Payment.getValueAt(i, 2).toString());
        QuantityTrans_Field.setText(Data_Payment.getValueAt(i, 5).toString());
        Price_Field.setText(Data_Payment.getValueAt(i, 6).toString());
        String dateStr = Data_Payment.getValueAt(i, 3).toString();
        try {
            // Định dạng ngày tháng (định dạng này có thể thay đổi theo định dạng của dữ liệu)
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = dateFormat.parse(dateStr);
            DateChooser.setDate(date); // Đặt giá trị cho DateChooser
            
            
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ComboBox.setSelectedItem(Data_Payment.getValueAt(i, 4).toString());
    }
    
    //Thêm thông tin
    public void insertData() {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; 
        String password = "123456789"; 

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "INSERT INTO ThanhToan (MaThanhToan, MaXe, MaKH, NgayThanhToan, PhuongThucThanhToan, SoLuongXeMua, ThanhTien) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1,IDPay_Field.getText()); 
            pstmt.setString(2, IDTrans_Field.getText());
            pstmt.setString(3, IDCus_Field.getText());

            // Chuyển đổi java.util.Date sang java.sql.Date
            pstmt.setDate(4, new java.sql.Date(DateChooser.getDate().getTime()));

            // Sử dụng getSelectedItem().toString() cho JComboBox
            pstmt.setString(5, ComboBox.getSelectedItem().toString()); 
            pstmt.setInt(6, Integer.parseInt(QuantityTrans_Field.getText())); 
            pstmt.setFloat(7, Float.parseFloat(Price_Field.getText())); 

            pstmt.executeUpdate(); 
            fetchData(); 

            ClearText();
            
            JOptionPane.showMessageDialog(this, "Thêm thông tin thành công");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Thêm thông tin không thành công");
        }
    }
    
    public void updateData() {
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa"; 
        String password = "123456789"; 

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String sql = "UPDATE ThanhToan SET MaXe = ?, MaKH = ?, NgayThanhToan = ?, PhuongThucThanhToan = ?, SoLuongXeMua = ?, ThanhTien = ? WHERE MaThanhToan = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1,IDTrans_Field.getText()); // Thay đổi ID xe
            pstmt.setString(2,IDCus_Field.getText()); // ID khách hàng
            pstmt.setDate(3, new java.sql.Date(DateChooser.getDate().getTime())); // Ngày thanh toán
            pstmt.setString(4, ComboBox.getSelectedItem().toString()); // Phương thức thanh toán
            pstmt.setInt(5, Integer.parseInt(QuantityTrans_Field.getText())); // Số lượng xe mua
            pstmt.setFloat(6, Float.parseFloat(Price_Field.getText())); // Thành tiền
            pstmt.setString(7,IDPay_Field.getText()); // MaThanhToan

            pstmt.executeUpdate(); // Thực thi truy vấn
            fetchData(); // Cập nhật lại dữ liệu hiển thị
            ClearText(); 
            
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin không thành công");
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

        Panel_ChucNang = new javax.swing.JPanel();
        Exit_btn = new javax.swing.JButton();
        Update_btn = new javax.swing.JButton();
        ChucNang = new javax.swing.JLabel();
        Panel_ThongTin = new javax.swing.JPanel();
        DatePay_Label = new javax.swing.JLabel();
        IDPay_Field = new javax.swing.JTextField();
        IDCus_Label = new javax.swing.JLabel();
        IDCus_Field = new javax.swing.JTextField();
        IDTrans_Field = new javax.swing.JTextField();
        MethodPay_Label = new javax.swing.JLabel();
        IDTrans_Label = new javax.swing.JLabel();
        Pay_Label = new javax.swing.JLabel();
        ThongTin = new javax.swing.JLabel();
        ComboBox = new javax.swing.JComboBox<>();
        PayPrice_Label = new javax.swing.JLabel();
        Price_Field = new javax.swing.JTextField();
        Quantity_Label = new javax.swing.JLabel();
        QuantityTrans_Field = new javax.swing.JTextField();
        DateChooser = new com.toedter.calendar.JDateChooser();
        Panel_DuLieu = new javax.swing.JPanel();
        Search_Label = new javax.swing.JLabel();
        Search_field = new javax.swing.JTextField();
        Search_btn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Data_Payment = new javax.swing.JTable();
        jComboBox1 = new javax.swing.JComboBox<>();
        Export = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("QUẢN LÝ BÁN HÀNG");
        setPreferredSize(new java.awt.Dimension(1000, 600));

        Panel_ChucNang.setBackground(new java.awt.Color(0, 102, 102));
        Panel_ChucNang.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        Panel_ChucNang.setForeground(new java.awt.Color(255, 255, 255));

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
                .addContainerGap(180, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_ChucNangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Update_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Exit_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Panel_ChucNangLayout.setVerticalGroup(
            Panel_ChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_ChucNangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel_ChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Exit_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Update_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );

        Panel_ThongTin.setBackground(new java.awt.Color(0, 102, 102));
        Panel_ThongTin.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        Panel_ThongTin.setForeground(new java.awt.Color(255, 255, 255));
        Panel_ThongTin.setPreferredSize(new java.awt.Dimension(500, 600));

        DatePay_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        DatePay_Label.setForeground(new java.awt.Color(255, 255, 255));
        DatePay_Label.setText("Nhập ngày TT");

        IDPay_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDPay_FieldActionPerformed(evt);
            }
        });

        IDCus_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        IDCus_Label.setForeground(new java.awt.Color(255, 255, 255));
        IDCus_Label.setText("Nhập mã KH");

        IDCus_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDCus_FieldActionPerformed(evt);
            }
        });

        IDTrans_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDTrans_FieldActionPerformed(evt);
            }
        });

        MethodPay_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        MethodPay_Label.setForeground(new java.awt.Color(255, 255, 255));
        MethodPay_Label.setText("Phương thức TT");

        IDTrans_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        IDTrans_Label.setForeground(new java.awt.Color(255, 255, 255));
        IDTrans_Label.setText("Nhập mã xe");

        Pay_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Pay_Label.setForeground(new java.awt.Color(255, 255, 255));
        Pay_Label.setText("Nhập mã TT");

        ThongTin.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        ThongTin.setForeground(new java.awt.Color(255, 255, 255));
        ThongTin.setText("Thông tin");

        ComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chuyển khoản", "Tiền mặt" }));
        ComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxActionPerformed(evt);
            }
        });

        PayPrice_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        PayPrice_Label.setForeground(new java.awt.Color(255, 255, 255));
        PayPrice_Label.setText("Tiền thanh toán");

        Price_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Price_FieldActionPerformed(evt);
            }
        });

        Quantity_Label.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Quantity_Label.setForeground(new java.awt.Color(255, 255, 255));
        Quantity_Label.setText("Số lượng xe");

        QuantityTrans_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuantityTrans_FieldActionPerformed(evt);
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
                            .addComponent(Pay_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(IDTrans_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MethodPay_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(IDCus_Label, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(DatePay_Label, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(PayPrice_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Quantity_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(IDCus_Field)
                            .addComponent(IDTrans_Field)
                            .addComponent(IDPay_Field)
                            .addComponent(QuantityTrans_Field)
                            .addComponent(Price_Field)
                            .addComponent(DateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                            .addComponent(ComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        Panel_ThongTinLayout.setVerticalGroup(
            Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Pay_Label)
                    .addComponent(IDPay_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IDTrans_Label)
                    .addComponent(IDTrans_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IDCus_Label)
                    .addComponent(IDCus_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(DateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DatePay_Label))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MethodPay_Label)
                    .addComponent(ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Quantity_Label)
                    .addComponent(QuantityTrans_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PayPrice_Label)
                    .addComponent(Price_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        Data_Payment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hóa đơn", "Mã xe", "Mã KH", "Ngày thanh toán", "Phương thức ", "Số lượng", "Thành tiền", "Tùy chọn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Data_Payment.setRowHeight(50);
        Data_Payment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Data_PaymentMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Data_Payment);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Mã HĐ", "Mã xe", "Mã KH", "Ngày TT", "Phương thức" }));

        Export.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-xls-20.png"))); // NOI18N
        Export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel_DuLieuLayout = new javax.swing.GroupLayout(Panel_DuLieu);
        Panel_DuLieu.setLayout(Panel_DuLieuLayout);
        Panel_DuLieuLayout.setHorizontalGroup(
            Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_DuLieuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_DuLieuLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Search_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Search_field, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Search_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Export)))
                .addContainerGap())
        );
        Panel_DuLieuLayout.setVerticalGroup(
            Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_DuLieuLayout.createSequentialGroup()
                .addGroup(Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_DuLieuLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Export))
                    .addGroup(Panel_DuLieuLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(Panel_DuLieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Search_Label)
                            .addComponent(Search_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Search_btn)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Panel_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                    .addComponent(Panel_ChucNang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Panel_DuLieu, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Panel_DuLieu, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Panel_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Panel_ChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Exit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Exit_btnActionPerformed
        show_infor.ExitMenuInformation(Panel_ThongTin, Panel_ChucNang, Panel_DuLieu);
    }//GEN-LAST:event_Exit_btnActionPerformed

    private void Update_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Update_btnActionPerformed
        String connectionUrl = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true";
        String user = "sa";
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)) {
            String checkSql = "SELECT COUNT(*) FROM ThanhToan WHERE MaThanhToan = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, IDPay_Field.getText()); // Kiểm tra MaXe

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

    private void IDPay_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDPay_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDPay_FieldActionPerformed

    private void IDCus_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDCus_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDCus_FieldActionPerformed

    private void IDTrans_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDTrans_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDTrans_FieldActionPerformed

    private void ComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboBoxActionPerformed

    private void Price_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Price_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Price_FieldActionPerformed

    private void QuantityTrans_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuantityTrans_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_QuantityTrans_FieldActionPerformed

    private void Search_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Search_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Search_fieldActionPerformed

    private void Search_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Search_btnActionPerformed
        SearchData();
    }//GEN-LAST:event_Search_btnActionPerformed

    private void Data_PaymentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Data_PaymentMouseClicked
        int i = Data_Payment.getSelectedRow();
        IDPay_Field.setText(Data_Payment.getValueAt(i, 0).toString());
        IDTrans_Field.setText(Data_Payment.getValueAt(i, 1).toString());
        IDCus_Field.setText(Data_Payment.getValueAt(i, 2).toString());
        QuantityTrans_Field.setText(Data_Payment.getValueAt(i, 5).toString());
        Price_Field.setText(Data_Payment.getValueAt(i, 6).toString());
        String dateStr = Data_Payment.getValueAt(i, 3).toString();
        try {
            // Định dạng ngày tháng (định dạng này có thể thay đổi theo định dạng của dữ liệu)
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = dateFormat.parse(dateStr);
            DateChooser.setDate(date); // Đặt giá trị cho DateChooser
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ComboBox.setSelectedItem(Data_Payment.getValueAt(i, 4).toString());
    }//GEN-LAST:event_Data_PaymentMouseClicked

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

    private void ExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportActionPerformed
        exportToExcel();
    }//GEN-LAST:event_ExportActionPerformed

    private void exportToExcel() {
        try {
            // Chọn file để lưu
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("Lưu file Excel");
            fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                // Thêm đuôi file nếu chưa có
                if (!filePath.endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                // Tạo workbook và sheet
                org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Payments");

                // Thêm tiêu đề bảng
                org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
                for (int i = 0; i < Data_Payment.getColumnCount(); i++) {
                    org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                    cell.setCellValue(Data_Payment.getColumnName(i));
                }

                // Thêm dữ liệu từ bảng
                for (int i = 0; i < Data_Payment.getRowCount(); i++) {
                    org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < Data_Payment.getColumnCount(); j++) {
                        org.apache.poi.ss.usermodel.Cell cell = row.createCell(j);
                        Object value = Data_Payment.getValueAt(i, j);
                        cell.setCellValue(value != null ? value.toString() : "");
                    }
                }

                // Ghi dữ liệu ra file
                try (java.io.FileOutputStream fileOut = new java.io.FileOutputStream(filePath)) {
                    workbook.write(fileOut);
                }
                workbook.close();

                // Thông báo thành công
                JOptionPane.showMessageDialog(this, "Xuất file Excel thành công: " + filePath, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi xuất file Excel.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ClearText() {
        IDPay_Field.setText("");
        IDTrans_Field.setText("");
        IDCus_Field.setText("");
        DateChooser.setDate(null);

        QuantityTrans_Field.setText("");
        Price_Field.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ChucNang;
    private javax.swing.JComboBox<String> ComboBox;
    private javax.swing.JTable Data_Payment;
    private com.toedter.calendar.JDateChooser DateChooser;
    private javax.swing.JLabel DatePay_Label;
    private javax.swing.JButton Exit_btn;
    private javax.swing.JButton Export;
    private javax.swing.JTextField IDCus_Field;
    private javax.swing.JLabel IDCus_Label;
    private javax.swing.JTextField IDPay_Field;
    private javax.swing.JTextField IDTrans_Field;
    private javax.swing.JLabel IDTrans_Label;
    private javax.swing.JLabel MethodPay_Label;
    private javax.swing.JPanel Panel_ChucNang;
    private javax.swing.JPanel Panel_DuLieu;
    private javax.swing.JPanel Panel_ThongTin;
    private javax.swing.JLabel PayPrice_Label;
    private javax.swing.JLabel Pay_Label;
    private javax.swing.JTextField Price_Field;
    private javax.swing.JTextField QuantityTrans_Field;
    private javax.swing.JLabel Quantity_Label;
    private javax.swing.JLabel Search_Label;
    private javax.swing.JButton Search_btn;
    private javax.swing.JTextField Search_field;
    private javax.swing.JLabel ThongTin;
    private javax.swing.JButton Update_btn;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
