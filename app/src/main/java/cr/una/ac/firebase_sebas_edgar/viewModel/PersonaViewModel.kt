package cr.una.ac.firebase_sebas_edgar.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import cr.una.ac.firebase_sebas_edgar.entity.Persona
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PersonaViewModel: ViewModel() {

    private val db: DatabaseReference = Firebase.database.reference


    private val _personaslist: MutableLiveData<List<Persona>> = MutableLiveData()
    var personaslist: LiveData<List<Persona>>  = _personaslist


    // Read
     fun loadPersonas() {
        Log.d("PersonaViewModel", "loadPersonas")
        val personasRef = db.child("personas")
        val personaListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val personas = dataSnapshot.children.mapNotNull {
                    val persona = it.getValue(Persona::class.java)
                    persona?.id = it.key  // asigna la clave de firebase como id
                    persona
                }
                _personaslist.value = personas
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("PersonaViewModel", "loadPersonas:onCancelled", databaseError.toException())
            }

        }
        personasRef.addValueEventListener(personaListener)
    }

    // Create
    fun addPersona(persona: Persona) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val personaId = db.child("personas").push().key
                if (personaId != null) {
                    db.child("personas").child(personaId).setValue(persona)
                }
            } catch (e: Exception) {

            }
        }
    }

    fun deletePersona(persona: Persona) {
        val id = persona.id
        if (id != null) {
            db.child("personas").child(id).removeValue()
        }
    }





}
