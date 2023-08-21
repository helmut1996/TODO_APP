package com.helcode.todo_app.utils

sealed class Resource<T>(val status: Status, val data:T?= null, val message:String?= null){
    class Success<T>(data: T?): Resource<T>(Status.SUCCESS,data)
    class Error<T>(meessage:String,data: T?= null): Resource<T>(Status.ERROR,data,meessage)
    class Loading<T>: Resource<T>(Status.LOADING)
}
