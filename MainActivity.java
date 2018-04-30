package edu.illinois.cs.cs125.mp7app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
        Toast.makeText(getApplicationContext(),
                "New deck of 52 shuffled and dealt. Ready to play.",
                Toast.LENGTH_LONG).show();
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
                            getID(response.toString());

                            try {
                                Log.d(TAG, response.toString(2));
                                display.setText(response.toString(2));
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

    /**
     * start next war move
     * @param press this button
     */
    public void nextMove(final View press) {
        // war();
        // run deck of cards API
        // if player > computer
        Toast.makeText(getApplicationContext(), "you won!",
                Toast.LENGTH_LONG).show();
        // if player > computer
        Toast.makeText(getApplicationContext(), "you lost!",
                Toast.LENGTH_LONG).show();

    }

    public void getID(final String json) {
        JsonParser parser = new JsonParser();
        JsonObject jsonobj = parser.parse(json).getAsJsonObject();
        deckID = jsonobj.get("deck_id").getAsString();
        Log.d(TAG, deckID);
    }


    public void startWar(final View press) {


        // get https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1
        // get deck_id; save as string;

        // Divide deck into 2 piles (player, computer)
        // https://deckofcardsapi.com/api/deck/<<deck_id>>/pile/player1/add/?count=26
        // https://deckofcardsapi.com/api/deck/<<deck_id>>/pile/player2/?count=26
        // https://deckofcardsapi.com/api/deck/<<deck_id>>/pile/pot/

        // Card images to card backs png

    }

    public void nextMove() {
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
        // if playerCardValue > computerCardValue, Toast "you won the WAR!"
        // put both cards into player's deck
        //
        // if playerCardValue < computerCardValue, Toast "you lost the WAR!"
        // put both cards into computer's deck
        //
        // if playerCardValue = computerCardValue, Toast "Stalemate! Play again!"
        //	play again, put these two cards in the winner's deck.
    }


    //get code
    //get value
    //get remaining
}
