package racman;

import java.util.Arrays;

public class Program
{
    public static void main(String[] args)
    {
        WebMAN.ip = (String) javax.swing.JOptionPane.showInputDialog("IP Address:");

        new RacmanWindow();
    }
}
