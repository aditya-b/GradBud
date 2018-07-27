package com.user.aditya.gradbud;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class Dashboard extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Dashboard() {
        // Required empty public constructor
    }
    String ID,subs[],classes[];
    Bundle e=new Bundle();
    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dashboard, container, false);
        ID=getArguments().getString("id");
        subs=getArguments().getStringArray("subjects");
        classes=getArguments().getStringArray("classes");
        e.putStringArray("subjects",subs);
        e.putStringArray("classes",classes);
        e.putString("id",ID);
        ImageButton att=(ImageButton)view.findViewById(R.id.att);
        att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),MarkAttendance.class).putExtras(e));
            }
        });

        ImageButton gassn=(ImageButton)view.findViewById(R.id.gassn);
        gassn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),give_assignment.class).putExtras(e));
            }
        });

        ImageButton massn=(ImageButton)view.findViewById(R.id.massn);
        massn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),MarkAssignment.class).putExtras(e));
            }
        });

        ImageButton mrks=(ImageButton)view.findViewById(R.id.marks);
        mrks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ChooseStudent.class).putExtras(e));
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event




    @Override
    public void onDetach() {
        super.onDetach();
    }
}
