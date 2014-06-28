package com.gpop.exwear;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

/** -----------------------------------------------------------------------------------------------
 *  [EXMain] CLASS
 *  DESCRIPTION: EXMain class is a FragmentActivity class that displays the main menu and displays
 *  the tutorial videoes.
 *  -----------------------------------------------------------------------------------------------
 */

public class EXMain extends FragmentActivity {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // CLASS VARIABLES
    private EXFont exFont; // Used for accessing the methods of the EXFont class.
    private EXImages exImages; // Used for accessing the methods of the EXImages class.
    private EXVideos exVideos; // Used for accessing the methods of the EXVideos class.

    // LAYOUT VARIABLES
    private Boolean showVidMenu = false; // Used to determine if the video menu is currently being displayed.
    private FrameLayout exmain_container_1, exmain_container_2, exmain_container_3, exmain_container_4;
    private ImageButton menuLeftButton, menuRightButton, video_1, video_2, video_3, video_4;
    private LinearLayout exmain_button_row_1, exmain_button_row_2, exmain_video_row_1, exmain_video_row_2;
    private TextView exmain_default_1, exmain_default_2, exmain_default_3, exmain_default_4,
            loading_text, video_text_1, video_text_2, video_text_3, video_text_4;
    private VideoView exmainVideoView; // Used to reference the VideoView view object.

    // SYSTEM VARIABLES
    private int currentOrientation = 0; // Used to determine the device's orientation. (0: PORTRAIT / 1: LANDSCAPE)
    private int displaySize; // Stores the device's display size.
    private final int RESULT_CLOSE_ALL = 1; // Used for startActivityForResult activity termination.
    private final String URL =  "http://www.yoonhuh.com/Misc/EXWEAR/"; // Stores the URL where the videos are stored.

    /** ACTIVITY LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // ACTIVITY INITIALIZATION: Initializes the activity and sets the XML layout.
        super.onCreate(savedInstanceState);

        // CLASS INITIALIZATION: Initializes the classes.
        exImages.getInstance().initializeImages(this); // Initializes the EXImages class.

        setUpLayout(); // Sets up the layout for the main activity.
        setUpButtons(); // Sets up the buttons for the main activity.

        // VIDEO INITIALIZATION: Initializes video playback functionality.
        exVideos.getInstance().initializeVideo(this, exmainVideoView); // Initializes the EXVideos class.
    }

    // onResume(): This function runs immediately after onCreate() finishes and is always re-run
    // whenever the activity is resumed from an onPause() state.
    @Override
    protected void onResume() {
        super.onResume();

        // If the video was previously playing, the video is resumed at the position before it was paused.
        if (showVidMenu) {
            exVideos.getInstance().launchVideo(exVideos.getInstance().currentVideo, exVideos.getInstance().videoPosition);
        }
    }

    // onPause(): This function is called whenever the current activity is suspended or another
    // activity is launched.
    @Override
    protected void onPause(){

        super.onPause();

        // If the video is currently playing, the video is paused before the activity enters into a paused state.
        if (showVidMenu) { exVideos.getInstance().pauseVideo(); }
    }

    // onStop(): This function runs when screen is no longer visible and the activity is in a
    // state prior to destruction.
    @Override
    protected void onStop() { super.onStop(); }

    // onDestroy(): This function runs when the activity has terminated and is being destroyed.
    // Calls recycleMemory() to free up memory allocation.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycleMemory(); // Recycles all ImageView and View objects to free up memory resources.
    }

    /** ACTIVITY EXTENSION FUNCTIONALITY _______________________________________________________ **/

    // onActivityResult(): Used for ensuring termination of the entire application, along with all
    // of it's activities.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(resultCode) {
            case RESULT_CLOSE_ALL:
                setResult(RESULT_CLOSE_ALL);
                finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // onConfigurationChanged(): If the screen orientation changes, this function loads the proper
    // layout, as well as updating all layout-related objects.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setUpLayout(); // Sets up the layout for the main activity.
        setUpButtons(); // Sets up the buttons for the main activity.
        displayVideo(showVidMenu); // Displays the video container if it was previously open.
    }

    /** PHYSICAL BUTTON FUNCTIONALITY __________________________________________________________ **/

    // BACK KEY:
    // onBackPressed(): Defines the action to take when the physical back button key is pressed.
    @Override
    public void onBackPressed() {

        // If the video container is currently being display
        if (showVidMenu) {
            exVideos.getInstance().stopVideo();
            displayVideo(false);
        }

        // Terminates the application, along with all related activities.
        else { finish(); } // Closes the application.
    }

    // dispatchKeyEvent(): Defines the action to take when the volume keys are pressed.
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        int action = event.getAction(); // Retrieves the action associated with the key press.
        int keyCode = event.getKeyCode(); // Retrieves the keycode associated with the key press.

        switch (keyCode) {

            // VOLUME KEY UP:
            case KeyEvent.KEYCODE_VOLUME_UP:

                // If the volume up key is long pressed, the video is rewinded.
                if ( (action == KeyEvent.ACTION_DOWN) && (event.isLongPress() == true) ) {
                    exVideos.getInstance().skipToPosition(false);
                    toastyPopUp("BUFFERING VIDEO...");
                }

                // Otherwise the default action is performed.
                else { super.dispatchKeyEvent(event); }

                return true;

            // VOLUME KEY DOWN:
            case KeyEvent.KEYCODE_VOLUME_DOWN:

                // If the volume down key is long pressed, the video is fast forwarded.
                if ( (action == KeyEvent.ACTION_DOWN) && (event.isLongPress() == true) ) {
                    exVideos.getInstance().skipToPosition(true); // Fast forwards.
                    toastyPopUp("BUFFERING VIDEO...");
                }

                // Otherwise the default action is performed.
                else { super.dispatchKeyEvent(event); }
                return true;

            default:
                return super.dispatchKeyEvent(event);
        }
    }

    /** LAYOUT FUNCTIONALITY _______________________________________________________________ **/

    // displayLoading(): Displays the loading screen.
    private void displayLoading(Boolean isLoading) {

        FrameLayout exmainLoadingContainer = (FrameLayout) findViewById(R.id.exmain_loading_container);

        // Displays the loading container.
        if (isLoading) {
            exmainLoadingContainer.setVisibility(View.VISIBLE);
        }

        // Hides the loading container.
        else {
            exmainLoadingContainer.setVisibility(View.GONE);
        }

    }

    // displayVideo(): Displays the video container containing the VideoView.
    private void displayVideo(Boolean isOn) {

        // References the exmain_video_container layout.
        FrameLayout exmainVideoContainer = (FrameLayout) findViewById(R.id.exmain_video_container);
        LinearLayout exmainButtonContainer = (LinearLayout) findViewById(R.id.exmain_button_container);

        // Displays the video container.
        if (isOn) {

            // Displays the video container and hides the menu buttons.
            exmainVideoContainer.setVisibility(View.VISIBLE);
            exmainButtonContainer.setVisibility(View.GONE);
            menuLeftButton.setVisibility(View.GONE);
            menuRightButton.setVisibility(View.GONE);

            showVidMenu = true;
        }

        else {

            // Hides the video container and displays the menu buttons.
            exmainVideoContainer.setVisibility(View.GONE);
            exmainButtonContainer.setVisibility(View.VISIBLE);
            menuLeftButton.setVisibility(View.VISIBLE);
            menuRightButton.setVisibility(View.VISIBLE);

            showVidMenu = false;
        }
    }

    // setUpLayout(): This function is responsible for setting up the layout for the activity.
    private void setUpLayout() {

        int activityLayout = R.layout.exmain_activity; // Used to reference the application layout.
        updateDisplayLayout(); // Retrieves the device's display and orientation attributes.
        setContentView(activityLayout); // Sets the layout for the current activity.

        // References the menus.
        exmain_video_row_1 = (LinearLayout) findViewById(R.id.exmain_video_row_1);
        exmain_video_row_2 = (LinearLayout) findViewById(R.id.exmain_video_row_2);
        exmain_container_1 = (FrameLayout) findViewById(R.id.exmain_video_1_default);
        exmain_container_2 = (FrameLayout) findViewById(R.id.exmain_video_2_default);
        exmain_container_3 = (FrameLayout) findViewById(R.id.exmain_video_3_default);
        exmain_container_4 = (FrameLayout) findViewById(R.id.exmain_video_4_default);

        // References the VideoView object.
        exmainVideoView = (VideoView) findViewById(R.id.exmain_video_view);
    }

    /** USER INTERFACE FUNCTIONALITY ___________________________________________________________ **/

    // setUpButtons(): Sets up the listeners for all the buttons in the CAVMain activity.
    private void setUpButtons() {

        // Sets the references to the buttons.
        menuLeftButton = (ImageButton) findViewById(R.id.exmain_left_button);
        menuRightButton = (ImageButton) findViewById(R.id.exmain_right_button);
        video_1 = (ImageButton) findViewById(R.id.exmain_video_1);
        video_2 = (ImageButton) findViewById(R.id.exmain_video_2);
        video_3 = (ImageButton) findViewById(R.id.exmain_video_3);
        video_4 = (ImageButton) findViewById(R.id.exmain_video_4);

        // References the TextView objects.
        exmain_default_1 = (TextView) findViewById(R.id.exmain_video_1_default_text);
        exmain_default_2 = (TextView) findViewById(R.id.exmain_video_2_default_text);
        exmain_default_3 = (TextView) findViewById(R.id.exmain_video_3_default_text);
        exmain_default_4 = (TextView) findViewById(R.id.exmain_video_4_default_text);
        loading_text = (TextView) findViewById(R.id.exmain_loading_text);
        video_text_1 = (TextView) findViewById(R.id.exmain_video_text_1);
        video_text_2 = (TextView) findViewById(R.id.exmain_video_text_2);
        video_text_3 = (TextView) findViewById(R.id.exmain_video_text_3);
        video_text_4 = (TextView) findViewById(R.id.exmain_video_text_4);

        // Sets a custom font style to the buttons.
        exmain_default_1.setTypeface(EXFont.getInstance(this).getTypeFace());
        exmain_default_2.setTypeface(EXFont.getInstance(this).getTypeFace());
        exmain_default_3.setTypeface(EXFont.getInstance(this).getTypeFace());
        exmain_default_4.setTypeface(EXFont.getInstance(this).getTypeFace());
        loading_text.setTypeface(EXFont.getInstance(this).getTypeFace());
        video_text_1.setTypeface(EXFont.getInstance(this).getTypeFace());
        video_text_2.setTypeface(EXFont.getInstance(this).getTypeFace());
        video_text_3.setTypeface(EXFont.getInstance(this).getTypeFace());
        video_text_4.setTypeface(EXFont.getInstance(this).getTypeFace());

        // Sets up the listener and the actions for the left menu button.
        menuLeftButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        // Sets up the listener and the actions for the right menu button.
        menuRightButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        // Sets up the hover listener and the actions for the 'TRAINING 1' button.
        exmain_container_1.setOnHoverListener(new View.OnHoverListener() {

            @Override
            public boolean onHover(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                    exmain_container_1.setVisibility(View.INVISIBLE);
                }
                else if (event.getAction() == MotionEvent.ACTION_HOVER_MOVE) {
                    exmain_container_1.setVisibility(View.INVISIBLE);
                }
                else if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                    exmain_container_1.setVisibility(View.VISIBLE);
                }

                return true;
            }
        });

        // Sets up the click listener and the actions for the 'TRAINING 1' button.
        video_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showVidMenu = true;
                String videoName = "exwear_video_1.mp4"; // Sets the file name for the video.
                displayVideo(true); // Displays the video container.
                exVideos.getInstance().launchVideo(URL + videoName, 0); // Launches the video.
                setVideoListener( exVideos.getInstance().exwearVideoView); // Sets a listener on the VideoView object.
            }
        });

        // Sets up the hover listener and the actions for the 'TRAINING 2' button.
        exmain_container_2.setOnHoverListener(new View.OnHoverListener() {

            @Override
            public boolean onHover(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                    exmain_container_2.setVisibility(View.INVISIBLE);
                }
                else if (event.getAction() == MotionEvent.ACTION_HOVER_MOVE) {
                    exmain_container_2.setVisibility(View.INVISIBLE);
                }
                else if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                    exmain_container_2.setVisibility(View.VISIBLE);
                }

                return true;
            }
        });

        // Sets up the listener and the actions for the 'TRAINING 2' button.
        video_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showVidMenu = true;
                String videoName = "exwear_video_1.mp4"; // Sets the file name for the video.
                displayVideo(true); // Displays the video container.
                exVideos.getInstance().launchVideo(URL + videoName, 300000); // Launches the video.
                setVideoListener( exVideos.getInstance().exwearVideoView); // Sets a listener on the VideoView object.
            }
        });

        // Sets up the hover listener and the actions for the 'TRAINING 3' button.
        exmain_container_3.setOnHoverListener(new View.OnHoverListener() {

            @Override
            public boolean onHover(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                    exmain_container_3.setVisibility(View.INVISIBLE);
                }
                else if (event.getAction() == MotionEvent.ACTION_HOVER_MOVE) {
                    exmain_container_3.setVisibility(View.INVISIBLE);
                }
                else if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                    exmain_container_3.setVisibility(View.VISIBLE);
                }

                return true;
            }
        });

        // Sets up the listener and the actions for the 'TRAINING 3' button.
        video_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showVidMenu = true;
                String videoName = "exwear_video_1.mp4"; // Sets the file name for the video.
                displayVideo(true); // Displays the video container.
                exVideos.getInstance().launchVideo(URL + videoName, 600000); // Launches the video.
                setVideoListener( exVideos.getInstance().exwearVideoView); // Sets a listener on the VideoView object.
            }
        });

        // Sets up the hover listener and the actions for the 'TRAINING 4' button.
        exmain_container_4.setOnHoverListener(new View.OnHoverListener() {

            @Override
            public boolean onHover(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                    exmain_container_4.setVisibility(View.INVISIBLE);
                }
                else if (event.getAction() == MotionEvent.ACTION_HOVER_MOVE) {
                    exmain_container_4.setVisibility(View.INVISIBLE);
                }
                else if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                    exmain_container_4.setVisibility(View.VISIBLE);
                }

                return true;
            }
        });

        //  Sets up the hover listener and the actions for the 'TRAINING 4' button.
        video_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showVidMenu = true;
                String videoName = "exwear_video_1.mp4"; // Sets the file name for the video.
                displayVideo(true); // Displays the video container.
                exVideos.getInstance().launchVideo(URL + videoName, 900000); // Launches the video.
                setVideoListener( exVideos.getInstance().exwearVideoView); // Sets a listener on the VideoView object.
            }
        });
    }

    // toastyPopUp(): Creates and displays a Toast popup, informing the user that Google Maps needs
    // to be installed to continue.
    private void toastyPopUp(String message) {

        // A Toast message appears, notifying the user that Google Maps is not installed.
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // updateDisplayLayout(): Retrieves the device's screen resolution and current orientation.
    private void updateDisplayLayout() {

        // Used for retrieving the device's screen resolution values.
        Display display = getWindowManager().getDefaultDisplay();
        Point displayDimen = exImages.getInstance().getResolution(display);

        // Retrieves the device's current screen orientation.
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            currentOrientation = 0;
        }

        else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            currentOrientation = 1;
        }

        displaySize = exImages.getInstance().getDisplaySize(displayDimen, currentOrientation);
    }

    /** ADDITIONAL FUNCTIONALITY _______________________________________________________________ **/

    // setVideoListener(): Used for handling situations while the video is loading.
    private void setVideoListener(VideoView view) {

        displayLoading(true); // Displays the loading container.

        // Sets a listener on the VideoView object.
        view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                displayLoading(false); // Displays the loading container.
                displayVideo(true); // Displays the video container.
            }
        });
    }

    /** MEMORY FUNCTIONALITY ___________________________________________________________________ **/

    // recycleMemory(): Recycles AnimationDrawable, ImageView, and View objects to clear up memory
    // resources prior to Activity destruction.
    private void recycleMemory() {

        // NullPointerException error handling.
        try {

            // Unbinds all Drawable objects attached to the current layout.
            exImages.getInstance().unbindDrawables(findViewById(R.id.exmain_activity_layout));
        }

        catch (NullPointerException e) {
            e.printStackTrace(); // Prints error message.
        }
    }
}