package frc.robot.subsystems;

import frc.robot.Robot;

import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionReceiver extends SubsystemBase{
    private DatagramSocket socket;
    private InetAddress ip;

    private int port = 5808;
    private int msgPort = 5805;
    private int bufferLength = 65537;

    private int ticks = 0;

    private boolean clientAddrExists = false;


    public VisionReceiver(){
        setupSocket();
    }

    private void setupSocket(){
        try {
            socket = new DatagramSocket();
        } catch(SocketException ex){
            ex.printStackTrace();
        }

        try {
            ip = InetAddress.getByName("raspberrypi.local");
            System.out.println(ip);
        } catch(UnknownHostException ex) {
            ex.printStackTrace();
        }
        
    }
    
    @Override
    public void periodic(){

        if(ticks++ % 50 == 0){
            sendControlHeartbeat();
        }

        /* 2019 periodic handles getting vision targets and starting autoDrive
        as we're not using vision targets that isn't necessary

        byte[] buf = new byte[bufferLength];

        DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
        socket.receive(receivePacket);

        if (receivePacket.getLength() > 0){
            clientAddrExists = true;
            receivePacket.getAddress();
        }
        */
    }
    
    private void sendControlHeartbeat(){
        String msg = (Robot.instance.isEnabled() ? "ENABLE" : "DISABLE") + " DRIVEOFF";
        sendControlMessage(msg);
    }

    private void sendControlMessage(String msg){
        if(clientAddrExists){
            DatagramSocket fd;
            try{
                fd = new DatagramSocket(msgPort);
            } catch(SocketException ex){
                ex.printStackTrace();
                return;
            }
            
            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length);
            
            try {
                fd.send(packet);
            } catch(IOException ex){
                ex.printStackTrace();
            }
            fd.close();
        }
    }
    
}
