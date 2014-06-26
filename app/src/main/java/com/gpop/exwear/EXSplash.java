package com.gpop.exwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/** -----------------------------------------------------------------------------------------------
 *  [EXSplash] CLASS
 *  DESCRIPTION: EXSplash class is an activity class that is used to display a quick splash screen
 *  before the main menu is shown.
 *  -----------------------------------------------------------------------------------------------
 */

public class EXSplash extends Activity {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // ACTIVITY VARIABLES
    private int waitTime = 1500; // This value is used to set how long the splash activity is displayed (in milliseconds).

    // CLASS VARIABLES
    private EXFont exFont; // Used for accessing the methods of the EXFont class.
    private EXImages exImages; // EXImages class object that is used for display-related functionality.

    // LAYOUT VARIABLES
    private TextView exsplash_text; // References the splash TextView object.

    /** ACTIVITY LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    protected void onCreate(Bundle savedInstanceState) {

        // ACTIVITY INITIALIZATION: Initializes the activity and sets the XML layout.
        super.onCreate(savedInstanceState);

        // CLASS INITIALIZATION: Initializes the exImages class object.
        exImages.getInstance().initializeImages(this); // Initializes the CAVImage class.
        setUpLayout(); // Sets up the layout for the splash activity.

        // Creates a new timer thread for temporarily pausing the app to display the splash screen.
        // The EXSplash activity is launched after the sleep time has been exceeded.
        Thread timer = new Thread() {

            public void run() {

                try {
                    sleep(waitTime); // Time to sleep in milliseconds.
                }
                catch (InterruptedException e) {
                    e.printStackTrace(); // Prints error code.
                }
                finally {

                    Intent i = new Intent("com.gpop.exwear.MAINACTIVITY");
                    startActivity(i);
                    finish(); // Terminates the CAVSplash activity.
                }
            }
        };
        timer.start(); // Starts the thread.
    }

    // onPause(): This function is called whenever the current activity is suspended or another
    // activity is launched.
    @Override
    protected void onPause() {

        super.onPause();
        finish(); // The Splash activity is terminated at this point.
    }

    // onStop(): This function runs when screen is no longer visible and the activity is in a
    // state prior to destruction.
    @Override
    protected void onStop() {
        super.onStop();
    }

    // onDestroy(): This function runs when the activity has terminated and is being destroyed.
    // Calls recycleMemory() to free up memory allocation.
    @Override
    protected void onDestroy() {

        super.onDestroy();
        recycleMemory(); // Recycles all ImageView and View objects to free up memory resources.
    }

    /** PHYSICAL BUTTON FUNCTIONALITY __________________________________________________________ **/

    // onBackPressed(): Defines the action to take when the physical back button key is pressed.
    // Does nothing, as it overrides the functionality of the back key.
    @Override
    public void onBackPressed() { }

    /** LAYOUT FUNCTIONALITY _______________________________________________________________ **/

    // setUpLayout(): This function is responsible for setting up the layout for the splash activity.
    private void setUpLayout() {

        setContentView(R.layout.exsplash_activity);
        exsplash_text = (TextView) findViewById(R.id.exsplash_text);
        exsplash_text.setTypeface(EXFont.getInstance(this).getTypeFace());
    }

    /** ADDITIONAL FUNCTIONALITY _______________________________________________________________ **/

    /** ERROR FUNCTIONALITY ___________________________________________________________________  **/

    /** MEMORY FUNCTIONALITY _______________________________________________________________ **/

    // recycleMemory(): Recycles the layout to clear up memory resources prior to Activity destruction.
    private void recycleMemory() {

        // Unbinds all Drawable objects attached to the current layout.
        try { exImages.getInstance().unbindDrawables(findViewById(R.id.exsplash_activity_layout)); }

        // NullPointerException error handling.
        catch (NullPointerException e) {
            e.printStackTrace(); // Prints error message.
         }
    }
}