package LisaCam;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import processing.video.Capture;

/**
 * sketch   ->  added incrementor param to shapeJous() to help with the trial of finding the perfect point count ( incrementor )
 * s    ->  saves .png
 * esc  ->  saves .png, kills sketch
 */


/**
 *  Observation : Processing's save() has started writing to /Applications/ this.png instead of the project root
 *  as before.
 *
 *      Running previous sketch doesn't fix, so don't know that it's related to Capture or not
 *
 *  // TODO : FIX FILE PATH SO PNGS SAVE WHERE YOU WANT THEM
    //            String base = new java.io.File( "." ).getCanonicalPath();
    //HACK : SAVE started saved to /Applications/
 *
 * - PNGs are now saving to the P5Test/out folder
 * save(  "/Users/fickes/Documents/IntelliJ/P5Test/out/" + this + "_" + ct + ".png");
 *
 * TODO : make 1 savePng() function and reuse instead of the current 3 different places save() is called
 *
 * - This is the example showing WebCam as the shape's fill texture.
 * -    Sketch requirement : import processing.video.Capture;
 * -    * doesn't look like this comes with P5 v3 anymore, so just
 *      * add the package via PDE or your preferred library install dance
 *
 *
 *
 *
 * - xyInc : PI CHANGES EVERYTHING!
 *  - changed to w*PI
 *      - grid looks great, nice spacing
 *      - shapes are totally different than before !
 * - w = 42
 * - grid adjusted
 *  - xx/yy starting point = w
 *  - xyInc : grid control in one place
 *
 * - strokeWeight(2) : 2 like xyInc which is w*2 -> PI too fat for this setup
 *  - if strokeWeight goes up, xyInc will need adjustment
 *
 * - using actual w param for shape size
 *
 * - Using a mixture of 2D & 3D shapes
 *
 * - 9 - 36 trials
 *  - NEW incrementer debug stamp in the middle-ish
 *
 * - Z VALUE IN SHAPEJOUS () CRRRRAAAAZZYYYYY MAAAANNNN
 *  - Z mods INC
 *
 * - BLACK & WHITE only until your lissajous adventure journey is complete
 *  - still prototyping to do before bringing color to the table
 *  - NOTE: trying to have colored 3D fill & stroke, but B&W stroke noFill 2D
 *  >> doesn't appear that P3D is liking my code, it's using the last fill called apparently
 *  - stroke fades w/frameCount

 * - setupStage() util for resetting stage.  Hoping to locate some P5 smoothing magic to draw better lines
 *  - still on TODO list : curveVertex() w/Lissajous point array
 */
public class Main extends PApplet {

    int cX, cY, xx, yy, xyInc;
    int ct = 9, w = 42;
    PShape tmp = new PShape();
    Capture cam;

    @Override
    public  void  settings ()  {
        size(1280, 720, "processing.opengl.PGraphics3D");
        smooth(8);
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
        xyInc = (int)(w*2);    // x|y increment grid control

        cam = new Capture(this, Capture.list()[15]);    //  15
        cam.start();

    }


    @Override
    public  void  draw ()  {


        if (cam.available() == true) {
            cam.read();
        }

        tmp = shapeJous(xx, yy, w, ct);

        //  no fill B&W stroke
        noFill();
        stroke(frameCount%255);

        //  CAMERA
        if(cam!=null){texture(cam);}
        shape(tmp);

        beginShape();
            //  CAMERA
            if(cam!=null){texture(cam);}

            stroke(frameCount%255);

            for(int vv = 0; vv < tmp.getVertexCount(); vv++ )
            {
                PVector vect = tmp.getVertex(vv);
                vertex( vect.x, vect.y, vect.z, vect.y, vect.x );

                // NOTE : texture() only works with vertex()
            }
        endShape();

        //  do the FredV
        pushMatrix();

    //TODO: how do you get 3D shapes different color than 2D shapes?
//            fill(255, frameCount%255, frameCount%255);
            stroke(frameCount%255);

            translate(xx, yy, w);
            scale(random(.6f,4.2f));
            rotate(random(HALF_PI, TWO_PI));
//  CAMERA
if(cam!=null){texture(cam);}

            shape(tmp);
        popMatrix();



        if (xx >= width) {
            xx = w;
            yy += xyInc;

        //  don't use 2D logic w/3D coordinates
        } else if (yy >= height ) {


            //  STAMP IT
            fill(0);
            rect(0, 0, w*TWO_PI, w*4.2f);

            fill(255);
            textSize(42);

            text( ct, w, w*PI);

// TODO : FIX FILE PATH SO PNGS SAVE WHERE YOU WANT THEM
//            String base = new java.io.File( "." ).getCanonicalPath();
//HACK : SAVE started saved to /Applications/
            //  save for reference
            save(  "/Users/fickes/Documents/IntelliJ/P5Test/out/" + this + "_" + ct + ".png");


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
            //  CAMERA
            if(cam!=null) {
                cam.stop();
                cam.dispose();
            }
            exit();
        }

    }


    @Override
    public void keyPressed(){

        switch(keyCode){

            case ESC:{
                save(  "/Users/fickes/Documents/IntelliJ/P5Test/out/" + this + "_" + ct + ".png");
                noLoop();

                //  CAMERA
                if(cam!=null) {
                    cam.stop();
                    cam.dispose();
                }

                super.exit();
            }
            break;

            case 's':
            case 'S':
            {
                save(  "/Users/fickes/Documents/IntelliJ/P5Test/out/" + this + "_" + ct + ".png");
            }
            break;
        }

    }

    /**
     * Helper function to clear stage
     */
    private void setupStage() {

        //  CAMERA
        /*
        if(cam!=null)
            background(cam);
        else
        */
        //  reset stage
        background(-1);

        strokeCap(ROUND);
        strokeJoin(ROUND);

hint(DISABLE_DEPTH_SORT);
hint(DISABLE_DEPTH_TEST);
hint(DISABLE_OPENGL_ERRORS);
hint(DISABLE_ASYNC_SAVEFRAME);
hint(DISABLE_BUFFER_READING);
hint(DISABLE_DEPTH_MASK);




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
     * Start and run LisaCam.Main
     * @param args
     */
    public  static  void  main ( String []  args ) {
        PApplet.main( "LisaCam.Main" );
    }

}
