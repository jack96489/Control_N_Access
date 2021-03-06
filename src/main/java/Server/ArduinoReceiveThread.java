package Server;


import Shared.Settings;
import Shared.SocketUDP;
import Shared.Sockets.StopAndWait;
import Shared.Sockets.UDPSocketUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * Riceve i dati dal client collegato all'Arduone e li elabora
 *
 * @author Giacomo Orsenigo
 */
public class ArduinoReceiveThread extends Thread {

    private final UDPSocketUtils socket;

    public ArduinoReceiveThread() throws SocketException {
        socket = new StopAndWait(Settings.SERVER_ARDUINO_PORT);
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        //final ServerManager dati = ServerManager.getInstance();
        while (true) {

            final String ris;
            try {
                ris = socket.receiveString();
                System.out.println("Ricevuto: " + ris);

                try {
                    // open a connection to the site
                    URL url = new URL("https://accesscontrol.altervista.org/receiveData.php");
                    URLConnection con = url.openConnection();
                    // activate the output
                    con.setDoOutput(true);
                    PrintStream ps = new PrintStream(con.getOutputStream());
                    // send your parameters to your site
                    ps.print("cod=" + ris);

                    // we have to get the input stream in order to actually send the
                    // request
                    System.out.println(new String(con.getInputStream().readAllBytes()));

                    // close the print stream
                    ps.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {
//                e.printStackTrace();
            }

        }
    }
}
