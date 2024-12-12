/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tools;

import javax.swing.JPanel;
/**
 *
 * @author Admin
 */
public class ShowInformation {
    public void ShowMenuInformation(JPanel Panel_ThongTin, JPanel Panel_ChucNang, JPanel Panel_DuLieu) {
        boolean isVisible = Panel_ThongTin.isVisible() && Panel_ChucNang.isVisible();

        // Đổi trạng thái hiển thị của các panel
        Panel_ThongTin.setVisible(true);
        Panel_ChucNang.setVisible(true);

        // Điều chỉnh kích thước của Panel_DuLieu dựa trên trạng thái hiển thị
        if (isVisible) {
            Panel_DuLieu.setPreferredSize(new java.awt.Dimension(1000, 600)); // Phóng to
        }
    }
    
    public void ExitMenuInformation(JPanel Panel_ThongTin, JPanel Panel_ChucNang, JPanel Panel_DuLieu) {
        boolean isVisible = Panel_ThongTin.isVisible() && Panel_ChucNang.isVisible();

        // Đổi trạng thái hiển thị của các panel
        Panel_ThongTin.setVisible(false);
        Panel_ChucNang.setVisible(false);

        // Điều chỉnh kích thước của Panel_DuLieu dựa trên trạng thái hiển thị
        if (isVisible) {
            Panel_DuLieu.setPreferredSize(new java.awt.Dimension(1000, 600)); // Phóng to
        }
    }
}
