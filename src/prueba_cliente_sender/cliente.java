/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba_cliente_sender;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ellioth
 */
public class cliente implements Runnable{
    private int _port;
    private String _ip;
    private Socket _socket;
    private BufferedReader _inFromServer;
    private DataOutputStream _outToServer;
    private String _msgFromServer;
    private boolean _flagMsgFromServer;
    
    public cliente(int pPort, String pIp){
        _ip=pIp;
        _port=pPort;
        try {
            InetAddress ip= InetAddress.getByName(_ip);
            _socket= new Socket(ip, _port);
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
    
    public void SendMsg( String pMensaje){
        try {
            _outToServer= new DataOutputStream(_socket.getOutputStream());
            _outToServer.write(pMensaje.getBytes());
            _outToServer.flush();
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        _flagMsgFromServer=false;
        try {
            _inFromServer= new BufferedReader (new InputStreamReader(_socket.getInputStream()));
            while(true){
                synchronized(this){
                    _msgFromServer=_inFromServer.readLine();
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
    
    public boolean getFlagMsgFromServer(){
        boolean temp;
        synchronized(this){
            temp=_flagMsgFromServer;
        }
        return temp;
    }
    
    public void setOffFlagMsgFromServer(){
        synchronized(this){
            _flagMsgFromServer=false;
        }
    }
    
    public int getPort(){
        return _port;
    }
    
    public String getIP(){
        return _ip;
    }
}
