package metropolia.edu.musicrecommendations;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * Created by petri on 23.4.2016.
 */
public class SendDataService extends Service {
    public JSONObject json = new JSONObject();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            json.put("artist", intent.getStringExtra("artist"));
            json.put("track", intent.getStringExtra("track"));
            Thread thread = new Thread(new ClientThread());
            thread.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class ClientThread implements Runnable{
        private String ipAddress = "37.59.140.12";
        private int serverPort = 8181;
        private boolean isConnected = false;
        @Override
        public void run() {
            try {
                InetAddress inetAddress = InetAddress.getByName(ipAddress);
                Socket socket = new Socket(inetAddress, serverPort);
                isConnected = true;
                while(isConnected){
                    Log.d("ClientThread", "Sending...");
                    try(OutputStreamWriter out = new OutputStreamWriter(
                            socket.getOutputStream(), StandardCharsets.UTF_8)){
                        out.write(json.toString());
                        Log.d("ClientThread", json.toString());
                    } catch (Exception e){
                        e.printStackTrace();
                        isConnected = false;
                    }
                }
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                isConnected = false;
            }
        }
    }
}
