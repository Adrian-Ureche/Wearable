package com.led_on_off.led;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Looper;
        import android.support.annotation.RequiresApi;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.ProtocolException;
        import java.net.URL;

        import javax.net.ssl.HttpsURLConnection;
public class LogInActivity extends AppCompatActivity {
    private TextView username;
    private TextView password;
    private Button loginButton;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.loginlayout);
        username=findViewById(R.id.UserNameTextbox);
        password=findViewById(R.id.PasswordTextbox);
        loginButton=findViewById(R.id.LogInButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
@Override
public void onClick(View view) {
    TextView userName=findViewById(R.id.UserNameTextbox);
    TextView pw=findViewById(R.id.PasswordTextbox);
     final LogInVerification verif=new LogInVerification(userName.getText().toString(),pw.getText().toString());
     Thread t=new Thread(verif);
     t.start();
    try {
        t.join();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    if(verif.verif==true)
    {
       CNP.setCnp(userName.getText().toString());
        Accelerometer mysensor=new Accelerometer(context);
        new Thread(mysensor).start();
        BackgroudThread mainThread=new BackgroudThread(mysensor,context);
        mainThread.execute();
        Intent intent = new Intent(LogInActivity.this, DeviceList.class);
        startActivity(intent);
    }
    else
    {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, verif.err, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
        });
    }
}
class LogInVerification implements Runnable
{
    private String userName;
    private String passWord;
    public boolean verif=false;
    public String err="";
    LogInVerification(String userName, String pw)
    {
        this.userName=userName;
        passWord=pw;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
            try {
                URL url = new URL("https://backend-ip-mediating-hedgehog-gl.cfapps.eu10.hana.ondemand.com/pacient/" + userName);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    // Do normal input or output stream reading
                    StringBuilder content;
                    try (BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        content = new StringBuilder();
                        System.out.println("recomandari citite: ");
                        line = in.readLine();
                        if(line==null)
                        {
                            throw new MalformedURLException();
                        }
                        else{
                            line=line.substring(1,line.length()-1);
                            String[] sp=line.split(",");
                            String psw=sp[11].split(":")[1];
                            if(passWord.compareTo(psw.substring(1,psw.length()-1))==0)
                            {
                                verif=true;
                            }
                            else
                            {
                                err="parola gresita";
                            }
                        }
                    } catch (IOException ioException) {
                        err="user inexistent";
                        ioException.printStackTrace();
                    } finally {
                        connection.disconnect();
                    }
                } else {
                    System.out.println("nu merge");
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
        }
    }
}