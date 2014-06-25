package com.gpop.exwear;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.util.SparseIntArray;
import android.widget.MediaController;
import android.widget.VideoView;

/** -----------------------------------------------------------------------------------------------
 *  [EXVideos] CLASS
 *  -----------------------------------------------------------------------------------------------
 */

public class EXVideos {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private Boolean isPaused; // Used for determining if a video has been paused.
    private Context exwear_context; // Context for the instance in which this class is used.
    public String currentVideo; // Used for determining what video is playing in the background.
    private VideoView exwearVideoView; // View for the instance.
    public int videoPosition; // Used for resuming playback on a video that was paused.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // CAVSounds(): Constructor for CAVSounds class.
    private final static EXVideos ex_videos = new EXVideos();

    // CAVSounds(): Deconstructor for CAVSounds class.
    private EXVideos() {}

    // getInstance(): Returns the cav_sounds instance.
    public static EXVideos getInstance() { return ex_videos; }

    // initializeCAV(): Initializes the CAVSounds class variables.
    public void initializeVideo(Context con, VideoView view) {

        exwear_context = con;
        exwearVideoView = view;
        isPaused = false;
        currentVideo = "STOPPED";
        videoPosition = 0;
    }

    /** CLASS FUNCTIONALITY ____________________________________________________________________ **/

    public void launchVideo(String url, int position) {

        this.currentVideo = url;

        exwearVideoView.setVideoURI(Uri.parse(url));
        exwearVideoView.setMediaController(new MediaController(exwear_context));
        exwearVideoView.requestFocus();

        // If the video was previously paused, resume the video at it's previous location.
        if (isPaused == true) {
            exwearVideoView.seekTo(videoPosition); // Jumps to the position where the video left off.
            isPaused = false; // Indicates that the video is no longer paused.
        }
        else {
            exwearVideoView.seekTo(position);
        }

        exwearVideoView.start();
    }

    public void skipToPosition(Boolean isForward) {

        // Checks to see if exwearVideo has been initiated first before fast forwarding.
        if (exwearVideoView != null) {

            videoPosition = exwearVideoView.getCurrentPosition(); // Retrieves the current video position and saves it.

            // If fast forwarding, add time onto the video.
            if (isForward) {
                videoPosition = videoPosition + 60000;
            }
            else {
                videoPosition = videoPosition - 60000;
            }

            // Stops the video only if there is a video is currently playing.
            if (exwearVideoView.isPlaying() == true) {
                exwearVideoView.seekTo(videoPosition); // Jumps to the position.
            }
        }
    }

    public void pauseVideo() {

        // Checks to see if exwearVideo has been initialized first before saving the video position and pausing the video.
        if (exwearVideoView != null) {

            videoPosition = exwearVideoView.getCurrentPosition(); // Retrieves the current video position and saves it.

            // Pauses the video only if there is a video is currently playing.
            if (exwearVideoView.isPlaying() == true) { exwearVideoView.pause(); } // Pauses the video.

            isPaused = true; // Indicates that the video is currently paused.
        }
    }

    //  stopVideo(): Stops any videos playing.
    public void stopVideo() {

        // Checks to see if exwearVideo has been initiated first before stopping video playback.
        if (exwearVideoView != null) {

            // Stops the video only if there is a video is currently playing.
            if (exwearVideoView.isPlaying() == true) { exwearVideoView.stopPlayback(); } // Pauses the video.
            currentVideo = "STOPPED";
        }
    }
}
