/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Table;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Admin
 */
public class ActionButton extends JButton{
    
    private boolean mousePress;
    
    public ActionButton() {
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(3, 3, 3, 3));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                mousePress = true;
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {
                mousePress = false;
            }
        });
    }
    
    @Override 
    
    protected void paintComponent(Graphics grp) {
        Graphics2D g2 = (Graphics2D)grp.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height);
        int x = (width - size)/2;
        int y = (height - size)/2;
        
        if(mousePress) {
            g2.setColor(new Color(158, 158, 158));
        }
        else {
            g2.setColor(new Color(199, 199, 199));
        }
        
        g2.fill(new Ellipse2D.Double(x, y, size, size));
        g2.dispose();
        
        super.paintComponent(grp);
        
        if (backgroundImage != null) {
            grp.drawImage(((ImageIcon) backgroundImage).getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    public void setBackgroundImage(Icon img)   {
        this.backgroundImage = img;
        revalidate();
        repaint();
    }
    public Icon getBackgroundImage() {
        return backgroundImage;
    }

    private Icon backgroundImage;
        
}
