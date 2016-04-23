package metropolia.edu.musicrecommendations;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by petri on 23.4.2016.
 */
public class SocketServerThread extends Thread {
    private int SocketServerPort = 6000;
    private String trackList;

    @Override
    public void run(){
        Socket socket = null;
        ServerSocket serverSocket = null;
        DataInputStream dataInputStream = null;

        try {
            Log.i("SocketServerThread", "Creating server socket");
            serverSocket = new ServerSocket(SocketServerPort);

            while(true){
                socket = serverSocket.accept();
                dataInputStream = new DataInputStream(
                        socket.getInputStream());

                String messageFromClient;

                //If no message sent from client, this code will block the Thread
                messageFromClient = dataInputStream.readUTF();

                final JSONObject jsondata;

                try {
                    jsondata = new JSONObject(messageFromClient);
                    Log.d("SocketServerThread", "Message received");
                    MainActivity.prosessTracks.prosessTrackList(jsondata);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
