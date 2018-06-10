package ALPHA;


import fixlib.Fixlib;
import processing.core.PApplet;

import hype.*;
import hype.extended.behavior.*;
import hype.extended.colorist.*;
import hype.extended.layout.*;
import hype.interfaces.*;


/**
 * ---------------------------------
 *  6/10/2018 : ALPHA
 *
 * ALPHA  : square one starting point P5/HYPE template sketch
 * * BLOOD-DRAGON : 1920 x 1071
 * * size(displayWidth, displayHeight, P3D)
 * * HDR w, h is 2x1 EX: 2048, 1024
 *
 * 2. Trim out all extra fat in this sketch down to only required code for HYPE HMetal RnD
 */
public class Main extends PApplet {


    /* ------------------------------------------------------------------------- */
    String PROJECT_ROOT = System.getProperty("user.dir");
//    String PNG_OUT = PROJECT_ROOT + "/out/";  //  standard project out

    //  NOTE: PNG saves to root of project, not sketch folder
    String PNG_OUT = PROJECT_ROOT + "/png/" + getClass().getName().replace(".Main","") + "/";


    Fixlib fix = Fixlib.init(this);
    HDrawablePool pool;

    int gridX,gridY;
    int colCt = 8;
    int rowCt = colCt;
    int colSpacing = 8;
    int drawW, drawH; //  HDrawable Width / Height

    /* ------------------------------------------------------------------------- */


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
        //  hint(ENABLE_DEPTH_SORT);


        //  init VARIABLES
        drawW = (int)( (width-(colSpacing))/colCt)-colSpacing;
        drawH = (int)( (height-(colSpacing))/rowCt)-colSpacing;
        gridX = (drawW/2)+colSpacing;
        gridY = (drawH/2)+colSpacing;

        //  init HYPE
        H.init(this).background(-1).use3D(true);

        pool = new HDrawablePool(colCt*colCt);
        pool.autoAddToStage()

                .add (

                        // swap this out with something else
                        new HRect()
                )

                .layout (
                        new HGridLayout()
                                .startLoc(gridX, gridY)
                                .spacing( drawW+colSpacing, drawH+colSpacing, colSpacing )
                                .cols(colCt)
                                .rows(rowCt)
                )

                .onCreate (
                        new HCallback() {
                            public void run(Object obj) {

                                //  DO STUFF HERE
                                HDrawable d = (HDrawable) obj;
                                d
                                        .size( drawW, drawH )
                                        .noFill()
                                        .stroke( (int) d.x()%255, (int) d.y()%255, (int) d.z()%255 )
                                        .anchorAt(H.CENTER)
                                ;



                            }
                        }
                )

                .requestAll()
        ;

        H.drawStage();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public  void  draw ()  {


        //  stopper
        if(frameCount>36){

            doExit();
        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
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
    */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * exit function
     */
    private void doExit(){
        String msg = "ericfickes.com";
        //  stamp bottom right based on textSize
        fill(0);
        textSize(16);
        text(msg, width-(textWidth(msg)+textAscent()), height-textAscent());

        save( PNG_OUT + fix.pdeName() + "-" + fix.getTimestamp()+".png" );

        //  cleanup
        fix = null;

        noLoop();
        exit();
        System.gc();
        System.exit(1);
    }

//TODO: import FIXLIB and rewire save code to use fix.pdeName() + "-" + fix.getTimestamp();
    /**
     String getTimestamp() {
     return new java.text.SimpleDateFormat("MMddyyyHHmmss").format(new java.util.Date());
     }

     String pdeName() {
     return split( this.toString(), "@")[0];
     }
     */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Start and run ALPHA.Main
     * @param args
     */
    public  static  void  main ( String []  args ) {
        PApplet.main( "ALPHA.Main" );
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