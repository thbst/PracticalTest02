package ro.pub.cs.systems.eim.practicaltest02.graphicuserinterface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientThreadSend;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientThreadReceive;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
//    private EditText cityEditText = null;
    private EditText commandEditText = null;
//    private Spinner informationTypeSpinner = null;
//    private Button getWeatherForecastButton = null;
    private Button submitButton = null;
//    private TextView weatherForecastTextView = null;
    private TextView resultTextView = null;

    private ServerThread serverThread = null;
    private ClientThreadSend clientThreadSend = null;
    private ClientThreadReceive clientThreadReceive = null;

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


    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Server port should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() != null) {
                serverThread.start();
            } else {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
            }

        }
    }

    private SubmitButtonClickListener submitButtonClickListener = new SubmitButtonClickListener();
    private class SubmitButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort    = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }

            String command = commandEditText.getText().toString();
//            String informationType = informationTypeSpinner.getSelectedItem().toString();
            if ((command == null || command.isEmpty())) {
                Toast.makeText(
                        getApplicationContext(),
                        "Parameters from client should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
//TODO
            resultTextView.setText(Constants.EMPTY_STRING);


            String cmd = commandEditText.getText().toString();
            ClientThreadSend clientThread = new ClientThreadSend(clientAddress, Integer.parseInt(clientPort),"IP", "");
            clientThread.start();
//            clientThread = new ClientThread(
//                    clientAddress,
//                    Integer.parseInt(clientPort),
//                    city,
//                    informationType,
//                    weatherForecastTextView);
//            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
//        cityEditText = (EditText)findViewById(R.id.city_edit_text);
        commandEditText = (EditText)findViewById(R.id.command_edit_text);
//        informationTypeSpinner = (Spinner)findViewById(R.id.information_type_spinner);
//        getWeatherForecastButton = (Button)findViewById(R.id.get_weather_forecast_button);
        submitButton = (Button)findViewById(R.id.submit_button);
        submitButton.setOnClickListener(submitButtonClickListener);
//        weatherForecastTextView = (TextView)findViewById(R.id.weather_forecast_text_view);
        resultTextView = (TextView)findViewById(R.id.result_text);
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
