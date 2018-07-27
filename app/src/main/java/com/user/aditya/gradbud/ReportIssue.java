package com.user.aditya.gradbud;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReportIssue extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressDialog pd;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/report.php",subject,content;
    //private OnFragmentInteractionListener mListener;

    public ReportIssue() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportIssue.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportIssue newInstance(String param1, String param2) {
        ReportIssue fragment = new ReportIssue();
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
        View view=inflater.inflate(R.layout.fragment_report_issue, container, false);
        final EditText sub=(EditText)view.findViewById(R.id.sub);
        final EditText cont=(EditText)view.findViewById(R.id.content);
        final TextView subchar=(TextView)view.findViewById(R.id.subchar);
        final TextView conchar=(TextView)view.findViewById(R.id.conchar);
        sub.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subchar.setText(charSequence.length()+"/50");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cont.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                conchar.setText(charSequence.length()+"/255");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try
                {
                    JSONObject result=new JSONObject(response);
                    if(result.getString("error").equalsIgnoreCase("false"))
                    {
                        Toast.makeText(getActivity(), "Issue recorded by id "+result.getString("id")+". We will get back to you shortly.", Toast.LENGTH_SHORT).show();
                        sub.setText("");
                        cont.setText("");
                        subchar.setText("0/50");
                        conchar.setText("0/255");
                    }
                    else
                        Toast.makeText(getActivity(), "Couldn't report. Please try again later.", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Unknown error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(getActivity(), "Server error! Please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("subject", subject);
                params.put("content", content);
                return params;
            }
        };
        FloatingActionButton fab=(FloatingActionButton)view.findViewById(R.id.floatingActionButton3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subject=sub.getText().toString();
                content=cont.getText().toString();
                if(subject.isEmpty())
                    Toast.makeText(getActivity(), "Please enter subject.", Toast.LENGTH_SHORT).show();
                else if(content.isEmpty())
                    Toast.makeText(getActivity(), "Please describe your issue.", Toast.LENGTH_SHORT).show();
                else
                {
                    pd=new ProgressDialog(getActivity());
                    pd.setMessage("Sending your concern...");
                    pd.setCancelable(false);
                    pd.show();
                    Volley.newRequestQueue(getActivity()).add(request);
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }
}
