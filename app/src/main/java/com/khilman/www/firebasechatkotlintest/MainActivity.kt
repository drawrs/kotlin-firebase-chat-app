package com.khilman.www.firebasechatkotlintest

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import com.khilman.www.firebasechatkotlintest.util.Util
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    // TODO : Deklarasi Variable
    var emojIcon: EmojIconActions? = null
    var contentRoot: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponent() // Menginisialisasi Komponen
        initEvent() // Menyiapkan Event

        var b: WebView

        // Check Internet Connection
        if (!Util().isConnectionAvaiable(this)){
            toast("Membutuhkan koneksi Internet!")
            finish()
            return
        }

    }

    private fun initEvent() {
        contentView?.onClick {
            // hide the keyboard
            hideSoftKeyboard()
        }
    }

    // TODO : Sembunyikan Keyboard
    private fun hideSoftKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    // TODO : Menginsialisasi Views
    private fun initComponent() {
        // set window title
        supportActionBar?.title = "FireChat"
        contentRoot = find(R.id.contentRoot)
        emojIcon = EmojIconActions(this, contentRoot, editTextMessage, buttonEmoji)
        emojIcon?.ShowEmojIcon()
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

    }
}
