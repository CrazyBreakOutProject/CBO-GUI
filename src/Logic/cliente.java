/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ellioth
 */
public class cliente implements Runnable, Constantes{
    private int _port;
    private String _ip;
    private Socket _socket;
    private BufferedReader _inFromServer;
    private DataOutputStream _outToServer;
    private String _msgFromServer;
    
    public cliente(int pPort, String pIp){
        _ip=pIp;
        _port=pPort;
        try {
            InetAddress ip= InetAddress.getByName(_ip);
            _socket= new Socket(ip, _port);
            _outToServer= new DataOutputStream(_socket.getOutputStream());
            _inFromServer= new BufferedReader (new InputStreamReader(_socket.getInputStream()));
        } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("error host desconocido");
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("error IO");
        }
    }
    
    public void alertServerViewer(){
        try {
            _outToServer.write(ESPECTADOR.getBytes());
            _outToServer.flush();
            _msgFromServer= _inFromServer.readLine();
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alertServerPlayer(){
        try {
            _outToServer.write(GAMER.getBytes());
            _outToServer.flush();
            _msgFromServer= _inFromServer.readLine();
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void SendMsg( String pMensaje){
        try {
            _outToServer.write(pMensaje.getBytes());
            _outToServer.flush();
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        String temp;
        try {
            while(true){
                temp=_inFromServer.readLine();
                synchronized(this){
                    _msgFromServer=temp;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getMsgFromServer(){
        String temp;
        synchronized(this){
            temp=_msgFromServer;
        }
        return temp;
    }
    
    public int getPort(){
        return _port;
    }
    
    public String getIP(){
        return _ip;
    }
}
