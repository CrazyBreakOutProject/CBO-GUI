/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author ellioth
 */
public class Ball implements Constantes{
    private int _Posx,_Posy,_pow;
    private JLabel _ballFigure;
    
    public Ball(int x, int y){
        _Posx=x;
        _Posy=y;
        _ballFigure= new JLabel(new ImageIcon(BALL_IMAGE));
        _ballFigure.setBounds(_Posx, _Posy, BALL_SIZE, BALL_SIZE);
    }
    
    public void setPos(int x, int y){
        _Posx=x;
        _Posy=y;
        _ballFigure.setLocation(_Posx, _Posy);
    }
    
    public void setPw(int type){
        _pow=type;
    }
    
    public JLabel getBallLable(){
        return _ballFigure;
    }
}
