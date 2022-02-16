package vc.nora.listadetarefas.fragments.update

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import vc.nora.listadetarefas.R
import vc.nora.listadetarefas.viewmodel.TaskViewModel
import vc.nora.listadetarefas.databinding.FragmentUpdateBinding
import vc.nora.listadetarefas.extensions.format
import vc.nora.listadetarefas.extensions.text
import vc.nora.listadetarefas.model.Task
import java.util.*

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: TaskViewModel
    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(layoutInflater, container, false)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        updateItem()
        fillFields()
        setOnClickDate()
        setOnClickTime()
        setOnClickCancel()

        return binding.root
    }

    private fun fillFields() {
        binding.tilTitle.text = args.currentTask.title
        binding.tilDescription.text = args.currentTask.description
        binding.tilDate.text = args.currentTask.date
        binding.tilTime.text = args.currentTask.time
    }

    private fun setOnClickCancel() {
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
    }

    private fun updateItem() {
        binding.btnUpdateTask.setOnClickListener {
            val title = binding.tilTitle.text.trim()
            val description = binding.tilDescription.text.trim()
            val date = binding.tilDate.text
            val time = binding.tilTime.text

            if (inputCheck(title, description, date, time)) {
                val updatedTask = Task(args.currentTask.id, title, description, date, time)
                taskViewModel.updateTask(updatedTask)

                Toast.makeText(
                    requireContext(),
                    "Tarefa '$title' atualizada com sucesso!",
                    Toast.LENGTH_LONG
                ).show()

                findNavController().navigate(R.id.action_updateFragment_to_listFragment)

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