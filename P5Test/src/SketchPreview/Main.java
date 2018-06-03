package SketchPreview;


import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import java.io.File;


/**
 * ---------------------------------
 *  9/29/2017 : SketchPreview
 *
 *  - Remake this GIF, only using PureJuice shapes for each cell
    - animate the 9-36 frames
    - make a GIF at the end of every sketch iteration ( preview.gif )


     GIFSICLE
         http://www.lcdf.org/gifsicle/
         Download https://github.com/kohler/gifsicle
         Unzip to FOLDER
         CD FOLDER
         ./configure
         make
         make install
         gifsicle installed!

    DOIT.SH
        chmod +x doit.sh

    NOTE: doit.sh is legacy or one way to do it.
    This sketch now prints a direct command line gifsicle that you can paste directly to Terminal to create

     > Turn this sketch into a routine workflow step when ending one iteration.
     > Each version has a {sketchname}_preview.gif allowing for quick preview when scanning source



 * ---------------------------------
 *  HISTORY
 * ---------------------------------
 *
 * Line vs Curve & all TODOs
 *
 * a. savePng() label action fixed!  Now shows label in bottom left corner on TOP of P3D code
 *
 * Continued curve vertec RnD : Bezier vs Curve vs Quadratic.
 *
 * bezierVertex() https://processing.org/reference/bezierVertex_.html
 *
 * X
 *
 * curveVertex() https://processing.org/reference/curveVertex_.html
 *
 * X
 *
 * quadraticVertex() https://processing.org/reference/quadraticVertex_.html
 *      LOSER -> quadraticVertex() is now voted off the project, too gnarly
 *      * don't spend any more time with quadraticVertex
 *
 * NEW FUNCTIONS : pdeName(), getTimestamp(), savePng()
 *
 * All saving of PNG goes through savePng("") now
 *
 * DEBUG - point count now showing
 *  // debug - show point count
    text( tmp.getVertexCount() , xx, yy );
 *
 * ---------------------------------
 *  HISTORY
 * ---------------------------------
 *

 *
 * getSeven() new helper
 *
 *
 * 4 x/y based COLOR themes in draw()
 **
 *
 * sweet FILL() bro!
 *
 * code cleanups
 *
 * - Dropping shapeJuan for shapeJous()
 *  ** NEW HOTNESS promoted - start looking at curves
 *
 * - FILL YOURSELF!Shape.texture( get() )
 *      * HOT FILL TWEAK
 *      texture( get(xx, yy, w,w) );
 *
 * - MODing out at 225 vs 255 ( don't stroke all the way white w/white background )
 *
 *
 *  STROKE HINTS ON :: things looking "pretty smooth", could it be smoother via native P5 or JAVA? ( before shaders )
 *
 *
 *
 *
 * - doExit() NO LONGER saves png.  This was causing a 37th blank png to be created
 *
 * - noFill()   ->  focusing on maths, not color ATM
 *

 * research knots?

 //  MATT BERNHARDT
 //  https://www.openprocessing.org/sketch/57613#


 * - solved relative base path situation
 *      String PROJECT_ROOT = System.getProperty("user.dir");
 String PNG_OUT = PROJECT_ROOT + "/out/";
 *
 *
 * - PNGs are now saving to the P5Test/out folder
 * save(  PNG_OUT + this + "_" + ct + ".png");
 *
 * - new util functions : doExit(), savePng().
 *
 * - xyInc : PI CHANGES EVERYTHING!
 *  - changed to w*PI
 *      - grid looks great, nice spacing
 *      - shapes are totally different than before !
 *
 * - w = 42
 *
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

 * - setupStage() util for resetting stage.  Hoping to locate some P5 smoothing magic to draw better lines

 *
 *  sketch   ->  added incrementor param to shapeJous() to help with the trial of finding the perfect point count ( incrementor )
 * s    ->  saves .png
 * esc  ->  saves .png, kills sketch
 */
public class Main extends PApplet {



    int CURVE_MODE = 0; //  0 bezier, 1 curve, 2 PALMTREE_SPLOSION, 3 line

    int cX, cY, xx, yy, xyInc;
    int ct = 9, w = 42;
    PShape tmp = new PShape();
    PVector vect;
    PVector ctl1 = new PVector();
    PVector ctl2 = new PVector();

    String PROJECT_ROOT = System.getProperty("user.dir");

    //    String PNG_OUT = PROJECT_ROOT + "/out/";  //  standard project out

    //  output to this sketch's /frames/ folder
    String SKETCH_PATH = PROJECT_ROOT + "/src/" + pdeName().replace(".Main","");
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
     * It's used to define initial environment properties such as screen size and to load media such as images and fonts as the program starts.
     * There can only be one setup() function for each program and it shouldn't be called again after its initial execution.
     If the sketch is a different dimension than the default, the size() function or fullScreen() function must be the first line in setup().
     Note: Variables DECLARED within setup() are not accessible within other functions, including draw().

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
        xyInc = (int)(w*2);    // x|y increment grid control

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


        tmp = shapeJous(xx, yy, w, ct);

        beginShape(POLYGON); // TODO: what effect does POLYGON have on this sketch?

            texture(get(xx,yy,width,w));

            //  put down a vertex for the curves - remember the XY for texture action w/LINE mode
            vertex(xx, yy, w, xx+w, yy+w);

            //  using final vertex as control point
            ctl2 = tmp.getVertex(tmp.getVertexCount() - 1);

//        // BETA x/y/z colored lighting
//        ambientLight( ctl2.x%255, ctl2.y%255, ctl2.y+w+ct%255 );
//        emissive(ctl2.x%255, ctl2.y%255, ctl2.y+w+ct%255 );
//        specular(ctl2.x%255, ctl2.y%255, ctl2.y+w+ct%255 );

            for(int vv = 0; vv < tmp.getVertexCount(); vv++ )
            {
                vect = tmp.getVertex(vv);   // GET VERTEX 1ST, color below depends on it

                // standard B&W action
                noFill();
                stroke(vect.x%255 );
                tint(vect.x%255 );  // because texture()

                    switch (CURVE_MODE) {

                        //  BEZIER
                        case 0:

                            //  DOUBLE DOWN
//                            stroke(vect.x %255, vect.y  %255, vect.z  %255);
//                            fill(vect.x %255, vect.y  %255, vect.z  %255, 210 );

                            bezierDetail( 20+vv );


                            //  LEAF or CRYSTAL GENERATOR
                            bezierVertex(xx, yy, w,
                                    ctl2.x-w, ctl2.y-w, ctl2.z- w,
                                        vect.x, vect.y, vect.z
                                         );


                            break;

                        //  CURVE
                        case 1:


//                            stroke(vect.z %255, vect.x  %255, vect.y  %255);
//                            fill(vect.z %255, vect.x  %255, vect.y  %255, 210);

                            curveDetail(20+vv);
                            curveVertex(vect.x, vect.y, vect.z);


                        break;

                        //  PALMTREE_SPLOSION
                        case 2:

//                            stroke(vect.z*vv %255, (vect.y+vv)  %255, vect.x  %255);
//                            fill(vect.z*vv %255, (vect.y+vv)  %255, vect.x  %255, 210);

                            curveDetail(w+vv);

                            // PALMTREE_SPLOSION -> small spikey explosion / palm tree leaf looking shapes
                            curveVertex(xx+vv, yy+vv, w+vv);
                            curveVertex(vect.x, vect.y, vect.z);


                            break;


                        //  LINE
                        case 3:


                            vertex(vect.x, vect.y, vect.z, xx+vv, yy+vv);

                            break;

                    }

            }

        endShape();//CLOSE);




        if (xx >= width) {
            xx = w;
            yy += xyInc;

            //  don't use 2D logic w/3D coordinates
        } else if (yy >= height ) {

//  DEBUG
println("ct : " + ct + " &  CURVE_MODE : " + CURVE_MODE);

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

//  TODO: continue to optimize this process so _preview.gif is small as possible, but still good quality
print("gifsicle -k 512 --use-col=web -i --batch -O=3 "+PNG_OUT+"*.gif -o "+PNG_OUT+"*.gif \n");
print("gifsicle -i -l -t=255 -D=2 -d42 --no-extensions --resize 640x360 -O3 "+PNG_OUT+"*.gif > "+SKETCH_PATH+"/"+pdeName()+getTimestamp()+"_preview.gif \n");

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
        save(  PNG_OUT + pdeName() + getTimestamp() + ".gif");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Helper function to clear stage
     */
    private void setupStage() {

        //  reset stage
        background(255);

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

        blendMode(ADD);  // TODO: experiment with blendMode() -> P3D doesn't seem to like this
        */




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
     * Spit out timestamp.  Current format "MMddyyyHHmmss"
     * @return
     */
    String getTimestamp() {
        return new java.text.SimpleDateFormat("MMddyyyHHmmss").format(new java.util.Date());
    }


    /**
     * Returns FOLDER.MAIN "pdeName"
     * aka : JAVA getClass().getName()
     *
     * @return
     */
    String pdeName() {
        return split( this.toString(), "@")[0];
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Start and run SketchPreview.Main
     * @param args
     */
    public  static  void  main ( String []  args ) {
        PApplet.main( "SketchPreview.Main" );
    }

}




/*

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