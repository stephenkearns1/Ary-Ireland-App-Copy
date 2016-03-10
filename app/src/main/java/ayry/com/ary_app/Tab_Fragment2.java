package ayry.com.ary_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Stephen J Kearns on 15/02/2016.
 */
public class Tab_Fragment2 extends Fragment{

    /*
         Code referance: http://guides.codepath.com/android/Google-Play-Style-Tabs-using-TabLayout
    */

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //if arguments need to be returned
        int someInt = getArguments().getInt("someInt",2);
        String someTitle = getArguments().getString("someTitle","");

        //what ever you want to do with arguments when returned
    }

    // new Instance constructor for creating fragment with arguments
    public static Tab_Fragment2 newInstance(int page, String title){
        //create new istance of the Tab_fragment1 class
        Tab_Fragment2 tab_fragment2 = new Tab_Fragment2();
        Bundle args = new Bundle();
        args.putInt("someInt", 2);
        args.putString("someTitle", title);
        tab_fragment2.setArguments(args);

        return tab_fragment2;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment3_layout, container, false);

        return view;
    }


} //end of class
