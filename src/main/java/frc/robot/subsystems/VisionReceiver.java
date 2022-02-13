package frc.robot.subsystems;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionReceiver extends SubsystemBase{
    private DatagramSocket socket;
    private InetAddress ip;
    private int port = 5808;

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
        char buf = [66537];
    }
    
}
