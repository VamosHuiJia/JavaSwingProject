/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tools;

import javax.swing.JTextField;

/**
 *
 * @author NAM
 */
public class CodeClearText {
    public void ClearTextAccountLogin(JTextField Name_Field,JTextField PhoneNum_Field,JTextField Acc_Field,JTextField Pass_Field) {
        Name_Field.setText("");
        PhoneNum_Field.setText("");
        Acc_Field.setText("");
        Pass_Field.setText("");
    }
    
    public void ClearTextCustomers(JTextField IDCus_field,JTextField NameCus_Field,JTextField PhoneNum_Field,JTextField Email_Field,JTextField Address_Field) {
        IDCus_field.setText("");
        NameCus_Field.setText("");
        PhoneNum_Field.setText("");
        Email_Field.setText("");
        Address_Field.setText("");
    }
    
    public void ClearTextEmployees(JTextField IDEmploy_Field,JTextField NameEmploy_Field,JTextField Position_Field,JTextField PhoneNum_Field,JTextField Email_Field) {
        IDEmploy_Field.setText("");
        NameEmploy_Field.setText("");
        Position_Field.setText("");
        PhoneNum_Field.setText("");
        Email_Field.setText("");
    }
    
    public void ClearTextInforBikes(JTextField ID_Trans,JTextField Name_Trans,JTextField Brand_Trans,JTextField Style_Trans,JTextField Price_Trans,JTextField quantity_Trans) {
        Name_Trans.setText("");
        Brand_Trans.setText("");
        Style_Trans.setText("");
        Price_Trans.setText("");
        quantity_Trans.setText("");
        ID_Trans.setText("");
    }
}
