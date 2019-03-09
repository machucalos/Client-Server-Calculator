package example.calculator;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

public class Client extends Thread {

    private Socket socket;
    final  String TAG = "efef";
    private char op;
    private double v1;
    private  double v2;
    private volatile  String result;
    Client(char op, double v1, double v2){
        this.op = op;
        this.v1 = v1;
        this.v2 = v2;
    }
    public String getResult(){
        return result;
    }

    public void run(){
        Socket socket = null;
        try{
            String address ="10.0.2.2";
            Log.d(TAG,"Test");
            int port = 9000;
            socket = new Socket(address, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.writeObject(op);
            out.writeObject(v1);
            out.writeObject(v2);
            out.flush();

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            result = (String)in.readObject();
            System.out.println(result);
            out.close();
            in.close();


            //OutputStream out = socket.getOutputStream();
            socket.close();



        }catch (IOException e)
        {
          Log.e(TAG, "Socket", e);
          return;
        }catch(ClassNotFoundException e){
            Log.e(TAG, "Object stream", e);
        }

    }
}
