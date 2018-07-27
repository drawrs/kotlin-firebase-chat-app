package com.khilman.www.firebasechatkotlintest

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.khilman.www.firebasechatkotlintest.adapter.ChatFirebaseAdapter
import com.khilman.www.firebasechatkotlintest.model.ChatModel
import com.khilman.www.firebasechatkotlintest.model.UserModel
import com.khilman.www.firebasechatkotlintest.util.Util
import com.khilman.www.firebasechatkotlintest.view.LoginActivity
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // TODO : Deklarasi Variable
    private var contentRoot: View? = null

    companion object {
        // Auth
        private var mFirebaseAuth: FirebaseAuth? = null
        private var mFirebaseUser: FirebaseUser? = null
        private var mGoogleApiClient: GoogleApiClient? = null
        private var mFirebaseDatabaseReference: DatabaseReference? = null
        // UI View
        private var mLayourManager: LinearLayoutManager? = null
        private var emojIcon: EmojIconActions? = null


    }
    // TINNGAL COBA TAMPILIN CHAT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponent() // Menginisialisasi Komponen
        initEvent() // Menyiapkan Event

        // Check Internet Connection
        if (!Util().isConnectionAvaiable(this)){
            toast("Membutuhkan koneksi Internet!")
            finish()
        } else {
            verifikasiUser()
        }

    }

    var userModel: UserModel? = null
    private fun verifikasiUser() {
        if (mFirebaseUser == null) {
            startActivity<LoginActivity>()
            finish()
        } else {
            userModel = UserModel(mFirebaseUser?.uid, mFirebaseUser?.displayName, mFirebaseUser?.photoUrl.toString())
            getFirebaseMessages()
        }
    }

    private val CHAT_REFERENCE: String? = "chatmodel"

    private fun getFirebaseMessages() {
        //val chatModel = userModel?.let { ChatModel("", it, editTextMessage.text.toString(), Calendar.getInstance().getTime().getTime().toString()) }
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
        val adapater: ChatFirebaseAdapter = ChatFirebaseAdapter(mFirebaseDatabaseReference?.child(CHAT_REFERENCE), userModel?.name, this)
        adapater.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                val friendlyMessageCount = adapater.itemCount
                val lastVisiblePosition = mLayourManager?.findLastCompletelyVisibleItemPosition()
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    // scrool to position of message
                    messageRecyclerView.scrollToPosition(positionStart)
                }
            }
        })
        messageRecyclerView.layoutManager = mLayourManager
        messageRecyclerView.adapter = adapater
       // adapater.registerAdapterDataObserver(RecyclerView.AdapterDataObserver)
    }

    // TODO : Menginsialisasi Views
    private fun initComponent() {
        // set window title
        supportActionBar?.title = "FireChat"
        contentRoot = find(R.id.contentRoot)
        emojIcon = EmojIconActions(this, contentRoot, editTextMessage, buttonEmoji)
        emojIcon?.ShowEmojIcon()

        // Firebase
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth?.currentUser
        mLayourManager = LinearLayoutManager(this)
        mLayourManager?.stackFromEnd = true
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build()
    }
    private fun initEvent() {
        contentView?.onClick {
            // hide the keyboard
            hideSoftKeyboard()
        }

        buttonMessage.onClick {
//            ChatModel model = new ChatModel(userModel,edMessage.getText().toString(), Calendar.getInstance().getTime().getTime()+"");
//            mFirebaseDatabaseReference.child(CHAT_REFERENCE).push().setValue(model);
//            edMessage.setText(null);
            val chatModel = ChatModel("", userModel, editTextMessage.text.toString(), Calendar.getInstance().getTime().getTime().toString())
            mFirebaseDatabaseReference?.child(CHAT_REFERENCE)?.push()?.setValue(chatModel)
            editTextMessage.setText("")
        }
    }

    // TODO : Sembunyikan Keyboard
    private fun hideSoftKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }



    // TODO : Menampilkan menu popup
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    // TODO : Listener ketika menu popup dipilih
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val item_id = item?.itemId

        when (item_id) {
            R.id.nav_profile -> {
                showProfile()
            }
            R.id.nav_logout -> {
                doLogout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // TODO : Tampilkan Profil
    private fun showProfile() {

    }
    // TODO : Logout
    private fun doLogout() {
//        mFirebaseAuth.signOut();
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
//        startActivity(new Intent(this, LoginActivity.class));
//        finish()
        mFirebaseAuth?.signOut()
        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
        startActivity<LoginActivity>()
        finish()
    }
}
