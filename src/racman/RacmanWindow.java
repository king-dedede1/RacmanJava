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
        super();
        initComponents();

        pid = WebMAN.getCurrentPID();

    }

    private void savePosition(ActionEvent e)
    {
        savedPos = WebMAN.readMemory(pid, 0xda2870, 12);
    }

    private void loadPosition(ActionEvent e)
    {
        WebMAN.writeMemory(pid, 0xda2870, 12, savedPos);
    }

    private void die(ActionEvent e)
    {
        WebMAN.writeMemory(pid, 0xda2878, 4, floatToByteArray(-50.0f));
    }

    private void setupManips(ActionEvent e)
    {
        WebMAN.writeMemory(pid, 0xc9165c, 1, new byte[]{7});
        WebMAN.writeMemory(pid, 0xc36bcc, 1, new byte[]{3});
        WebMAN.writeMemory(pid, 0xef6098, 1, new byte[]{0xE});
        WebMAN.writeMemory(pid, 0xc4f918, 4, new byte[]{0,0,0,2});
        WebMAN.writeMemory(pid, 0x148a100, 4, new byte[]{0,0,0,1});

    }

    private void setAside(ActionEvent e)
    {
        WebMAN.writeMemory(pid, 0xd9ff02, 1, new byte[]{1});
    }

    private void loadFile(ActionEvent e)
    {
        WebMAN.writeMemory(pid, 0xd9ff01, 1, new byte[]{1});
    }

    private void enableSavefileHelper(ActionEvent e)
    {
        WebMAN.pauseRSX();

        // im lazy lol
        String tramp = "3821FF70F80100787C0802A6F80100707C0902A6F8010068F8610060F8810058F8A10050F8C10048F8E10040F9010038F9210030F9410028E80100787C0903A64E800421E9410028E9210030E9010038E8E10040E8C10048E8A10050E8810058E8610060E80100687C0903A6E80100707C0803A6382100904E800020";
        String lv2 = "3821FFF07D6802A6F96100007C6B1B787C8323787CA42B787CC533787CE63B787D0743787D284B787D49537844000002E96100007D6803A6382100104E800020";
        String inputp = "3821FFF87C0802A6F801FFF83F4000986340100048980503E801FFF87C0803A638210008E80101504E800020";
        String input = "9421FFD03D2000D93D4000D96129FF02614AFF0039000001990A0000894900002C0A0000418200AC3D4000D99381002093A100247C0802A693C10028614AFEFC3F8000CB9361001C3FA0009993E1002C3BC000009001003438E000003900000090EA00007D5F537899090000639C0A9863BD256C63DE8000392000003F600020815C00003C6901107FC5F3787FA903A6808A00047C844A144E800421813F00003D290001392980007C09D800913F00004180FFD0800100348361001C838100207C0803A683A1002483C1002883E1002C3D2000D96129FF0189490000280A000141820060280A000241820010382100304E800020600000003D4001A7614A0B30814A00002C0A001E4081FFE43D0001343D4001346108EBD438C00003614AEE7090C8000038E0010139000000B0EA000099090000382100304E800020600000003D2001107C0802A690010034812900002C0900004082008C93E1002C3FE000D9938100203F8000CB93A100243FA0009993C100283BC0000063FFFEFC9361001C639C0A9863BD256C913F000063DE80003F60002060000000815C00003C6901107FC5F3787FA903A6808A00047C844A144E800421813F00003D290001392980007C09D800913F00004180FFD08361001C8381002083A1002483C1002883E1002C3D20001E3C80011061291CF4386000007D2903A64E8004213D4000D938E00002614AFF013D2001A798EA0000390000008001003461290B307C0803A691090000382100304E8000200000001000000000017A5200047C41011B0C01000000006800000018FFFFFDBC0000022800410E304B9C04419D0341094100419E02439B05429F014211417F59DB41DC41064141DD41DE41DF480A0E00420B500A0E00420B420941004111417F449F01429C04429D03429E02439B0554DB41DC41DD41DE41DF4F0641420E0000";

        WebMAN.writeMemory(pid, 0x980500, tramp);
        WebMAN.writeMemory(pid, 0x980600, lv2);
        WebMAN.writeMemory(pid, 0x97C7E8, "489804CB");
        WebMAN.writeMemory(pid, 0x9804C8, inputp);
        WebMAN.writeMemory(pid, 0x981000, input);

        WebMAN.continueRSX();
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
