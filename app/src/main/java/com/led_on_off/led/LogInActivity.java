package com.led_on_off.led;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.annotation.RequiresApi;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

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
        if(verificare(username.getText().toString(),password.getText().toString())==true)
        {
            Accelerometer mysensor=new Accelerometer(context);
            new Thread(mysensor).start();
            BackgroudThread mainThread=new BackgroudThread(mysensor,context);
            mainThread.execute();
            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(intent);
//            Intent intent = new Intent(LogInActivity.this, DeviceList.class);
//            startActivity(intent);
        }
        }
        });
    }
    private boolean verificare(String username,String password)
    {
        return true;
    }
}