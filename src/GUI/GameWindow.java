/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Logic.Constantes;
import Logic.Ball;
import Logic.Player;
import Logic.Brick;
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
import Logic.cliente;
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
    private boolean _flagForTypeOfConnection, _flagForContinueGame;
    private JLabel _ScoreLabel;
    private JLabel _ScoreTitleLabel;
    private int _Score, _playerStatesToScreen;
    
    /**
     * constructor que establece los datos iniciales y recibe el primer
     * mensaje que proviene del servidor.
     * @param pCliente dato de la clase cliente para realizar la conexion 
     * contra el servidor.
     */
    public GameWindow(cliente pCliente){
        _flagForContinueGame=true;
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
        //_clienteEspectador.SendMsg(ESPECTADOR);
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
        if(_flagForTypeOfConnection){
            _clienteJugador= new cliente(_clienteEspectador.getPort(), 
                    _clienteEspectador.getIP());
            _clienteJugador.alertServerPlayer();
        }
        while(_flagForContinueGame){
            setNewJsonMsg(_clienteEspectador.getMsgFromServer());
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
            //obtenemos los datos de todos los jugadores actualmente conectados.
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
                    this.add(_plyrs.get(i).getPLayerLabel());
                }
                _playerStatesToScreen=ObjPlayers.length();
            }
            //obtenemos los datos nuevos del bloque que acabos de cambiar.
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
            //aqui vamos obteniendo la posicion actual de la pelota.
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
            //revisamos si ya tenemos que terminar el juego.
            if(obj.optBoolean(TERMINATE)){
                System.out.println("Juego terminado");
                synchronized(this){
                    _flagForContinueGame=false;
                }
                this.setVisible(false);
                this.dispose();
                return;
            }
            //obtenemos todos los datos de los jugadores actualmente conectados.
            JSONObject ObjPlayers =obj.optJSONObject(PLAYERS);
            if(ObjPlayers!=null){
                String tempJsonPlyrs;
                for(int i=0;i<ObjPlayers.length();i++){
                    tempJsonPlyrs= ObjPlayers.getString("p"+ String.valueOf(i));
                    int IndexComa=tempJsonPlyrs.indexOf(",");
                    int pX= Integer.parseInt(tempJsonPlyrs.substring(CERO,IndexComa));
                    int pY= Integer.parseInt(tempJsonPlyrs.substring(IndexComa+ 
                            UNO,tempJsonPlyrs.length()));
                    //agregamos a la lista al nuevo jugador
                    if(_plyrs.size()==i){
                        _plyrs.add(new Player(i));
                    }
                    //establecemos sus datos.
                    _plyrs.get(i).setPosi(pX, pY);
                }
                //ciclo para agregar las nuevas paletas a la pantalla
                for(int i = _playerStatesToScreen; i<ObjPlayers.length(); i++){
                    this.add(_plyrs.get(i).getPLayerLabel());
                }
                _playerStatesToScreen=ObjPlayers.length();
            }
            //obtenemos la informacion del bloque ya esta golpeado.
            JSONObject ObjBricks =obj.optJSONObject(BRICK_HIT);
            if(ObjBricks!=null){
                int id= ObjBricks.getInt(ID);
                if(id>-UNO){
                    int power=ObjBricks.getInt(BRICK_DEL);
                    if(power>(-UNO))
                        _bricks.get(id).setChangeColor(power);
                    else{
                        _bricks.get(id).destroyBrick();
                    }
                }
            }
            //obtenemos la nueva posicion de la pelota.
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
        //si somos un jugador y apretamos la tecla derecha 
        if((e.getKeyCode()== KeyEvent.VK_RIGHT || 
                e.getKeyCode()== KeyEvent.VK_A) 
                && _flagForTypeOfConnection){
            synchronized(this){
                _clienteJugador.SendMsg(RIGHT);
            }
            System.out.println(RIGHT);
        }
        else if((e.getKeyCode()== KeyEvent.VK_LEFT || 
                e.getKeyCode()== KeyEvent.VK_D) 
                && _flagForTypeOfConnection){
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
}
