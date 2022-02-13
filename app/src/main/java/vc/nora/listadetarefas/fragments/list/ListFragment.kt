package vc.nora.listadetarefas.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import vc.nora.listadetarefas.R
import vc.nora.listadetarefas.databinding.FragmentListBinding
import vc.nora.listadetarefas.viewmodel.TaskViewModel

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { TaskListAdapter() }
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(layoutInflater, container, false)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        binding.rvTasksList.adapter = adapter
        updateDate()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_insertFragment)
        }

        editTask()
        deleteTask()

        return binding.root
    }

    private fun updateDate() {
        taskViewModel.readAllData.observe(viewLifecycleOwner) {
            adapter.submitList(it?.toMutableList())
            binding.included.emptyState.visibility =
                if (it.isNullOrEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun editTask() {
        adapter.listenerEdit = {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun deleteTask() {
        adapter.listenerDelete = { task ->
            taskViewModel.deleteTask(task)

            Snackbar.make(requireView(), "Tarefa '${task.title}' deletada", Snackbar.LENGTH_LONG)
                .setAction("Desfazer") {
                    taskViewModel.insertTask(task)
                }.show()
        }
    }
}