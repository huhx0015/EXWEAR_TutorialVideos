package us.gpop.exwear;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/** -----------------------------------------------------------------------------------------------
 *  [EXImages] CLASS
 *  DESCRIPTION: This class contains methods for determining the display and resolution attributes
 *  of the device.
 *  -----------------------------------------------------------------------------------------------
 */

public class EXImages {

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // EXImages(): Constructor for the EXImages class.
    private final static EXImages EX_IMAGES = new EXImages();

    // EXImages(): Deconstructor for the EXImages class.
    private EXImages() {}

    // getInstance(): Returns the EX_IMAGES instance.
    public static EXImages getInstance() {
        return EX_IMAGES;
    }

    // initializeImages(): Initializes the EXImages class variables.
    public void initializeImages(Context con) {
        Context cav_context = con; // Context for the instance in which this class is used.
    }

    /** IMAGE FUNCTIONALITY ____________________________________________________________________ **/

    // getDisplaySize(): Used for retrieving the display size of the device.
    public int getDisplaySize(Point resolution, int currentOrientation) {

        int size = 0;

        // PORTRAIT MODE: Determines the display size from the resolution.x value.
        if (currentOrientation == 0) {
            size = resolution.x;
        }

        // LANDSCAPE MODE: Determines the display size from the resolution.y value.
        else if (currentOrientation == 1) {
            size = resolution.y;
        }

        return size;
    }

    // getResolution(): Retrieves the device's screen resolution (width and height).
    public Point getResolution(Display display) {

        Point displayDimen = new Point(); // Used for determining the device's display resolution.

        // If API Level is 13 and higher (HONEYCOMB_MR2>), use the new method.
        if (android.os.Build.VERSION.SDK_INT > 12) {
            display.getSize(displayDimen);
        }

        // If API Level is less than 13 (HONEYCOMB_MR1<), use the depreciated method.
        else {
            displayDimen.x = display.getWidth();
            displayDimen.y = display.getHeight();
        }

        return displayDimen;
    }

    /** MEMORY FUNCTIONALITY ___________________________________________________________________ **/

    // unbindDrawables(): Unbinds all Drawable objects attached to the view layout by setting them
    // to null, freeing up memory resources and preventing Context-related memory leaks. This code
    // is borrowed from Roman Guy at www.curious-creature.org.
    public void unbindDrawables(View view)
    {
        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}
