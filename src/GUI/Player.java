/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author ellioth
 */
public class Player implements Constantes{
    private int _id, _PosX,_PosY, _size;
    private JLabel _playerLabel;
    
    public Player(int pId){
        _id=pId;
        _playerLabel= new JLabel(new ImageIcon(PLAYER_IMAGE));
        _playerLabel.setBounds(INIT_POS_X_PLY, POS_Y_PLY, PLAYER_LENGHT, PLAYER_LENGHT_Y);
    }
    
    public int getSize(){
        return _size;
    }
    
    public void resize(int pOP){
        if(pOP==DECREMENT)
            _size+=DECREMENT;
        _size+=INCREMENT;
    }
    
    public int getPosX(){
        return _PosX;
    }
    
    public int getPosY(){
        return _PosY;
    }
    
    public JLabel getPLayerLabel(){
        return _playerLabel;
    }
    
    public void setPosi(int pPx, int pPy){
        _PosX=pPx;
        _PosY=pPy;
        _playerLabel.setLocation(_PosX, _PosY);
    }
}
