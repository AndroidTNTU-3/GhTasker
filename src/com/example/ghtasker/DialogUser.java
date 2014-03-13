package com.example.ghtasker;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class DialogUser extends DialogFragment implements OnClickListener {
	
	Button button;
	EditText textUser;
	EditText textPass;
	
	String user;
	
	private DialogCallBack dialogCallBack;

	public static interface DialogCallBack{
		public void setUser(String user);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_user, null);
		textUser = (EditText) v.findViewById(R.id.edit_username);
		textPass = (EditText) v.findViewById(R.id.editPassword);
		button = (Button) v.findViewById(R.id.button_set_user);
		button.setOnClickListener(this);
		return v;
		
	}
	
	@Override
	public void onClick(View v) {
		user = textUser.getText().toString();
		dialogCallBack.setUser(user);
		dismiss();
	}		
	
	public void setDialogCallBack(DialogCallBack dialogCallBack){
		this.dialogCallBack = dialogCallBack;
	}



}
