package Thirteen;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;

/**
 * sketch   ->  added incrementor param to shapeJous() to help with the trial of finding the perfect point count ( incrementor )
 * s    ->  saves .png
 * esc  ->  saves .png, kills sketch
 */


/**
 *
 * - w = 13
 * - grid adjusted
 *  - xx/yy starting point = w
 *  - xyInc : grid control in one place
 *
 * - strokeWeight(2) : 2 like xyInc which is w*2
 *  - if strokeWeight goes up, xyInc will need adjustment
 *
 * - using actual w param for shape size
 *
 * - Don't use translate()
 *  - solves need for screenX(x,y,z) and screenY(x,y,z) checks
 *
 * - 9 - 36 trials
 *  - NEW incrementer debug stamp in the middle-ish
 *
 * - Z VALUE IN SHAPEJOUS () CRRRRAAAAZZYYYYY MAAAANNNN
 *  - Z mods INC
 *
 * - BLACK & WHITE only until your lissajous adventure journey is complete
 *  - still prototyping to do before bringing color to the table
 *
 * - setupStage() util for resetting stage.  Hoping to locate some P5 smoothing magic to draw better lines
 *  - still on TODO list : curveVertex() w/Lissajous point array
 */
public class Main extends PApplet {


    int cX, cY, xx, yy, xyInc;
    int ct = 9, w = 13;
    PShape tmp;

    @Override
    public  void  settings ()  {
        size(1280, 720, "processing.opengl.PGraphics3D");
    }

    @Override
    /**
     * The setup() function is run once, when the program starts.
     * It's used to define initial environment properties such as screen size and to load media such as images and fonts as the program starts.
     * There can only be one setup() function for each program and it shouldn't be called again after its initial execution.
     If the sketch is a different dimension than the default, the size() function or fullScreen() function must be the first line in setup().
     Note: Variables DECLARED within setup() are not accessible within other functions, including draw().
     */
    public  void  setup ()  {
        setupStage();
        strokeWeight(2);

        //  setup variables
        cX = width/2;
        cY = height/2;

        xx = yy = w;
        xyInc = w*2;    // x|y increment grid control

    }


    @Override
    public  void  draw ()  {

        //  make it
        tmp = shapeJous(xx, yy, w, ct);
        //  draw it
        shape( tmp );

        if (xx >= width) {
            xx = w;
            yy += xyInc;

        //  don't use 2D logic w/3D coordinates
        } else if (yy >= height ) {


            //  STAMP IT
            fill(0);
            rect(cX-w, cY-(w*3), w*TWO_PI, w*4);

            fill(255);
            textSize(42);
            text( ct, cX, cY );
            noFill();


            //  save for reference
            save(this+"_"+ ct +".png");

            xx = yy = w;

            // only increment after drawing finishes entire screen
            ct++;

            //  reset stage
            setupStage();

            System.gc(); // optionally add one of these
        } else {
            xx += xyInc;
        }



        //  stopper
        if(ct>36){
            exit();
        }

    }


    @Override
    public void keyPressed(){

        switch(keyCode){

            case ESC:{
                save(this+".png");
                noLoop();
                super.exit();
            }
            break;

            case 's':
            case 'S':
            {
                save(this+".png");
            }
            break;
        }

    }

    /**
     * Helper function to clear stage
     */
    private void setupStage() {
        //  reset stage
        background(-1);
        smooth();
        strokeCap(ROUND);
        strokeJoin(ROUND);
    }

    /**
     * Lissajous PShape maker
     * @param a     X coordinate
     * @param b     Y coordinate
     * @param amp   Amplitude or size
     * @param inc   Loop magic incrementer [ 1 - 36 supported ]. (360 / inc) = number of points in returned PShape
     * @return  PShape containing vertices in the shape of a lissajous pattern
     */
//////////////////////////////////////////////////////
    //
    private PShape shapeJous( float a, float b, float amp, int inc )
    {
        //  PROTOTYPING : trying to locate universal ideal INCrementor for lisajouss loop
        //  Ideal range is someplace between 1 and 36
        if( ( inc < 1 ) || ( inc > 36 ) ) {
            inc = 1;
        }

        PShape shp = createShape();
        shp.beginShape();

        float x, y;

        for ( int t = 0; t <= 360; t+=inc)
        {
            x = a - amp * sin((a * t * PI)/180);
            y = b - amp * sin((b * t * PI)/180);

            //  Z mods INC
            shp.vertex(x, y, t%inc);
        }
        shp.endShape();

        return shp;
    }



    /**
     * Start and run Test1.Main
     * @param args
     */
    public  static  void  main ( String []  args ) {
        PApplet.main( "Thirteen.Main" );
    }

}
