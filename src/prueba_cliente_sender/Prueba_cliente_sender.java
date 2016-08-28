/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba_cliente_sender;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author ellioth
 */
public class Prueba_cliente_sender extends JFrame implements KeyListener{
    private final String _Left="{\"id\": 0,\"move\":0}";
    private final String _Right="{\"id\": 0,\"move\":2}";
    private cliente _cliente;
    
    public Prueba_cliente_sender(int pPort, String pIp){
        super(pIp);
        JPanel p = new JPanel();
        add(p);
        addKeyListener(this);
        //setSize(200, 100);
        setVisible(true);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        if(e.getKeyCode()== KeyEvent.VK_RIGHT){
            _cliente.SendMsg(_Right);
            //System.out.println(_Right);
        }
        else if(e.getKeyCode()== KeyEvent.VK_LEFT){
            _cliente.SendMsg(_Left);
            //System.out.println(_Left);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
    /*
    public static void main(String[] args) {
        //new Prueba_cliente_sender(1,"0");
        
    }*/
}
