package cr.una.ac.firebase_sebas_edgar

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.una.ac.firebase_sebas_edgar.adapter.PersonaAdapter
import cr.una.ac.firebase_sebas_edgar.databinding.FragmentFirstBinding
import cr.una.ac.firebase_sebas_edgar.entity.Persona
import cr.una.ac.firebase_sebas_edgar.viewModel.PersonaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var personaViewModel : PersonaViewModel
    private lateinit var personaslist : List<Persona>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,

        ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        val listView = view.findViewById<RecyclerView>(R.id.textview_first)
        personaslist = mutableListOf<Persona>()
        var adapter =  PersonaAdapter(personaslist as ArrayList<Persona>)
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(requireContext())

        personaViewModel = ViewModelProvider(requireActivity()).get(PersonaViewModel::class.java)

        personaViewModel.personaslist.observe(viewLifecycleOwner) { elementos ->
            adapter.updateData(elementos as ArrayList<Persona>)
            personaslist = elementos
        }

        GlobalScope.launch(Dispatchers.Main) {
            personaViewModel.loadPersonas()!!
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position != 0) {
                    val persona = personaslist[position]
                    GlobalScope.launch(Dispatchers.Main) {
                        personaViewModel.deletePersona(persona)
                    }
                }
            }


            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (viewHolder is PersonaAdapter.ViewHolder) {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                        val itemView = viewHolder.itemView
                        val paint = Paint()
                        paint.color = Color.RED
                        val deleteIcon = ContextCompat.getDrawable(
                            requireContext(),
                            android.R.drawable.ic_menu_delete
                        )
                        val iconMargin = (itemView.height - deleteIcon!!.intrinsicHeight) / 2
                        val iconTop =
                            itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
                        val iconBottom = iconTop + deleteIcon.intrinsicHeight

                        c.drawRect(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat(),
                            paint
                        )

                        val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                        deleteIcon.draw(c)
                    }
                }
            }
        })

        itemTouchHelper.attachToRecyclerView(listView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}