package br.fecapccp.a250507_seekbarvideo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.media.MediaPlayer;
import android.widget.VideoView;
import android.os.Looper;



public class MainActivity extends AppCompatActivity {
    private VideoView videoView;
    private SeekBar videoSeekBar;

    private TextView textTempo;

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Vincular os objetos com os Ids.
        videoSeekBar = findViewById(R.id.videoSeekbar);
        videoView = findViewById(R.id.videoView);
        textTempo = findViewById(R.id.textTempo);
        //Configurar o caminho do Video e Carregar no videoview
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videoc);
        videoView.setVideoURI(videoUri);

        //Listener para Saber Quando o video esta preparado para reproduzir
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

            @Override
            public void onPrepared(MediaPlayer mp){
                videoSeekBar.setMax(videoView.getDuration());
                /*comentado para nao inciar o video ao iniciar o app*/


           //Inicia automaticamente

                //videoView.start();
            }
        });

        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                videoView.seekTo(progress);
            }}
            @Override
            public void onStartTrackingTouch(SeekBar seekbar){

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekbar){

            }

        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //Metodo para Atualizar o tempo do video e posição do Seekbar;

    private Runnable atualizarSeekBar = new Runnable() {
        @Override
        public void run () {
            if(videoView.isPlaying()){
                videoSeekBar.setProgress(videoView.getCurrentPosition());
                atualizarTextTempo();
            }
            handler.postDelayed(this, 1000);
        }};




    private void atualizarTextTempo(){
        int tempoAtual = videoView.getCurrentPosition();
        int tempoTotal = videoView.getDuration();

        String tempoFormatado = formataTempo(tempoAtual) + " / " + formataTempo(tempoTotal);
        textTempo.setText(tempoFormatado);

    }

    //Metodo para Formatar o tempo em mm:ss
    private String formataTempo(int tempo){
        int min = (tempo/1000) /60;
        int sec = (tempo/1000) % 60;
        return String.format("%02d:%02d", min, sec);


    }


    public void startVideo(View view){
        //Manter o handle abaixo caso nao deseje que o video inicie com o app
        //caso deseja manter o inicio do video com o inicio do app(comentar abaixo
        //descomentar no onPrepared()
        videoView.start();
        handler.post(atualizarSeekBar);
    }

    public void pauseVideo(View view){
        videoView.pause();

    }

    public void stopVideo(View view){
        videoView.pause();
        videoView.seekTo(0);
        videoSeekBar.setProgress(0);
        atualizarTextTempo();
    }


}