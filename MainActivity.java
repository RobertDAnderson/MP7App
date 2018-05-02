package edu.illinois.cs.cs125.mp7app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

//hi there 2

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MP7:Main";
    public String deckID = "";
    private static RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
            }
        });

        requestQueue.add(jsonObjectRequest);
        JsonParser parser = new JsonParser();
        JsonObject deckIDgetter = parser.parse(String.valueOf(jsonObjectRequest)).getAsJsonObject();
        if (deckIDgetter.has("deck_id")) {
            deckIDgetter = deckIDgetter.get("deck_id").getAsJsonObject();
            String deckID = deckIDgetter.toString();
            TextView id = findViewById(R.id.deckID);
            id.setText(deckID);

        }
        */
    }

    protected void onPause() {
        super.onPause();
    }

    public void onClickNew(final View view) {

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            TextView display = findViewById(R.id.deckID);
                            Log.d(TAG, response.toString());
                            Toast.makeText(getApplicationContext(),
                                    "New deck of 52 shuffled and dealt. Ready to play.",
                                    Toast.LENGTH_LONG).show();
                            getID(response.toString());
                            String twentysix1 = initialTwentySix();


                            try {
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                        Request.Method.GET,
                                        "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player1/add/?cards=" + twentysix1,
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(final JSONObject response) {
                                                Log.d(TAG, "Cards in player1's pile: " + getRemainingPileAdd(response.toString()));

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
                            String twentysix2 = initialTwentySix();
                            try {
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                        Request.Method.GET,
                                        "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player2/add/?cards=" + twentysix2,
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(final JSONObject response) {
                                                Log.d(TAG, "Cards in player2's pile: " + getRemainingPileAdd(response.toString()));

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
                            /*
                            try {
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                        Request.Method.GET,
                                        "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/pot/add/?count=0",
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(final JSONObject response) {
                                                Log.d(TAG, "Cards in the pot: " + getRemaining(response.toString()));
                                                try {
                                                    Log.d(TAG, response.toString(2));

                                                } catch (JSONException ignored) { }
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
                            */

                            // Deal 26 to each pile
                            // https://deckofcardsapi.com/api/deck/<<deck_id>>/pile/player1/add/?count=26
                            // https://deckofcardsapi.com/api/deck/<<deck_id>>/pile/player2/add?count=26
                            //
                            // initialize a pot pile
                            // https://deckofcardsapi.com/api/deck/<<deck_id>>/pile/pot/add?count=0
                            //
                            // reset all images
                            ImageView player1CardImage = (ImageView)findViewById(R.id.player1CardImage);
                            player1CardImage.setImageResource(R.drawable.empty);
                            ImageView player2CardImage = (ImageView)findViewById(R.id.player2CardImage);
                            player2CardImage.setImageResource(R.drawable.empty);


                            try {
                                Log.d(TAG, response.toString(2));
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

    public void getID(final String json) {
        JsonParser parser = new JsonParser();
        JsonObject jsonobj = parser.parse(json).getAsJsonObject();
        deckID = jsonobj.get("deck_id").getAsString();
        Log.d(TAG, "The deck's ID is: " + deckID);
    }


    public void nextMove(final View view) {

        /*
        String json = getAsJson("https://deckofcardsapi.com/api/deck/" + <<deck_id>> + "/pile/player1/draw/?count=1")
        player1Pile.draw(1);
        player1Image.setImage(player1DrawPile.getCode());
        int player1Value = player1DrawPile.getValue(json);
        player1DrawPile.addToPile(pot);

        json = getAsJson("https://deckofcardsapi.com/api/deck/" + <<deck_id>> + "/pile/player2/draw/?count=1")
        player2Pile.draw(1);
        player2Image.setImage(player2DrawPile.getCode());
        int player2Value = player2DrawPile.getValue(json);
        player1DrawPile.addToPile(pot);

        if (player1Value > player2Value) {
            pot.addToPile(player1Pile);
            if (player2Pile.getRemaining <= 0) {
                //CODE FOR DECLARING WINNER HERE//
            }
            return;
        } else if (player2Value > player1Value) {
            pot.addToPile(player2Pile);
            if (player1Pile.getRemaining <= 0) {
                //CODE FOR DECLARING WINNER HERE//
            }
            return;
        }
        war(press);
         */
        final int[] player1Value = new int[1];
        final String[] player1Code = new String[1];
        final int[] player2Value = new int[1];
        final String[] player2Code = new String[1];

        // image updaters
        final String[] img1id = new String[1];
        final String[] img2id = new String[1];

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://deckofcardsapi.com/api/deck/" + deckID + "/draw/?count=3",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {

                            Log.d(TAG, "The list is: " + response.toString());
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



        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player1/draw/?count=1/bottom/",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, "cards left in player 1 after dealing a card: " + (getRemainingPile(response.toString(), "player1")));
                            JsonParser parser = new JsonParser();
                            JsonObject jsonobj = parser.parse(response.toString()).getAsJsonObject();
                            JsonArray card = jsonobj.get("cards").getAsJsonArray();
                            jsonobj = card.get(0).getAsJsonObject();
                            // value getter
                            player1Value[0] = getValue(jsonobj.get("value").getAsString());
                            // code getter
                            player1Code[0] = getCode(response.toString(), 0);
                            // image getter
                            img1id[0] = getImageFileString(getCode(response.toString(), 0));
                            ImageView player1CardImage = findViewById(R.id.player1CardImage);
                            player1CardImage.setImageResource(getResources().getIdentifier(img1id[0],"drawable", getPackageName()));


                            Log.d(TAG, "The value of Player 1's card is: " + player1Value[0]);
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
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player2/draw/?count=1/bottom/",
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
                            // code getter
                            player2Code[0] = getCode(response.toString(), 0);
                            // image getter
                            img2id[0] = getImageFileString(getCode(response.toString(), 0));
                            ImageView player2CardImage = findViewById(R.id.player2CardImage);
                            player2CardImage.setImageResource(getResources().getIdentifier(img2id[0],"drawable", getPackageName()));



                            Log.d(TAG, "The value of Player 2's card is: " + player2Value[0]);
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
        if (player1Value[0] > player2Value[0]) {
            try {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player1/add/?cards=" + player1Code[0] + "," + player2Code[0],
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                Log.d(TAG, player1Code[0] + " and " + player2Code[0] + " added to player 1's pile.");

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
        if (player1Value[0] < player2Value[0]) {
            try {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player2/add/?cards=" + player1Code[0] + "," + player2Code[0],
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                Log.d(TAG, player1Code[0] + " and " + player2Code[0] + " added to player 2's pile.");

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
        } if (player1Value[0] == player2Value[0]) {
            try {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player1/add/?cards=" + player1Code[0],
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                Log.d(TAG, player1Code[0] + "added back into player 1's pile.");

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
            try {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        "https://deckofcardsapi.com/api/deck/" + deckID + "/pile/player2/add/?cards=" + player2Code[0],
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                Log.d(TAG, player2Code[0] + "added back into player 2's pile.");

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

        // Draw bottom card from each deck.
        // https://deckofcardsapi.com/api/deck/<<deck_id>>/pile/player1/draw/?count=1
        // https://deckofcardsapi.com/api/deck/<<deck_id>>/pile/player2/draw/?count=1
        // get value of player 1's card
        //      convert value string to int p1value
        //      save code as string p1code, add to pot list[0]
        // get value of player 2's card
        //      convert value string to int p2value
        //      save code as string p2code, add to pot list[1]
        // add p1code and p2code to pot list
        // https://deckofcardsapi.com/api/deck/<<deck_id>>/pile/pot/add/?cards=p1code,p2code
        // if p1value > p2value
        //      recurse through pot list add all cards in pot list to pile player1
        //      https://deckofcardsapi.com/api/deck/<<deck_id>>/pile/player1/add/?cards=p1code,p2code
        //      clear list
        // if p1value > p2value
        //      recurse through pot list add all cards in pot list to pile player1
        //      https://deckofcardsapi.com/api/deck/<<deck_id>>/pile/player2/add/?cards=p1code,p2code
        //      clear list
        // if p1value == p2value
        //


        // Display each player's card on their card image
        //
        // comparing the cards
        // if Player 1 value > Player 2 value,
        /*
        Toast.makeText(getApplicationContext(), "Player 1 won!",
                Toast.LENGTH_LONG).show();

        // put both cards into Player 1's deck
        //
        // if Player 1 value < Player 2 value,

        Toast.makeText(getApplicationContext(), "Player 2 won!",
                Toast.LENGTH_LONG).show();
        */
        // put both cards into Player 2's deck
        //
        // if playerCardValue = computerCardValue, Toast "Stalemate! Play again!"
        //	play again, put these two cards in the winner's deck.
    }

    //get code - gets the two letter code for each card to use when placing in the pot pile.
    public String getCode(final String json, final int index) {
        JsonParser parser = new JsonParser();
        JsonObject jsonobj = parser.parse(json).getAsJsonObject();
        JsonArray cardsArray;
        cardsArray = jsonobj.get("cards").getAsJsonArray();
        JsonObject cardobj;
        cardobj = cardsArray.get(index).getAsJsonObject();
        String code = cardobj.get("code").getAsString();
        Log.d(TAG, "The drawn card's code is: " + code);
        return code;
    }


    //get image -- get the image filepath of the card to be displayed
    public String getImageFileString(final String code) {
        String imgFileString;
        imgFileString = code.toLowerCase();
        StringBuilder sb = new StringBuilder(imgFileString);
        sb.insert(0, 'i');
        Log.d(TAG, imgFileString);
        return imgFileString;


    }
    // set image -- change the image on the activity_main.xml to the current card.
    public void setImage(final String json, final int player) {
        if (player == 1) {
            String code = getImageFileString(getCode(json, 0));
            ImageView player1CardImage = findViewById(R.id.player1CardImage);
            player1CardImage.setImageResource(getResources().getIdentifier(code,"drawable", getPackageName()));
        }
        if (player == 2) {
            String code = getImageFileString(getCode(json, 0));
            ImageView player2CardImage = findViewById(R.id.player2CardImage);
            player2CardImage.setImageResource(getResources().getIdentifier(code,"drawable", getPackageName()));
        }

    }

    //get value -- look at "value" json object and get an int based on the card value
    public int getValue(final String json) {
        String value;
        int valueInt = 0;
        JsonParser parser = new JsonParser();
        JsonObject jsonobj = parser.parse(json).getAsJsonObject();
        value = jsonobj.get("value").getAsString();
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
        Log.d(TAG, "The value of this card is: " + valueInt);
        return valueInt;

    }

    //get remaining -- get the remaining cards of each deck
    public int getRemaining(final String json) {
        int remaining;
        JsonParser parser = new JsonParser();
        JsonObject jsonobj = parser.parse(json).getAsJsonObject();
        remaining = jsonobj.get("remaining").getAsInt();
        Log.d(TAG, "Cards remaining: " + remaining);
        return remaining;
    }

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

    public int getRemainingPileAdd(final String json) {
        int remaining;
        JsonParser parser = new JsonParser();
        JsonObject jsonobj = parser.parse(json).getAsJsonObject();
        remaining = jsonobj.get("remaining").getAsInt();

        Log.d(TAG, "Cards remaining in pile after calling getRemainingPileAdd: " + remaining);
        return remaining;
    }

    public void getRemainingUpdate() {

    }
    public String initialTwentySix() {
        String ts = "";
        final StringBuilder sb = new StringBuilder(ts);
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://deckofcardsapi.com/api/deck/" + deckID + "/draw/?count=26",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            String ts = "";

                            for (int i = 0; i < 26; i++) {
                                if (i == 0) {
                                    sb.insert(0, (getCode(response.toString(), i)));
                                } else {
                                    sb.insert(0, (getCode(response.toString(), i) + ","));
                                }
                            }
                            ts = sb.toString();
                            Log.d(TAG, "Cards drawn: " + ts);

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
        return ts;
    }
}