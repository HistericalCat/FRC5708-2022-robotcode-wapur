package frc.robot.subsystems;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionReceiver extends SubsystemBase{
    private DatagramSocket socket;
    private InetAddress ip;

    public VisionReceiver(){
        setupSocket();
    }

    private void setupSocket(){
        try {
            ip = InetAddress.getByName("raspberrypi.local");
        System.out.println(ip);
        } catch(UnknownHostException ex) {
            ex.printStackTrace();
        }
        
    }
}
