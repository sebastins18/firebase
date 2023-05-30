package cr.una.ac.firebase_sebas_edgar.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.una.ac.firebase_sebas_edgar.R
import cr.una.ac.firebase_sebas_edgar.entity.Persona

class PersonaAdapter(var listaPersonas : ArrayList<Persona>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            ViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listaPersonas[position]

        if (holder is HeaderViewHolder) {
            holder.bind()
        } else if (holder is ViewHolder) {
            val personaItem = item
            holder.bind(personaItem)
        }
    }

    override fun getItemCount(): Int {
        return listaPersonas.size
    }
    fun updateData(newData: ArrayList<Persona>) {
        // Primero, verifiquemos si ya existe un encabezado en la lista.
        if (newData.isNotEmpty() && newData[0]._Nombre == null) {
            // Si el primer elemento no tiene nombre, asumimos que es el encabezado y no hacemos nada.
        } else {
            // Si el primer elemento tiene un nombre, entonces agregamos un encabezado al inicio de la lista.
            newData.add(0, Persona(null, "", "", 0))
        }

        // Ahora sí, actualizamos listaPersonas con los nuevos datos.
        listaPersonas = newData
        notifyDataSetChanged()
    }


    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(){
            val nombreTextView = itemView.findViewById<TextView>(R.id._Nombre)
            val apellidoTextView = itemView.findViewById<TextView>(R.id._Apellido)
            val edadTextView = itemView.findViewById<TextView>(R.id._Edad)

            nombreTextView.text = "Nombre"
            apellidoTextView.text = "Apellido"
            edadTextView.text = "Edad"

        }

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView = itemView.findViewById<TextView>(R.id._Nombre)
        val apellidoTextView = itemView.findViewById<TextView>(R.id._Apellido)
        val edadTextView = itemView.findViewById<TextView>(R.id._Edad)

        fun bind(persona: Persona) {
            nombreTextView.text = persona._Nombre.toString()
            apellidoTextView.text = persona._Apellido.toString()
            edadTextView.text = persona._Edad.toString()
        }
    }


}

