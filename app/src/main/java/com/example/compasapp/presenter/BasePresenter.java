package com.example.compasapp.presenter;

public abstract class BasePresenter<T>  {

    public T view;

   public void attach(T view){
       this.view = view;
   }

    public void detach(T view){
        this.view = null;
    }

    public boolean isViewAttached(){
        return this.view != null;
    }
}
