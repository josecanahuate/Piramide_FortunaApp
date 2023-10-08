package com.pyramid.piramidedelafortuna;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import com.google.android.gms.ads.AdRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Fecha_numero extends AppCompatActivity {

    Button btn_sueno;
    EditText ent_s1, ent_s2, ent_s3;
    TickerView tickerView11, tickerView12, tickerView13;

    BottomNavigationView bottomNavigationView;
    private AdView mAdView;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private int clickCounter = 0;
    //ID ANUNCIO INTERSTICIAL
    private static final String AD_UNIT_ID = "ca-app-pub-4434685305331116/9423741753";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ad_clicked");
        mFirebaseAnalytics.logEvent("ad_clicked", bundle);

//APLICACION DE DIMENSIONES
        /*int layoutResourceId = 0;
        // Obtener la densidad de píxeles de la pantalla
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.densityDpi;

        // Asignar el layout resource id según la densidad de píxeles
        if (density <= 120) {
            // Nueva opción para 240x320
            layoutResourceId = R.layout.fecha_240x320;

        } else if  (density <= DisplayMetrics.DENSITY_MEDIUM) {
            // Pantalla con densidad de píxeles de 160 dpi - 320x480
            //setContentView(layout_320x480);
            layoutResourceId = R.layout.fecha_320x480;

        } else if (density <= DisplayMetrics.DENSITY_HIGH) {
            // Pantalla con densidad de píxeles de 240 dpi - 480x800
            layoutResourceId = R.layout.fecha_480x800;

        } else if (density <= DisplayMetrics.DENSITY_XHIGH) {
            // Pantalla con densidad de píxeles de 320 dpi - 720x1280
            //setContentView(layout_720x1280);
            layoutResourceId = R.layout.fecha_720x1280;

        } else if (density <= DisplayMetrics.DENSITY_XXHIGH) {
            // Pantalla con densidad de píxeles de 480 dpi - 1080x1920
            layoutResourceId = R.layout.fecha_1080x1920;

        } else if (density >= 420 && density <= 540) { //si no toma el de arriba cambiar el 540 por 480
            // Nueva opción para 1080x2400
            layoutResourceId = R.layout.fecha_1080x2400;

        } else if (density <= DisplayMetrics.DENSITY_XXXHIGH) {
            // Pantalla con densidad de píxeles de 640 dpi - 1440x2560
            layoutResourceId = R.layout.fecha_1440x2560;

        } else if (density >= 640 &&density <= 650) {
            // Nueva opción para 1440x2960
            layoutResourceId = R.layout.fecha_1440x2960;

        } else {
            // Otras densidades de píxeles
            layoutResourceId = R.layout.activity_main;
        }
        setContentView(layoutResourceId);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
*/


        //APLICACION DE DIMENSIONES
        int layoutResourceId = 0;
        // Obtener el ancho y alto en píxeles
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;

        if (widthPixels == 240 && heightPixels == 320) {
            // 240x320
            layoutResourceId = R.layout.fecha_240x320;

        } else if (widthPixels == 320 && heightPixels == 480) {
            // 320x480
            layoutResourceId = R.layout.fecha_320x480;

        } else if (widthPixels == 480 && heightPixels == 800) {
            // 480x800
            layoutResourceId = R.layout.fecha_480x800;

        } else if (widthPixels == 720 && heightPixels <= 1280) {
            // 720x1280
            layoutResourceId = R.layout.fecha_720x1280;

        } else if (widthPixels == 1080 && heightPixels <= 1920) {
            // 1080x1920
            layoutResourceId = R.layout.fecha_1080x1920;

        } else if (widthPixels == 1080 && heightPixels <= 2160) {
            // 1080x1920
            layoutResourceId = R.layout.fecha_1080x2160;

        } else if (widthPixels == 1080 && heightPixels <= 2400) {
            // 1080x2400
            layoutResourceId = R.layout.fecha_1080x2400;

        } else if (widthPixels == 1440 && heightPixels <= 2560) {
            // 1440x2560
            layoutResourceId = R.layout.fecha_1440x2560;

        } else if (widthPixels == 1440 && heightPixels <= 2960) {
            // 1440x2960
            layoutResourceId = R.layout.fecha_1440x2960;

        } else {
            // Otras resoluciones de pantalla
            layoutResourceId = R.layout.activity_fecha_numero;
        }
        setContentView(layoutResourceId);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
        //carga Banner
        mAdView = findViewById(R.id.adView);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                FirebaseAnalytics.getInstance(Fecha_numero.this).logEvent("ad_clicked", null);
            }

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                FirebaseAnalytics.getInstance(Fecha_numero.this).logEvent("ad_load_failed", null);
            }

            @Override
            public void onAdImpression() {
                FirebaseAnalytics.getInstance(Fecha_numero.this).logEvent("ad_impression", null);
            }

            @Override
            public void onAdLoaded() {
                FirebaseAnalytics.getInstance(Fecha_numero.this).logEvent("ad_loaded", null);
            }

            @Override
            public void onAdOpened() {
                FirebaseAnalytics.getInstance(Fecha_numero.this).logEvent("ad_opened", null);
            }

        });
        // Cargar el anuncio intersticial
        InterstitialAd.load(this,AD_UNIT_ID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // El anuncio se cargó correctamente
                        mInterstitialAd = interstitialAd;
                        Log.d("MainActivity", "Anuncio intersticial cargado");
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Error al cargar el anuncio
                        //Log.d("MainActivity", loadAdError.toString());
                        Log.d("MainActivity", "Error al cargar anuncio intersticial");
                        mInterstitialAd = null;
                    }
                });

        // Inicializar vistas
        tickerView11 = findViewById(R.id.sueno1);
        tickerView11.setCharacterList(TickerUtils.getDefaultNumberList());

        tickerView12 = findViewById(R.id.sueno2);
        tickerView12.setCharacterList(TickerUtils.getDefaultNumberList());

        tickerView13 = findViewById(R.id.sueno3);
        tickerView13.setCharacterList(TickerUtils.getDefaultNumberList());

        //Default data
        tickerView11.setText("0");
        tickerView12.setText("0");
        tickerView13.setText("0");
        btn_sueno = findViewById(R.id.btn_sueno);

        // Inicializar vistas de los EditText
        ent_s1 = findViewById(R.id.ent_s1);
        ent_s2 = findViewById(R.id.ent_s2);
        ent_s3 = findViewById(R.id.ent_s3);

        btn_sueno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los valores ingresados en los EditText
                String valorS1 = ent_s1.getText().toString().trim();
                String valorS2 = ent_s2.getText().toString().trim();
                String valorS3 = ent_s3.getText().toString().trim();

                // Validar si los campos están vacíos
                if (valorS1.isEmpty() || valorS2.isEmpty() || valorS3.isEmpty()) {
                    // Mostrar un error dialog indicando que los campos son requeridos
                    AlertDialog.Builder builder = new AlertDialog.Builder(Fecha_numero.this);
                    builder.setTitle("Error");
                    builder.setMessage("Por favor, complete todos los campos.");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    // Los campos están rellenados, proceder a generar las combinaciones aleatorias
                    Random r = new Random();

                    clickCounter++;
                    // Generar número aleatorio entre 2 y 4
                    int randomNum = new Random().nextInt(4) + 2;

                   if (clickCounter >= randomNum) {
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(Fecha_numero.this);
                            mInterstitialAd = null;
                            InterstitialAd.load(getApplicationContext(), AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
                                @Override
                                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                    super.onAdLoaded(interstitialAd);
                                    mInterstitialAd = interstitialAd;
                                }
                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    super.onAdFailedToLoad(loadAdError);
                                    mInterstitialAd = null;
                                }
                            });
                            clickCounter = 0;
                        }
                    }

                    // Generar números aleatorios de 0 a 999 para las milesimas
                    int milesimas1 = r.nextInt(10000);
                    int milesimas2 = r.nextInt(10000);
                    int milesimas3 = r.nextInt(10000);

                    // Formatear los números aleatorios para que tengan tres dígitos con ceros a la izquierda
                    String formattedMilesimas1 = String.format("%04d", milesimas1);
                    String formattedMilesimas2 = String.format("%04d", milesimas2);
                    String formattedMilesimas3 = String.format("%04d", milesimas3);

                    // Mostrar las combinaciones aleatorias en los TextView sueno1, sueno2, sueno3
                    tickerView11.setText(formattedMilesimas1);
                    tickerView12.setText(formattedMilesimas2);
                    tickerView13.setText(formattedMilesimas3);
                }
            }
        });

    }
        // Método para mostrar el anuncio intersticial
        public void showInterstitial() {
            if (mInterstitialAd != null) {
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        FirebaseAnalytics.getInstance(Fecha_numero.this).logEvent("ad_clicked", null);
                        Log.d(TAG, "El anuncio se clickeo.");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Se llama cuando se cierra el anuncio
                        Log.d("MainActivity", "El anuncio se cerró.");
                        mInterstitialAd = null;
                        // Recargamos el anuncio para que esté listo para el siguiente botón
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Se llama si hay un error al mostrar el anuncio
                        Log.d("MainActivity", "No se pudo mostrar el anuncio.");
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdImpression() {
                        FirebaseAnalytics.getInstance(Fecha_numero.this).logEvent("ad_impression", null);
                        Log.d(TAG, "Ad recorded an impression.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Se llama cuando el anuncio se muestra correctamente
                        Log.d("MainActivity", "El anuncio se mostró.");
                    }
                });
                mInterstitialAd.show(this);
            } else {
                Log.d("MainActivity", "El anuncio no está listo todavía.");
            }

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu2) {
        getMenuInflater().inflate(R.menu.menu2, menu2);

        return super.onCreateOptionsMenu(menu2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                Intent intentMain = new Intent(this, MainActivity.class);
                startActivity(intentMain);
                break;
            /*return true;*/

            case R.id.suerte:
                Intent intentFechaNumero = new Intent(this, Fecha_numero.class);
                startActivity(intentFechaNumero);
                break;
            /*return true;*/

            case R.id.comofunciona:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.comofunciona); // Título del AlertDialog
                builder.setMessage("Obtén los números de la suerte según tus fechas más relevantes.");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();  // Cerrar el AlertDialog
                    }
                });
                builder.show();
                return true;

            case R.id.mas:
                // Abrir la URL correspondiente a "Mas apps"
                openURL("https://play.google.com/store/apps/developer?id=Tristar+Dev&hl=es&gl=US");
                return true;

            case R.id.calificanos:
                // Abrir la URL correspondiente a "Calificanos"
                openURL("https://play.google.com/store/apps/details?id=com.pyramid.piramidedelafortuna");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void openURL(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}

