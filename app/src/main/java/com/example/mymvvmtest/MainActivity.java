package com.example.mymvvmtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mymvvmtest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView textView;
    TextView textView2;
    MyViewModel viewModel;
    ActivityMainBinding databind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        databind= DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);
        button=findViewById(R.id.mybutton);
        textView=findViewById(R.id.mytextview);
        textView2=findViewById(R.id.mytextview2);
        viewModel=new ViewModelProvider(this,new SavedStateViewModelFactory(getApplication(),this)).get(MyViewModel.class);
        //由于下面的User的值改变了所以值被覆盖了，因此也是显示viewModel.handAge的值
        databind.setUser(new User("dataBinding赋值",1));
        databind.setViewModel(viewModel);
        //要记住databind要和这个生命周期绑定，这样子数据才会和viewModel绑定，这个特别关键，
        databind.setLifecycleOwner(this);
        viewModel.getUserData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                //这里是和databind绑定，textView和Uer.age绑定所以值也是viewModel.handAge的值
                textView.setText(String.valueOf(user.getAge()));
                //这里和viewModel绑定，因此一开始显示的是下面的viewModel.handAge的值
                textView2.setText(String.valueOf(user.getAge()));
            }
        });
        //这个步骤很关键，这个步骤给User赋值了，直接取SavedStateHandle中的数值，然后赋予User，User就不为空，点击add就直接往上加数
        viewModel.setUserData(viewModel.handAge);
    }

    @Override
    protected void onResume() {
        super.onResume();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setUserData(viewModel.add(5));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.save();
    }
}