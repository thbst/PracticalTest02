package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class ClientThreadReceive extends Thread {

    private Socket socket;
    private String address,city,info;
    private int port;
    private String key;
    private String result;
    private TextView resultTextView;

    public ClientThreadReceive(String clientAddress,int clientPort, String key){
        this.address = clientAddress;
        this.port = clientPort;
        this.key = key;
    }
//clasa ce updateaza resultTextView cu informatii


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
                printWriter.println(key);
                printWriter.flush();
                String wordInfo;
                while ((wordInfo = bufferedReader.readLine()) != null) {
                    Log.e("Result client", "Result " + wordInfo);
                    final String finalizedWeatherInformation = wordInfo;
                    resultTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            resultTextView.append(finalizedWeatherInformation + "\n");
                        }
                    });
                }
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