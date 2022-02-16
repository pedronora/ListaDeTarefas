package vc.nora.listadetarefas.fragments.insert

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import vc.nora.listadetarefas.R
import vc.nora.listadetarefas.databinding.FragmentInsertBinding
import vc.nora.listadetarefas.extensions.format
import vc.nora.listadetarefas.extensions.text
import vc.nora.listadetarefas.model.Task
import vc.nora.listadetarefas.viewmodel.TaskViewModel
import java.util.*

class InsertFragment : Fragment() {

    private var _binding: FragmentInsertBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInsertBinding.inflate(layoutInflater, container, false)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        setOnClickDate()
        setOnClickTime()
        insertDataToDataBase()
        cancelInsert()

        return binding.root
    }

    private fun cancelInsert() {
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_insertFragment_to_listFragment)
        }
    }

    private fun insertDataToDataBase() {
        binding.btnNewTask.setOnClickListener {
            val title = binding.tilTitle.text.trim()
            val description = binding.tilDescription.text.trim()
            val date = binding.tilDate.text
            val time = binding.tilTime.text

            if (inputCheck(title, description, date, time)) {
                val task = Task(0, title, description, date, time)
                taskViewModel.insertTask(task)

                Toast.makeText(
                    requireContext(),
                    "Tarefa '$title' adicionada com sucesso!",
                    Toast.LENGTH_LONG
                )
                    .show()

                findNavController().navigate(R.id.action_insertFragment_to_listFragment)

            } else {
                Toast.makeText(
                    requireContext(),
                    "Por favor, preencha todos os campos!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun inputCheck(
        title: String,
        description: String,
        date: String,
        time: String
    ): Boolean {
        return !(TextUtils.isEmpty(title) ||
                TextUtils.isEmpty(description) ||
                TextUtils.isEmpty(date) ||
                TextUtils.isEmpty(time))
    }

    private fun setOnClickDate() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            val timeZone = TimeZone.getDefault()
            val offSet = timeZone.getOffset(Date().time) * -1
            datePicker.addOnPositiveButtonClickListener {
                binding.tilDate.text = Date(it + offSet).format()
            }
            datePicker.show(parentFragmentManager, "DATE_PICKER_TAG")
        }
    }

    private fun setOnClickTime() {
        binding.tilTime.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val hour = "${timePicker.hour}".padStart(2, '0')
                val minute = "${timePicker.minute}".padStart(2, '0')
                binding.tilTime.text = "$hour:$minute"
            }
            timePicker.show(parentFragmentManager, null)
        }
    }
}