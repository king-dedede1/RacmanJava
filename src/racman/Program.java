package racman;

public class Program
{
    public static void main(String[] args)
    {
        var dialogResult = javax.swing.JOptionPane.showInputDialog("IP Address:");

        if (dialogResult != null)
        {
            WebMAN.ip = dialogResult;
            new RacmanWindow();
        }
    }
}
