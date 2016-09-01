/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import prueba_cliente_sender.cliente;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ellioth
 */
public class GameWindow extends JFrame implements KeyListener, Constantes, Runnable{
    private cliente _clienteEspectador;
    private cliente _clienteJugador;
    private List<Ball> _balls;
    private List<Brick> _bricks;
    private List<Player> _plyrs;
    private boolean _flagForTypeOfConnection;
    private JLabel _ScoreLabel;
    private JLabel _ScoreTitleLabel;
    private int _Score;
    
    /**
     * constructor que establece los datos iniciales y recibe el primer
     * mensaje que proviene del servidor.
     * @param pCliente dato de la clase cliente para realizar la conexion 
     * contra el servidor.
     */
    public GameWindow(cliente pCliente){
        //_flagForTypeOfConnection=flag
        _balls= new ArrayList<>();
        _bricks= new ArrayList<>();
        _plyrs= new ArrayList<>();
        for( int j =0; j<ROW_BRICK; j++){
            for(int i=0; i<COL_BRICK; i++){
                _bricks.add(new Brick(j*BRICK_SIZE,(i*BRICK_SIZE)+(CINCUENTA),UNO));
            }
        }
        _balls.add(new Ball(SCREEN_X/DOS-BALL_SIZE,POS_Y_PLY-(BALL_SIZE+CINCO)));
        _clienteEspectador= pCliente;
        _clienteEspectador.SendMsg(ESPECTADOR);
        //(new Thread(_clienteEspectador)).start();
        setFirstData(_clienteEspectador.getMsgFromServer());
        //createScreen();
    }
    
    /**
     * metodo para crear la pantalla y establece todos los datos en pantalla
     * una vez recibidos y establecido el primer mensaje.
     */
    public void createScreen(){
        setLayout(null);
        setTitle("Crazy Break Out");
        setBackground(Color.GRAY);
        setSize(SCREEN_X,SCREEN_Y);
        setMinimumSize(new Dimension(SCREEN_X,SCREEN_Y));
        //setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        _ScoreTitleLabel= new JLabel();
        _ScoreTitleLabel.setText(SCORE+":");
        _ScoreTitleLabel.setBounds(10, 550, 50, 20);
        _ScoreLabel= new JLabel();
        _ScoreTitleLabel.setText(String.valueOf(_Score));
        _ScoreTitleLabel.setBounds(60, 550, 120, 20);
        add(_ScoreTitleLabel);
        add(_ScoreLabel);
        for(int i =0; i<_balls.size(); i++){
            add(_balls.get(i).getBallLable());
        }
        for(int i =0; i<_bricks.size(); i++){
            add(_bricks.get(i).getBrickLabel());
        }
        for(int i =0; i<_plyrs.size(); i++){
            add(_plyrs.get(i).getPLayerLabel());
        }
        pack();
    }
    
    /**
     * metodo que establece la bandera para el tipo de conexion que
     * vamos a establecer contra el servidor.
     * @param pFalg la bandera que indicara el tipo de conexion.
     */
    public void setFalgForPlayer(boolean pFalg){   
        _flagForTypeOfConnection=pFalg;
        if(_flagForTypeOfConnection)
            addKeyListener(this);
    }
    
    /**
     * hilo donde ejecutaremos el recibidor de mensajes por parte del 
     * servidor.
     */
    @Override
    public void run() {
        String msgFromServer;
        if(_flagForTypeOfConnection){
            _clienteJugador= new cliente(_clienteEspectador.getPort(), 
                    _clienteEspectador.getIP());
            _clienteJugador.SendMsg(GAMER);
        }
        while(true){
            if(_clienteEspectador.getFlagMsgFromServer()){
                msgFromServer=_clienteEspectador.getMsgFromServer();
                _clienteEspectador.setOffFlagMsgFromServer();
                setNewJsonMsg(msgFromServer);
            }
        }
    }
    
    /**
     * metodo para establecer el primer Json de parte del servidor
     * para tener igualdad contra el servidor.
     * @param pMsg 
     */
    public void setFirstData( String pMsg){
        try {
            JSONObject obj = new JSONObject(pMsg);
            JSONObject ObjPlayers =obj.optJSONObject(PLAYERS);
            if(ObjPlayers!=null){
                String tempJsonPlyrs;
                for(int i=0;i<ObjPlayers.length();i++){
                    tempJsonPlyrs= ObjPlayers.getString("p"+ String.valueOf(i));
                    int IndexComa=tempJsonPlyrs.indexOf(",");
                    int pX= Integer.parseInt(tempJsonPlyrs.substring(CERO,IndexComa));
                    int pY= Integer.parseInt(tempJsonPlyrs.substring(IndexComa+
                            UNO,tempJsonPlyrs.length()));
                    _plyrs.add(new Player(i));
                    _plyrs.get(i).setPosi(pX, pY);
                }
            }
            JSONObject ObjBricks =obj.optJSONObject(BRICKS);
            if(ObjBricks!=null){
                String tempJsonBricks;
                for(int i=0;i<ObjBricks.length();i++){
                    tempJsonBricks= ObjBricks.getString("b"+ String.valueOf(i));
                    int IndexComa=tempJsonBricks.indexOf(",");
                    int id= Integer.parseInt(tempJsonBricks.substring(CERO,IndexComa));
                    int power= Integer.parseInt(tempJsonBricks.substring(
                            IndexComa+UNO,tempJsonBricks.length()));
                    if(power>(-UNO))
                        _bricks.get(id).setChangeColor(power);
                    else{
                        _bricks.get(id).destroyBrick();
                    }
                }
            }
            JSONObject ObjBalls =obj.optJSONObject(BALL_POS);
            if(ObjBalls!=null){
                String tempJsonBricks;
                for(int i=0;i<ObjBalls.length();i++){
                    tempJsonBricks= ObjBalls.getString(POS+ String.valueOf(i));
                    int IndexComa=tempJsonBricks.indexOf(",");
                    int pX= Integer.parseInt(tempJsonBricks.substring(CERO,IndexComa));
                    int pY= Integer.parseInt(tempJsonBricks.substring(IndexComa+
                            UNO,tempJsonBricks.length()));
                    _balls.get(i).setPos(pX, pY);
                }
            }
            _Score =obj.getInt(SCORE);
            
        } catch (JSONException ex) {
            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * metodo para establecer los nuevos datos provenientes del 
     * servidor. 
     * @param pMsg 
     */
    public void setNewJsonMsg(String pMsg){
        try {
            JSONObject obj = new JSONObject(pMsg);
            JSONObject ObjPlayers =obj.optJSONObject(PLAYERS);
            if(ObjPlayers!=null){
                String tempJsonPlyrs;
                for(int i=0;i<ObjPlayers.length();i++){
                    tempJsonPlyrs= ObjPlayers.getString("p"+ String.valueOf(i));
                    int IndexComa=tempJsonPlyrs.indexOf(",");
                    int pX= Integer.parseInt(tempJsonPlyrs.substring(CERO,IndexComa));
                    int pY= Integer.parseInt(tempJsonPlyrs.substring(IndexComa+ 
                            UNO,tempJsonPlyrs.length()));
                    _plyrs.add(new Player(i));
                    _plyrs.get(i).setPosi(pX, pY);
                }
            }
            JSONObject ObjBricks =obj.optJSONObject(BRICK_HIT);
            if(ObjBricks!=null){
                int id= ObjBricks.getInt(ID);
                int power=ObjBricks.getInt(BRICK_DEL);
                if(power>(-UNO))
                    _bricks.get(id).setChangeColor(power);
                else{
                    _bricks.get(id).destroyBrick();
                }
            }
            JSONObject ObjBalls =obj.optJSONObject(BALL_POS);
            if(ObjBalls!=null){
                String tempJsonBricks;
                for(int i=0;i<ObjBalls.length();i++){
                    tempJsonBricks= ObjBalls.getString(POS+ String.valueOf(i));
                    int IndexComa=tempJsonBricks.indexOf(",");
                    int pX= Integer.parseInt(tempJsonBricks.substring(CERO,IndexComa));
                    int pY= Integer.parseInt(tempJsonBricks.substring(IndexComa+ 
                            UNO,tempJsonBricks.length()));
                    _balls.get(i).setPos(pX, pY);
                }
            }
            _Score =obj.getInt(SCORE);
            
        } catch (JSONException ex) {
            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * metodo que vamos a utilizar para la deteccion de las 
     * teclas cuando utilicemos el programa como un player.
     * @param e 
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_RIGHT && _flagForTypeOfConnection){
            synchronized(this){
                _clienteJugador.SendMsg(RIGHT);
            }
            System.out.println(RIGHT);
        }
        else if(e.getKeyCode()== KeyEvent.VK_LEFT && _flagForTypeOfConnection){
            synchronized(this){
                _clienteJugador.SendMsg(LEFT);
            }
            System.out.println(LEFT);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    /*public static void main(String[] args) {
        GameWindow nueva= new GameWindow(null);
        String JsonTemplateExampleFirstMsg= "{\"players\":-1,\"bricks\":{\"b0\":"
                + "\"0,1\",\"b1\":\"1,3\",\"b2\":\"2,2\",\"b3\":\"3,1\",\"b4\":"
                + "\"4,1\",\"b5\":\"5,2\",\"b6\":\"6,1\",\"b7\":\"7,2\",\"b8\":"
                + "\"8,2\",\"b9\":\"9,3\",\"b10\":\"10,2\",\"b11\":\"11,2\","
                + "\"b12\":\"12,3\",\"b13\":\"13,2\",\"b14\":\"14,2\",\"b15\":"
                + "\"15,1\",\"b16\":\"16,3\",\"b17\":\"17,2\",\"b18\":\"18,3\","
                + "\"b19\":\"19,1\",\"b20\":\"20,3\",\"b21\":\"21,3\",\"b22\":"
                + "\"22,2\",\"b23\":\"23,3\",\"b24\":\"24,2\",\"b25\":\"25,3\","
                + "\"b26\":\"26,1\",\"b27\":\"27,1\",\"b28\":\"28,2\",\"b29\":"
                + "\"29,1\",\"b30\":\"30,3\",\"b31\":\"31,2\",\"b32\":\"32,1\","
                + "\"b33\":\"33,2\",\"b34\":\"34,2\",\"b35\":\"35,2\",\"b36\":"
                + "\"36,1\",\"b37\":\"37,1\",\"b38\":\"38,2\",\"b39\":\"39,3\","
                + "\"b40\":\"40,3\",\"b41\":\"41,3\",\"b42\":\"42,3\",\"b43\":"
                + "\"43,1\",\"b44\":\"44,2\",\"b45\":\"45,2\",\"b46\":\"46,1\","
                + "\"b47\":\"47,1\",\"b48\":\"48,3\",\"b49\":\"49,1\",\"b50\":"
                + "\"50,1\",\"b51\":\"51,1\",\"b52\":\"52,3\",\"b53\":\"53,3\","
                + "\"b54\":\"54,3\",\"b55\":\"55,3\",\"b56\":\"56,3\",\"b57\":"
                + "\"57,2\",\"b58\":\"58,1\",\"b59\":\"59,2\",\"b60\":\"60,2\","
                + "\"b61\":\"61,3\",\"b62\":\"62,1\",\"b63\":\"63,3\"},"
                + "\"ballPos\":{\"pos0\":\"380,455\"},\"score\":0}";
        nueva.setFirstData(JsonTemplateExampleFirstMsg);
        nueva.createScreen();
    }*/
}
