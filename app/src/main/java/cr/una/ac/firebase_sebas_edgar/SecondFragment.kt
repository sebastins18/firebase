package cr.una.ac.firebase_sebas_edgar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cr.una.ac.firebase_sebas_edgar.databinding.FragmentSecondBinding
import cr.una.ac.firebase_sebas_edgar.entity.Persona
import cr.una.ac.firebase_sebas_edgar.viewModel.PersonaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var personaViewModel: PersonaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        personaViewModel = ViewModelProvider(requireActivity()).get(PersonaViewModel::class.java)


        val editTextNombre = view.findViewById<EditText>(R.id.editText_nombre)
        val editTextApellido = view.findViewById<EditText>(R.id.editText_apellido)
        val editTextEdad = view.findViewById<EditText>(R.id.editText_edad)

        binding.buttonSubmit.setOnClickListener{
            val nombre = editTextNombre.text.toString()
            val apellido = editTextApellido.text.toString()
            val edad = editTextEdad.text.toString().toIntOrNull() ?: 0

            val newPersona = Persona(null, nombre, apellido, edad)
            GlobalScope.launch(Dispatchers.IO) {
                personaViewModel.addPersona(newPersona)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
