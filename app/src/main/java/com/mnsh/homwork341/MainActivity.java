package com.mnsh.homwork341;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements TextWatcher {

    private TextView txtVaqtMomenti, txtVaqtUmumiy, txtMisol, txtNatija;
    private SeekBar seekBar;
    private ListView listView;
    private EditText edtJavob;
    private String foydakanuvchiIsmi, misol;
    private int misolNechtaligi, javob, misolRaqami=1;
    private static final long START_TIME_MILLIS=5000;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis= START_TIME_MILLIS;

    private ArrayList<String> misolRohat = new ArrayList<>();

    private boolean hasMusic=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtVaqtMomenti= (TextView) findViewById(R.id.txt_vaqt_momenti);
        txtVaqtUmumiy= (TextView) findViewById(R.id.txt_vaqt_umumiy);
        listView= (ListView) findViewById(R.id.list);

        txtMisol = (TextView) findViewById(R.id.txt_misol);
        txtNatija = (TextView) findViewById(R.id.txt_natija);

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setEnabled(false);
        seekBar.setBackgroundColor(Color.GREEN);
        edtJavob = (EditText) findViewById(R.id.edt_javob);
        edtJavob.addTextChangedListener(this);

        foydakanuvchiIsmi = "";
        misolNechtaligi = 10;
        mTimeLeftInMillis = misolNechtaligi*5000;

        int min =(int) misolNechtaligi*5/60;
        int sek =(int) misolNechtaligi*5%60;

        txtVaqtUmumiy.setText(String.format("%02d:%02d", min, sek));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, misolRohat);
        listView.setAdapter(adapter);
        misolRohat.add(" ");

        Random();
        startTimer();
    }


    public boolean hasRandom(boolean hasBajar){
        if((misolRaqami < misolNechtaligi || misolRaqami==misolNechtaligi) && hasBajar==true ){
            return true;
        }
        else return false;
    }
    public void Random() {
        if (hasRandom(true)) {
            int a = (new Random()).nextInt(20);
            int b = (new Random()).nextInt(20);
            int amal = (new Random()).nextInt(4);


            switch (amal) {
                case 0:
                    misol = " " + a + " " + "+" + " " + b + " =";
                    txtMisol.setText(misol);
                    javob = a + b;
                    misolRaqami++;
                    break;
                case 1:
                    if (a > b) {
                        misol = " " + a + " " + "-" + " " + b + " =";
                        txtMisol.setText(misol);
                        javob = a - b;
                        misolRaqami++;
                        break;
                    } else {
                        Random();
                        break;
                    }
                case 2:
                    misol = " " + a + " " + "*" + " " + b + " =";
                    txtMisol.setText(misol);
                    javob = a * b;
                    misolRaqami++;
                    break;
                case 3:
                    try {
                        if (a % b == 0) {
                            misol = " " + a + " " + "/" + " " + b + " =";
                            txtMisol.setText(misol);
                            javob = a / b;
                            misolRaqami++;
                            break;
                        } else {
                            Random();
                            break;
                        }
                    } catch (Exception ex) {
                        Random();
                        break;
                    }

            }
        }else {
            txtMisol.setText("Javob berib bo'dingiz");
            txtMisol.setTextColor(Color.RED);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int uzunlikJavob = String.valueOf(javob).length();
        String uzunlikEdit = edtJavob.getText().toString();
        if(uzunlikJavob == uzunlikEdit.length() && txtMisol.getText().equals("Javob berib bo'dingiz")==false){

            try {
                int javobEdt = Integer.parseInt(String.valueOf(edtJavob.getText()));
                if(javob==javobEdt){
                    if (hasMusic==false) {
                        MediaPlayer mediaPlayer1 = MediaPlayer.create(MainActivity.this, R.raw.togri_avob);
                        mediaPlayer1.start();
                    }
                    Toast.makeText( MainActivity.this,"To'g'ri javob", Toast.LENGTH_LONG).show();

                    togriJavob++;
                    if(txtMisol.getText().equals("Javob berib bo'dingiz") == false)
                        misolRohat.add(misol+" "+javob+"     to'gri topdingiz");
                }else {
                    Toast.makeText(MainActivity.this, "Not'g'ri javob", Toast.LENGTH_LONG).show();
                    if (hasMusic==false) {
                        MediaPlayer mediaPlayer2 = MediaPlayer.create(MainActivity.this, R.raw.notogri_avob);
                        mediaPlayer2.start();
                    }
                    notogriJavob++;

                    if(txtMisol.getText().equals("Javob berib bo'dingiz") == false)
                        misolRohat.add(misol+" "+javob+"     noto'g'ri topdingiz "+ javobEdt+" deb");
                }



            }catch (Exception ex){
                Toast.makeText(this, "Xatolik, faqat raqam kiriting iltimos", Toast.LENGTH_SHORT).show();
                MediaPlayer mediaPlayer3=MediaPlayer.create(MainActivity.this, R.raw.xatolik);
                mediaPlayer3.start();
            }
            Random();
            edtJavob.setText("");

        }


    }


    @Override
    public void afterTextChanged(Editable s) {

    }


    int togriJavob=0;
    int notogriJavob=0;
    double vaqtUchun=0;
    private void updateCountDownText(){
        if(txtMisol.getText().equals("Javob berib bo'dingiz") == false) {
            int minute = (int) mTimeLeftInMillis / 1000 / 60;
            int seconds = (int) mTimeLeftInMillis / 1000 % 60;
            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minute, seconds);
            txtVaqtMomenti.setText(timeLeftFormatted);

            vaqtUchun += (double) (100 / (5 * misolNechtaligi));
            try {
                seekBar.setProgress((int) vaqtUchun);

            } catch (Exception e) {

            }
        }else{
            seekBar.setBackgroundColor(Color.YELLOW);
            misolRohat.set(0,foydakanuvchiIsmi+" siz "+misolNechtaligi+" ta savoldan "+togriJavob+" " +
                    " ta savolga to'g'ri, "+notogriJavob+" ta savolga noto'g'ri va "+
                    (misolNechtaligi-togriJavob-notogriJavob)+" ta savolga javob bera olmadingiz");
            edtJavob.setEnabled(false);
        }
    }

    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis= millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                //to'g'ri yoki noto'griligini aytadi vaqt tugadi
                if (txtMisol.getText().equals("Javob berib bo'dingiz") == false) {
                    txtMisol.setText("Vaqtingiz tugadi");
                    txtMisol.setTextColor(Color.RED);
                    txtNatija.setText("Keyingi safar tezroq harakat qiling  "+foydakanuvchiIsmi);
                    txtNatija.setTextColor(Color.YELLOW);
                    txtNatija.setBackgroundColor(Color.GRAY);
                    edtJavob.setEnabled(false);
                    edtJavob.setText(" " + foydakanuvchiIsmi);
                    hasRandom(false);
                    seekBar.setBackgroundColor(Color.RED);
                    Toast.makeText(MainActivity.this, "Vaqtingiz tugadi", Toast.LENGTH_SHORT).show();
                    misolRohat.set(0, foydakanuvchiIsmi + " siz " + misolNechtaligi + " ta savoldan " + togriJavob + " " +
                            " ta savolga to'g'ri, " + notogriJavob + " ta savolga noto'g'ri va " +
                            (misolNechtaligi - togriJavob - notogriJavob) + " ta savolga javob bera olmadingiz");
                }
            }
        }.start();

    }
}