package com.user.aditya.gradbud;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ChangePassTeach extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressDialog pd;
    String URL= "https://talentyaari.000webhostapp.com/Android_Retrievals_GradBud/changePassTeach.php",pass,ID,p1,p2,p3;
    DB_Controller db_controller;

    public ChangePassTeach() {
        // Required empty public constructor
    }

    public static ChangePassTeach newInstance(String param1, String param2) {
        ChangePassTeach fragment = new ChangePassTeach();
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

        View view= inflater.inflate(R.layout.fragment_change_pass_teach, container, false);
        ID=getArguments().getString("id");
        final EditText op=(EditText)view.findViewById(R.id.op);
        final EditText np=(EditText)view.findViewById(R.id.np);
        final EditText cp=(EditText)view.findViewById(R.id.cp);
        final EditText opin=(EditText)view.findViewById(R.id.opin);
        final EditText npin=(EditText)view.findViewById(R.id.npin);
        final EditText cpin=(EditText)view.findViewById(R.id.cpin);
        op.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()!=0) {
                    opin.setEnabled(false);
                    npin.setEnabled(false);
                    cpin.setEnabled(false);
                }
                else
                {
                    opin.setEnabled(true);
                    npin.setEnabled(true);
                    cpin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        np.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()!=0) {
                    opin.setEnabled(false);
                    npin.setEnabled(false);
                    cpin.setEnabled(false);
                }
                else
                {
                    opin.setEnabled(true);
                    npin.setEnabled(true);
                    cpin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()!=0) {
                    opin.setEnabled(false);
                    npin.setEnabled(false);
                    cpin.setEnabled(false);
                }
                else
                {
                    opin.setEnabled(true);
                    npin.setEnabled(true);
                    cpin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        opin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(opin.getText().length()!=0) {
                    op.setEnabled(false);
                    np.setEnabled(false);
                    cp.setEnabled(false);
                }
                else
                {
                    op.setEnabled(true);
                    np.setEnabled(true);
                    cp.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(opin.getText().length()==4){
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        });
        npin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()!=0) {
                    op.setEnabled(false);
                    np.setEnabled(false);
                    cp.setEnabled(false);
                }
                else
                {
                    op.setEnabled(true);
                    np.setEnabled(true);
                    cp.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(npin.getText().length()==4){
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        });
        cpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()!=0) {
                    op.setEnabled(false);
                    np.setEnabled(false);
                    cp.setEnabled(false);
                }
                else
                {
                    op.setEnabled(true);
                    np.setEnabled(true);
                    cp.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(cpin.getText().length()==4){
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        });
        final StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject result=new JSONObject(response);
                    pd.dismiss();
                    if (result.getString("error").equalsIgnoreCase("false")) {
                        Toast.makeText(getActivity(), "Password Successfully Updated", Toast.LENGTH_SHORT).show();
                            db_controller.update_password(pass);
                    }
                    else
                        Toast.makeText(getActivity(), "Password Updation Failed", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
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
            protected Map<String,String> getParams(){
                HashMap<String,String> params=new HashMap<>();
                params.put("id",ID);
                params.put("password",p1);
                params.put("npassword",p2);
                return params;
            }
        };
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.floatingActionButton4);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db_controller=new DB_Controller(getActivity(),"",null,2);
                if(cp.isEnabled())
                {
                    p1=op.getText().toString();
                    p2=np.getText().toString();
                    p3=cp.getText().toString();
                    if(db_controller.check_password(p1)){
                        if(p2.equals(p3)&&p2.length()>=6)
                        {
                            pass=p2;
                            pd=new ProgressDialog(getActivity());
                            pd.setMessage("Updating password...");
                            pd.setCancelable(false);
                            pd.show();
                            Volley.newRequestQueue(getActivity()).add(request);
                        }
                        else
                            Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getActivity(), "Wrong Password Entered", Toast.LENGTH_SHORT).show();
                }
                if(cpin.isEnabled())
                {
                    String pin1=opin.getText().toString();
                    String pin2=npin.getText().toString();
                    String pin3=cpin.getText().toString();
                    if(db_controller.check_PIN(pin1,2))
                    {
                        if(pin2.equals(pin3)&&pin2.length()==4) {
                            db_controller.update_pin(pin2);
                            Toast.makeText(getActivity(), "PIN updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getActivity(), "PINs do not match", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getActivity(), "Wrong PIN entered", Toast.LENGTH_SHORT).show();
                }
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
