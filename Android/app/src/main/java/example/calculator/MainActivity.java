package example.calculator;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton add;
    ImageButton sub;
    ImageButton div;
    ImageButton mul;
    EditText t1;
    EditText t2;

    static TextView dply_result;
    private static class Client extends AsyncTask<Object,Void, String>{
        private String result = null;
        private String TAG = "Async Task";
        @Override
        protected String doInBackground(Object... objs) {
            Socket socket = null;
            try{
                char op = (char)objs[0];
                double v1 = (double)objs[1];
                double v2 = (double)objs[2];

                // USED FOR COMPUTER ADDRESS AVD
                String address ="10.0.2.2";

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

                socket.close();



            }catch (IOException e)
            {
                Log.e(TAG, "Socket", e);
                result ="Connection Error";
                return result;
            }catch(ClassNotFoundException e){
                Log.e(TAG, "Object stream", e);
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            dply_result.setText(result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = findViewById(R.id.btn_add);
        sub = findViewById(R.id.btn_sub);
        div = findViewById(R.id.btn_div);
        mul = findViewById(R.id.btn_mul);

        t1 = findViewById(R.id.tvw_num1);
        t2 = findViewById(R.id.tvw_num2);
        dply_result = findViewById(R.id.tvw_result);

        add.setOnClickListener(this);
        mul.setOnClickListener(this);
        div.setOnClickListener(this);
        sub.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        char oper ='\u0000';

        double n1 = 0;
        double n2 = 0;
        double result = 0;

        String tv_1 = t1.getText().toString();
        String tv_2 = t2.getText().toString();
        if(tv_1.matches("") || tv_2.matches("")  )
            return;
        n1 = Double.parseDouble(tv_1);
        n2 = Double.parseDouble(tv_2);

        switch (v.getId())
        {
            case R.id.btn_add:
                oper = '+';
                break;
            case R.id.btn_sub:
                oper = '-';
                break;
            case R.id.btn_div:
                oper = '/';
                break;
            case R.id.btn_mul:
                oper = '*';
                break;
            default:
                break;
        }
        new Client().execute(oper, n1, n2);


    }
}
