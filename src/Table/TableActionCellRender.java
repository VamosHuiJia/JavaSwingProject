/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
/**
 *
 * @author Admin
 */
public class TableActionCellRender extends DefaultTableCellRenderer {
    
    @Override 
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSelected, boolean blnl, int i, int i1) {
        Component com = super.getTableCellRendererComponent(jtable, o, isSelected, blnl, TOP, SOUTH);
        
        PanelAction action = new PanelAction();
        if( isSelected == false && i%2 == 0) {
            action.setBackground(Color.WHITE);
        }
        else {
            action.setBackground(com.getBackground());
        }
        return action;
    } 
}
