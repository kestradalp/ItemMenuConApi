package uteq.solutions.myapplication.Activities


import Alumnos
import Maestros
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.HeaderViewListAdapter
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import uteq.solutions.myapplication.R
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import uteq.solutions.myapplication.Models.User
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray
import uteq.solutions.myapplication.Fragments.*
import uteq.solutions.myapplication.Models.Type
import uteq.solutions.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var user: User
    lateinit var type: Type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.drawerLayout.addDrawerListener(
            ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.open_drawer,
                R.string.close_drawer
            )
        )

        user = intent.getParcelableExtra<User>("user") as User

        getJsonAPI()


    }
// Obtengo el JSON
    fun getJsonAPI() {

        val url = "https://mocki.io/v1/d21960b0-575b-48fc-8e02-1a9190f3434a"
        val queueRequest = Volley.newRequestQueue(this)

        val request = object : StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                process(response)

            },
            Response.ErrorListener {
                Log.d("Error: ", it.toString())
            }) {
        }
        queueRequest.add(request)

    }

// Aqui se realiza el procesado
    fun process(jsonString: String) {

        setTypeUser(jsonString)

        val header: View = binding.navView.getHeaderView(0)

        Picasso.get().load(user.avatar).into(
            header.findViewById<CircleImageView>(R.id.profile_image)
        )

        header.findViewById<TextView>(R.id.lblNombreUser).text = user.name
        header.findViewById<TextView>(R.id.lblTipoUsuario).text = type.name
        addOptions()

    }

// Asigno el tipo de usuario en base al json que obtuve enteriormente
    fun setTypeUser(strJsonType: String) {
        val typeArray = Type.jsonObjectsBuild(JSONArray(strJsonType))
        for (type in typeArray) {
            if (user.type == type.id) {
                this.type = type
                break
            }
        }
    }

// Agrego las opciones al menú
    fun addOptions() {
        for (option in type.options!!) {
            binding.navView.menu.add(option.toString()).setIcon(R.drawable.iconmenu)
                .setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener { //do what u want
                    binding.toolbar.title = it.title
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    setFragment(defFragment(it.title.toString()))
                    true
                })
        }
    }

// Defino que fragment se va a abrir en cada item
    fun defFragment(itemTitle: String): Fragment {
        return when (itemTitle) {
            "PENDIENTES" -> {
                Pendientes()
            }
            "AULA VIRTUAL" -> {
                FichaMedica()
            }
            "FICHA MÉDICA" -> {
                FichaMedica()
            }
            "MALLA" -> {
                Malla()
            }
            "HOJA DE VIDA" -> {
                HojaDeVida()
            }
            "MAESTROS" -> {
                Maestros()
            }
            else -> {
                Alumnos()
            }
        }
    }

// Lanzo el fragment que defini
    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.content_frame, fragment)
            commit()
        }
    }

}

