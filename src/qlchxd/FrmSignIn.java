/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package qlchxd;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author Admin
 */
public class FrmSignIn extends javax.swing.JFrame {

    private String userRole;
    /**
     * Creates new form FrmSignIn
     */
    public FrmSignIn() {
        initComponents();
    }

    public class DatabaseConnection {
        private static final String URL = "jdbc:sqlserver://HOMHINHHA\\SQLEXPRESS:1433;databaseName=QLCHXEDAP;trustServerCertificate=true"; 
        private static final String USER = "sa"; 
        private static final String PASSWORD = "123456789";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    private void setupPlaceholderBehavior() {
        Username_Field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (Username_Field.getText().equals("Username")) {
                    Username_Field.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (Username_Field.getText().isEmpty()) {
                    Username_Field.setText("Username");
                }
            }
        });

        Password_Field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(Password_Field.getPassword()).equals("jPasswordField1")) {
                    Password_Field.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(Password_Field.getPassword()).isEmpty()) {
                    Password_Field.setText("jPasswordField1");
                }
            }
        });
    }

    private void onLoginButtonClicked() {
        String username = Username_Field.getText();
        String password = String.valueOf(Password_Field.getPassword());

        if (isValidLogin(username, password)) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT HoTen, SoDienThoai, ID, ROLE FROM DangNhap WHERE ID = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String hoTen = resultSet.getString("HoTen");
                    String soDienThoai = resultSet.getString("SoDienThoai");
                    String role = resultSet.getString("ROLE");

                    JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Bạn là: " + role);

                    // Tạo instance của Home_Page với đầy đủ thông tin người dùng
                    Home_Page home = new Home_Page(username, role, hoTen, soDienThoai);
                    home.setVisible(true);
                    this.setVisible(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Tên người dùng hoặc mật khẩu không hợp lệ.");
        }
    }

    private boolean isValidLogin(String username, String password) {
    try (Connection connection = DatabaseConnection.getConnection()) {
        String query = "SELECT PASS FROM DangNhap WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String passwordFromDB = resultSet.getString("PASS");
                // So sánh mật khẩu người dùng nhập với mật khẩu từ DB
                return password.equals(passwordFromDB);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; // Trả về false nếu không tìm thấy người dùng
}

//===========================================Xu ly Password dạng băm===================================================//
//    private boolean isValidLogin(String username, String password) {
//        try (Connection connection = DatabaseConnection.getConnection()) {
//            String query = "SELECT PASS FROM DangNhap WHERE ID = ?";
//            try (PreparedStatement statement = connection.prepareStatement(query)) {
//                statement.setString(1, username);
//
//                ResultSet resultSet = statement.executeQuery();
//                if (resultSet.next()) {
//                    // Lấy mật khẩu đã băm từ cơ sở dữ liệu
//                    byte[] hashedPasswordFromDB = resultSet.getBytes("PASS");
//
//                    // Băm mật khẩu người dùng nhập
//                    byte[] hashedInputPassword = hashPassword(password);
//
//                    // So sánh mật khẩu đã băm
//                    return java.util.Arrays.equals(hashedPasswordFromDB, hashedInputPassword);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false; // Trả về false nếu không tìm thấy người dùng
//    }
//
//    private byte[] hashPassword(String password) {
//        try {
//            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
//            return digest.digest(password.getBytes("UTF-8"));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
    
    private String getUserRole(String username) {
        String role = null;
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT ROLE FROM DangNhap WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                role = resultSet.getString("ROLE");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Panel_DangNhap = new javax.swing.JPanel();
        Username_Field = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        Login_btn = new javax.swing.JButton();
        Password_Field = new javax.swing.JPasswordField();
        Label_DangNhap = new java.awt.Label();
        Label_MatKhau = new java.awt.Label();
        Label_TaiKhoan = new java.awt.Label();
        Label_SignUp = new java.awt.Label();
        CheckBox_ShowPass = new javax.swing.JCheckBox();
        Label_Back = new javax.swing.JLabel();
        SignUp_Label = new javax.swing.JLabel();
        Panel_Logo = new javax.swing.JPanel();
        Logo = new javax.swing.JLabel();
        Label_Logo = new java.awt.Label();
        Label_Slogan = new java.awt.Label();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Đăng nhập");

        Panel_DangNhap.setBackground(new java.awt.Color(255, 255, 255));
        Panel_DangNhap.setPreferredSize(new java.awt.Dimension(500, 500));

        Username_Field.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Username_Field.setForeground(new java.awt.Color(0, 102, 51));
        Username_Field.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(0, 51, 51)));
        Username_Field.setPreferredSize(new java.awt.Dimension(80, 30));
        Username_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Username_FieldActionPerformed(evt);
            }
        });

        Login_btn.setBackground(new java.awt.Color(0, 149, 118));
        Login_btn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Login_btn.setForeground(new java.awt.Color(255, 255, 255));
        Login_btn.setText("ĐĂNG NHẬP");
        Login_btn.setPreferredSize(new java.awt.Dimension(140, 30));
        Login_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Login_btnActionPerformed(evt);
            }
        });

        Password_Field.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Password_Field.setForeground(new java.awt.Color(0, 102, 51));
        Password_Field.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(0, 102, 102)));
        Password_Field.setPreferredSize(new java.awt.Dimension(100, 30));
        Password_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Password_FieldActionPerformed(evt);
            }
        });

        Label_DangNhap.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        Label_DangNhap.setForeground(new java.awt.Color(0, 102, 102));
        Label_DangNhap.setText("ĐĂNG NHẬP");

        Label_MatKhau.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        Label_MatKhau.setText("Mật khẩu");

        Label_TaiKhoan.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        Label_TaiKhoan.setText("Tài khoản");

        Label_SignUp.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        Label_SignUp.setText("Chưa có tài khoản ?");

        CheckBox_ShowPass.setText("Hiển thị mật khẩu");
        CheckBox_ShowPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBox_ShowPassActionPerformed(evt);
            }
        });

        Label_Back.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Label_Back.setText("<< Trở lại");
        Label_Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Label_BackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Label_BackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Label_BackMouseExited(evt);
            }
        });

        SignUp_Label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        SignUp_Label.setForeground(new java.awt.Color(0, 102, 102));
        SignUp_Label.setText("ĐĂNG KÝ");
        SignUp_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SignUp_LabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SignUp_LabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SignUp_LabelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout Panel_DangNhapLayout = new javax.swing.GroupLayout(Panel_DangNhap);
        Panel_DangNhap.setLayout(Panel_DangNhapLayout);
        Panel_DangNhapLayout.setHorizontalGroup(
            Panel_DangNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_DangNhapLayout.createSequentialGroup()
                .addGroup(Panel_DangNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_DangNhapLayout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addGroup(Panel_DangNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Username_Field, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(Panel_DangNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Panel_DangNhapLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(Label_MatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Password_Field, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_DangNhapLayout.createSequentialGroup()
                                .addComponent(Label_DangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11))))
                    .addGroup(Panel_DangNhapLayout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(Label_SignUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SignUp_Label)))
                .addContainerGap(116, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_DangNhapLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(Panel_DangNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_DangNhapLayout.createSequentialGroup()
                        .addComponent(CheckBox_ShowPass)
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_DangNhapLayout.createSequentialGroup()
                        .addComponent(Login_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(177, 177, 177))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_DangNhapLayout.createSequentialGroup()
                        .addComponent(Label_Back)
                        .addContainerGap())))
            .addGroup(Panel_DangNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Panel_DangNhapLayout.createSequentialGroup()
                    .addGap(124, 124, 124)
                    .addComponent(Label_TaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(273, Short.MAX_VALUE)))
        );
        Panel_DangNhapLayout.setVerticalGroup(
            Panel_DangNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_DangNhapLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_Back)
                .addGap(17, 17, 17)
                .addComponent(Label_DangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74)
                .addComponent(Username_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_MatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(Password_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CheckBox_ShowPass)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(Login_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addGroup(Panel_DangNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_DangNhapLayout.createSequentialGroup()
                        .addComponent(Label_SignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_DangNhapLayout.createSequentialGroup()
                        .addComponent(SignUp_Label)
                        .addGap(37, 37, 37))))
            .addGroup(Panel_DangNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Panel_DangNhapLayout.createSequentialGroup()
                    .addGap(136, 136, 136)
                    .addComponent(Label_TaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(339, Short.MAX_VALUE)))
        );

        Panel_Logo.setBackground(new java.awt.Color(0, 102, 102));
        Panel_Logo.setPreferredSize(new java.awt.Dimension(500, 500));

        Logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Logo3.png"))); // NOI18N

        Label_Logo.setFont(new java.awt.Font("Algerian", 1, 48)); // NOI18N
        Label_Logo.setForeground(new java.awt.Color(255, 255, 255));
        Label_Logo.setText("HUY NAM");

        Label_Slogan.setFont(new java.awt.Font("Cambria Math", 0, 14)); // NOI18N
        Label_Slogan.setForeground(new java.awt.Color(255, 255, 255));
        Label_Slogan.setText("THƯƠNG HIỆU UY TÍN TẠO NÊN ĐẲNG CẤP NGƯỜI DÙNG");

        javax.swing.GroupLayout Panel_LogoLayout = new javax.swing.GroupLayout(Panel_Logo);
        Panel_Logo.setLayout(Panel_LogoLayout);
        Panel_LogoLayout.setHorizontalGroup(
            Panel_LogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_LogoLayout.createSequentialGroup()
                .addGroup(Panel_LogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Panel_LogoLayout.createSequentialGroup()
                        .addContainerGap(49, Short.MAX_VALUE)
                        .addComponent(Label_Slogan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Panel_LogoLayout.createSequentialGroup()
                        .addGroup(Panel_LogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Panel_LogoLayout.createSequentialGroup()
                                .addGap(142, 142, 142)
                                .addComponent(Logo))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Panel_LogoLayout.createSequentialGroup()
                                .addGap(122, 122, 122)
                                .addComponent(Label_Logo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(92, Short.MAX_VALUE))
        );
        Panel_LogoLayout.setVerticalGroup(
            Panel_LogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_LogoLayout.createSequentialGroup()
                .addContainerGap(97, Short.MAX_VALUE)
                .addComponent(Logo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_Logo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_Slogan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(151, 151, 151))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Panel_Logo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Panel_DangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel_DangNhap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Panel_Logo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void Username_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Username_FieldActionPerformed

    }//GEN-LAST:event_Username_FieldActionPerformed

    private void Login_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Login_btnActionPerformed
        onLoginButtonClicked();
    }//GEN-LAST:event_Login_btnActionPerformed

    private void Password_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Password_FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Password_FieldActionPerformed

    private void CheckBox_ShowPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBox_ShowPassActionPerformed
        if (CheckBox_ShowPass.isSelected()) {
            Password_Field.setEchoChar((char) 0); 
        } else {
            Password_Field.setEchoChar('●'); 
        }
    }//GEN-LAST:event_CheckBox_ShowPassActionPerformed

    private void Label_BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Label_BackMouseClicked
        Home_withoutSignIn Homews = new Home_withoutSignIn();
 
        Homews.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_Label_BackMouseClicked

    private void SignUp_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SignUp_LabelMouseClicked
        FrmSignUp register = new FrmSignUp();
 
        register.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_SignUp_LabelMouseClicked

    private void SignUp_LabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SignUp_LabelMouseEntered
        SignUp_Label.setForeground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_SignUp_LabelMouseEntered

    private void SignUp_LabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SignUp_LabelMouseExited
        SignUp_Label.setForeground(new Color(0, 102, 102));
    }//GEN-LAST:event_SignUp_LabelMouseExited

    private void Label_BackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Label_BackMouseEntered
        Label_Back.setForeground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_Label_BackMouseEntered

    private void Label_BackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Label_BackMouseExited
        Label_Back.setForeground(Color.BLACK);
    }//GEN-LAST:event_Label_BackMouseExited

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(FrmSignIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(FrmSignIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(FrmSignIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(FrmSignIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new FrmSignIn().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox CheckBox_ShowPass;
    private javax.swing.JLabel Label_Back;
    private java.awt.Label Label_DangNhap;
    private java.awt.Label Label_Logo;
    private java.awt.Label Label_MatKhau;
    private java.awt.Label Label_SignUp;
    private java.awt.Label Label_Slogan;
    private java.awt.Label Label_TaiKhoan;
    private javax.swing.JButton Login_btn;
    private javax.swing.JLabel Logo;
    private javax.swing.JPanel Panel_DangNhap;
    private javax.swing.JPanel Panel_Logo;
    private javax.swing.JPasswordField Password_Field;
    private javax.swing.JLabel SignUp_Label;
    private javax.swing.JTextField Username_Field;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
