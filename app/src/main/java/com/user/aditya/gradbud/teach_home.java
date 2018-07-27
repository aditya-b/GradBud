package com.user.aditya.gradbud;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link teach_home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link teach_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class teach_home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public teach_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment teach_home.
     */
    // TODO: Rename and change types and number of parameters
    public static teach_home newInstance(String param1, String param2) {
        teach_home fragment = new teach_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_teach_home, container, false);
        TextView id=(TextView)view.findViewById(R.id.id);
        TextView name=(TextView)view.findViewById(R.id.name);
        TextView designation=(TextView)view.findViewById(R.id.designation);
        CircleImageView img=(CircleImageView)view.findViewById(R.id.dp);
        Bundle e=getArguments();
        id.setText(e.getString("id"));
        name.setText(e.getString("name"));
        designation.setText(e.getString("designation"));
        Picasso.with(getActivity()).load(e.getString("imgurl")).placeholder(R.drawable.download).error(R.drawable.download).into(img);
        Button about_teacher=(Button)view.findViewById(R.id.about_teacher);
        Button faq_teacher=(Button)view.findViewById(R.id.faq_teacher);
        FloatingActionButton fab=(FloatingActionButton)view.findViewById(R.id.logout);
        faq_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),FAQ.class));
            }
        });
        about_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AboutDev.class));
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
                alert.setTitle("Confirm your action");
                alert.setMessage("Are you sure you want to logout of this device?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DB_Controller dc=new DB_Controller(getActivity(),null,null,2);
                        dc.delete_login();
                        startActivity(new Intent(getActivity(),Main3Activity.class));
                        getActivity().finish();
                        Toast.makeText(getActivity(), "Logged out Successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getActivity(), "Logout", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
