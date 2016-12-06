/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author ellioth
 */
public class Brick implements Constantes{
    private int _Posx, _Posy, _hitsLft;
    private JLabel _brickLabel;
    
    public Brick(int pX, int pY, int pType){
        _Posx=pX;
        _Posy=pY;
        _brickLabel= new JLabel();
        _brickLabel.setBounds(_Posx, _Posy, BRICK_SIZE, BRICK_SIZE);
        _brickLabel.setOpaque(true);
        _hitsLft=pType;
        if(pType==UNO){
            _brickLabel.setBackground(Color.GREEN);
        }else if(pType== DOS){
            _brickLabel.setBackground(Color.BLUE);
        }else if(pType== TRES){
            _brickLabel.setBackground(Color.RED);
        }
    }
    
    public void setChangeColor(int pType){
        _hitsLft=pType;
        if(pType==UNO){
            _brickLabel.setBackground(Color.GREEN);
        }else if(pType== DOS){
            _brickLabel.setBackground(Color.BLUE);
        }else if(pType== TRES){
            _brickLabel.setBackground(Color.RED);
        }
    }
    
    public void destroyBrick(){
        _brickLabel.setLocation(SCREEN_X*DOS, CERO);
    }
    
    public JLabel getBrickLabel(){
        return _brickLabel;
    }
    
    public int getHitLft(){
        return _hitsLft;
    }
    
    public int getPosX(){
        return _Posx;
    }
    
    public int getPosY(){
        return _Posy;
    }
    
}
