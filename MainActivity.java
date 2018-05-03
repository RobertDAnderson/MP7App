package edu.illinois.cs.cs125.mp7app;

import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

//  .------..------..------.
//  |M.--. ||P.--. ||7.--. |
//  | (\/) || :/\: || :(): |
//  | :\/: || (__) || ()() |
//  | '--'M|| '--'P|| '--'7|
//  `------'`------'`------'

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MP7:Main";
    public String deckID = null;
    private static RequestQueue requestQueue;
    final int[] player1remaining = new int[1];

    final int[] player2remaining = new int[1];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player1remaining[0] = 0;
        player2remaining[0] = 0;
        TextView player2remain = findViewById(R.id.computerCountValue);
        player2remain.setText(String.valueOf(player2remaining[0]));
        TextView player1remain = findViewById(R.id.playerCountValue);
        player1remain.setText(String.valueOf(player1remaining[0]));




    }

    protected void onPause() {
        super.onPause();
    }

    //                 ____ _ _      _    _   _
    //     ___  _ __  / ___| (_) ___| | _| \ | | _____      __
    //    / _ \| '_ \| |   | | |/ __| |/ /  \| |/ _ \ \ /\ / /
    //   | (_) | | | | |___| | | (__|   <| |\  |  __/\ V  V /
    //    \___/|_| |_|\____|_|_|\___|_|\_\_| \_|\___| \_/\_/
    //
    public void onClickNew(final View view) {
        setContentView(R.layout.activity_main);
        Button war = findViewById(R.id.warButton);
        war.setEnabled(true);
        player1remaining[0] = 26;
        player2remaining[0] = 26;
        TextView player2remain = findViewById(R.id.computerCountValue);
        player2remain.setText(String.valueOf(player2remaining[0]));
        final TextView player1remain = findViewById(R.id.playerCountValue);
        player1remain.setText(String.valueOf(player1remaining[0]));
        if (player1remaining[0] == 0) {
            ImageView player1CardImagebg = (ImageView) findViewById(R.id.Player1DeckImageBackground);
            player1CardImagebg.setImageResource(R.drawable.empty);
        }
        if (player1remaining[0] > 0) {
            ImageView player1CardImagebg = (ImageView) findViewById(R.id.Player1DeckImageBackground);
            player1CardImagebg.setImageResource(R.drawable.bicycledark);
        }
        if (player2remaining[0] == 0) {
            ImageView player2CardImagebg = (ImageView) findViewById(R.id.Player2DeckImageBackground);
            player2CardImagebg.setImageResource(R.drawable.empty);
        }
        if (player2remaining[0] > 0) {
            ImageView player2CardImagebg = (ImageView) findViewById(R.id.Player2DeckImageBackground);
            player2CardImagebg.setImageResource(R.drawable.bicycledark);
        }



        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.w(TAG, "call 1: " + response.toString());
                            TextView display = findViewById(R.id.deckID);
                            Log.d(TAG, response.toString());

                            getID(response.toString());
                            initialTwentySix1();
                            initialTwentySix2();

                            Toast.makeText(getApplicationContext(),
                                    "New deck of " + (player1remaining[0] + player2remaining[0]) + " shuffled and dealt. Ready to play.",
                                    Toast.LENGTH_LONG).show();

                            ImageView player1CardImage = (ImageView)findViewById(R.id.player1CardImage);
                            player1CardImage.setImageResource(R.drawable.empty);
                            ImageView player2CardImage = (ImageView)findViewById(R.id.player2CardImage);
                            player2CardImage.setImageResource(R.drawable.empty);

                            try {
                                Log.d(TAG, "f: " + response.toString(2));
                                display.setText(deckID);
                            } catch (JSONException ignored) {

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //               _   ___ ____
    //     __ _  ___| |_|_ _|  _ \
    //    / _` |/ _ \ __|| || | | |
    //   | (_| |  __/ |_ | || |_| |
    //    \__, |\___|\__|___|____/
    //    |___/
    //
    public void getID(final String json) {
        JsonParser parser = new JsonParser();
        JsonObject jsonobj = parser.parse(json).getAsJsonObject();
        deckID = jsonobj.get("deck_id").getAsString();
        Log.d(TAG, "The deck's ID is: " + deckID);
    }

    //                    _   __  __
    //    _ __   _____  _| |_|  \/  | _____   _____
    //   | '_ \ / _ \ \/ / __| |\/| |/ _ \ \ / / _ \
    //   | | | |  __/>  <| |_| |  | | (_) \ V /  __/
    //   |_| |_|\___/_/\_\\__|_|  |_|\___/ \_/ \___|
    //
    public void nextMove(final View view) {

        player1remaining[0] -= 1;
        player2remaining[0] -= 1;
        TextView player2remain = findViewById(R.id.computerCountValue);
        player2remain.setText(String.valueOf(player2remaining[0]));
        TextView player1remain = findViewById(R.id.playerCountValue);
        player1remain.setText(String.valueOf(player1remaining[0]));

        if (isSomeoneZero(player1remaining[0], player2remaining[0])) {
            Toast toast = Toast.makeText(getApplicationContext(), "LAST CARD!",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            if (player1remaining[0] == 0 && player2remaining[0] != 0) {
                ImageView player1CardImagebg = (ImageView) findViewById(R.id.Player1DeckImageBackground);
                player1CardImagebg.setImageResource(R.drawable.empty);
            }
            if (player1remaining[0] > 0) {
                ImageView player1CardImagebg = (ImageView) findViewById(R.id.Player1DeckImageBackground);
                player1CardImagebg.setImageResource(R.drawable.bicycledark);

            }
            if (player2remaining[0] == 0 && player1remaining[0] != 0) {
                ImageView player2CardImagebg = (ImageView) findViewById(R.id.Player2DeckImageBackground);
                player2CardImagebg.setImageResource(R.drawable.empty);
            }
            if (player2remaining[0] > 0) {
                ImageView player2CardImagebg = (ImageView) findViewById(R.id.Player2DeckImageBackground);
                player2CardImagebg.setImageResource(R.drawable.bicycledark);
            }
            // easter egg
            if (player1remaining[0] == 0 && player2remaining[0] == 0) {
                Button war = findViewById(R.id.warButton);
                war.setEnabled(false);
                android.support.constraint.ConstraintLayout a = (android.support.constraint.ConstraintLayout) findViewById(R.id.backg);
                a.setBackgroundResource(R.drawable.back_iheartjava);
                player1remain.setText(" ");
                player2remain.setText(" ");
                ImageView player1CardImagebg = (ImageView) findViewById(R.id.Player1DeckImageBackground);
                player1CardImagebg.setImageResource(R.drawable.actuallyempty);
                ImageView player2CardImagebg = (ImageView) findViewById(R.id.Player2DeckImageBackground);
                player2CardImagebg.setImageResource(R.drawable.actuallyempty);


                }

        }


        // value placeholders
        final int[] player1Value = new int[1];
        final int[] player2Value = new int[1];

        // code placeholders
        final String[] player1Code = new String[1];
        final String[] player2Code = new String[1];

        // image updaters
        final String[] img1id = new String[1];
        final String[] img2id = new String[1];

        try {
            JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player1/draw/bottom/?count=1",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, "cards left in player 1 after dealing a card: " + (getRemainingPile(response.toString(), "player1")));
                            JsonParser parser = new JsonParser();
                            JsonObject jsonobj = parser.parse(response.toString()).getAsJsonObject();
                            JsonArray card = jsonobj.get("cards").getAsJsonArray();
                            jsonobj = card.get(0).getAsJsonObject();
                            Log.w(TAG, jsonobj.toString());
                            // value getter
                            player1Value[0] = getValue(jsonobj.get("value").getAsString());
                            Log.w(TAG, "P1 value: " + jsonobj.get("value").getAsString());
                            // code getter
                            player1Code[0] = getCode(response.toString(), 0);
                            Log.w(TAG, "P1 code: " + getCode(response.toString(), 0));
                            // image getter
                            img1id[0] = getImageFileString(getCode(response.toString(), 0));
                            ImageView player1CardImage = findViewById(R.id.player1CardImage);
                            player1CardImage.setImageResource(getResources().getIdentifier(img1id[0], "drawable", getPackageName()));
                            if (player1Value[0] != 0 && player2Value[0] != 0 && player1Code[0] != null && player2Code[0] != null) {
                                compare(player1Value[0], player2Value[0], player1Code[0], player2Code[0]);
                            }


                            Log.d(TAG, "The value of Player 1's card is: " + player1Value[0]);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JsonObjectRequest jsonObjectRequest4 = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player2/draw//bottom/?count=1",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            JsonParser parser = new JsonParser();
                            JsonObject jsonobj = parser.parse(response.toString()).getAsJsonObject();
                            JsonArray card = jsonobj.get("cards").getAsJsonArray();
                            jsonobj = card.get(0).getAsJsonObject();
                            // value getter
                            player2Value[0] = getValue(jsonobj.get("value").getAsString());
                            Log.w(TAG, "P2 value: " + jsonobj.get("value").getAsString());
                            // code getter
                            player2Code[0] = getCode(response.toString(), 0);
                            Log.w(TAG, "P2 code: " + getCode(response.toString(), 0));
                            // image getter
                            img2id[0] = getImageFileString(getCode(response.toString(), 0));
                            ImageView player2CardImage = findViewById(R.id.player2CardImage);
                            player2CardImage.setImageResource(getResources().getIdentifier(img2id[0], "drawable", getPackageName()));

                            Log.d(TAG, "The value of Player 2's card is: " + player2Value[0]);
                            if (player1Value[0] != 0 && player2Value[0] != 0 && player1Code[0] != null && player2Code[0] != null) {
                                compare(player1Value[0], player2Value[0], player1Code[0], player2Code[0]);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest4);

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (isSomeoneZero(player1remaining[0], player2remaining[0])) {
            final Toast toast0 = Toast.makeText(getApplicationContext(), "LAST CARD!",
                    Toast.LENGTH_SHORT);
            toast0.setGravity(Gravity.CENTER, 0, 0);
            toast0.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast0.cancel();
                }
            }, 400);
            if (player1remaining[0] == 0) {
                ImageView player1CardImagebg = (ImageView) findViewById(R.id.Player1DeckImageBackground);
                player1CardImagebg.setImageResource(R.drawable.empty);
            }
            if (player1remaining[0] > 0) {
                ImageView player1CardImagebg = (ImageView) findViewById(R.id.Player1DeckImageBackground);
                player1CardImagebg.setImageResource(R.drawable.bicycledark);

            }
            if (player2remaining[0] == 0) {
                ImageView player2CardImagebg = (ImageView) findViewById(R.id.Player2DeckImageBackground);
                player2CardImagebg.setImageResource(R.drawable.empty);
            }
            if (player2remaining[0] > 0) {
                ImageView player2CardImagebg = (ImageView) findViewById(R.id.Player2DeckImageBackground);
                player2CardImagebg.setImageResource(R.drawable.bicycledark);
            }

        }

    }

    //     ___ ___  _ __ ___  _ __   __ _ _ __ ___
    //    / __/ _ \| '_ ` _ \| '_ \ / _` | '__/ _ \
    //   | (_| (_) | | | | | | |_) | (_| | | |  __/
    //    \___\___/|_| |_| |_| .__/ \__,_|_|  \___|
    //                       |_|
    //
    public void compare(final int player1Value, final int player2Value, final String player1Code, final String player2Code) {
        Log.d(TAG, player1Value + "//" + player2Value + "//" + player1Code + "//" + player2Code);
        if (player1Value > player2Value && player2remaining[0] != 0) {
            try {
                JsonObjectRequest jsonObjectRequest5 = new JsonObjectRequest(
                        Request.Method.GET,
                        "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player1/add/?cards=" + player1Code + "," + player2Code,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                Log.d(TAG, player1Code + " and " + player2Code + " added to player 1's pile.");

                                player1remaining[0] += 2;

                                TextView player2remain = findViewById(R.id.computerCountValue);
                                player2remain.setText(String.valueOf(player2remaining[0]));
                                TextView player1remain = findViewById(R.id.playerCountValue);
                                player1remain.setText(String.valueOf(player1remaining[0]));



                                final Toast toast1 = Toast.makeText(getApplicationContext(), "Player 1 won!",
                                        Toast.LENGTH_SHORT);
                                toast1.setGravity(Gravity.LEFT, 0, 0);
                                toast1.show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast1.cancel();
                                    }
                                }, 400);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                });
                requestQueue.add(jsonObjectRequest5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (player1Value > player2Value && player2remaining[0] == 0) {
            Log.d(TAG, player1Code + " and " + player2Code + " added to player 1's pile.");

            player1remaining[0] += 2;

            TextView player1remain = findViewById(R.id.playerCountValue);
            player1remain.setText(String.valueOf(player1remaining[0]));
            ImageView player2cardimage = findViewById(R.id.player2CardImage);
            player2cardimage.setImageResource(R.drawable.empty);
            Button war = findViewById(R.id.warButton);
            war.setEnabled(false);

            final Toast toast1 = Toast.makeText(getApplicationContext(), "PLAYER 1 WINS THE WAR!",
                    Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.LEFT, 0, 0);
            toast1.show();



            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast1.cancel();
                }
            }, 3000);
        }
        if (player1Value < player2Value && player1remaining[0] != 0) {
            try {
                JsonObjectRequest jsonObjectRequest6 = new JsonObjectRequest(
                        Request.Method.GET,
                        "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player2/add/?cards=" + player1Code + "," + player2Code,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {

                                player2remaining[0] += 2;
                                if (player1remaining[0] == 0) {
                                    ImageView player1CardImagebg = (ImageView) findViewById(R.id.Player1DeckImageBackground);
                                    player1CardImagebg.setImageResource(R.drawable.empty);
                                }
                                if (player1remaining[0] > 0) {
                                    ImageView player1CardImagebg = (ImageView) findViewById(R.id.Player1DeckImageBackground);
                                    player1CardImagebg.setImageResource(R.drawable.bicycledark);

                                }
                                if (player2remaining[0] == 0) {
                                    ImageView player2CardImagebg = (ImageView) findViewById(R.id.Player2DeckImageBackground);
                                    player2CardImagebg.setImageResource(R.drawable.empty);
                                }
                                if (player2remaining[0] > 0) {
                                    ImageView player2CardImagebg = (ImageView) findViewById(R.id.Player2DeckImageBackground);
                                    player2CardImagebg.setImageResource(R.drawable.bicycledark);
                                }

                                TextView player2remain = findViewById(R.id.computerCountValue);
                                player2remain.setText(String.valueOf(player2remaining[0]));
                                TextView player1remain = findViewById(R.id.playerCountValue);
                                player1remain.setText(String.valueOf(player1remaining[0]));


                                Log.d(TAG, player1Code + " and " + player2Code + " added to player 2's pile.");
                                final Toast toast2 = Toast.makeText(getApplicationContext(), "Player 2 won!",
                                        Toast.LENGTH_SHORT);
                                toast2.setGravity(Gravity.RIGHT, 0, 0);
                                toast2.show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast2.cancel();
                                    }
                                }, 400);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                });
                requestQueue.add(jsonObjectRequest6);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (player2Value > player1Value && player1remaining[0] == 0) {
            Log.d(TAG, player2Code + " and " + player1Code + " added to player 2's pile.");

            player1remaining[0] += 2;

            TextView player2remain = findViewById(R.id.playerCountValue);
            player2remain.setText(String.valueOf(player2remaining[0]));
            ImageView player1cardimage = findViewById(R.id.player1CardImage);
            player1cardimage.setImageResource(R.drawable.empty);
            if (player1remaining[0] == 0) {
                ImageView player1CardImagebg = (ImageView) findViewById(R.id.Player1DeckImageBackground);
                player1CardImagebg.setImageResource(R.drawable.empty);
            }
            if (player1remaining[0] > 0) {
                ImageView player1CardImagebg = (ImageView) findViewById(R.id.Player1DeckImageBackground);
                player1CardImagebg.setImageResource(R.drawable.bicycledark);

            }
            if (player2remaining[0] == 0) {
                ImageView player2CardImagebg = (ImageView) findViewById(R.id.Player2DeckImageBackground);
                player2CardImagebg.setImageResource(R.drawable.empty);
            }
            if (player2remaining[0] > 0) {
                ImageView player2CardImagebg = (ImageView) findViewById(R.id.Player2DeckImageBackground);
                player2CardImagebg.setImageResource(R.drawable.bicycledark);
            }


            final Toast toast1 = Toast.makeText(getApplicationContext(), "PLAYER 2 WINS THE WAR!",
                    Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.LEFT, 0, 0);
            toast1.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast1.cancel();
                }
            }, 3000);
        }
        if (player1Value == player2Value) {
            try {
                JsonObjectRequest jsonObjectRequest7 = new JsonObjectRequest(
                        Request.Method.GET,
                        "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player1/add/?cards=" + player1Code,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                Log.d(TAG, player1Code + " added back into player 1's pile.");

                                player1remaining[0] += 1;

                                TextView player2remain = findViewById(R.id.computerCountValue);
                                player2remain.setText(String.valueOf(player2remaining[0]));
                                TextView player1remain = findViewById(R.id.playerCountValue);
                                player1remain.setText(String.valueOf(player1remaining[0]));
                                if (player1remaining[0] == 0) {
                                    ImageView player1CardImagebg = (ImageView) findViewById(R.id.Player1DeckImageBackground);
                                    player1CardImagebg.setImageResource(R.drawable.empty);
                                }
                                if (player1remaining[0] > 0) {
                                    ImageView player1CardImagebg = (ImageView) findViewById(R.id.Player1DeckImageBackground);
                                    player1CardImagebg.setImageResource(R.drawable.bicycledark);

                                }
                                if (player2remaining[0] == 0) {
                                    ImageView player2CardImagebg = (ImageView) findViewById(R.id.Player2DeckImageBackground);
                                    player2CardImagebg.setImageResource(R.drawable.empty);
                                }
                                if (player2remaining[0] > 0) {
                                    ImageView player2CardImagebg = (ImageView) findViewById(R.id.Player2DeckImageBackground);
                                    player2CardImagebg.setImageResource(R.drawable.bicycledark);
                                }

                                final Toast toast3 = Toast.makeText(getApplicationContext(), "TIE!",
                                        Toast.LENGTH_SHORT);
                                toast3.setGravity(Gravity.CENTER, 0, 0);
                                toast3.show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast3.cancel();
                                    }
                                }, 400);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                });
                requestQueue.add(jsonObjectRequest7);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JsonObjectRequest jsonObjectRequest8 = new JsonObjectRequest(
                        Request.Method.GET,
                        "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player2/add/?cards=" + player2Code,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                Log.d(TAG, player2Code + " added back into player 2's pile.");

                                player2remaining[0] += 1;

                                TextView player2remain = findViewById(R.id.computerCountValue);
                                player2remain.setText(String.valueOf(player2remaining[0]));
                                TextView player1remain = findViewById(R.id.playerCountValue);
                                player1remain.setText(String.valueOf(player1remaining[0]));

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                });
                requestQueue.add(jsonObjectRequest8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //               _    ____          _
    //     __ _  ___| |_ / ___|___   __| | ___
    //    / _` |/ _ \ __| |   / _ \ / _` |/ _ \
    //   | (_| |  __/ |_| |__| (_) | (_| |  __/
    //    \__, |\___|\__|\____\___/ \__,_|\___|
    //    |___/
    //get code - gets the two letter code for each card to use when placing in the pot pile.
    public String getCode(final String json, final int index) {
        JsonParser parser = new JsonParser();
        JsonObject jsonobj = parser.parse(json).getAsJsonObject();
        JsonArray cardsArray;
        cardsArray = jsonobj.get("cards").getAsJsonArray();
        JsonObject cardobj;
        cardobj = cardsArray.get(index).getAsJsonObject();
        String code = cardobj.get("code").getAsString();
        return code;
    }

    //               _   ___
    //     __ _  ___| |_|_ _|_ __ ___   __ _  __ _  ___ / _(_) | ___/ ___|| |_ _ __(_)_ __   __ _
    //    / _` |/ _ \ __|| || '_ ` _ \ / _` |/ _` |/ _ \ |_| | |/ _ \___ \| __| '__| | '_ \ / _` |
    //   | (_| |  __/ |_ | || | | | | | (_| | (_| |  __/  _| | |  __/___) | |_| |  | | | | | (_| |
    //    \__, |\___|\__|___|_| |_| |_|\__,_|\__, |\___|_| |_|_|\___|____/ \__|_|  |_|_| |_|\__, |
    //    |___/                              |___/                                          |___/
    //get image -- get the image filepath of the card to be displayed
    public String getImageFileString(final String code) {
        String imgFileString;
        imgFileString = code.toLowerCase();
        StringBuilder sb = new StringBuilder(imgFileString);
        sb.insert(0, 'i');
        imgFileString = sb.toString();
        return imgFileString;


    }

    //               _ __     __    _
    //     __ _  ___| |\ \   / /_ _| |_   _  ___
    //    / _` |/ _ \ __\ \ / / _` | | | | |/ _ \
    //   | (_| |  __/ |_ \ V / (_| | | |_| |  __/
    //    \__, |\___|\__| \_/ \__,_|_|\__,_|\___|
    //    |___/
    //get value -- look at "value" json object and get an int based on the card value
    public int getValue(final String json) {
        String value;
        int valueInt = 0;

        value = json;
        if (value.equals("ACE")) {
            valueInt = 14;
        }
        if (value.equals("KING")) {
            valueInt = 13;
        }
        if (value.equals("QUEEN")) {
            valueInt = 12;
        }
        if (value.equals("JACK")) {
            valueInt = 11;
        }
        if (value.equals("10")) {
            valueInt = 10;
        }
        if (value.equals("9")) {
            valueInt = 9;
        }
        if (value.equals("8")) {
            valueInt = 8;
        }
        if (value.equals("7")) {
            valueInt = 7;
        }
        if (value.equals("6")) {
            valueInt = 6;
        }
        if (value.equals("5")) {
            valueInt = 5;
        }
        if (value.equals("4")) {
            valueInt = 4;
        }
        if (value.equals("3")) {
            valueInt = 3;
        }
        if (value.equals("2")) {
            valueInt = 2;
        }
        return valueInt;

    }

    //               _   ____                      _       _             ____  _ _
    //     __ _  ___| |_|  _ \ ___ _ __ ___   __ _(_)_ __ (_)_ __   __ _|  _ \(_) | ___
    //    / _` |/ _ \ __| |_) / _ \ '_ ` _ \ / _` | | '_ \| | '_ \ / _` | |_) | | |/ _ \
    //   | (_| |  __/ |_|  _ <  __/ | | | | | (_| | | | | | | | | | (_| |  __/| | |  __/
    //    \__, |\___|\__|_| \_\___|_| |_| |_|\__,_|_|_| |_|_|_| |_|\__, |_|   |_|_|\___|
    //    |___/                                                    |___/
    public int getRemainingPile(final String json, final String id) {
        int remaining;
        JsonParser parser = new JsonParser();
        JsonObject jsonobj = parser.parse(json).getAsJsonObject();
        jsonobj = jsonobj.get("piles").getAsJsonObject();
        jsonobj = jsonobj.get(id).getAsJsonObject();
        remaining = jsonobj.get("remaining").getAsInt();

        Log.d(TAG, "Cards remaining: " + remaining);
        return remaining;
    }

    //               _   ____                      _       _             ____  _ _         _       _     _
    //     __ _  ___| |_|  _ \ ___ _ __ ___   __ _(_)_ __ (_)_ __   __ _|  _ \(_) | ___   / \   __| | __| |
    //    / _` |/ _ \ __| |_) / _ \ '_ ` _ \ / _` | | '_ \| | '_ \ / _` | |_) | | |/ _ \ / _ \ / _` |/ _` |
    //   | (_| |  __/ |_|  _ <  __/ | | | | | (_| | | | | | | | | | (_| |  __/| | |  __// ___ \ (_| | (_| |
    //    \__, |\___|\__|_| \_\___|_| |_| |_|\__,_|_|_| |_|_|_| |_|\__, |_|   |_|_|\___/_/   \_\__,_|\__,_|
    //    |___/                                                    |___/
    //
    public int getRemainingPileAdd(final String json) {
        int remaining;
        JsonParser parser = new JsonParser();
        JsonObject jsonobj = parser.parse(json).getAsJsonObject();
        remaining = jsonobj.get("remaining").getAsInt();


        return remaining;
    }

    //    _     ____                                        _____
    //   (_)___/ ___|  ___  _ __ ___   ___  ___  _ __   ___|__  /___ _ __ ___
    //   | / __\___ \ / _ \| '_ ` _ \ / _ \/ _ \| '_ \ / _ \ / // _ \ '__/ _ \
    //   | \__ \___) | (_) | | | | | |  __/ (_) | | | |  __// /|  __/ | | (_) |
    //   |_|___/____/ \___/|_| |_| |_|\___|\___/|_| |_|\___/____\___|_|  \___/
    //
    public boolean isSomeoneZero(final int p1, final int p2) {
        return (p1 == 0 || p2 == 0);
    }
    
    //    _       _ _   _       _ ____   __
    //   (_)_ __ (_) |_(_) __ _| |___ \ / /_
    //   | | '_ \| | __| |/ _` | | __) | '_ \
    //   | | | | | | |_| | (_| | |/ __/| (_) |
    //   |_|_| |_|_|\__|_|\__,_|_|_____|\___/
    //
    public void initialTwentySix1() {
        String ts = "";
        final StringBuilder sb = new StringBuilder(ts);
        try {
            JsonObjectRequest jsonObjectTS1 = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://deckofcardsapi.com/api/deck/" + deckID + "/draw/?count=26",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.w(TAG, "call 2: " + response.toString());
                            String ts = "";

                            for (int i = 0; i < 26; i++) {
                                if (i == 0) {
                                    sb.insert(0, (getCode(response.toString(), i)));
                                } else {
                                    sb.insert(0, (getCode(response.toString(), i) + ","));
                                }
                            }
                            ts = sb.toString();
                            Log.d(TAG, "Cards drawn from deck to give to player 1: " + ts);
                            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(
                                    Request.Method.GET,
                                    "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player1/add/?cards=" + ts + "/",
                                    null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(final JSONObject response) {
                                            Log.d(TAG, "Cards remaining in player1's pile: " + getRemainingPileAdd(response.toString()));
                                            Log.w(TAG, "call 3: " + response.toString());

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(final VolleyError error) {
                                    Log.e(TAG, error.toString());
                                }
                            });
                            requestQueue.add(jsonObjectRequest1);


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectTS1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initialTwentySix2() {
        String ts = "";
        final StringBuilder sb = new StringBuilder(ts);
        try {
            JsonObjectRequest jsonObjectTS2 = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://deckofcardsapi.com/api/deck/" + deckID + "/draw/?count=26",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.w(TAG, "call 4: " + response.toString());
                            String ts = "";

                            for (int i = 0; i < 26; i++) {
                                if (i == 0) {
                                    sb.insert(0, (getCode(response.toString(), i)));
                                } else {
                                    sb.insert(0, (getCode(response.toString(), i) + ","));
                                }
                            }
                            ts = sb.toString();
                            Log.d(TAG, "Cards drawn from deck to give to player 2: " + ts);
                            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(
                                    Request.Method.GET,
                                    "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player2/add/?cards=" + ts + "/",
                                    null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(final JSONObject response2) {
                                            Log.d(TAG, "Cards remaining in player2's pile: " + getRemainingPileAdd(response2.toString()));
                                            Log.w(TAG, "call 5: " + response.toString());

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(final VolleyError error) {
                                    Log.e(TAG, error.toString());
                                }
                            });
                            requestQueue.add(jsonObjectRequest2);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectTS2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    //
    //
}