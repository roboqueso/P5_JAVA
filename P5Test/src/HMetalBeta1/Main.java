package HMetalBeta1;


import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import processing.video.Capture;

/**
 * THIS ISN'T USING HMetal from HYPE
 *
 *
 * ---------------------------------
 *  10/12/2017 : HMetalBeta1
 *
 *  - This sketch originated from BootCampCAM
 *  - First RnD sketch to mirror HFICKES branch of HYPE_PROCESSING library project
 *  1.  MIRROR API CHANGES FROM HYPE LIB TO THIS SKETCH FOR EASY PORTABILITY
 *  EX: CURVE_MODE
 *
         //  TODO - P5Test shapeJouse RnD to mirror this lib
         public static final int POINTS = PConstants.POINTS;
         public static final int LINE = PConstants.LINE;
         public static final int BEZIER = PConstants.BEZIER_VERTEX;
         public static final int QUADRATIC = PConstants.QUADRATIC_VERTEX;
         public static final int CURVE = PConstants.CURVE_VERTEX;
 *
 * 2. Trim out all extra fat in this sketch down to only required code for HYPE HMetal RnD
 */
public class Main extends PApplet {


// TODO: refactor CURVE_MODE and sketch to use PConstants listed above and
    int CURVE_MODE = 1; //  0 bezier, 1 curve, 2 line

    int cX, cY, xx, yy, xyInc;
    int ct = 9, w = 42;
    PShape tmp = new PShape();
    PVector vect;
    PVector ctl2 = new PVector();

    Capture cam;

    String PROJECT_ROOT = System.getProperty("user.dir");
//    String PNG_OUT = PROJECT_ROOT + "/out/";  //  standard project out

    //  output to this sketch's /frames/ folder
    String SKETCH_PATH = PROJECT_ROOT + "/src/" + getClass().getName().replace(".Main","");
    String PNG_OUT = SKETCH_PATH + "/frames/";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public  void  settings ()  {
        size(1280, 720, "processing.opengl.PGraphics3D");

        smooth(8);  //  smooth() can only be used in settings();
        pixelDensity(displayDensity());
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    /**
     * The setup() function is run once, when the program starts.
     hint() can go in here
     */
    public  void  setup ()  {

        //  Using hint(ENABLE_DEPTH_SORT) can improve the appearance of 3D geometry drawn to 2D file formats.
        hint(ENABLE_DEPTH_SORT);

        setupStage();

        //  setup variables
        cX = width/2;
        cY = height/2;

        xx = yy = w;
        xyInc = w;    // x|y increment grid control

        cam = new Capture(this, Capture.list()[0]);    //  15
        cam.start();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public  void  draw ()  {

        // 3D code
        hint(DISABLE_DEPTH_TEST);
        camera();
        lights(); //    because P3D

        ambientLight(ct,ct,ct);
        emissive(ct,ct,ct);
        specular(ct,ct,ct);


        //  READ CAMERA
        if (cam.available() == true) {
            cam.read();
        }

        //  MAKE SHAPE
        tmp = shapeJous(xx, yy, w, ct);

        //noStroke();

        beginShape(POLYGON);

        //  CAMERA  TEXTURE
        if(cam!=null){texture( cam );}
        else {
            texture(get());
        }

        //  put down a vertex for the curves - remember the XY for texture action w/LINE mode
        vertex(xx, yy, w, xx+w, yy+w);

        //  MIDDLE point
        try {
            ctl2 = tmp.getVertex((tmp.getVertexCount() / 2) - 1);
        } catch(Exception exc){
            println("EXC: " + exc.getMessage());
            // make a point ~ xx/yy
            ctl2 = new PVector(xx+(w/2),yy+(w/2), w+ct );
        }


        for(int vv = 0; vv < tmp.getVertexCount(); vv++ )
        {
            vect = tmp.getVertex(vv);   // GET VERTEX 1ST, color below depends on it


            // standard B&W action
            noFill();

            switch (CURVE_MODE) {

                //  BEZIER
                case 0:

                    bezierDetail( 20+vv );

                    //  LEAF or CRYSTAL GENERATOR
                    bezierVertex(xx, yy, w,
                            ctl2.x-w, ctl2.y-w, ctl2.z- w,
                            vect.x, vect.y, vect.z
                    );


                    break;

                //  CURVE
                case 1:

                    curveDetail(20+vv);
                    curveVertex(vect.x, vect.y, vect.z);

                    break;

                //  LINE
                case 2:


                    vertex(vect.x, vect.y, vect.z, xx+vv, yy+vv);

                    break;

            }

        }

        endShape(CLOSE);




        if (xx >= width) {
            xx = w;
            yy += xyInc;

            //  don't use 2D logic w/3D coordinates
        } else if (yy >= height ) {

// debug
println("ct: " + ct + " CURVE_MODE: " + CURVE_MODE);

            //  Save for reference
            savePng(ct + " = " + CURVE_MODE);

            xx = yy = w;

            // only increment after drawing finishes entire screen
            ct++;

            System.gc();

            //  reset stage
            setupStage();

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

//  make preview.gif using gifsicle
print("gifsicle -b -i -D=2 --no-extensions --resize 640x360 -O=3 "+PNG_OUT+"*.gif -o "+PNG_OUT+"*.gif \n");
print("gifsicle -i -l -t=255 -D=2 -d42 "+PNG_OUT+"*.gif > "+SKETCH_PATH+"/"+pdeName()+getTimestamp()+"_preview.gif  \n");

            doExit();
        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void keyPressed(){

        switch(keyCode){

            case ESC:{
                doExit();
            }
            break;

            case 's':
            case 'S':
            {
                savePng("");
            }
            break;
        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * exit function
     */
    private void doExit(){
        //  save on exit
//        savePng("ERICFICKES.COM");

        System.gc();
        noLoop();
        exit();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Stamps bottom left corner of sketch with current sketchname + timestamp + msg.
     * Next saves a PNG named SketchnameTimestamp.png
     *
     * @implNote : this function uses the global variable w
     */
    void savePng(String msg){

        String lbl = pdeName() + "/" + msg;

        textSize(w/2);

        // 2D code - to force label to always be on top of 3D
        hint(ENABLE_DEPTH_TEST);

        fill(0, 125);
        rect(0, height-(w/2), textWidth(lbl), w*HALF_PI);

        fill(255);
        text(lbl, 0, height-(textDescent()-3) );

//        save(  PNG_OUT + pdeName() + getTimestamp() + ".png");
        //  NOTE: save as .gif so gifsicle can compile preview.gif.  Doesn't work with .PNG
        save(  PNG_OUT + pdeName() + getTimestamp() + ".gif");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Helper function to clear stage
     */
    private void setupStage() {

        //  reset stage
        background(255);

        // TODO: is there a blendMode P3D likes?
        /*
        BLEND - linear interpolation of colours: C = A*factor + B. This is the default blending mode.
        ADD - additive blending with white clip: C = min(A*factor + B, 255)
        SUBTRACT - subtractive blending with black clip: C = max(B - A*factor, 0)
        DARKEST - only the darkest colour succeeds: C = min(A*factor, B)
        LIGHTEST - only the lightest colour succeeds: C = max(A*factor, B)
        DIFFERENCE - subtract colors from underlying image.
        EXCLUSION - similar to DIFFERENCE, but less extreme.
        MULTIPLY - multiply the colors, result will always be darker.
        SCREEN - opposite multiply, uses inverse values of the colors.
        REPLACE - the pixels entirely replace the others and don't utilize alpha (transparency) values
        */
//        blendMode(SCREEN);

        strokeCap(ROUND);
        strokeJoin(ROUND);
        strokeWeight(2);

        textSize(w/2);
        textMode(SHAPE);

        noFill();

    }







    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Lissajous PShape maker
     * @param a     X coordinate
     * @param b     Y coordinate
     * @param amp   Amplitude or size
     * @param inc   Loop magic incrementer [ 1 - 36 supported ]. (360 / inc) = number of points in returned PShape
     * @return  PShape containing vertices in the shape of a lissajous pattern
     */
    private PShape shapeJous( float a, float b, float amp, int inc )
    {
        //  PROTOTYPING : trying to locate universal ideal INCrementor for lisajouss loop
        //  Ideal range is someplace between 1 and 36
        if( ( inc < 1 ) || ( inc > 36 ) ) {
            inc = 1;
        }

        PShape shp = createShape();
        shp.beginShape(POLYGON);

        float x, y;

        for ( int t = 0; t <= 160  ; t+=inc)  //  160 is the NEW hotness -> slightly less points, no blank frames 9-36
        {
            //  NEW HOTNESS!
            x = a - amp * cos((a * t * TWO_PI)/360);
            y = b - amp * sin((b * t * TWO_PI)/360);

            //  Z mods INC
            shp.vertex(x, y, t%inc);
        }

        shp.endShape(CLOSE);

        return shp;
    }




    /**
     * Pick a random multiple of 7 between a min and max number pair
     *
     * TODO: gogo JAVA magic, let's smarten up this old P5 helper function
     */
    int getSeven(int min, int max)
    {
        int safecatch = 1, val=1;

        //	be safe
        if((min<max) && max>0)
        {
            while( ((val%7)!=0 ) && safecatch<7)
            {
                val = (int)random(min,max);
                // don't loop more than 7 times
                safecatch++;
            }
        }
        return val;
    }



    /**
     * Spit out timestamp.  Current format "MMddyyyHHmmss"
     * @return
     */
    String getTimestamp() {
        return new java.text.SimpleDateFormat("MMddyyyHHmmss").format(new java.util.Date());
    }


    /**
     * Returns FOLDER.MAIN "pdeName"
     *
     *
     * @return
     */
    String pdeName() {
        return split( this.toString(), "@")[0];
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Start and run HMetalBeta1.Main
     * @param args
     */
    public  static  void  main ( String []  args ) {
        PApplet.main( "HMetalBeta1.Main" );
    }

}


/*
TODO: come back to this when tuning shapeJous for potentially lower point counts
NOTE: LEAVING HERE AS IT MIGHT COME IN HANDY FOR LATER

//  Create a PShape of 7 points, centered at specified cx, cy
    PShape shape7(int cx, int cy, int sz)
    {
        //  createShape(kind, params)
        //  @kind   int: either POINT, LINE, TRIANGLE, QUAD, RECT, ELLIPSE, ARC, BOX, SPHERE
        //  @p      float[]: parameters that match the kind of shape
        PShape shape7;

        shape7 = createShape();
        shape7.beginShape();

        //  LET'S BE POSITIVE
        cx = ( cx < 0 ) ? -cx : cx;
        cy = ( cy < 0 ) ? -cy : cy;


        for (int i = 0; i < 6; i = i+1)
        {
            //  pick seven points using circle logic
            shape7.vertex(   cx - int( cos(radians(random(360))) * sz ),
                cy - int( sin(radians(random(360))) * sz ) );
        }

        shape7.endShape(CLOSE);

        return shape7;
    }
*/