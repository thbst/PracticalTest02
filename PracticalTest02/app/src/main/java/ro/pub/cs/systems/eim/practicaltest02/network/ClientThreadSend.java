package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

//clasa ce imi trimite comanda catre server

public class ClientThreadSend extends Thread {

    private Socket socket;
    private String address;
    private int port;
    private String key;
    private String value;
    private String result;
    private TextView resultTextView;

    public ClientThreadSend(String clientAddress,int clientPort, String key, String value){
        this.address = clientAddress;
        this.port = clientPort;
        this.key = key;
        this.value = value;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e("Error", "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter    = Utilities.getWriter(socket);
            if (bufferedReader != null && printWriter != null) {
                printWriter.println(key + ";" + value);
            } else {
                Log.e("Error", "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
            }
            socket.close();
        } catch (IOException ioException) {
            Log.e("Error", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();
        }
    }
}
