package uteq.solutions.myapplication.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import uteq.solutions.myapplication.Models.User
import uteq.solutions.myapplication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIngresar.setOnClickListener {
            getJsonAPI()
        }
    }

    fun getJsonAPI() {

        val url = "https://mocki.io/v1/f1b2c021-2e6e-4fc6-939b-80549712796e"
        val queueRequest = Volley.newRequestQueue(this)

        val request = object : StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                processResp(response)
            },
            Response.ErrorListener {
                Log.d("Error: ", it.toString())
            }) {
        }

        queueRequest.add(request)

    }

    fun processResp(jsonString: String) {

        val nickname: String = binding.txtUser.text.toString()
        val pass: String = binding.txtPass.text.toString()
        val user: User? = validUser(jsonString, nickname, pass)

        if (user != null) {
            startActivity(
                Intent(this, MainActivity::class.java).putExtra(
                    "user", user as Parcelable
                )
            )
            finish()
        }

    }

    fun validUser(respJsonUsers: String, nickname: String, pass: String): User? {

        val userArray = User.jsonObjectsBuild(JSONArray(respJsonUsers))
        for (user in userArray) {
            if (nickname == user.user && pass == user.pass) {
                return user
            }
        }
        return null

    }

}