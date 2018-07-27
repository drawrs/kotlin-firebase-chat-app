package com.khilman.www.firebasechatkotlintest.model

data class ChatModel(var id: String = "",
                     var userModel: UserModel?,
                     var message: String,
                     var timeStamp: String) {
    constructor() : this("", UserModel(), "", ""){

    }
}