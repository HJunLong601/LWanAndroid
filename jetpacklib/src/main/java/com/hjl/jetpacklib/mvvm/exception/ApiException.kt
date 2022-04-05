package com.hjl.jetpacklib.mvvm.exception

class ApiException(val errorCode: Int, val errorMessage: String) : RuntimeException(errorMessage)