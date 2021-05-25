package com.led_on_off.led;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.led_on_off.led.ui.main.SectionsPagerAdapter;
import com.led_on_off.led.databinding.ActivityMain2Binding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutorService;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity  {

    private ActivityMain2Binding binding;
    private Thread readRecommendationsThread;
    private ReadRecommendations readRecommendations;
    private String myCnp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readRecommendations=new ReadRecommendations();
        readRecommendationsThread=new Thread(readRecommendations);
        readRecommendationsThread.start();
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageSelected(int i) {
                if(i==1) {
                    if(readRecommendationsThread.isAlive()!=true) {
                        readRecommendationsThread = new Thread(readRecommendations);
                        readRecommendationsThread.start();
                    }
                    TextView t = (TextView) findViewById(R.id.ViewTextRecomandari);
                    t.setText("");
                    String a=readRecommendations.recommendation.substring(1,readRecommendations.recommendation.length()-1);
                    String[] sp=a.split(";");
                    for(int j=0;j<sp.length;j++)
                    t.append(sp[j]+"\n");
                }
                else
                    if(i==0)
                {
                    if(readRecommendationsThread.isAlive()!=true) {
                        readRecommendationsThread = new Thread(readRecommendations);
                        readRecommendationsThread.start();
                    }
                    TextView t = (TextView) findViewById(R.id.viewTextActivity);
                    t.setText("");
                    String a=readRecommendations.activity.substring(1,readRecommendations.activity.length()-1);
                    String[] sp=a.split(";");
                    for(int j=0;j<sp.length;j++)
                        t.append(sp[j]+"\n");
                }
                    else
                        if(i==1)
                        {
                            TextView t=findViewById(R.id.editTextTextPersonName3);
                            t.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    updateMentiuni();
                                }
                            });
                        }
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
    private void updateMentiuni()
    {
        TextView t=findViewById(R.id.editTextTextPersonName3);
        Mentiuni.mentiune=t.getText().toString();
    }
}
class ReadRecommendations implements Runnable
{
    public String recommendation="";
    public String activity="";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        try {
            URL url = new URL("https://backend-ip-mediating-hedgehog-gl.cfapps.eu10.hana.ondemand.com/datemed/"+CNP.getCnp());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK){
                // Do normal input or output stream reading
                StringBuilder content;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    content = new StringBuilder();
                    System.out.println("recomandari citite: ");
                    line = in.readLine();
                    line=line.substring(0,line.length()-1);
                    String[] splited=line.split(",");
                    System.out.println(splited[4].split(":")[1]);
                    recommendation=splited[4].split(":")[1];
                    activity=splited[5].split(":")[1];
                }
                catch (IOException ioException) {
                    ioException.printStackTrace();
                }  finally {
                    connection.disconnect();
                }
            }
            else
            {
                System.out.println("nu merge");
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Mentiuni
{
    public static String mentiune="";
}