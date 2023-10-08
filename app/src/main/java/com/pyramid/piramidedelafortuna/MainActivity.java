package com.pyramid.piramidedelafortuna;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.tv.AdRequest;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
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

public class MainActivity extends AppCompatActivity {
    Button btn_generar;
    TextView textView2, textView3;
    LottieAnimationView lottie1, lottie2;
    BottomNavigationView bottomNavigationView;
    private AdView mAdView;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private int clickCounter = 0;
    TickerView tickerView, tickerView1, tickerView2, tickerView3;

    //ID ANUNCIO INTERSTICIAL
    private static final String AD_UNIT_ID = "ca-app-pub-4434685305331116/9423741753";

    // HashMap para asociar números con palabras
    HashMap<String, String> palabras;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ad_clicked");
        mFirebaseAnalytics.logEvent("ad_clicked", bundle);

        //APLICACION DE DIMENSIONES
        // Obtener el ancho y alto en píxeles
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = getResources().getDisplayMetrics().density;
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;

        int screenWidthDp = getResources().getConfiguration().screenWidthDp;
        int screenHeightDp = getResources().getConfiguration().screenHeightDp;

        int layoutResourceId = 0;


        if (widthPixels == 240 && heightPixels == 320) {
            // 240x320
            layoutResourceId = R.layout.layout_240x320;

        } else if (widthPixels == 320 && heightPixels == 480) {
            // 320x480
            layoutResourceId = R.layout.layout_320x480;

        } else if (widthPixels == 480 && heightPixels == 800) {
            // 480x800
            layoutResourceId = R.layout.layout_480x800;

        } else if (widthPixels == 720 && heightPixels == 1280) {
            // 720x1280
            layoutResourceId = R.layout.layout_720x1280;

        } else if (widthPixels == 1080 && heightPixels <= 1920) {
            // 1080x1920
            layoutResourceId = R.layout.layout_1080x1920;

        } else if (widthPixels == 1080 && heightPixels <= 2160) {
            // 1080x2160 --440dpi
            layoutResourceId = R.layout.layout_1080x2160;

        } else if (widthPixels == 1080 && heightPixels <= 2340) {
            layoutResourceId = R.layout.layout_1080x2340;


        } else if (widthPixels == 1080 && heightPixels == 2400) {
            // 1080x2400 --> RESOLUCION ORIGINAL DEL A52
            layoutResourceId = R.layout.activity_main;

        } else if (widthPixels == 1440 && heightPixels <= 2560) {
            // 1440x2560
            layoutResourceId = R.layout.layout_1440x2560;

        } else if (widthPixels == 1440 && heightPixels <= 2960) {
            // 1440x2960
            layoutResourceId = R.layout.layout_1440x2960;

        } else {
            // Otras resoluciones de pantalla
            layoutResourceId = R.layout.activity_main;
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
                FirebaseAnalytics.getInstance(MainActivity.this).logEvent("ad_clicked", null);
            }

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                FirebaseAnalytics.getInstance(MainActivity.this).logEvent("ad_load_failed", null);
            }

            @Override
            public void onAdImpression() {
                FirebaseAnalytics.getInstance(MainActivity.this).logEvent("ad_impression", null);
            }

            @Override
            public void onAdLoaded() {
                FirebaseAnalytics.getInstance(MainActivity.this).logEvent("ad_loaded", null);
            }

            @Override
            public void onAdOpened() {
                FirebaseAnalytics.getInstance(MainActivity.this).logEvent("ad_opened", null);
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

        // Inicializar HashMap de números y palabras
        palabras = new HashMap<>();
        palabras.put("00", "Huevos");
        palabras.put("01", "Agua");
        palabras.put("02", "Zapato");
        palabras.put("03", "Niño");
        palabras.put("04", "Cama");
        palabras.put("05", "Gato");
        palabras.put("06", "Perro");
        palabras.put("07", "Revólver");
        palabras.put("08", "Incendio");
        palabras.put("09", "Un arroyo");
        palabras.put("10", "Leche");
        palabras.put("11", "Minero");
        palabras.put("12", "Soldado");
        palabras.put("13", "Mala suerte");
        palabras.put("14", "Un borracho");
        palabras.put("15", "Quinceaños");
        palabras.put("16", "Anillo");
        palabras.put("17", "Accidente");
        palabras.put("18", "Sangre");
        palabras.put("19", "Pescado");
        palabras.put("20", "Fiesta");
        palabras.put("21", "Mujer");
        palabras.put("22", "Loco");
        palabras.put("23", "Cocinero");
        palabras.put("24", "Caballo");
        palabras.put("25", "Gallina");
        palabras.put("26", "Misa");
        palabras.put("27", "Peine");
        palabras.put("28", "Colina");
        palabras.put("29", "San Pedro");
        palabras.put("30", "Santa Rosa");
        palabras.put("31", "La luz");
        palabras.put("32", "Dinero");
        palabras.put("33", "Cristo");
        palabras.put("34", "Alegría");
        palabras.put("35", "Pajarito");
        palabras.put("36", "Familia");
        palabras.put("37", "Diente");
        palabras.put("38", "Piedras");
        palabras.put("39", "Lluvia");
        palabras.put("40", "Sacerdote");
        palabras.put("41", "Cuchillo");
        palabras.put("42", "Zapatillas");
        palabras.put("43", "Balcón");
        palabras.put("44", "La cárcel");
        palabras.put("45", "El vino");
        palabras.put("46", "Tomates");
        palabras.put("47", "Un muerto");
        palabras.put("48", "Muerto que habla");
        palabras.put("49", "La carne");
        palabras.put("50", "El pan");
        palabras.put("51", "Serrucho");
        palabras.put("52", "Madre e hijo");
        palabras.put("53", "Barco");
        palabras.put("54", "Vaca");
        palabras.put("55", "Música");
        palabras.put("56", "Caída");
        palabras.put("57", "Trabajo");
        palabras.put("58", "Ahogado");
        palabras.put("59", "Plantas");
        palabras.put("60", "La virgen");
        palabras.put("61", "Escopeta");
        palabras.put("62", "Inundación");
        palabras.put("63", "Casamiento");
        palabras.put("64", "Llanto");
        palabras.put("65", "Cazador");
        palabras.put("66", "Lombrices");
        palabras.put("67", "Víbora");
        palabras.put("68", "Sobrinos");
        palabras.put("69", "Vicios");
        palabras.put("70", "Descanso");
        palabras.put("71", "Construcción");
        palabras.put("72", "Sorpresa");
        palabras.put("73", "Hospital");
        palabras.put("74", "Relación");
        palabras.put("75", "Payaso");
        palabras.put("76", "Fuego");
        palabras.put("77", "Piernas");
        palabras.put("78", "Cortesana");
        palabras.put("79", "Ladrón");
        palabras.put("80", "Pelota");
        palabras.put("81", "Flores");
        palabras.put("82", "Pelea");
        palabras.put("83", "Mal tiempo");
        palabras.put("84", "La iglesia");
        palabras.put("85", "Linterna");
        palabras.put("86", "Humo");
        palabras.put("87", "Piojos");
        palabras.put("88", "El Papa");
        palabras.put("89", "Ratón");
        palabras.put("90", "Miedo");
        palabras.put("91", "Pintor");
        palabras.put("92", "El médico");
        palabras.put("93", "Enamorado");
        palabras.put("94", "Cementerio");
        palabras.put("95", "Anteojos");
        palabras.put("96", "Esposo");
        palabras.put("97", "Mesa");
        palabras.put("98", "Lavanderia");
        palabras.put("99", "Hermano(a)");


        // Inicializar vistas
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        tickerView = findViewById(R.id.txtpf1);
        tickerView.setCharacterList(TickerUtils.getDefaultNumberList());

        tickerView1 = findViewById(R.id.txtpf2);
        tickerView1.setCharacterList(TickerUtils.getDefaultNumberList());

        tickerView2 = findViewById(R.id.txtpf3);
        tickerView2.setCharacterList(TickerUtils.getDefaultNumberList());

        tickerView3 = findViewById(R.id.txtpf4);
        tickerView3.setCharacterList(TickerUtils.getDefaultNumberList());

        //Default data
        tickerView.setText("0");
        tickerView1.setText("0");
        tickerView2.setText("0");
        tickerView3.setText("0");

        btn_generar = findViewById(R.id.btn_sueno);
        lottie1 = findViewById(R.id.lottie1);
        lottie2 = findViewById(R.id.lottie2);
        lottie1.setSpeed(1);
        lottie2.setVisibility(View.GONE);
        textView2.setVisibility(View.INVISIBLE);
        textView3.setVisibility(View.INVISIBLE);


        btn_generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView2.setVisibility(View.INVISIBLE);
                textView3.setVisibility(View.INVISIBLE);

                clickCounter++;
                // Generar número aleatorio entre 3 y 5
                int randomNum = new Random().nextInt(2) + 3;
                // Ocultar textView2 y textView3

                if (clickCounter >= randomNum) {
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(MainActivity.this);
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

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Random r = new Random();
                                        int low = 0;
                                        int high = 10;
                                        int low1 = 0;
                                        int high1 = 100;
                                        final int result = r.nextInt(high - low) + low;
                                        final int result1 = r.nextInt(high1 - low1) + low1;
                                        final int result2 = r.nextInt(high - low) + low;
                                        final int result3 = r.nextInt(high - low) + low;
                                        tickerView.setText("" + result);
                                        String result1Text = (result1 == 100) ? "00" : String.format("%02d", result1);
                                        tickerView1.setText(result1Text);
                                        tickerView2.setText("" + result2);
                                        tickerView3.setText("" + result3);
                                        //  handler.postDelayed(this, 10000);
                                        handler.removeCallbacks(this);

                                        // Obtener el número mostrado en txtpf2
                                        String numero = String.format("%02d", result1);

                                        // Obtener la palabra correspondiente al número
                                        String palabra = palabras.get(numero);

                                        // Mostrar la palabra en textView2
                                        textView3.setText(palabra);
                                    }
                                });
                            }
                        });
                    }
                    //CUANDO PARA DE GIRAR LA PIRAMIDE, SE MUESTRAN LOS NUMEROS 3.5SEG
                }, 3500);

                //SEGUNDOS DE INICIO BOTON
                int Tiempo = 100;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lottie1.playAnimation();
                        lottie1.setSpeed(7);
                        tickerView.setText("0");
                        tickerView1.setText("0");
                        tickerView2.setText("0");
                        tickerView3.setText("0");
                    }
                }, Tiempo);

                //PASADOS 3 SEGUNDOS SE DETIENE LA PIRAMIDE
                int Tiempo2 = 3000;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lottie1.setSpeed(0);
                        lottie1.playAnimation();
                    }
                }, Tiempo2);

                //PASADOS 4 SEGUNDOS SE ACTIVA LA ANIMACION DE MONEDAS
                int Tiempo3 = 4000;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lottie2.setVisibility(View.VISIBLE);
                        lottie2.playAnimation();
                    }
                }, Tiempo3);

                //AL SEGUNDO SE DETIENE LA ANIMACION DE MONEDAS
                int Tiempo4 = 5000;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lottie2.setVisibility(View.GONE);
                    }
                }, Tiempo4);

                // Mostrar textView2 y textView3 después de 7 segundos
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView2.setVisibility(View.VISIBLE);
                        textView3.setVisibility(View.VISIBLE);
                    }
                }, 4500);
            }
        });

    }

    // Método para mostrar el anuncio intersticial
    public void showInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    FirebaseAnalytics.getInstance(MainActivity.this).logEvent("ad_clicked", null);
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
                    FirebaseAnalytics.getInstance(MainActivity.this).logEvent("ad_impression", null);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                Intent intentMain = new Intent(this, MainActivity.class);
                startActivity(intentMain);
                return true;

            case R.id.suerte:
                Intent intentFechaNumero = new Intent(this, Fecha_numero.class);
                startActivity(intentFechaNumero);
                return true;

            case R.id.comofunciona:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.comofunciona); // Título del AlertDialog
                builder.setMessage("La \"Pirámide de la Fortuna\" genera y muestra los números de la suerte más relevantes en la pirámide con la cual podras formar combinaciones para futuros sorteos.");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();  // Cerrar el AlertDialog
                    }
                });
                builder.show();
                return true;

            case R.id.tabla:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle(R.string.tabla);

                // Crear el diseño de dos columnas programáticamente
                LinearLayout layout = new LinearLayout(this);
                layout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.setOrientation(LinearLayout.VERTICAL);

                // Crear un ScrollView para permitir el desplazamiento vertical
                ScrollView scrollView = new ScrollView(this);
                scrollView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                // Crear un LinearLayout para contener las columnas
                LinearLayout columnLayout = new LinearLayout(this);
                columnLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                columnLayout.setOrientation(LinearLayout.HORIZONTAL);

                // Ordenar las claves
                List<String> sortedKeys = new ArrayList<>(palabras.keySet());
                Collections.sort(sortedKeys);

                // Iterar a través de las palabras y agregarlas a las columnas
                int numColumns = 2;
                StringBuilder column1Text = new StringBuilder();
                StringBuilder column2Text = new StringBuilder();

                int count = 0;
                for (String key : sortedKeys) {
                    String value = palabras.get(key);

                    if (count % numColumns == 0) {
                        column1Text.append(key).append(" - ").append(value).append("\n");
                    } else {
                        column2Text.append(key).append(" - ").append(value).append("\n");
                    }

                    count++;
                }

                // Crear los TextViews para cada columna
                TextView column1TextView = new TextView(this);
                TextView column2TextView = new TextView(this);

                column1TextView.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f));
                column1TextView.setGravity(Gravity.START);
                column1TextView.setTextSize(16);
                column1TextView.setText(column1Text.toString());

                column2TextView.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f));
                column2TextView.setGravity(Gravity.START);
                column2TextView.setTextSize(16);
                column2TextView.setText(column2Text.toString());

                // Agregar los TextViews al LinearLayout de las columnas
                columnLayout.addView(column1TextView);
                columnLayout.addView(column2TextView);

                // Agregar el LinearLayout de las columnas al ScrollView
                scrollView.addView(columnLayout);

                // Agregar el ScrollView al layout general
                layout.addView(scrollView);

                // Establecer el layout personalizado en el AlertDialog
                builder2.setView(layout);

                builder2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();  // Cerrar el AlertDialog
                    }
                });

                builder2.show();
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
    }


    private void openURL(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

}

