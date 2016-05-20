package ro.pub.cs.systems.eim.practicaltest02.network;

import android.content.Context;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;
import ro.pub.cs.systems.eim.practicaltest02.graphicuserinterface.PracticalTest02MainActivity;
import ro.pub.cs.systems.eim.practicaltest02.model.TimeInformation;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;


    HashMap<String, HashValue> hashData = new HashMap<String, HashValue>();


    public class HashValue{
        String value;
        int date;

        public HashValue(String Value, int createDate){
            this.value = Value;
            this.date = createDate;
        }
        public String GetValue(){
            return this.value;
        }

        public int GetDate(){
            return this.date;
        }

    }

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }


    @Override
    public void run() {
        if (socket != null) {
            try {
                BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter    printWriter    = Utilities.getWriter(socket);
                if (bufferedReader != null && printWriter != null) {
                    //Log.d("Debug", "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
                    String word = bufferedReader.readLine();

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Constants.WEB_SERVICE_ADDRESS);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    String pageSourceCode = httpClient.execute(httpPost, responseHandler);
                    int minut=0;
                    if (pageSourceCode != null) {
                        //serverThread.setData(pageSourceCode);
                        minut = Integer.parseInt(pageSourceCode.substring(16,17));
                        //printWriter.println(pageSourceCode);
                        //printWriter.flush();

                    } else {
                        Log.e("Error", "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                    }
                    //Log.d("Debug", "[COMMUNICATION THREAD] GetData ");
                    //String data = serverThread.getData();
                    //Log.d("Debug", "[COMMUNICATION THREAD] I got = "+data);
                    if (word != null && !word.isEmpty() ) {
                        String delims = "[;]";
                        String[] tokens = word.split(delims);

                        //TODO
                        if(tokens.length == 1){
                            //get
                            if(hashData.containsKey(tokens[0])){
                                if(minut - hashData.get(tokens[0]).date <= 1)
                                    printWriter.println(hashData.get(tokens[0]).value);
                                printWriter.flush();
                            }

                        }else if(tokens.length == 2){
                            //put
                            hashData.put(tokens[0], new HashValue(tokens[1], minut));
                        }



                        //Log.d("Debug", "[COMMUNICATION THREAD] Getting the information from the webservice...");

                    }
                } else {
                    Log.e("Error", "[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
                }
                socket.close();
            } catch (IOException ioException) {
                Log.e("Error", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                ioException.printStackTrace();
            }
        } else {
            Log.e("Error", "[COMMUNICATION THREAD] Socket is null!");
        }
    }
}
