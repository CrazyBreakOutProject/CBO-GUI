/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba_cliente_sender;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import GUI.Constantes;
/**
 *
 * @author ellioth
 */

public class Cliente_Test implements Runnable, Constantes {
    private int _port;
    private String _ip;
    private Socket _socket;
    private BufferedReader _inFromServer;
    private DataOutputStream _outToServer;
    
    public Cliente_Test(int pPort, String pIp){
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
        this.run();
    }
    
    public void SendMsg(){
        String pMensaje;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                System.out.println("ingrese mensaje");
                pMensaje= br.readLine();
                _outToServer= new DataOutputStream(_socket.getOutputStream());
                _outToServer.write(pMensaje.getBytes());
                _outToServer.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        String _msgFromServer="";
        try {
            _inFromServer= new BufferedReader (new InputStreamReader(_socket.getInputStream()));
            while(true){
                    String temp=_inFromServer.readLine();
                    synchronized(this){
                        _msgFromServer=temp;
                    }
                    System.out.println(_msgFromServer);
            }
        }
         catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
}
