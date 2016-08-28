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
    private BufferedReader _inFromServer;
    private DataOutputStream _outToServer;
    private String _msgFromServer;
    private boolean _flagMsgFromServer;
    
    public cliente(int pPort, String pIp){
        _ip=pIp;
        _port=pPort;
        Socket clientSocket;
        try {
            clientSocket = new Socket(_ip, _port);
            _outToServer = new DataOutputStream(clientSocket.getOutputStream());
            _inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //sentence = inFromUser.readLine();
            //outToServer.writeBytes(sentence + '\n');
            //modifiedSentence = inFromServer.readLine();
            //System.out.println("FROM SERVER: " + modifiedSentence);
            //clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void SendMsg( String pMensaje){
        try {
            _outToServer.writeUTF(pMensaje);
            //System.out.println(pMensaje);
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        _flagMsgFromServer=false;
        while(true){
            try {
                if(!_flagMsgFromServer){
                    synchronized(this){
                    _msgFromServer=_inFromServer.readLine();
                    _flagMsgFromServer=true;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
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
