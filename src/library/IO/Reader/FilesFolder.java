package library.IO.Reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilesFolder {
	


    public static String[] getFiles( String dir_path ) {

        String[] arr_res = null;

        File f = new File( dir_path );

        if ( f.isDirectory( )) {

            List<String> res   = new ArrayList<String>();
            File[] arr_content = f.listFiles();

            int size = arr_content.length;

            for ( int i = 0; i < size; i ++ ) {

                if ( arr_content[ i ].isFile( ))
                res.add( arr_content[ i ].toString( ));
            }


            arr_res = res.toArray( new String[ 0 ] );

        } else
            System.err.println( "¡ Path NO válido !" );


        return arr_res;
    }

}
