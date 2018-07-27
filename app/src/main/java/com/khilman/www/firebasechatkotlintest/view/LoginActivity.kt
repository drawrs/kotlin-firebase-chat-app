package com.khilman.www.firebasechatkotlintest.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.khilman.www.firebasechatkotlintest.MainActivity
import com.khilman.www.firebasechatkotlintest.R
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    // TODO: Variable
    companion object {
        protected val TAG = LoginActivity.javaClass.simpleName
        protected val RC_SIGN_IN = 9001
        private var mGoogleApiClinet: GoogleApiClient? = null
        private var mFirebaseAuth: FirebaseAuth? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.title = "FireChat Login"

        initComponent()
        initEvent()
    }

    private fun initComponent() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleApiClinet = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
        // firebase
        mFirebaseAuth = FirebaseAuth.getInstance()
    }

    private fun initEvent() {
        buttonSignIn.onClick {
            // TODO: Login
            doSignIn()
        }
    }

    private fun doSignIn() {
        // TODO : Login ke firebase dengan Intent ke service firebase
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClinet)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // TODO : Cek kode request
        if(requestCode == RC_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

            if (result.isSuccess){
                // TODO : Lakukan login ke server firebase
                // Dapatkan data pengguna
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
            } else {
                // Tampilkan pesan login Gagal
                toast("Google Sign In failed")
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        Log.d(TAG, "account ID ${account?.id}")
        // TODO : Siapkan data kredential
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        mFirebaseAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener {
                    Log.d(TAG, "signInWithCredential ${it.isSuccessful}")
                    if (!it.isSuccessful){
                        // Jika login gagal maka wajib mengulang 1 tahun kembali
                        toast("Authentication failed")
                    } else {
                        // Move to aother activity
                        startActivity<MainActivity>()
                        finish()
                    }
                }
    }


}
