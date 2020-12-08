package com.example.mymvvmtest;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

public class MyViewModel extends AndroidViewModel {
    Application application;
    MutableLiveData<User> userData;
    SavedStateHandle stateHandle;
    String key=getApplication().getResources().getString(R.string.key);
    String shap_content=getApplication().getResources().getString(R.string.shap_content);
    String handle=getApplication().getResources().getString(R.string.handle);
    int handAge;
    public MyViewModel(@NonNull Application application, SavedStateHandle savehandle) {
        super(application);
        this.application=application;
        this.stateHandle=savehandle;

        load();
        if (stateHandle!=null){
            handAge=stateHandle.get(handle);
        }
    }

    public LiveData<User> getUserData(){
        if (userData==null){
            userData=new MutableLiveData<>();
        }
        return userData;
    }

    public void save(){
        //将用户年龄存储在SharedPreferences中
        SharedPreferences shp=getApplication().getSharedPreferences(shap_content, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=shp.edit();
        editor.putInt(key,getUserData().getValue().getAge());
        editor.apply();
    }

    public void load(){
        //提取出年龄
        SharedPreferences shp=getApplication().getSharedPreferences(shap_content,Context.MODE_PRIVATE);
        int age=shp.getInt(key,0);
        //将年龄放进SavedStateHandle中
        stateHandle.set(handle,age);
    }

    public void setUserData(int age){
        userData.setValue(new User("就阿嘎",age));
    }

    public int add(int num){
        User user= getUserData().getValue();
        if (user != null) {
            int age= user.getAge();
            Log.d("Demo","add不为空");
            return age+num;
        }else {
            //方法1：
            //这样子写才可以，这里的意思是，如果User的对象为空的话，那么就赋值给User对象
            // 也就是userData.setValue(new User("就阿嘎",age));，然后User就不为空了
            //为什么一开始页面上会有内容？因为一开始是DataBinding在给UI赋值，这部分不是viewModel的
            Log.d("Demo","add为空");
            setUserData(handAge);
            return handAge;
        }
        //这样子写是不可以的，因为只有在LiveData给某个对象赋值以后，这个User对象才有数据，否则为空
        // 就如：userData.setValue(new User("就阿嘎",age));要等这个语句运行完也就是活动中的viewModel.setUserData(age);这个方法
//        return 0;
        //方法2：
        //我们可以在点击事件之前，使用viewModel.setUserData(viewModel.handAge);方法，这样子就通过viewModel 中的setUserData()方法对User进行赋值，这样子，user就不为空了，上面的方法1就不会运行了
    }



}


