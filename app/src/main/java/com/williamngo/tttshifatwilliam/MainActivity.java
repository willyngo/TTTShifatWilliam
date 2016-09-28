package com.williamngo.tttshifatwilliam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import static com.williamngo.tttshifatwilliam.R.string.vsDroid;

public class MainActivity extends AppCompatActivity {
    public boolean againstDroid = true; //Determines whether user is playing against the droid or another human
    public boolean playersTurn = true; //Determines whose turn it currently is
    public ImageButton[] imgBtnArray = new ImageButton[9]; //Array holding all the imgBtns
    public int[] dataArray = new int[9]; //Array containing values 0 and 1 that represents X and O
    public int turnCounter = 0; //Counter checks for tie game when it reaches 9 and there is no winner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set all image button in array
        imgBtnArray[0] = (ImageButton) findViewById(R.id.imageButton0);
        imgBtnArray[1] = (ImageButton) findViewById(R.id.imageButton1);
        imgBtnArray[2] = (ImageButton) findViewById(R.id.imageButton2);
        imgBtnArray[3] = (ImageButton) findViewById(R.id.imageButton3);
        imgBtnArray[4] = (ImageButton) findViewById(R.id.imageButton4);
        imgBtnArray[5] = (ImageButton) findViewById(R.id.imageButton5);
        imgBtnArray[6] = (ImageButton) findViewById(R.id.imageButton6);
        imgBtnArray[7] = (ImageButton) findViewById(R.id.imageButton7);
        imgBtnArray[8] = (ImageButton) findViewById(R.id.imageButton8);

        //Set Tag for all image
        imgBtnArray[0].setTag(0);
        imgBtnArray[1].setTag(1);
        imgBtnArray[2].setTag(2);
        imgBtnArray[3].setTag(3);
        imgBtnArray[4].setTag(4);
        imgBtnArray[5].setTag(5);
        imgBtnArray[6].setTag(6);
        imgBtnArray[7].setTag(7);
        imgBtnArray[8].setTag(8);


        //Set event listener and set up image buttons to their cover images
        setUpImageButtons();
    }

    public void LaunchAbout(View view)
    {
        Intent intent = new Intent(this, about.class);
        startActivity(intent);
    }

    public void LaunchScore(View view)
    {
        Intent intent = new Intent(this, score.class);
        startActivity(intent);
    }

    /*******************Gameplay related methods********************************/

    /**
     * Resets board by putting every image back to cover and setting onClickListener
     * as well as resetting the dataArray values backt to 0
     */
    public void resetBoard_onClick(View view) {
        resetBoard();
    }

    public void resetBoard()
    {
        playersTurn = true;
        //Resets all images back to the cover
        setUpImageButtons();

        //Resets all values of array to 0
        for (int i : dataArray)
            i = 0;
    }

    /**
     * Once player has played his/her/its turn, set appropriate value into the dataArray,
     * Switch turn and disables the clicking of the image button that was clicked.
     * Afterwards, check across the board if there is a winner.
     *
     * @param view
     */
    public void playedTurn(View view)
    {
        ImageButton ib = (ImageButton)findViewById(view.getId());
        int index = (Integer) ib.getTag();

        //Check the players turn before associating correct image
        //Insert value into dataArray according to the player
        if(playersTurn) {
            dataArray[index] = 1;
            ib.setImageResource(R.drawable.cross);
        }
        else {
            dataArray[index] = 2;
            ib.setImageResource(R.drawable.tlzino);
        }

        //Increment turn coutner
        turnCounter++;

        //give turn to other player
        playersTurn = !playersTurn;

        //Disable image button
        view.setOnClickListener(null);

        checkWinner();
    }

    /**
     * This methods checks for a winner after every turn has been played.
     * It checks every row and column as well as both diagonal for a series of 3 consecutive symbols
     * Once it finds a match, it ends the game and increments the scores
     * If after 9 turns and there is still no winner, the game ends in a tie
     */
    public void checkWinner()
    {
        boolean winner = false;
        int player;

        if(!playersTurn)
            player = 1;
        else
            player = 2;

        //Checks for each row, cols, and diagonals if there are 3 symbols in a row for each player

            //Checking for each row
            if(dataArray[0] == player && dataArray[1] == player && dataArray[2] == player)
                winner = true;
            if(dataArray[3] == player && dataArray[4] == player && dataArray[5] == player)
                winner = true;
            if(dataArray[6] == player && dataArray[7] == player && dataArray[8] == player)
                winner = true;

            //Checking for each column
            if(dataArray[0] == player && dataArray[3] == player && dataArray[6] == player)
                winner = true;
            if(dataArray[1] == player && dataArray[4] == player && dataArray[7] == player)
                winner = true;
            if(dataArray[2] == player && dataArray[5] == player && dataArray[8] == player)
                winner = true;

            //Checking for both diagonals
            if(dataArray[0] == player && dataArray[4] == player && dataArray[8] == player)
                winner = true;
            if(dataArray[2] == player && dataArray[4] == player && dataArray[6] == player)
                winner = true;

            //If a winner has been found, end game and exit loop
            if(winner) {
                endGame(player);
            }

        //If played all pieces and there is no winner, end game in a tie
        if(turnCounter == 9 && !winner)
            endGame(9);

    }

    /**
     * When the game ends, this method checks which player has won. If the value is 9 then that means
     * there is no winner. Increments appropriate values for winner and decrement for loser.
     *
     * @param player - The player who has won the game. If the value is 9, then it's a tie game
     */
    public void endGame(int player)
    {
        //Disables all buttons
        for(ImageButton imgBtn : imgBtnArray)
            imgBtn.setOnClickListener(null);

        //Increment wins
        if(player == 1) // If user wins
        {
            //Get textview of player 1's wins and increment
            TextView p1wins_textview = (TextView)findViewById(R.id.p1_wins);
            int p1wins = Integer.parseInt(p1wins_textview.getText().toString());
            p1wins++;
            p1wins_textview.setText(p1wins);

            //Increment losses of player 2
            TextView p2losses_textview = (TextView)findViewById(R.id.p2_losses);
            int p2losses = Integer.parseInt(p2losses_textview.getText().toString());
            p2losses++;
            p2losses_textview.setText(p2losses);
        }

        if(player == 2) //If opponent wins
        {
            if(againstDroid)// If user played against droid
            {
                TextView cpuwins_textview = (TextView)findViewById(R.id.cpu_wins);
                int cpuwins = Integer.parseInt(cpuwins_textview.getText().toString());
                cpuwins++;
                cpuwins_textview.setText(cpuwins);
            }
            else // means user played against a human
            {
                //Get textview of player 2's wins and increment
                TextView p2wins_textview = (TextView)findViewById(R.id.p2_wins);
                int p2wins = Integer.parseInt(p2wins_textview.getText().toString());
                p2wins++;
                p2wins_textview.setText(p2wins);
            }


            //Increment losses of player 1
            TextView p1losses_textview = (TextView)findViewById(R.id.p1_losses);
            int p1losses = Integer.parseInt(p1losses_textview.getText().toString());
            p1losses++;
            p1losses_textview.setText(p1losses);
        }

        if(player == 9)//Tie game;
        {
            if(againstDroid)
            {
                TextView cputies_textview = (TextView)findViewById(R.id.cpu_ties);
                int cputies = Integer.parseInt(cputies_textview.getText().toString());
                cputies++;
                cputies_textview.setText(cputies);
            }
            else
            {
                TextView p2ties_textview = (TextView)findViewById(R.id.p2_ties);
                int p2ties = Integer.parseInt(p2ties_textview.getText().toString());
                p2ties++;
                p2ties_textview.setText(p2ties);
            }

            TextView p1ties_textview = (TextView)findViewById(R.id.p1_ties);
            int p1ties = Integer.parseInt(p1ties_textview.getText().toString());
            p1ties++;
            p1ties_textview.setText(p1ties);
        }


        //Show win message

    }


    /**
     * This methods reset the images of all ImageButton to their default
     * cover images and sets all the onClickListener
     */
    public void setUpImageButtons()
    {
        //Sets every imgBtns src to the cover picture
        //and adds onClick listener to enable user to click
        for (ImageButton imgBtn : imgBtnArray){
            imgBtn.setImageResource(R.drawable.quest2);
            imgBtn.setOnClickListener(myListener);
        }
    }

    /**
     * Switches between vs the CPU and vs a Human
     * Resets the board while doing so.
     * @param view
     */
    public void switchMode(View view)
    {
        againstDroid = !againstDroid;
        TextView t = (TextView)findViewById(R.id.playmode_text);

        if(againstDroid)
            t.setText(vsDroid);
        else
            t.setText(R.string.vsHuman);

        resetBoard();
    }

    /**
     * This listener acts when a player has played its turn
     */
    public View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playedTurn(v);

        }
    };

}
