package racman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RacmanWindow extends JFrame
{
    private JButton savfileHelperButton,
                    setasideButton,
                    loadButton,
                    setupManipButton,
                    loadPosButton,
                    savePosButton,
                    dieButton;
    int pid;
    byte[] savedPos;

    public RacmanWindow()
    {
        initComponents();
        pid = WebMAN.getCurrentPID();
        WebMAN.notify("RaCMAN-J Connected");
    }

    private void savePosition(ActionEvent e)
    {
        savedPos = WebMAN.readMemory(pid, 0xda2870, 12);
    }

    private void loadPosition(ActionEvent e)
    {
        WebMAN.writeMemory(pid, 0xda2870, savedPos);
    }

    private void die(ActionEvent e)
    {
        WebMAN.writeMemory(pid, 0xda2878, floatToByteArray(-50.0f));
    }

    private void setupManips(ActionEvent e)
    {
        WebMAN.writeMemory(pid, 0xc9165c, new byte[]{7});
        WebMAN.writeMemory(pid, 0xc36bcc, new byte[]{3});
        WebMAN.writeMemory(pid, 0xef6098, new byte[]{0xE});
        WebMAN.writeMemory(pid, 0xc4f918, new byte[]{0,0,0,2});
        WebMAN.writeMemory(pid, 0x148a100, new byte[]{0,0,0,1});

        WebMAN.notify("Manips set up for NG+/No QE");
    }

    private void setAside(ActionEvent e)
    {
        WebMAN.writeMemory(pid, 0xd9ff02, new byte[]{1});
    }

    private void loadFile(ActionEvent e)
    {
        WebMAN.writeMemory(pid, 0xd9ff01, new byte[]{1});
    }

    private void enableSavefileHelper(ActionEvent e)
    {
        WebMAN.pauseRSX();

        // im lazy lol
        byte[] input = Util.readFileBytes("res/input.bin");
        byte[] inputp = Util.readFileBytes("res/inputp.bin");
        byte[] lv2 = Util.readFileBytes("res/lv2.bin");
        byte[] tramp = Util.readFileBytes("res/tramp.bin");

        WebMAN.writeMemory(pid, 0x980500, tramp);
        WebMAN.writeMemory(pid, 0x980600, lv2);
        WebMAN.writeMemory(pid, 0x97C7E8, "489804CB");
        WebMAN.writeMemory(pid, 0x9804C8, inputp);
        WebMAN.writeMemory(pid, 0x981000, input);

        WebMAN.continueRSX();

        WebMAN.notify("Enabled savefile heper mod");
    }

    private void initComponents()
    {
        this.setSize(100,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("RaCMAN");
        this.setVisible(true);
        this.setLayout(new FlowLayout());

        this.savfileHelperButton = new JButton("Enable Savefile Helper");
        this.savfileHelperButton.addActionListener(this::enableSavefileHelper);
        this.add(this.savfileHelperButton);

        this.setasideButton = new JButton("Set aside file");
        this.setasideButton.addActionListener(this::setAside);
        this.add(this.setasideButton);

        this.loadButton = new JButton("Load file");
        this.loadButton.addActionListener(this::loadFile);
        this.add(this.loadButton);

        this.setupManipButton = new JButton("Setup NG+/No QE");
        this.setupManipButton.addActionListener(this::setupManips);
        this.add(this.setupManipButton);

        this.loadPosButton = new JButton("Load Position");
        this.loadPosButton.addActionListener(this::loadPosition);
        this.add(this.loadPosButton);

        this.savePosButton = new JButton("Save Position");
        this.savePosButton.addActionListener(this::savePosition);
        this.add(this.savePosButton);

        this.dieButton = new JButton("Die");
        this.dieButton.addActionListener(this::die);
        this.add(this.dieButton);
    }


    // both of these stolen from internet
    public static byte[] intToByteArray(int source) {

        byte[] result = new byte[4];

        for (int i = 0; i < 4; ++i) {

            result[3 - i] = (byte) ((source & (0xff << (i << 3))) >>> (i << 3));

        }

        return result;
    }

    public static byte[] floatToByteArray(float source) {
        return intToByteArray(Float.floatToRawIntBits(source));
    }
}
